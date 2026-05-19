package es.us.isa.restest.generators;

import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowStep;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the contract that the single-root dedup pass short-circuits on
 * already-tagged scenarios so newly-constructed shattered children (which
 * Phase 3 cannot represent via identity) survive Phase 3.5's repeated
 * dedup pass.
 *
 * <p>The dedup logic lives in the package-private
 * {@code es.us.isa.restest.workflow.pipeline.stages.DedupSupport#runPass}
 * static helper after the phase-body lift. This test reaches it via
 * reflection so the production signature stays clean and the test does not
 * need to live in the {@code stages} package alongside the helper.
 */
public class PhaseTwoFiveDedupTest {

    /**
     * Build a 1-root WorkflowScenario whose sole root has the given operation
     * name (e.g. {@code "GET /stations"}). The canonical key extracted by the
     * stage helper is {@code "GET__stations"}.
     */
    private static WorkflowScenario singleRoot(String operationName, String serviceName) {
        WorkflowStep root = new WorkflowStep(
                "trace-" + System.nanoTime(),
                "span-" + System.nanoTime(),
                serviceName,
                operationName,
                0L, 0L,
                Collections.emptyMap(),
                Collections.emptyMap());
        WorkflowScenario sc = new WorkflowScenario();
        sc.addRootStep(root);
        return sc;
    }

    /** Two-root scenario whose roots target the given operation names. */
    private static WorkflowScenario twoRoot(String op1, String svc1, String op2, String svc2) {
        WorkflowScenario sc = new WorkflowScenario();
        sc.addRootStep(new WorkflowStep("trace-a-" + System.nanoTime(), "span-a-" + System.nanoTime(),
                svc1, op1, 0L, 0L, Collections.emptyMap(), Collections.emptyMap()));
        sc.addRootStep(new WorkflowStep("trace-b-" + System.nanoTime(), "span-b-" + System.nanoTime(),
                svc2, op2, 0L, 0L, Collections.emptyMap(), Collections.emptyMap()));
        return sc;
    }

    /**
     * Invoke the package-private static {@code DedupSupport.runPass(String, List, Set)}
     * helper via reflection. The class is package-private so we have to load it
     * by name, but the contract being tested (the dedup pass itself) is the
     * same logic that used to live as an instance method on the generator.
     */
    private static void invokeDedup(String label,
                                    List<WorkflowScenario> scenarios,
                                    Set<String> approvedKeys) throws Exception {
        Class<?> dedupSupport = Class.forName(
                "es.us.isa.restest.workflow.pipeline.stages.DedupSupport");
        Method m = dedupSupport.getDeclaredMethod(
                "runPass", String.class, List.class, Set.class);
        m.setAccessible(true);
        m.invoke(null, label, scenarios, approvedKeys);
    }

    @Test
    public void phase25DropsDuplicateSingleRootScenarios() throws Exception {
        // Three 1-root scenarios all targeting GET /stations.
        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(singleRoot("GET /stations", "station-service"));
        scenarios.add(singleRoot("GET /stations", "station-service"));
        scenarios.add(singleRoot("GET /stations", "station-service"));

        Set<String> approvedKeys = new LinkedHashSet<>();
        invokeDedup("TEST: PHASE 2.5", scenarios, approvedKeys);

        assertEquals("Three identical 1-root scenarios should collapse to one survivor",
                1, scenarios.size());
        assertTrue("Canonical key for GET /stations must be recorded in approvedApiKeys",
                approvedKeys.contains("GET__stations"));
        assertTrue("Surviving scenario must be tagged approvedInDedupPass=true",
                scenarios.get(0).isApprovedInDedupPass());
    }

    @Test
    public void phase35PreservesShatteredComponents() throws Exception {
        // Phase 2.5 input:
        //   A — single-root GET /stations
        //   B — two-root [POST /trip, GET /stations]  (the multi-root is preserved)
        WorkflowScenario a = singleRoot("GET /stations", "station-service");
        WorkflowScenario b = twoRoot("POST /trip", "trip-service",
                                     "GET /stations", "station-service");

        List<WorkflowScenario> scenarios = new ArrayList<>();
        scenarios.add(a);
        scenarios.add(b);

        Set<String> approvedKeys = new LinkedHashSet<>();
        invokeDedup("TEST: PHASE 2.5", scenarios, approvedKeys);

        // A and B both survive Phase 2.5 (A is a 1-root duplicate of nothing; B is multi-root).
        assertEquals("Phase 2.5 keeps both A and B (multi-root B always passes through)",
                2, scenarios.size());
        assertTrue("Phase 2.5 must approve A by API key",
                approvedKeys.contains("GET__stations"));
        assertTrue("A's approved tag must be set after Phase 2.5",
                a.isApprovedInDedupPass());
        assertTrue("B (multi-root) is tagged approved so Phase 3.5 lets it through",
                b.isApprovedInDedupPass());

        // Simulate Phase 3 shattering of B → B' (GET /stations) + B'' (POST /trip).
        // The optimizer propagates the parent's approvedInDedupPass tag to every child.
        WorkflowScenario bShatteredStations =
                singleRoot("GET /stations", "station-service");
        bShatteredStations.setApprovedInDedupPass(b.isApprovedInDedupPass());

        WorkflowScenario bShatteredTrip =
                singleRoot("POST /trip", "trip-service");
        bShatteredTrip.setApprovedInDedupPass(b.isApprovedInDedupPass());

        // Replace B with its two shattered children — mimicking ScenarioOptimizer's output.
        scenarios.remove(b);
        scenarios.add(bShatteredStations);
        scenarios.add(bShatteredTrip);

        // Phase 3.5 invocation, sharing the SAME approvedApiKeys set as Phase 2.5.
        invokeDedup("TEST: PHASE 3.5", scenarios, approvedKeys);

        // Both shattered children carry the propagated approval tag, so the
        // pass-through guard keeps them BOTH.
        assertEquals("Phase 3.5 must keep A, B' (tag-approved), and B'' (tag-approved)",
                3, scenarios.size());
        assertTrue("A still survives", scenarios.contains(a));
        assertTrue("B' (shattered GET /stations) survives via the propagated approval tag",
                scenarios.contains(bShatteredStations));
        assertTrue("B'' (shattered POST /trip) survives — distinct canonical key",
                scenarios.contains(bShatteredTrip));
    }

    /**
     * Defence in depth: when shattering produces a NEW 1-root component that
     * did NOT inherit the approval tag (regression in propagation, future
     * optimizer path), the dedup pass still drops the duplicate by canonical
     * key against the shared {@code approvedApiKeys} set.
     */
    @Test
    public void phase35DropsUntaggedDuplicateByCanonicalKey() throws Exception {
        // Seed approvedApiKeys as if Phase 2.5 approved GET /stations earlier.
        List<WorkflowScenario> phase25 = new ArrayList<>();
        phase25.add(singleRoot("GET /stations", "station-service"));
        Set<String> approvedKeys = new LinkedHashSet<>();
        invokeDedup("TEST: PHASE 2.5", phase25, approvedKeys);
        assertEquals(1, phase25.size());

        // Phase 3 emits a NEW 1-root scenario for GET /stations WITHOUT the
        // tag — simulates a code path that fails to propagate.
        WorkflowScenario shatteredDup = singleRoot("GET /stations", "station-service");
        assertFalse("Untagged shatter child starts with default tag=false",
                shatteredDup.isApprovedInDedupPass());

        WorkflowScenario shatteredNew = singleRoot("POST /trip", "trip-service");

        List<WorkflowScenario> phase35Input = new ArrayList<>();
        phase35Input.add(shatteredDup);
        phase35Input.add(shatteredNew);

        invokeDedup("TEST: PHASE 3.5", phase35Input, approvedKeys);

        assertEquals("Untagged duplicate of an already-approved key is dropped",
                1, phase35Input.size());
        assertTrue("The genuinely-new POST /trip survives and gets approved",
                phase35Input.contains(shatteredNew));
        assertTrue("POST /trip canonical key now recorded",
                approvedKeys.contains("POST__trip"));
    }
}
