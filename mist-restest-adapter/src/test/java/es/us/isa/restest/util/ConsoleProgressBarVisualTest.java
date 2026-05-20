package es.us.isa.restest.util;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Pins the visual contract of {@link ConsoleProgressBar#currentBarString()}:
 * spinner frame, fractional fill character, percent, phase label, and ETA
 * all show up where expected. The bar's stateful nature (static stack +
 * monotonic clamp) makes these tests stateful too — {@link #reset()} drains
 * the stack between tests so order does not matter.
 */
public class ConsoleProgressBarVisualTest {

    @After
    public void reset() {
        // Drain any frames left by a prior test so the next test starts cold.
        while (ConsoleProgressBar.isActive()) {
            ConsoleProgressBar.complete();
        }
    }

    @Test
    public void barContainsSpinnerFrame() {
        ConsoleProgressBar.begin("Variant Gen", 10);
        String bar = ConsoleProgressBar.currentBarString();
        // At least one of the Braille spinner frames must appear.
        boolean foundSpinner = false;
        String[] frames = { "⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏" };
        for (String f : frames) {
            if (bar.contains(f)) { foundSpinner = true; break; }
        }
        assertTrue("bar must contain a spinner frame: " + bar, foundSpinner);
    }

    @Test
    public void barContainsPercentAndPhaseLabel() {
        ConsoleProgressBar.begin("Variant Gen", 10);
        ConsoleProgressBar.update("v1");
        String bar = ConsoleProgressBar.currentBarString();
        // Percent appears as e.g. "  0%" or "  1%" (3-wide format) and phase
        // labels are emitted verbatim.
        assertTrue("bar must contain a percent token: " + bar, bar.matches("(?s).*\\d{1,3}%.*"));
        assertTrue("bar must contain phase label: " + bar, bar.contains("Variant Gen"));
    }

    @Test
    public void barContainsCounterWhenTotalIsPositive() {
        ConsoleProgressBar.begin("Variant Gen", 10);
        ConsoleProgressBar.update("v1");
        ConsoleProgressBar.update("v2");
        String bar = ConsoleProgressBar.currentBarString();
        // After 2 updates of a 10-item phase, the "2/10" counter should be
        // somewhere in the bar.
        assertTrue("bar must contain '2/10' counter: " + bar, bar.contains("2/10"));
    }

    @Test
    public void barContainsFilledBlockGlyph() {
        ConsoleProgressBar.begin("Variant Gen", 4);
        // Advance to make the fill non-empty
        ConsoleProgressBar.update("v1");
        ConsoleProgressBar.update("v2");
        String bar = ConsoleProgressBar.currentBarString();
        // Full block char must appear somewhere (the fill region).
        assertTrue("bar must contain at least one full-block glyph: " + bar,
                bar.contains("█"));
    }

    @Test
    public void emptyStackProducesEmptyString() {
        // No begin() called yet → no frames → empty string.
        // (Note: prior tests may have started + completed; reset() handled that.)
        assertEquals("", ConsoleProgressBar.currentBarString());
    }

    @Test
    public void completeRemovesFrameFromStack() {
        ConsoleProgressBar.begin("Variant Gen", 5);
        assertTrue(ConsoleProgressBar.isActive());
        ConsoleProgressBar.complete();
        assertFalse("isActive must be false after complete()", ConsoleProgressBar.isActive());
    }
}
