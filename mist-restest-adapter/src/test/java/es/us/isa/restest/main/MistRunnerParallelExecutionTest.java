package es.us.isa.restest.main;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the parallel-execution contract used by {@link MistRunner} when
 * {@code mst.test.parallelism} > 1: each test class runs on its own
 * {@link JUnitCore} on a thread from a fixed pool, and per-class {@link Result}
 * counters are summed into a single aggregate. We test the *pattern* here
 * (not MistRunner.run() directly — that method has too much surrounding setup
 * to invoke in isolation), so any regression in MistRunner's aggregation
 * arithmetic or its parallel dispatch shape will be caught by porting the
 * same shape into this test.
 */
public class MistRunnerParallelExecutionTest {

    // ── Synthetic test classes ─────────────────────────────────────────────────
    // Each carries a measurable Thread.sleep so wall-clock distinguishes
    // sequential from parallel execution. Not @Ignored — JUnit picks them up.

    public static class SleepyTestA {
        @Test public void test() throws InterruptedException { Thread.sleep(300); }
    }
    public static class SleepyTestB {
        @Test public void test() throws InterruptedException { Thread.sleep(300); }
    }
    public static class SleepyTestC {
        @Test public void test() throws InterruptedException { Thread.sleep(300); }
    }
    public static class FailingTest {
        @Test public void test() { throw new AssertionError("intentional"); }
    }

    @Test
    public void parallelExecution_aggregatesPassCounts() throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(SleepyTestA.class);
        classes.add(SleepyTestB.class);
        classes.add(SleepyTestC.class);

        long wallStart = System.currentTimeMillis();
        AggregatedResult agg = runParallel(classes, /*parallelism*/ 3);
        long wall = System.currentTimeMillis() - wallStart;

        // 3 × 300ms sequential = 900ms; 3-thread parallel should be < 700ms even with
        // JUnit/Allure overhead. Headroom prevents false-failure on slow CI.
        assertTrue("parallel wall-clock (" + wall + " ms) should be < 700ms — sequential would be ~900ms",
                wall < 700);
        assertEquals(3, agg.runCount);
        assertEquals(0, agg.failureCount);
    }

    @Test
    public void parallelExecution_aggregatesFailures() throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(SleepyTestA.class);
        classes.add(FailingTest.class);
        classes.add(SleepyTestC.class);

        AggregatedResult agg = runParallel(classes, /*parallelism*/ 2);

        assertEquals("3 tests ran across 3 classes", 3, agg.runCount);
        assertEquals("1 failure expected", 1, agg.failureCount);
        assertEquals(1, agg.failures.size());
        assertTrue("failure should be from FailingTest",
                agg.failures.get(0).getDescription().getClassName().endsWith("FailingTest"));
    }

    @Test
    public void sequentialPath_preservesSingleClassResultSemantics() {
        // Mirror MistRunner's parallelism<=1 branch: one JUnitCore.run(allClasses)
        // returns one Result whose counters cover the whole batch.
        Result r = new JUnitCore().run(SleepyTestA.class, SleepyTestB.class);
        assertEquals(2, r.getRunCount());
        assertEquals(0, r.getFailureCount());
    }

    // ── Aggregation harness mirroring MistRunner's parallel branch ─────────────

    private static AggregatedResult runParallel(List<Class<?>> classes, int parallelism) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(parallelism);
        List<Callable<Result>> tasks = new ArrayList<>();
        for (Class<?> cls : classes) {
            final Class<?> c = cls;
            tasks.add(() -> new JUnitCore().run(c));
        }
        List<Future<Result>> futures = pool.invokeAll(tasks);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        AggregatedResult agg = new AggregatedResult();
        for (Future<Result> f : futures) {
            Result r = f.get();
            agg.runCount += r.getRunCount();
            agg.failureCount += r.getFailureCount();
            agg.ignoreCount += r.getIgnoreCount();
            agg.failures.addAll(r.getFailures());
        }
        return agg;
    }

    private static class AggregatedResult {
        int runCount = 0;
        int failureCount = 0;
        int ignoreCount = 0;
        List<Failure> failures = new ArrayList<>();
    }
}
