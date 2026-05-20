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
 *       (pool generation -> fault pools -> variants -> writing -> test execution ->
 *       enhancement). Each major phase carries a preassigned weight summing to 100.</li>
 *   <li><b>Nested phase stack.</b> Inner phases like "Pool Params" inside "Pool Gen"
 *       no longer clobber the outer phase. The outer phase remains the one
 *       contributing weight to the overall bar; the inner phase is shown as
 *       detail in the "current item" field.</li>
 *   <li><b>Sticky bottom.</b> A {@link PrintStream} wrapper is installed on
 *       {@code System.out} and {@code System.err}. Every log line that ends with
 *       {@code \n} triggers a redraw so the bar always appears on the line
 *       immediately below the latest log output rather than scrolling away.</li>
 *   <li><b>Polished look.</b> Sub-block characters render fractional fill at
 *       1/8-cell resolution; a Braille spinner rotates per redraw; ANSI colors
 *       group visual sections (filled vs empty bar, percent, phase, ETA);
 *       {@code [2K} clears the line before every redraw so no leftover
 *       characters from a longer previous bar bleed through.</li>
 * </ol>
 *
 * <h2>Public API (unchanged)</h2>
 * <pre>{@code
 *   ConsoleProgressBar.begin("Pool Gen", 10);   // recognized phase -> overall tracker
 *   ConsoleProgressBar.update("GET /stations"); // increments counter, redraws
 *   ConsoleProgressBar.complete();              // marks phase done
 * }</pre>
 *
 * <h2>Auto-disable</h2>
 * <ul>
 *   <li>{@code -Drestest.progress.bar=false} -> bar disabled</li>
 *   <li>{@code CI} env var set -> bar disabled</li>
 *   <li>{@code -Drestest.progress.bar.color=false} or {@code NO_COLOR} env -> colors disabled (bar still renders)</li>
 *   <li>otherwise -> enabled (including IntelliJ / IDE run consoles)</li>
 * </ul>
 */
public final class ConsoleProgressBar {

    // -----------------------------------------------------------------------
    //  Phase catalog (weights sum to 100)
    // -----------------------------------------------------------------------

    public enum Phase {
        POOL_GEN      ("Pool Gen",       3),
        FAULTY_POOLS  ("Faulty Pools",   7),
        VARIANT_GEN   ("Variant Gen",    5),
        WRITING       ("Writing Tests",  2),
        ENHANCE_ROUNDS("Enhance Rounds", 83);   // dominates because tests execute here

        final String label;
        final int weight;

        Phase(String label, int weight) { this.label = label; this.weight = weight; }

        static Phase fromLabel(String s) {
            if (s == null) return null;
            for (Phase p : values()) if (p.label.equalsIgnoreCase(s)) return p;
            return null;
        }
    }

    // -----------------------------------------------------------------------
    //  Visual constants
    // -----------------------------------------------------------------------

    private static final Object LOCK = new Object();
    private static final int BAR_WIDTH = 32;

    private static final String FILLED_BLOCK = "█";  // U+2588 full block

    /**
     * Sub-block characters for fractional fill — bar moves at 1/8-cell
     * resolution rather than jumping a full cell per percent. Index = number
     * of eighths gained beyond the last whole block.
     */
    private static final String[] SUB_BLOCKS = {
        "",         // 0/8
        "▏",   // 1/8 left-one-eighth block
        "▎",   // 2/8 left-quarter block
        "▍",   // 3/8 left-three-eighths block
        "▌",   // 4/8 left-half block
        "▋",   // 5/8 left-five-eighths block
        "▊",   // 6/8 left-three-quarters block
        "▉",   // 7/8 left-seven-eighths block
    };

    /** Braille spinner — one frame per redraw makes the bar feel alive. */
    private static final String[] SPINNER_FRAMES = {
        "⠋", "⠙", "⠹", "⠸",
        "⠼", "⠴", "⠦", "⠧",
        "⠇", "⠏"
    };
    private static int spinnerIdx = 0;

    // ANSI escape sequences. We use line-clear rather than space-padding
    // because (a) cleanup is reliable regardless of bar length, and (b)
    // colored bars contain invisible escape codes that any fixed-width pad
    // would miscount and leave artifacts behind.
    private static final String ESC                = "[";
    private static final String ANSI_CLEAR_LINE    = ESC + "2K";
    private static final String ANSI_CR            = "\r";
    private static final String ANSI_RESET         = ESC + "0m";
    private static final String ANSI_BOLD          = ESC + "1m";
    private static final String ANSI_DIM           = ESC + "2m";
    private static final String ANSI_BRIGHT_CYAN   = ESC + "96m";
    private static final String ANSI_BRIGHT_GREEN  = ESC + "92m";
    private static final String ANSI_YELLOW        = ESC + "33m";
    private static final String ANSI_BRIGHT_WHITE  = ESC + "97m";
    private static final String ANSI_GRAY          = ESC + "90m";

    private static final boolean enabled  = detectEnabled();
    private static final boolean useColor = detectColor();

    // -----------------------------------------------------------------------
    //  State
    // -----------------------------------------------------------------------

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

    // -----------------------------------------------------------------------
    //  Detection
    // -----------------------------------------------------------------------

    private static boolean detectEnabled() {
        String prop = System.getProperty("restest.progress.bar");
        if (prop != null) return Boolean.parseBoolean(prop);
        if (System.getenv("CI") != null) return false;
        return true;
    }

    private static boolean detectColor() {
        // NO_COLOR is the de-facto cross-tool opt-out
        // (see https://no-color.org). Honor it before any other check.
        if (System.getenv("NO_COLOR") != null) return false;
        String prop = System.getProperty("restest.progress.bar.color");
        if (prop != null) return Boolean.parseBoolean(prop);
        // Default ON: IntelliJ Run console, modern terminals, and CI logs all
        // handle ANSI cleanly enough that the gain in readability outweighs
        // the risk of escape leakage. Set restest.progress.bar.color=false to
        // disable for log scrapers that mangle escapes.
        return true;
    }

    private static String color(String code, String s) {
        return useColor ? code + s + ANSI_RESET : s;
    }

    // -----------------------------------------------------------------------
    //  Public API (unchanged signatures)
    // -----------------------------------------------------------------------

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

    // -----------------------------------------------------------------------
    //  Rendering
    // -----------------------------------------------------------------------

    /** Returns the fully-formatted bar string (no leading framing). Caller adds framing. */
    static String currentBarString() {
        synchronized (LOCK) {
            if (STACK.isEmpty()) return "";
            return buildBar();
        }
    }

    private static void render() {
        if (STACK.isEmpty()) return;
        spinnerIdx = (spinnerIdx + 1) % SPINNER_FRAMES.length;
        String bar = buildBar();
        SUPPRESS_REDRAW.set(Boolean.TRUE);
        try {
            // ANSI clear-line + carriage return guarantees the line is empty
            // before we draw, regardless of how long the previous bar was or
            // whether it contained color escapes that confuse byte counters.
            RAW_STDOUT.print(ANSI_CR + ANSI_CLEAR_LINE);
            RAW_STDOUT.print(bar);
            RAW_STDOUT.flush();
        } finally {
            SUPPRESS_REDRAW.set(Boolean.FALSE);
        }
    }

    private static String buildBar() {
        double overall = computeOverallFraction();
        int overallPct = (int) Math.round(overall * 100.0);

        // Fractional fill: compute the exact eighths-of-a-cell and choose
        // the matching sub-block character for the boundary cell.
        double exactFilled = overall * BAR_WIDTH;
        int wholeBlocks = (int) Math.floor(exactFilled);
        int subBlockIdx = (int) Math.round((exactFilled - wholeBlocks) * 8);
        if (subBlockIdx >= SUB_BLOCKS.length) {
            wholeBlocks++;
            subBlockIdx = 0;
        }
        if (wholeBlocks > BAR_WIDTH) wholeBlocks = BAR_WIDTH;
        int emptyCells = BAR_WIDTH - wholeBlocks - (subBlockIdx > 0 ? 1 : 0);
        if (emptyCells < 0) emptyCells = 0;

        StringBuilder filledPart = new StringBuilder(BAR_WIDTH);
        for (int i = 0; i < wholeBlocks; i++) filledPart.append(FILLED_BLOCK);
        if (subBlockIdx > 0) filledPart.append(SUB_BLOCKS[subBlockIdx]);
        StringBuilder emptyPart = new StringBuilder(BAR_WIDTH);
        for (int i = 0; i < emptyCells; i++) emptyPart.append(' ');

        // Locate the outermost recognized Phase AND the innermost frame so the
        // displayed labels are consistent with the percentage math below.
        Frame outerRecognized = null;
        Frame innermost = null;
        for (Frame f : STACK) {
            if (innermost == null) innermost = f;
            if (f.phase != null && !completedFraction.containsKey(f.phase)) {
                outerRecognized = f;
            }
        }

        long elapsedNs = System.nanoTime() - pipelineStartNanos;
        String elapsed = formatDuration(elapsedNs);
        String eta = computeEta(elapsedNs, overall);

        StringBuilder bar = new StringBuilder(256);

        // Spinner — one frame per render. Cyan so it's distinct from the bar.
        bar.append(color(ANSI_BRIGHT_CYAN, SPINNER_FRAMES[spinnerIdx])).append(' ');

        // [ fill / empty ] frame — bright green fill, dim empty.
        bar.append(color(ANSI_GRAY, "["));
        bar.append(color(ANSI_BRIGHT_GREEN, filledPart.toString()));
        bar.append(color(ANSI_DIM,         emptyPart.toString()));
        bar.append(color(ANSI_GRAY, "]"));

        // Percent — bold yellow, fixed-width so it doesn't jitter the layout.
        bar.append(' ').append(color(ANSI_BOLD + ANSI_YELLOW,
                String.format(Locale.ROOT, "%3d%%", overallPct)));

        // Separator chars use box-drawing vertical bars in gray.
        String sep = color(ANSI_GRAY, " │ ");

        // Phase label + current/total.
        if (outerRecognized != null) {
            int phaseNumber = outerRecognized.phase.ordinal() + 1;
            int totalPhases = Phase.values().length;
            bar.append(sep);
            bar.append(color(ANSI_GRAY, "Phase "));
            bar.append(color(ANSI_BRIGHT_WHITE, phaseNumber + "/" + totalPhases));
            bar.append(' ').append(color(ANSI_BRIGHT_CYAN, outerRecognized.phase.label));
            if (outerRecognized.total > 0) {
                bar.append(' ').append(color(ANSI_BRIGHT_WHITE,
                        outerRecognized.current + "/" + outerRecognized.total));
            }
        }

        // Inner detail (when nested).
        if (innermost != null && innermost != outerRecognized) {
            bar.append(sep);
            bar.append(color(ANSI_GRAY, "inner "));
            bar.append(color(ANSI_BRIGHT_CYAN, innermost.label));
            if (innermost.total > 0) {
                bar.append(' ').append(color(ANSI_BRIGHT_WHITE,
                        innermost.current + "/" + innermost.total));
            }
            if (innermost.itemName != null && !innermost.itemName.isEmpty()) {
                bar.append(color(ANSI_DIM, " → " + truncate(innermost.itemName, 40)));
            }
        } else if (outerRecognized != null
                && outerRecognized.itemName != null
                && !outerRecognized.itemName.isEmpty()) {
            bar.append(color(ANSI_DIM, " → " + truncate(outerRecognized.itemName, 40)));
        }

        // Elapsed + ETA.
        bar.append(sep).append(color(ANSI_GRAY, "elapsed "))
           .append(color(ANSI_BRIGHT_WHITE, elapsed));
        if (eta != null) {
            bar.append(sep).append(color(ANSI_GRAY, "ETA "))
               .append(color(ANSI_BRIGHT_WHITE, eta));
        }

        return bar.toString();
    }

    /**
     * Overall pipeline fraction, normalized against the fixed 100-unit weight
     * budget of {@link Phase}. The inner-phase term credits fine-grained
     * progress during long-running inner loops so the bar moves smoothly
     * instead of jumping in huge slot-sized steps. A monotonic clamp prevents
     * regressions when one inner phase completes and the next one begins.
     */
    private static double computeOverallFraction() {
        double accumulated = 0;
        for (Map.Entry<Phase, Double> e : completedFraction.entrySet()) {
            accumulated += e.getKey().weight * e.getValue();
        }

        Frame outerRecognized = null;
        Frame innermost = null;
        for (Frame f : STACK) {
            if (innermost == null) innermost = f;
            if (f.phase != null && !completedFraction.containsKey(f.phase)) {
                outerRecognized = f;
            }
        }

        if (outerRecognized != null) {
            int outerTotal = Math.max(outerRecognized.total, 1);
            double outerFrac = ((double) outerRecognized.current) / outerTotal;
            if (innermost != null && innermost != outerRecognized && innermost.total > 0) {
                double innerFrac = ((double) innermost.current) / innermost.total;
                outerFrac += innerFrac / outerTotal;
            }
            if (outerFrac > 1.0) outerFrac = 1.0;
            accumulated += outerRecognized.phase.weight * outerFrac;
        }

        double overall = Math.max(0.0, Math.min(1.0, accumulated / 100.0));
        if (overall < lastOverallFraction) overall = lastOverallFraction;
        lastOverallFraction = overall;
        return overall;
    }

    private static String computeEta(long elapsedNs, double overall) {
        if (overall <= 0.02) return null;
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

    // -----------------------------------------------------------------------
    //  Sticky-bottom wrapper
    // -----------------------------------------------------------------------

    /** Must be called under LOCK. Idempotent. */
    private static void installStickyOutput() {
        if (stickyInstalled) return;
        stickyInstalled = true;
        System.setOut(new ProgressAwarePrintStream(System.out));
        System.setErr(new ProgressAwarePrintStream(System.err));
    }

    /**
     * Forwards writes to the real underlying stream, then — whenever a write
     * ends with '\n' and the progress bar is active — prints a fresh copy of
     * the bar (no trailing newline) so it appears as the last line on the
     * terminal.
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
                // CRITICAL: write via RAW_STDOUT (FileDescriptor.out) — NOT via
                // delegate. The app installs a LoggerStream wrapper on
                // System.out that would otherwise pipe each write to log4j,
                // embedding the bar in the log file with a timestamp prefix.
                //
                // Clear-line first so any leftover characters on this line
                // (e.g. a partial write) are wiped before the bar lands.
                RAW_STDOUT.print(ANSI_CLEAR_LINE);
                RAW_STDOUT.print(bar);
                RAW_STDOUT.flush();
            } finally {
                SUPPRESS_REDRAW.set(Boolean.FALSE);
            }
        }
    }
}
