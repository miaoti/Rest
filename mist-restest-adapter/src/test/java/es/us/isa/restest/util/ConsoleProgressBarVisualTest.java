package es.us.isa.restest.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Pins the visual contract of {@link ConsoleProgressBar#currentBarString()}:
 * spinner frame, percent, phase label, counter, and filled-block glyph all
 * appear where expected. The bar's stateful nature (static stack +
 * completedFraction map + monotonic clamp) makes these tests stateful too;
 * {@link #reset()} drains every static field before each test so they pass
 * in any order without leaking phase-completed state.
 */
public class ConsoleProgressBarVisualTest {

    @Before
    public void reset() {
        ConsoleProgressBar.resetForTesting();
    }

    @Test
    public void barContainsSpinnerFrame() {
        ConsoleProgressBar.begin("Variants", 10);
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
        ConsoleProgressBar.begin("Variants", 10);
        ConsoleProgressBar.update("v1");
        String bar = ConsoleProgressBar.currentBarString();
        assertTrue("bar must contain a percent token: " + bar, bar.matches("(?s).*\\d{1,3}%.*"));
        assertTrue("bar must contain phase label 'Variants': " + bar, bar.contains("Variants"));
    }

    @Test
    public void barContainsCounterWhenTotalIsPositive() {
        ConsoleProgressBar.begin("Variants", 10);
        ConsoleProgressBar.update("v1");
        ConsoleProgressBar.update("v2");
        String bar = ConsoleProgressBar.currentBarString();
        assertTrue("bar must contain '2/10' counter: " + bar, bar.contains("2/10"));
    }

    @Test
    public void barContainsFilledBlockGlyph() {
        ConsoleProgressBar.begin("Variants", 4);
        ConsoleProgressBar.update("v1");
        ConsoleProgressBar.update("v2");
        String bar = ConsoleProgressBar.currentBarString();
        assertTrue("bar must contain at least one full-block glyph: " + bar,
                bar.contains("█"));
    }

    @Test
    public void emptyStackProducesEmptyString() {
        assertEquals("", ConsoleProgressBar.currentBarString());
    }

    @Test
    public void completeRemovesFrameFromStack() {
        ConsoleProgressBar.begin("Variants", 5);
        assertTrue(ConsoleProgressBar.isActive());
        ConsoleProgressBar.complete();
        assertFalse("isActive must be false after complete()", ConsoleProgressBar.isActive());
    }

    @Test
    public void legacyPhaseLabelStillResolves() {
        // Existing call sites pass the old "Variant Gen" / "Writing Tests" /
        // "Enhance Rounds" strings. The Phase.fromLabel alias map keeps them
        // recognized so the outer phase still contributes weight to the
        // overall percentage and shows up in the bar.
        ConsoleProgressBar.begin("Variant Gen", 5);
        String bar = ConsoleProgressBar.currentBarString();
        // The bar emits the canonical short label ("Variants"), not the
        // legacy long form passed in.
        assertTrue("legacy 'Variant Gen' must map to current 'Variants' label: " + bar,
                bar.contains("Variants"));
    }
}
