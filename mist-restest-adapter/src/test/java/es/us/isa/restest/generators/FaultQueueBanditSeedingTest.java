package es.us.isa.restest.generators;

import es.us.isa.restest.inputs.smart.InputFetchRegistry;
import es.us.isa.restest.inputs.smart.ParameterError;
import io.mist.core.bandit.ThompsonScheduler;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Pins the contract between {@link MultiServiceTestCaseGenerator#seedBanditFromRegistry}
 * and {@link MultiServiceTestCaseGenerator#rankWithBandit}: a parameter
 * with recorded errors in the {@link InputFetchRegistry} ends up with
 * α &gt; β in the scheduler, which biases the ranked queue to put it
 * near the front. The Beta-sample math itself is covered by
 * {@code io.mist.core.bandit.ThompsonSchedulerTest}.
 */
public class FaultQueueBanditSeedingTest {

    private static MultiServiceTestCaseGenerator.FaultTarget target(String api, String param, String type) {
        return new MultiServiceTestCaseGenerator.FaultTarget(
                1, api, param, "query", type, /*value*/ "x");
    }

    @Test
    public void nullRegistryLeavesBanditCold() {
        ThompsonScheduler bandit = new ThompsonScheduler(new Random(1));
        List<MultiServiceTestCaseGenerator.FaultTarget> q = Arrays.asList(
                target("POST_/a", "p1", "OVERFLOW"));
        MultiServiceTestCaseGenerator.seedBanditFromRegistry(bandit, q, null);
        double[] c = bandit.counters(MultiServiceTestCaseGenerator.banditKey(q.get(0)));
        assertEquals(1.0, c[0], 1e-9);  // untouched prior
        assertEquals(1.0, c[1], 1e-9);
    }

    @Test
    public void registryWithKnownErrorsRaisesAlpha() {
        InputFetchRegistry registry = new InputFetchRegistry();
        registry.addParameterError("POST_/orders", "userId",
                new ParameterError("OVERFLOW", "id too long", "POST_/orders", "userId"));
        registry.addParameterError("POST_/orders", "userId",
                new ParameterError("BOUNDARY_VIOLATION", "id negative", "POST_/orders", "userId"));

        ThompsonScheduler bandit = new ThompsonScheduler(new Random(2));
        List<MultiServiceTestCaseGenerator.FaultTarget> q = Arrays.asList(
                target("POST_/orders", "userId", "OVERFLOW"),
                target("POST_/orders", "unknownParam", "OVERFLOW"));
        MultiServiceTestCaseGenerator.seedBanditFromRegistry(bandit, q, registry);

        double[] known   = bandit.counters(MultiServiceTestCaseGenerator.banditKey(q.get(0)));
        double[] unknown = bandit.counters(MultiServiceTestCaseGenerator.banditKey(q.get(1)));
        assertEquals(3.0, known[0],   1e-9);  // 1 prior + 2 errors
        assertEquals(1.0, known[1],   1e-9);
        assertEquals(1.0, unknown[0], 1e-9);  // untouched
        assertEquals(1.0, unknown[1], 1e-9);
    }

    @Test
    public void registrySeededBanditFavoursKnownBuggyTargetInRanking() {
        // A registry with even one recorded error per target tips the bandit
        // toward that target in the ranked queue: known α = 1 + #errors,
        // unknown α = 1 (uniform prior). The empirical dominance over many
        // random seeds is bounded by the conjugate posterior, not 100%.
        // Use registry calls with semantically-distinct reasons so the
        // semantic-dedup gate in addParameterError keeps each one as a
        // separate observation rather than collapsing them.
        InputFetchRegistry registry = new InputFetchRegistry();
        String[] reasons = {
                "must be alphabetic",
                "exceeds maximum length 32",
                "value is reserved by the server",
                "format does not match UUID v4"
        };
        for (String reason : reasons) {
            registry.addParameterError("POST_/orders", "userId",
                    new ParameterError("OVERFLOW", reason, "POST_/orders", "userId"));
        }

        List<MultiServiceTestCaseGenerator.FaultTarget> q = Arrays.asList(
                target("POST_/orders", "userId", "OVERFLOW"),
                target("POST_/orders", "shippingNote", "OVERFLOW"));

        int knownFirst = 0;
        int trials = 400;
        for (int i = 0; i < trials; i++) {
            ThompsonScheduler bandit = new ThompsonScheduler(new Random(100 + i));
            MultiServiceTestCaseGenerator.seedBanditFromRegistry(bandit, q, registry);
            List<MultiServiceTestCaseGenerator.FaultTarget> ranked = bandit.rank(q, MultiServiceTestCaseGenerator::banditKey);
            if ("userId".equals(ranked.get(0).paramName)) knownFirst++;
        }
        // Bound is loose by design: with α≈5 vs α≈1 (β=1 both), the known
        // arm wins ~85-95% of draws depending on the dedup outcome. Anything
        // comfortably above 50% (random) demonstrates the bandit's effect.
        assertTrue("known-buggy target should dominate top rank: " + knownFirst + "/" + trials,
                knownFirst > (int) (0.65 * trials));
    }

    @Test
    public void explicitlySeededBanditDominatesRankingDeterministically() {
        // Same property as above, but skipping the registry to isolate the
        // bandit math: α=11 vs α=1 should dominate the top slot >= 95% of trials.
        List<MultiServiceTestCaseGenerator.FaultTarget> q = Arrays.asList(
                target("POST_/x", "high", "OVERFLOW"),
                target("POST_/x", "low",  "OVERFLOW"));

        int highFirst = 0;
        int trials = 200;
        for (int i = 0; i < trials; i++) {
            ThompsonScheduler bandit = new ThompsonScheduler(new Random(200 + i));
            bandit.seed(MultiServiceTestCaseGenerator.banditKey(q.get(0)), 11.0, 1.0);
            // q.get(1) keeps the (1, 1) prior implicitly.
            List<MultiServiceTestCaseGenerator.FaultTarget> ranked = bandit.rank(q, MultiServiceTestCaseGenerator::banditKey);
            if ("high".equals(ranked.get(0).paramName)) highFirst++;
        }
        // Theoretical P(Beta(11,1) > Beta(1,1)) = 11/12 ≈ 0.917; 200-trial 95% CI
        // is roughly [0.88, 0.95], so 0.85 is a safe bound.
        assertTrue("strongly seeded target should dominate top rank: " + highFirst + "/" + trials,
                highFirst > (int) (0.85 * trials));
    }

    @Test
    public void rankingPreservesLength() {
        InputFetchRegistry registry = new InputFetchRegistry();
        List<MultiServiceTestCaseGenerator.FaultTarget> q = Arrays.asList(
                target("POST_/a", "p1", "OVERFLOW"),
                target("POST_/a", "p2", "OVERFLOW"),
                target("POST_/b", "p3", "BOUNDARY_VIOLATION"));
        ThompsonScheduler bandit = new ThompsonScheduler(new Random(4));
        MultiServiceTestCaseGenerator.seedBanditFromRegistry(bandit, q, registry);
        List<MultiServiceTestCaseGenerator.FaultTarget> ranked = bandit.rank(q);
        assertEquals(q.size(), ranked.size());
    }

    @Test
    public void banditKeyIsTuple() {
        // Same param name across two endpoints / fault types must hash to
        // distinct bandit arms — otherwise observations on one would mis-credit
        // the other.
        String k1 = MultiServiceTestCaseGenerator.banditKey(target("POST_/a", "id", "OVERFLOW"));
        String k2 = MultiServiceTestCaseGenerator.banditKey(target("POST_/b", "id", "OVERFLOW"));
        String k3 = MultiServiceTestCaseGenerator.banditKey(target("POST_/a", "id", "BOUNDARY_VIOLATION"));
        assertNotEquals(k1, k2);
        assertNotEquals(k1, k3);
    }
}
