package es.us.isa.restest.main;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Pins {@link MistRunner#resolveTestParallelism(int, String, String, boolean)}
 * — the smart-default policy for parallel test execution. The contract:
 * <ul>
 *   <li>-D system property beats .properties value beats auto-fallback;</li>
 *   <li>"auto" caps at 8 (or 4 when LLM validation is on), bounded by CPU count;</li>
 *   <li>explicit integers pass through unchanged (no cap);</li>
 *   <li>malformed input degrades to the auto branch rather than throwing;</li>
 *   <li>result is always &ge; 1.</li>
 * </ul>
 */
public class TestParallelismResolutionTest {

    // ─────────────────────────── Priority order ────────────────────────────

    @Test
    public void systemPropertyWinsOverPropertiesFile() {
        assertEquals(16, MistRunner.resolveTestParallelism(32, "16", "4", false));
    }

    @Test
    public void propertiesFileUsedWhenSystemPropertyAbsent() {
        assertEquals(4, MistRunner.resolveTestParallelism(32, null, "4", false));
    }

    @Test
    public void propertiesFileUsedWhenSystemPropertyBlank() {
        assertEquals(4, MistRunner.resolveTestParallelism(32, "  ", "4", false));
    }

    @Test
    public void autoFallbackWhenBothAbsent() {
        // 16 CPUs, no LLM → cap 8 → resolved 8
        assertEquals(8, MistRunner.resolveTestParallelism(16, null, null, false));
    }

    // ───────────────────────── "auto" branch policy ────────────────────────

    @Test
    public void autoCapsAt8WithoutLLM() {
        assertEquals(8, MistRunner.resolveTestParallelism(32, "auto", null, false));
        assertEquals(8, MistRunner.resolveTestParallelism(16, null, "auto", false));
    }

    @Test
    public void autoCapsAt4WithLLM() {
        assertEquals(4, MistRunner.resolveTestParallelism(32, "auto", null, true));
        assertEquals(4, MistRunner.resolveTestParallelism(16, null, "auto", true));
    }

    @Test
    public void autoIsCaseInsensitive() {
        assertEquals(8, MistRunner.resolveTestParallelism(16, "AUTO", null, false));
        assertEquals(8, MistRunner.resolveTestParallelism(16, " Auto ", null, false));
    }

    @Test
    public void autoIsBoundedByAvailableCpus() {
        // 2 CPUs, no LLM → cap 8 but min(2, 8) = 2.
        assertEquals(2, MistRunner.resolveTestParallelism(2, "auto", null, false));
        // 1 CPU edge case (single-core CI container).
        assertEquals(1, MistRunner.resolveTestParallelism(1, "auto", null, false));
    }

    // ─────────────────────────── Explicit integers ─────────────────────────

    @Test
    public void explicitIntegerOverridesCap() {
        // User asked for 24 explicitly, even though auto would cap at 8.
        assertEquals(24, MistRunner.resolveTestParallelism(32, "24", null, false));
        assertEquals(24, MistRunner.resolveTestParallelism(32, null, "24", false));
    }

    @Test
    public void explicitOneStaysSequential() {
        assertEquals(1, MistRunner.resolveTestParallelism(32, "1", null, false));
    }

    @Test
    public void negativeOrZeroFloorsAtOne() {
        assertEquals(1, MistRunner.resolveTestParallelism(32, "0", null, false));
        assertEquals(1, MistRunner.resolveTestParallelism(32, "-4", null, false));
    }

    // ────────────────────────── Malformed input ────────────────────────────

    @Test
    public void malformedSystemPropertyFallsThroughToAuto() {
        // "lots" → NumberFormatException → fall to auto-cap policy.
        assertEquals(8, MistRunner.resolveTestParallelism(16, "lots", null, false));
        assertEquals(4, MistRunner.resolveTestParallelism(16, "lots", null, true));
    }

    // ──────────────────────── parseBooleanProperty ─────────────────────────

    @Test
    public void parseBooleanRespectsPriority() {
        assertTrue(MistRunner.parseBooleanProperty("true", "false", false));
        assertFalse(MistRunner.parseBooleanProperty("false", "true", true));
    }

    @Test
    public void parseBooleanFallsThroughToProperties() {
        assertTrue(MistRunner.parseBooleanProperty(null, "true", false));
        assertTrue(MistRunner.parseBooleanProperty("  ", "true", false));
    }

    @Test
    public void parseBooleanReturnsDefaultWhenBothAbsent() {
        assertFalse(MistRunner.parseBooleanProperty(null, null, false));
        assertTrue(MistRunner.parseBooleanProperty(null, null, true));
    }
}
