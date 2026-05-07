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
 * server answers a request with HTTP 401, this filter:
 *
 * <ol>
 *   <li>invalidates the cached JWT in {@link MstAuthHandler},</li>
 *   <li>triggers a fresh login via {@link MstAuthHandler#ensureReady()},</li>
 *   <li>swaps the {@code Authorization} header for the new token, and</li>
 *   <li>retries the request <em>once</em>.</li>
 * </ol>
 *
 * If the second call also returns 401, the original 401 is returned to the
 * test - no infinite loop.
 *
 * The MST writer only attaches this filter on steps whose auth flow follows
 * the configured default. Steps with a per-test override (the
 * {@code __mstOverrideToken} / {@code __mstDisableAuth} locals set by the
 * {@code AuthManipulationStrategy}) bypass the filter so that exploration
 * tests targeting 401 (INVALID_TOKEN, EXPIRED_TOKEN, REMOVE_AUTH) actually
 * observe the 401 they were designed to trigger.
 *
 * Classic RESTest writers ({@code RESTAssuredWriter}) never reference this
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

        if (response == null || response.getStatusCode() != 401) return response;
        if (!MstAuthHandler.isRefreshOn401Enabled()) return response;

        String header = MstAuthHandler.getTokenHeader();
        // Don't refresh if the request had no Authorization header at all -
        // means the caller deliberately opted out (skip pattern, NONE mode,
        // or per-test override gone wrong); refreshing would change semantics.
        if (requestSpec.getHeaders().getValue(header) == null) return response;

        log.info("MstAuthRefreshFilter: 401 received from {} {}, refreshing token and retrying once",
                requestSpec.getMethod(), requestSpec.getURI());

        MstAuthHandler.invalidate();
        if (!MstAuthHandler.ensureReady()) {
            log.warn("MstAuthRefreshFilter: re-login failed, returning original 401");
            return response;
        }

        String newToken = MstAuthHandler.getDefaultToken();
        if (newToken == null || newToken.isEmpty()) return response;

        // Swap the Authorization header for the new value.
        requestSpec.removeHeader(header);
        requestSpec.header(header, MstAuthHandler.getTokenPrefix() + newToken);

        Response retried = ctx.next(requestSpec, responseSpec);
        if (retried != null && retried.getStatusCode() == 401) {
            log.warn("MstAuthRefreshFilter: retry also returned 401 - token may be revoked or auth.login.* misconfigured");
        }
        return retried;
    }

    /** Run after the auth header is already on the request. */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
