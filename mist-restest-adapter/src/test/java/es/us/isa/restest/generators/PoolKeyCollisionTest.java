package es.us.isa.restest.generators;

import es.us.isa.restest.configuration.pojos.Auth;
import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfiguration;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the contract that the invalid-input pool keys by
 * {@code (paramName, paramLocation)} rather than {@code paramName} alone, so
 * two parameters that share a name but live in different OpenAPI locations
 * (e.g. path {@code {id}} and header {@code id}) coexist as distinct entries
 * instead of one silently overwriting the other.
 *
 * <p>Same plumbing as
 * {@link es.us.isa.restest.workflow.pipeline.SharedPoolGenerationStagePathParamTest}:
 * Mockito {@code CALLS_REAL_METHODS} on the generator + reflective field
 * injection + reflective invocation of the private
 * {@code generateFaultyPoolForSingleRoot}.
 */
public class PoolKeyCollisionTest {

    /**
     * Build an operation that exposes the SAME parameter name {@code id} in two
     * different OpenAPI locations. With the old {@code Map<String, InvalidInputPool>}
     * the second {@code put} would overwrite the first; with the new
     * {@code Map<PoolKey, InvalidInputPool>} both entries must coexist.
     */
    private static Operation duplicateNameDifferentLocationOperation() {
        TestParameter pathId = new TestParameter();
        pathId.setName("id");
        pathId.setIn("path");
        pathId.setType("string");
        pathId.setRequired(true);

        TestParameter headerId = new TestParameter();
        headerId.setName("id");
        headerId.setIn("header");
        headerId.setType("string");
        headerId.setRequired(true);

        Operation op = new Operation();
        op.setMethod("get");
        op.setTestPath("/items/{id}");
        op.setOperationId("getItemById");
        op.setTestParameters(new ArrayList<>(Arrays.asList(pathId, headerId)));
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

    @Test
    public void sameNameDifferentLocationsDoNotCollideInPool() throws Exception {
        MultiServiceTestCaseGenerator gen = Mockito.mock(
                MultiServiceTestCaseGenerator.class, Mockito.CALLS_REAL_METHODS);

        String service = "item-service";
        Map<String, TestConfigurationObject> serviceConfigs = new HashMap<>();
        serviceConfigs.put(service, configWith(duplicateNameDifferentLocationOperation()));
        inject(gen, "serviceConfigs", serviceConfigs);
        inject(gen, "useLLM", true);
        inject(gen, "llmGen", new AiDrivenLLMGenerator());

        WorkflowStep root = new WorkflowStep(
                "trace-collide", "span-collide", service,
                "GET /items/{id}",
                0L, 0L, Collections.emptyMap(), Collections.emptyMap());

        // generateFaultyPoolForSingleRoot was lifted from the generator into
        // SharedPoolSupport (see S-1b), where it is a package-private static
        // method taking explicit configuration arguments rather than reading
        // generator fields. The mock-and-inject setup above is kept so the
        // contract — a Mockito CALLS_REAL_METHODS instance is constructible —
        // continues to be exercised, but the call itself goes through the
        // lifted helper with null receiver.
        Method m = es.us.isa.restest.workflow.pipeline.stages.SharedPoolSupport.class.getDeclaredMethod(
                "generateFaultyPoolForSingleRoot",
                WorkflowStep.class, String.class, Map.class, boolean.class, AiDrivenLLMGenerator.class);
        m.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool> faultyPool =
                (Map<MultiServiceTestCaseGenerator.PoolKey, InvalidInputPool>) m.invoke(null,
                        root, "GET__items__id_",
                        serviceConfigs, true, new AiDrivenLLMGenerator());

        assertNotNull("Pool map must be returned", faultyPool);

        MultiServiceTestCaseGenerator.PoolKey pathKey =
                new MultiServiceTestCaseGenerator.PoolKey("id", "path");
        MultiServiceTestCaseGenerator.PoolKey headerKey =
                new MultiServiceTestCaseGenerator.PoolKey("id", "header");

        // The whole point of the PoolKey change: same name, different locations →
        // distinct entries. If keys collide, only ONE entry survives and this
        // assertion fails — which is exactly the regression we want to lock out.
        assertEquals("Pool must contain exactly two entries (one per location) — "
                        + "size " + faultyPool.size() + " means the keys collided",
                2, faultyPool.size());

        assertTrue("Pool must contain (id, path)",   faultyPool.containsKey(pathKey));
        assertTrue("Pool must contain (id, header)", faultyPool.containsKey(headerKey));

        InvalidInputPool pathPool   = faultyPool.get(pathKey);
        InvalidInputPool headerPool = faultyPool.get(headerKey);
        assertNotNull("Path-located 'id' must have its own non-null pool",   pathPool);
        assertNotNull("Header-located 'id' must have its own non-null pool", headerPool);

        // Distinct entries = distinct InvalidInputPool instances; if both keys mapped
        // to the same object reference we'd have a different kind of collision.
        assertTrue("Each PoolKey must map to its own InvalidInputPool instance",
                pathPool != headerPool);
    }

    @Test
    public void poolKeyEqualityAndHashContractHoldsForSamePair() throws Exception {
        // Equal pair → equal keys and matching hash codes. Required for HashMap
        // lookups to work after the type change.
        MultiServiceTestCaseGenerator.PoolKey a = new MultiServiceTestCaseGenerator.PoolKey("id", "path");
        MultiServiceTestCaseGenerator.PoolKey b = new MultiServiceTestCaseGenerator.PoolKey("id", "path");
        assertEquals("Two PoolKeys with the same (name, location) must be equal", a, b);
        assertEquals("Two equal PoolKeys must have the same hashCode", a.hashCode(), b.hashCode());
    }

    @Test
    public void poolKeyEqualityDistinguishesLocation() throws Exception {
        // Same name, different location → MUST be unequal, otherwise the pool
        // would collide the way the pre-PoolKey code did.
        MultiServiceTestCaseGenerator.PoolKey pathKey   = new MultiServiceTestCaseGenerator.PoolKey("id", "path");
        MultiServiceTestCaseGenerator.PoolKey headerKey = new MultiServiceTestCaseGenerator.PoolKey("id", "header");
        assertTrue("PoolKey('id','path') and PoolKey('id','header') must not be equal",
                !pathKey.equals(headerKey));
    }
}
