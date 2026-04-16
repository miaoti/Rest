package es.us.isa.restest.util;

import java.util.concurrent.TimeUnit;

/**
 * Console progress bar utility for tracking long-running phases in RESTest.
 * Uses carriage return (\r) to overwrite the current line, providing
 * real-time progress visibility during slow LLM inference operations.
 *
 * <p>Thread-safe: all state access is synchronized.
 *
 * <p>Auto-detects CI / non-TTY environments and becomes a no-op when
 * a real terminal is not available (override with -Drestest.progress.bar=true).
 */
public final class ConsoleProgressBar {

    private static final Object LOCK = new Object();
    private static final int BAR_WIDTH = 30;
    private static final String FILLED = "\u2588";  // █
    private static final String EMPTY  = "\u2591";  // ░

    private static final boolean enabled = detectEnabled();

    private static String phaseName = "";
    private static int total = 0;
    private static int current = 0;
    private static String currentItemName = "";
    private static long startTimeNanos = 0;
    private static boolean active = false;

    private ConsoleProgressBar() {}

    /**
     * Detect whether the progress bar should be enabled.
     * Priority: explicit system property > CI env var > console check > default on.
     */
    private static boolean detectEnabled() {
        // 1. Explicit override: -Drestest.progress.bar=true/false
        String prop = System.getProperty("restest.progress.bar");
        if (prop != null) return Boolean.parseBoolean(prop);
        // 2. CI environment detection — most CI systems set CI=true
        if (System.getenv("CI") != null) return false;
        // 3. No real terminal attached (piped/redirected stdout)
        if (System.console() == null) return false;
        // 4. Default: enabled
        return true;
    }

    /** Start a new progress phase. Resets counter to 0. */
    public static void begin(String phase, int totalItems) {
        if (!enabled) return;
        synchronized (LOCK) {
            phaseName = phase;
            total = totalItems;
            current = 0;
            currentItemName = "";
            startTimeNanos = System.nanoTime();
            active = true;
            render();
        }
    }

    /** Increment counter by 1 and redraw. */
    public static void update(String itemName) {
        if (!enabled) return;
        synchronized (LOCK) {
            if (!active) return;
            current++;
            currentItemName = itemName != null ? itemName : "";
            render();
        }
    }

    /** Show sub-progress (inner loop) without changing the outer phase counter. */
    public static void update(int cur, int tot, String itemName) {
        if (!enabled) return;
        synchronized (LOCK) {
            if (!active) return;
            currentItemName = itemName != null ? itemName : "";
            renderCustom(phaseName, cur, tot, currentItemName);
        }
    }

    /** Mark phase complete, print newline so next output starts fresh. */
    public static void complete() {
        if (!enabled) return;
        synchronized (LOCK) {
            if (!active) return;
            current = total;
            currentItemName = "Done";
            render();
            System.out.println();
            active = false;
        }
    }

    public static boolean isActive() {
        synchronized (LOCK) {
            return active;
        }
    }

    private static void render() {
        renderCustom(phaseName, current, total, currentItemName);
    }

    private static void renderCustom(String phase, int cur, int tot, String item) {
        if (tot <= 0) return;

        int pct = (cur * 100) / tot;
        int filled = (cur * BAR_WIDTH) / tot;
        int empty = BAR_WIDTH - filled;

        StringBuilder bar = new StringBuilder();
        bar.append("\r[").append(phase).append("] [");
        for (int i = 0; i < filled; i++) bar.append(FILLED);
        for (int i = 0; i < empty; i++) bar.append(EMPTY);
        bar.append("] ").append(String.format("%3d", pct)).append("% (")
           .append(cur).append("/").append(tot).append(")");

        if (item != null && !item.isEmpty()) {
            bar.append(" | ").append(truncate(item, 50));
        }

        long elapsedNanos = System.nanoTime() - startTimeNanos;
        bar.append(" | ").append(formatElapsed(elapsedNanos));

        // Pad to 140 chars to overwrite any previous longer line
        while (bar.length() < 140) bar.append(' ');

        System.out.print(bar.toString());
        System.out.flush();
    }

    private static String formatElapsed(long nanos) {
        long totalSeconds = TimeUnit.NANOSECONDS.toSeconds(nanos);
        if (totalSeconds < 60) {
            return totalSeconds + "s";
        }
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return minutes + "m " + seconds + "s";
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 3) + "...";
    }
}
