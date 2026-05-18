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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the contract that the Sniper fault-target enrolment loop visits
 * EVERY OpenAPI parameter location — most importantly path parameters like
 * {@code {orderId}} which a previous code path silently dropped.
 *
 * <p>The pool builder ({@code generateFaultyPoolForSingleRoot}) is private,
 * so we drive it via reflection on a Mockito mock configured with
 * {@code CALLS_REAL_METHODS}; required fields ({@code serviceConfigs},
 * {@code useLLM}, {@code llmGen}) are injected reflectively. With a single
 * path parameter {@code orderId} attached to {@code GET /orders/{orderId}},
 * the resulting pool must contain an entry for {@code orderId} populated by
 * the underlying invalid-input generator.
 */
public class SharedPoolGenerationStagePathParamTest {

    /** Build a one-parameter operation whose parameter lives in the URL path. */
    private static Operation pathParamOperation() {
        TestParameter orderId = new TestParameter();
        orderId.setName("orderId");
        orderId.setIn("path");
        orderId.setType("string");
        orderId.setRequired(true);

        Operation op = new Operation();
        op.setMethod("get");
        op.setTestPath("/orders/{orderId}");
        op.setOperationId("getOrderById");
        op.setTestParameters(Collections.singletonList(orderId));
        op.setExpectedResponse("200");
        return op;
    }

    /** Wrap an operation in a minimal TestConfigurationObject the generator accepts. */
    private static TestConfigurationObject configWith(Operation op) {
        TestConfigurationObject cfg = new TestConfigurationObject();
        TestConfiguration tc = new TestConfiguration();
        tc.setOperations(new ArrayList<>(Collections.singletonList(op)));
        cfg.setTestConfiguration(tc);
        cfg.setAuth(new Auth());
        return cfg;
    }

    /** Inject a value into a private field via reflection. */
    private static void inject(Object target, String fieldName, Object value) throws Exception {
        Field f = MultiServiceTestCaseGenerator.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        // Final fields require unfreezing on the Field's modifiers — but the
        // private fields we touch here are non-final.
        f.set(target, value);
    }

    @Test
    public void pathParameterIsEnrolledInFaultPool() throws Exception {
        // Real generator instance (via Mockito mock with CALLS_REAL_METHODS) so
        // we don't have to feed the heavy AbstractTestCaseGenerator constructor.
        MultiServiceTestCaseGenerator gen = Mockito.mock(
                MultiServiceTestCaseGenerator.class, Mockito.CALLS_REAL_METHODS);

        // Inject the bits the private helper reads.
        String service = "order-service";
        Map<String, TestConfigurationObject> serviceConfigs = new HashMap<>();
        serviceConfigs.put(service, configWith(pathParamOperation()));
        inject(gen, "serviceConfigs", serviceConfigs);
        inject(gen, "useLLM", true);
        // llmGen is a private final field initialised at declaration to
        // `new AiDrivenLLMGenerator()`. The mock skips the field-init, so we
        // construct a real one for the test. The LLM call path resolves to
        // the deterministic "smart" mode by default, which uses hardcoded
        // payloads for the universal categories (no network needed).
        inject(gen, "llmGen", new es.us.isa.restest.generators.AiDrivenLLMGenerator());

        // Hand-built root step targeting GET /orders/{orderId}. The
        // extractRootApiFromStep helper recognises the verb/route via the
        // HTTP_OPERATION_PATTERN regex.
        WorkflowStep root = new WorkflowStep(
                "trace-1", "span-1", service,
                "GET /orders/{orderId}",
                0L, 0L, Collections.emptyMap(), Collections.emptyMap());

        // Drive the private method directly so we can assert against the
        // returned Map<String, InvalidInputPool> rather than the side-effect
        // storage that requires generateSharedParameterPools to wire up.
        Method m = MultiServiceTestCaseGenerator.class.getDeclaredMethod(
                "generateFaultyPoolForSingleRoot", WorkflowStep.class, String.class);
        m.setAccessible(true);

        // The rootApiKey shape matches the verb_normalisedPath convention.
        @SuppressWarnings("unchecked")
        Map<String, InvalidInputPool> faultyPool =
                (Map<String, InvalidInputPool>) m.invoke(gen, root, "GET__orders__orderId_");

        assertNotNull("Pool map must be returned even when only path params are present",
                faultyPool);
        assertTrue("Path parameter 'orderId' must be enrolled in the fault pool",
                faultyPool.containsKey("orderId"));

        InvalidInputPool pool = faultyPool.get("orderId");
        assertNotNull("Enrolled path param must have a non-null InvalidInputPool", pool);
        assertTrue("Path param fault pool must have at least one populated fault category — "
                        + "0 values means the loop body never fired",
                pool.getTotalCount() > 0);
    }

    @Test
    public void normaliseParamLocationAcceptsPath() throws Exception {
        Method m = MultiServiceTestCaseGenerator.class.getDeclaredMethod(
                "normaliseParamLocation", String.class);
        m.setAccessible(true);
        assertEquals("path",     m.invoke(null, "path"));
        assertEquals("path",     m.invoke(null, "PATH"));
        assertEquals("path",     m.invoke(null, " Path "));
    }
}
