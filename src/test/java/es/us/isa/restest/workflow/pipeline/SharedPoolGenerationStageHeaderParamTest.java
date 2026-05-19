package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.pojos.Auth;
import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfiguration;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.inputs.InvalidInputPool;
import es.us.isa.restest.workflow.WorkflowStep;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Header parameters (e.g. {@code X-API-Key}, {@code Authorization}) MUST be
 * enrolled in the fault-target queue. A previous code path dropped every
 * non-body / non-query location, so the 8-fault-per-parameter coverage was
 * unreachable for endpoints that take a header parameter.
 *
 * <p>Companion to {@link SharedPoolGenerationStagePathParamTest}; same plumbing,
 * different parameter location.
 */
public class SharedPoolGenerationStageHeaderParamTest {

    /** Operation with one header parameter and one cookie parameter, no body/query. */
    private static Operation headerAndCookieParamOperation() {
        TestParameter apiKey = new TestParameter();
        apiKey.setName("X-API-Key");
        apiKey.setIn("header");
        apiKey.setType("string");
        apiKey.setRequired(true);

        TestParameter sessionId = new TestParameter();
        sessionId.setName("sessionId");
        sessionId.setIn("cookie");
        sessionId.setType("string");
        sessionId.setRequired(false);

        Operation op = new Operation();
        op.setMethod("get");
        op.setTestPath("/secure/data");
        op.setOperationId("getSecureData");
        op.setTestParameters(new ArrayList<>(Arrays.asList(apiKey, sessionId)));
        op.setExpectedResponse("200");
        return op;
    }

    private static TestConfigurationObject configWith(Operation op) {
        TestConfigurationObject cfg = new TestConfigurationObject();
        TestConfiguration tc = new TestConfiguration();
        tc.setOperations(new ArrayList<>(Collections.singletonList(op)));
        cfg.setTestConfiguration(tc);
        cfg.setAuth(new Auth());
        return cfg;
    }

    private static void inject(Object target, String fieldName, Object value) throws Exception {
        Field f = MultiServiceTestCaseGenerator.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    /**
     * Build a {@code PoolKey} reflectively. The class is package-private in
     * {@code es.us.isa.restest.generators}, so cross-package tests must
     * construct it through its declared constructor rather than referencing
     * the type directly.
     */
    private static Object poolKey(String paramName, String paramLocation) throws Exception {
        Class<?> keyCls = Class.forName("es.us.isa.restest.generators.MultiServiceTestCaseGenerator$PoolKey");
        java.lang.reflect.Constructor<?> ctor = keyCls.getDeclaredConstructor(String.class, String.class);
        ctor.setAccessible(true);
        return ctor.newInstance(paramName, paramLocation);
    }

    @Test
    public void headerAndCookieParametersAreEnrolledInFaultPool() throws Exception {
        MultiServiceTestCaseGenerator gen = Mockito.mock(
                MultiServiceTestCaseGenerator.class, Mockito.CALLS_REAL_METHODS);

        String service = "secure-service";
        Map<String, TestConfigurationObject> serviceConfigs = new HashMap<>();
        serviceConfigs.put(service, configWith(headerAndCookieParamOperation()));
        inject(gen, "serviceConfigs", serviceConfigs);
        inject(gen, "useLLM", true);
        inject(gen, "llmGen", new es.us.isa.restest.generators.AiDrivenLLMGenerator());

        WorkflowStep root = new WorkflowStep(
                "trace-2", "span-2", service,
                "GET /secure/data",
                0L, 0L, Collections.emptyMap(), Collections.emptyMap());

        Method m = MultiServiceTestCaseGenerator.class.getDeclaredMethod(
                "generateFaultyPoolForSingleRoot", WorkflowStep.class, String.class);
        m.setAccessible(true);

        // Pool entries are now keyed by PoolKey(paramName, normalisedLocation) so
        // same-name parameters at different locations do not collide.
        // PoolKey is package-private, so we reflectively construct the lookup key.
        @SuppressWarnings("unchecked")
        Map<Object, InvalidInputPool> faultyPool =
                (Map<Object, InvalidInputPool>) m.invoke(gen, root, "GET__secure_data");

        assertNotNull(faultyPool);
        Object headerKey = poolKey("X-API-Key", "header");
        Object cookieKey = poolKey("sessionId", "cookie");
        assertTrue("Header parameter 'X-API-Key' must be enrolled in the fault pool under (X-API-Key,header)",
                faultyPool.containsKey(headerKey));
        assertTrue("Cookie parameter 'sessionId' must be enrolled in the fault pool under (sessionId,cookie)",
                faultyPool.containsKey(cookieKey));

        InvalidInputPool headerPool = faultyPool.get(headerKey);
        assertNotNull(headerPool);
        assertTrue("Header param fault pool must be populated (loop body must fire for it)",
                headerPool.getTotalCount() > 0);

        InvalidInputPool cookiePool = faultyPool.get(cookieKey);
        assertNotNull(cookiePool);
        assertTrue("Cookie param fault pool must be populated (loop body must fire for it)",
                cookiePool.getTotalCount() > 0);
    }

    @Test
    public void normaliseParamLocationAcceptsHeaderCookieAndFolds() throws Exception {
        Method m = MultiServiceTestCaseGenerator.class.getDeclaredMethod(
                "normaliseParamLocation", String.class);
        m.setAccessible(true);
        assertEquals("header",   m.invoke(null, "header"));
        assertEquals("header",   m.invoke(null, "HEADER"));
        assertEquals("cookie",   m.invoke(null, "cookie"));
        assertEquals("cookie",   m.invoke(null, "Cookie"));
        assertEquals("query",    m.invoke(null, "query"));
        assertEquals("body",     m.invoke(null, "body"));
        // formData (OpenAPI 2) folds into body — historical convention preserved.
        assertEquals("body",     m.invoke(null, "formData"));
        assertEquals("body",     m.invoke(null, "FORMDATA"));
        // Null/empty default to body — matches the writer's body-path fallback
        // so unspecified locations don't bypass fault enrolment.
        assertEquals("body",     m.invoke(null, (String) null));
        assertEquals("body",     m.invoke(null, ""));
        assertEquals("body",     m.invoke(null, "   "));
        // Unknown locations land in "other" — visible in the logged breakdown
        // so operators can spot spec authoring mistakes.
        assertEquals("other",    m.invoke(null, "made-up-location"));
    }
}
