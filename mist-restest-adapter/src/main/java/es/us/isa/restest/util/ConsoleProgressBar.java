package es.us.isa.restest.util;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Unified pipeline progress bar for RESTest.
 *
 * <h2>Design goals</h2>
 * <ol>
 *   <li><b>Global pipeline view.</b> A single bar shows overall pipeline progress
 *       (pool generation → fault pools → variants → writing → test execution →
 *       enhancement). Each major phase carries a preassigned weight summing to 100.</li>
 *   <li><b>Nested phase stack.</b> Inner phases like "Pool Params" inside "Pool Gen"
 *       no longer clobber the outer phase — they become sub-progress. The outer phase
 *       remains the one contributing weight to the overall bar; the inner phase is
 *       shown as detail in the "current item" field.</li>
 *   <li><b>Sticky bottom.</b> A {@link PrintStream} wrapper is installed on
 *       {@code System.out} and {@code System.err}. Every log line that ends with
 *       {@code \n} triggers a redraw, so the bar always appears on the line
 *       immediately below the latest log output rather than scrolling away.</li>
 * </ol>
 *
 * <h2>Public API (unchanged)</h2>
 * <pre>{@code
 *   ConsoleProgressBar.begin("Pool Gen", 10);   // recognized phase → overall tracker
 *   ConsoleProgressBar.update("GET /stations"); // increments counter, redraws
 *   ConsoleProgressBar.complete();              // marks phase done
 * }</pre>
 *
 * <h2>Auto-disable</h2>
 * <ul>
 *   <li>{@code -Drestest.progress.bar=false} → disabled</li>
 *   <li>{@code CI} env var set → disabled</li>
 *   <li>otherwise → enabled (including IntelliJ / IDE run consoles)</li>
 * </ul>
 */
public final class ConsoleProgressBar {

    // ────────────────────────────────────────────────────────────────────────
    //  Phase catalog — weights must sum to 100
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Top-level phases in the MST pipeline.  Each carries a weight (0-100);
     * the bar displays the weighted sum as the overall percentage.
     */
    public enum Phase {
        POOL_GEN      ("Pool Gen",       3),
        FAULTY_POOLS  ("Faulty Pools",   7),
        VARIANT_GEN   ("Variant Gen",    5),
        WRITING       ("Writing Tests",  2),
        ENHANCE_ROUNDS("Enhance Rounds", 83);   // dominates because tests execute here

        final String label;
        final int weight;

        Phase(String label, int weight) { this.label = label; this.weight = weight; }

        /** Map the phase-name string used at call sites → enum.  Null when nested/unknown. */
        static Phase fromLabel(String s) {
            if (s == null) return null;
            for (Phase p : values()) if (p.label.equalsIgnoreCase(s)) return p;
            return null;
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    //  State
    // ────────────────────────────────────────────────────────────────────────

    private static final Object LOCK = new Object();
    private static final int BAR_WIDTH = 30;
    private static final int BAR_LINE_WIDTH = 160; // pad-to width — must exceed any bar variant
    private static final String FILLED = "\u2588";  // █
    private static final String EMPTY  = "\u2591";  // ░

    private static final boolean enabled = detectEnabled();

    /** Raw FileDescriptor stream that BYPASSES our own wrapper, used by the bar to draw itself. */
    private static final PrintStream RAW_STDOUT =
            new PrintStream(new FileOutputStream(FileDescriptor.out), true);

    /** Per-thread guard to prevent infinite recursion when the wrapper is used by the bar itself. */
    private static final ThreadLocal<Boolean> SUPPRESS_REDRAW = ThreadLocal.withInitial(() -> Boolean.FALSE);

    /** Nested phase frames — top of stack is the currently-displayed phase. */
    private static final Deque<Frame> STACK = new ArrayDeque<>();

    /** Fraction (0..1) of each top-level phase that has completed. */
    private static final Map<Phase, Double> completedFraction = new EnumMap<>(Phase.class);

    private static long pipelineStartNanos = 0;
    private static boolean stickyInstalled = false;
    /** Monotonic clamp so the bar never regresses when inner phases transition. */
    private static double lastOverallFraction = 0.0;

    private ConsoleProgressBar() {}

    /** One nested phase frame (outer or inner). */
    private static final class Frame {
        final String label;
        final Phase phase;      // null for nested/unknown
        int total;
        int current;
        String itemName = "";
        final long startNanos = System.nanoTime();

        Frame(String label, int total) {
            this.label = label;
            this.total = Math.max(total, 0);
            this.phase = Phase.fromLabel(label);
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Detection
    // ────────────────────────────────────────────────────────────────────────

    private static boolean detectEnabled() {
        String prop = System.getProperty("restest.progress.bar");
        if (prop != null) return Boolean.parseBoolean(prop);
        if (System.getenv("CI") != null) return false;
        return true;
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Public API (unchanged signatures)
    // ────────────────────────────────────────────────────────────────────────

    public static void begin(String phase, int totalItems) {
        if (!enabled) return;
        synchronized (LOCK) {
            installStickyOutput();
            if (pipelineStartNanos == 0) pipelineStartNanos = System.nanoTime();
            STACK.push(new Frame(phase, totalItems));
            render();
        }
    }

    public static void update(String itemName) {
        if (!enabled) return;
        synchronized (LOCK) {
            Frame top = STACK.peek();
            if (top == null) return;
            top.current = Math.min(top.current + 1, top.total);
            top.itemName = itemName != null ? itemName : "";
            render();
        }
    }

    /** Sub-progress without incrementing the outer phase (rarely needed now). */
    public static void update(int cur, int tot, String itemName) {
        if (!enabled) return;
        synchronized (LOCK) {
            Frame top = STACK.peek();
            if (top == null) return;
            top.current = Math.min(cur, tot);
            top.total   = tot;
            top.itemName = itemName != null ? itemName : "";
            render();
        }
    }

    public static void complete() {
        if (!enabled) return;
        synchronized (LOCK) {
            Frame finished = STACK.poll();
            if (finished == null) return;

            // If this is a recognized top-level phase, credit its full weight.
            if (finished.phase != null) {
                completedFraction.put(finished.phase, 1.0);
            }

            if (STACK.isEmpty()) {
                // Last frame popped — render final state, then newline.
                render();
                RAW_STDOUT.println();
                RAW_STDOUT.flush();
            } else {
                render();
            }
        }
    }

    public static boolean isActive() {
        synchronized (LOCK) {
            return !STACK.isEmpty();
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Rendering
    // ────────────────────────────────────────────────────────────────────────

    /** Returns the fully-formatted bar string (no leading \r). Caller adds framing. */
    static String currentBarString() {
        synchronized (LOCK) {
            if (STACK.isEmpty()) return "";
            return buildBar();
        }
    }

    private static void render() {
        if (STACK.isEmpty()) return;
        String bar = buildBar();
        SUPPRESS_REDRAW.set(Boolean.TRUE);
        try {
            // \r returns cursor to col 0; the bar is padded to BAR_LINE_WIDTH chars
            // so it fully overwrites any previous bar content without needing ANSI.
            RAW_STDOUT.print('\r');
            RAW_STDOUT.print(bar);
            RAW_STDOUT.flush();
        } finally {
            SUPPRESS_REDRAW.set(Boolean.FALSE);
        }
    }

    private static String buildBar() {
        double overall = computeOverallFraction();
        int overallPct = (int) Math.round(overall * 100.0);
        int filled = (int) Math.round(overall * BAR_WIDTH);
        if (filled < 0) filled = 0;
        if (filled > BAR_WIDTH) filled = BAR_WIDTH;

        StringBuilder bar = new StringBuilder(160);
        bar.append('[');
        for (int i = 0; i < filled; i++)       bar.append(FILLED);
        for (int i = 0; i < BAR_WIDTH - filled; i++) bar.append(EMPTY);
        bar.append(']');
        bar.append(' ').append(String.format(Locale.ROOT, "%3d%%", overallPct)).append(" overall");

        // Locate outer recognized phase AND innermost frame, same as in
        // computeOverallFraction() so the bar and the % always tell a consistent story.
        Frame outerRecognized = null;
        Frame innermost = null;
        for (Frame f : STACK) {
            if (innermost == null) innermost = f;
            if (f.phase != null && !completedFraction.containsKey(f.phase)) {
                outerRecognized = f;
            }
        }

        // Phase position in pipeline (e.g. "Phase 2/5 Faulty Pools 1/5")
        if (outerRecognized != null) {
            int phaseNumber = outerRecognized.phase.ordinal() + 1;
            int totalPhases = Phase.values().length;
            bar.append(" | Phase ").append(phaseNumber).append('/').append(totalPhases)
               .append(' ').append(outerRecognized.phase.label);
            if (outerRecognized.total > 0) {
                bar.append(' ').append(outerRecognized.current).append('/').append(outerRecognized.total);
            }
        }

        // Inner detail (only when nested under an outer phase). Tells the user
        // what fine-grained work is happening right now.
        if (innermost != null && innermost != outerRecognized) {
            bar.append(" | inner: ").append(innermost.label);
            if (innermost.total > 0) {
                bar.append(' ').append(innermost.current).append('/').append(innermost.total);
            }
            if (innermost.itemName != null && !innermost.itemName.isEmpty()) {
                bar.append(" → ").append(truncate(innermost.itemName, 40));
            }
        } else if (outerRecognized != null && outerRecognized.itemName != null && !outerRecognized.itemName.isEmpty()) {
            // No inner phase — show the outer phase's current item inline.
            bar.append(" → ").append(truncate(outerRecognized.itemName, 40));
        }

        // Elapsed + ETA (only when we have enough signal)
        long elapsedNs = System.nanoTime() - pipelineStartNanos;
        bar.append(" | ").append(formatDuration(elapsedNs));
        String eta = computeEta(elapsedNs, overall);
        if (eta != null) bar.append(" | ETA ").append(eta);

        // Pad to a fixed width to fully overwrite any previous longer line.
        while (bar.length() < BAR_LINE_WIDTH) bar.append(' ');
        return bar.toString();
    }

    /**
     * Overall pipeline fraction, normalized against the fixed 100-unit weight budget
     * of {@link Phase} so the bar honestly reflects end-to-end progress (not just
     * progress through phases we've encountered so far).
     *
     * <p>Formula:
     * <pre>
     *   overall = (Σ completedWeight × completionFraction
     *            + currentPhaseWeight × (outerCurrent/outerTotal
     *                                    + innerCurrent/(innerTotal × outerTotal))) / 100
     * </pre>
     * The inner-phase term credits fine-grained progress during long-running inner
     * loops (e.g. 8 000 test executions inside a 2-round enhancement phase) so the
     * bar moves smoothly instead of jumping in huge slot-sized steps.
     *
     * <p>A monotonic clamp prevents regressions when one inner phase completes and
     * the next one (with its own progress counter) begins.
     */
    private static double computeOverallFraction() {
        double accumulated = 0;

        // Fully-completed top-level phases (credit full weight)
        for (Map.Entry<Phase, Double> e : completedFraction.entrySet()) {
            accumulated += e.getKey().weight * e.getValue();
        }

        // Walk the stack to find:
        //   outermost recognized Phase (= the one contributing weight right now)
        //   AND the innermost frame (= fine-grained progress signal, if nested)
        Frame outerRecognized = null;
        Frame innermost = null;
        for (Frame f : STACK) {              // iteration: top-down (newest → oldest)
            if (innermost == null) innermost = f;
            if (f.phase != null && !completedFraction.containsKey(f.phase)) {
                outerRecognized = f;         // keep overwriting → last = outermost
            }
        }

        if (outerRecognized != null) {
            int outerTotal = Math.max(outerRecognized.total, 1);
            double outerFrac = ((double) outerRecognized.current) / outerTotal;

            // If a nested inner phase is in progress, credit its fractional work
            // toward the outer's CURRENT slot (the one not yet ticked).
            if (innermost != null && innermost != outerRecognized && innermost.total > 0) {
                double innerFrac = ((double) innermost.current) / innermost.total;
                outerFrac += innerFrac / outerTotal;
            }
            if (outerFrac > 1.0) outerFrac = 1.0;

            accumulated += outerRecognized.phase.weight * outerFrac;
        }

        double overall = Math.max(0.0, Math.min(1.0, accumulated / 100.0));
        // Monotonic clamp — never regress visually
        if (overall < lastOverallFraction) overall = lastOverallFraction;
        lastOverallFraction = overall;
        return overall;
    }

    private static String computeEta(long elapsedNs, double overall) {
        if (overall <= 0.02) return null;                 // not enough signal yet
        long totalEstimateNs = (long) (elapsedNs / overall);
        long remainingNs = totalEstimateNs - elapsedNs;
        if (remainingNs <= 0) return null;
        return formatDuration(remainingNs);
    }

    private static String formatDuration(long nanos) {
        long totalSeconds = TimeUnit.NANOSECONDS.toSeconds(nanos);
        long hours = totalSeconds / 3600;
        long mins  = (totalSeconds % 3600) / 60;
        long secs  = totalSeconds % 60;
        if (hours > 0) return hours + "h " + mins + "m";
        if (mins > 0)  return mins + "m " + secs + "s";
        return secs + "s";
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }

    // ────────────────────────────────────────────────────────────────────────
    //  Sticky-bottom: System.out/err wrapper that redraws the bar after each line.
    // ────────────────────────────────────────────────────────────────────────

    /** Must be called under LOCK.  Idempotent. */
    private static void installStickyOutput() {
        if (stickyInstalled) return;
        stickyInstalled = true;
        System.setOut(new ProgressAwarePrintStream(System.out));
        System.setErr(new ProgressAwarePrintStream(System.err));
    }

    /**
     * Forwards writes to the real underlying stream, then — whenever a write ends
     * with '\n' and the progress bar is active — prints a fresh copy of the bar
     * (without a trailing newline) so it appears as the last line on the terminal.
     */
    private static final class ProgressAwarePrintStream extends PrintStream {
        private final PrintStream delegate;

        ProgressAwarePrintStream(PrintStream delegate) {
            super(delegate, true);
            this.delegate = delegate;
        }

        @Override
        public void write(int b) {
            delegate.write(b);
            if (b == '\n') maybeRedraw();
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            delegate.write(buf, off, len);
            if (len > 0 && buf[off + len - 1] == '\n') maybeRedraw();
        }

        @Override
        public void write(byte[] buf) throws IOException {
            delegate.write(buf);
            if (buf.length > 0 && buf[buf.length - 1] == '\n') maybeRedraw();
        }

        @Override
        public void flush() { delegate.flush(); }

        private void maybeRedraw() {
            if (Boolean.TRUE.equals(SUPPRESS_REDRAW.get())) return;
            String bar;
            synchronized (LOCK) {
                if (STACK.isEmpty()) return;
                bar = buildBar();
            }
            SUPPRESS_REDRAW.set(Boolean.TRUE);
            try {
                // CRITICAL: write via RAW_STDOUT (FileDescriptor.out) — NOT via delegate.
                // The app installs a LoggerStream wrapper on System.out that pipes every
                // write to log4j, which would embed the bar into the log file with a
                // timestamp prefix (e.g. "2026-04-16 INFO stdout:36 - [bar...]").
                // Bypassing via the OS-level stream keeps the bar visual-only.
                //
                // No leading \r: we're immediately after a log line's '\n', so the
                // cursor is already on a fresh line. The bar is padded so the next
                // render()'s '\r + bar' will cleanly overwrite it.
                RAW_STDOUT.print(bar);
                RAW_STDOUT.flush();
            } finally {
                SUPPRESS_REDRAW.set(Boolean.FALSE);
            }
        }
    }
}
