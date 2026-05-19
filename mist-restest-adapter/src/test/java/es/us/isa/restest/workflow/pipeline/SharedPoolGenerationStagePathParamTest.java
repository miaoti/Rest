package es.us.isa.restest.workflow.pipeline;

import es.us.isa.restest.configuration.pojos.Auth;
import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfiguration;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import es.us.isa.restest.generators.AiDrivenLLMGenerator;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import es.us.isa.restest.inputs.InvalidInputPool;
import es.us.isa.restest.workflow.WorkflowStep;
import org.junit.Test;

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
 * <p>The pool builder ({@code SharedPoolSupport.generateFaultyPoolForSingleRoot})
 * is package-private static; we drive it via reflection because the test
 * lives in {@code workflow.pipeline} and not the helper's {@code .stages}
 * sub-package. With a single path parameter {@code orderId} attached to
 * {@code GET /orders/{orderId}}, the resulting pool must contain an entry
 * for {@code orderId} populated by the underlying invalid-input generator.
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

    /** Wrap an operation in a minimal TestConfigurationObject the helper accepts. */
    private static TestConfigurationObject configWith(Operation op) {
        TestConfigurationObject cfg = new TestConfigurationObject();
        TestConfiguration tc = new TestConfiguration();
        tc.setOperations(new ArrayList<>(Collections.singletonList(op)));
        cfg.setTestConfiguration(tc);
        cfg.setAuth(new Auth());
        return cfg;
    }

    /** Build a {@code PoolKey} for assertion (PoolKey is now public after the merge). */
    private static MultiServiceTestCaseGenerator.PoolKey poolKey(String paramName, String paramLocation) {
        return new MultiServiceTestCaseGenerator.PoolKey(paramName, paramLocation);
    }


    @Test
    public void pathParameterIsEnrolledInFaultPool() throws Exception {
        String service = "order-service";
        Map<String, TestConfigurationObject> serviceConfigs = new HashMap<>();
        serviceConfigs.put(service, configWith(pathParamOperation()));

        // Hand-built root step targeting GET /orders/{orderId}. The
        // extractRootApiFromStep helper recognises the verb/route via the
        // HTTP_OPERATION_PATTERN regex.
        WorkflowStep root = new WorkflowStep(
                "trace-1", "span-1", service,
                "GET /orders/{orderId}",
                0L, 0L, Collections.emptyMap(), Collections.emptyMap());

        // Drive the helper directly so we can assert against the returned
        // Map<String, InvalidInputPool> rather than the side-effect storage
        // that requires generateSharedParameterPools to wire up.
        Class<?> sharedPool = Class.forName(
                "es.us.isa.restest.workflow.pipeline.stages.SharedPoolSupport");
        Method m = sharedPool.getDeclaredMethod(
                "generateFaultyPoolForSingleRoot",
                WorkflowStep.class, String.class, Map.class, boolean.class,
                AiDrivenLLMGenerator.class);
        m.setAccessible(true);

        // Pool entries are keyed by PoolKey(paramName, normalisedLocation) so
        // same-name parameters at different locations do not collide.
        @SuppressWarnings("unchecked")
        Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool> faultyPool =
                (Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool>) m.invoke(null,
                        root, "GET__orders__orderId_",
                        serviceConfigs, true, new AiDrivenLLMGenerator());

        assertNotNull("Pool map must be returned even when only path params are present",
                faultyPool);
        MultiServiceTestCaseGenerator.PoolKey orderIdKey = poolKey("orderId", "path");
        assertTrue("Path parameter 'orderId' must be enrolled in the fault pool under (orderId,path)",
                faultyPool.containsKey(orderIdKey));

        InvalidInputPool pool = faultyPool.get(orderIdKey);
        assertNotNull("Enrolled path param must have a non-null InvalidInputPool", pool);
        assertTrue("Path param fault pool must have at least one populated fault category — "
                        + "0 values means the loop body never fired",
                pool.getTotalCount() > 0);
    }

    @Test
    public void normaliseParamLocationAcceptsPath() throws Exception {
        Class<?> stageSupport = Class.forName(
                "es.us.isa.restest.workflow.pipeline.stages.StageSupport");
        Method m = stageSupport.getDeclaredMethod("normaliseParamLocation", String.class);
        m.setAccessible(true);
        assertEquals("path",     m.invoke(null, "path"));
        assertEquals("path",     m.invoke(null, "PATH"));
        assertEquals("path",     m.invoke(null, " Path "));
    }
}
