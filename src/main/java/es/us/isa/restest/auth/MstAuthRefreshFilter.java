package es.us.isa.restest.auth;

import io.restassured.filter.OrderedFilter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * RestAssured filter used <strong>only</strong> by the MST writer. When the
 * server answers a request with HTTP <strong>401 or 403</strong>, this filter:
 *
 * <ol>
 *   <li>invalidates the cached JWT in {@link MstAuthHandler},</li>
 *   <li>triggers a fresh login via {@link MstAuthHandler#ensureReady()},</li>
 *   <li>swaps the {@code Authorization} header for the new token, and</li>
 *   <li>retries the request <em>once</em>.</li>
 * </ol>
 *
 * If the second call also returns 401/403, that response is returned to the
 * test - no infinite loop.
 *
 * <p><strong>Why 403 in addition to 401?</strong> RFC 7235 says servers should
 * return 401 when authentication is missing/expired and 403 only after the
 * caller has been authenticated but lacks privilege. In practice many Spring
 * Security setups (train-ticket included) return 403 in both cases — a default
 * JWT filter that throws an unhandled exception ends up routed through the
 * AccessDeniedHandler, not the AuthenticationEntryPoint. Treating 403 as a
 * possible auth-staleness signal lets tests survive multi-hour runs where the
 * cached JWT expires partway through; the cost of a legitimate 403 (admin
 * authenticated but lacks role for a specific endpoint) is one wasted re-login
 * before the original 403 is returned.
 *
 * <p>The MST writer only attaches this filter on steps whose auth flow follows
 * the configured default. Steps with a per-test override (the
 * {@code __mstOverrideToken} / {@code __mstDisableAuth} locals set by the
 * {@code AuthManipulationStrategy}) bypass the filter so that exploration
 * tests targeting 401/403 (INVALID_TOKEN, EXPIRED_TOKEN, REMOVE_AUTH) actually
 * observe the response code they were designed to trigger.
 *
 * <p>Classic RESTest writers ({@code RESTAssuredWriter}) never reference this
 * class - it only ships in the MST code path.
 */
public final class MstAuthRefreshFilter implements OrderedFilter {

    private static final Logger log = LogManager.getLogger(MstAuthRefreshFilter.class);

    public static final MstAuthRefreshFilter INSTANCE = new MstAuthRefreshFilter();

    private MstAuthRefreshFilter() {}

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        if (response == null) return response;
        int status = response.getStatusCode();
        // Refresh on either 401 (Unauthorized — well-behaved services) or 403
        // (Forbidden — services that route expired/invalid-JWT failures through
        // the AccessDeniedHandler instead of the AuthenticationEntryPoint).
        if (status != 401 && status != 403) return response;
        if (!MstAuthHandler.isRefreshOn401Enabled()) return response;

        String header = MstAuthHandler.getTokenHeader();
        // Don't refresh if the request had no Authorization header at all -
        // means the caller deliberately opted out (skip pattern, NONE mode,
        // or per-test override gone wrong); refreshing would change semantics.
        if (requestSpec.getHeaders().getValue(header) == null) return response;

        log.info("MstAuthRefreshFilter: {} received from {} {}, refreshing token and retrying once",
                status, requestSpec.getMethod(), requestSpec.getURI());

        MstAuthHandler.invalidate();
        if (!MstAuthHandler.ensureReady()) {
            log.warn("MstAuthRefreshFilter: re-login failed, returning original {}", status);
            return response;
        }

        String newToken = MstAuthHandler.getDefaultToken();
        if (newToken == null || newToken.isEmpty()) return response;

        // Swap the Authorization header for the new value.
        requestSpec.removeHeader(header);
        requestSpec.header(header, MstAuthHandler.getTokenPrefix() + newToken);

        Response retried = ctx.next(requestSpec, responseSpec);
        if (retried != null) {
            int retriedStatus = retried.getStatusCode();
            if (retriedStatus == 401 || retriedStatus == 403) {
                log.warn("MstAuthRefreshFilter: retry also returned {} - token may be revoked, " +
                        "auth.login.* misconfigured, or the caller genuinely lacks privilege",
                        retriedStatus);
            }
        }
        return retried;
    }

    /** Run after the auth header is already on the request. */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
