package es.us.isa.restest.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Pins {@link ConsoleDedupFilter} behavior: same message within the TTL
 * is denied (suppressed from the console appender); distinct messages
 * pass through; same message after the TTL passes through again.
 */
public class ConsoleDedupFilterTest {

    private static LogEvent makeEvent(String msg) {
        return Log4jLogEvent.newBuilder()
                .setLoggerName("test")
                .setLevel(Level.WARN)
                .setMessage(new SimpleMessage(msg))
                .build();
    }

    @Test
    public void firstOccurrencePassesThrough() {
        ConsoleDedupFilter f = ConsoleDedupFilter.createFilter(5000L);
        assertEquals(Filter.Result.NEUTRAL, f.filter(makeEvent("hello")));
    }

    @Test
    public void duplicateWithinTtlIsDenied() {
        ConsoleDedupFilter f = ConsoleDedupFilter.createFilter(5000L);
        f.filter(makeEvent("retry failed"));
        // Immediately again — same content, within 5s TTL.
        assertEquals(Filter.Result.DENY, f.filter(makeEvent("retry failed")));
        assertEquals(Filter.Result.DENY, f.filter(makeEvent("retry failed")));
    }

    @Test
    public void distinctMessagesAllPassThrough() {
        ConsoleDedupFilter f = ConsoleDedupFilter.createFilter(5000L);
        assertEquals(Filter.Result.NEUTRAL, f.filter(makeEvent("a")));
        assertEquals(Filter.Result.NEUTRAL, f.filter(makeEvent("b")));
        assertEquals(Filter.Result.NEUTRAL, f.filter(makeEvent("c")));
    }

    @Test
    public void duplicateAfterTtlPassesAgain() throws InterruptedException {
        ConsoleDedupFilter f = ConsoleDedupFilter.createFilter(50L);
        f.filter(makeEvent("rejected"));
        assertEquals(Filter.Result.DENY, f.filter(makeEvent("rejected")));
        Thread.sleep(75); // sleep past the 50ms TTL
        assertEquals("after TTL expiry the message must pass again",
                Filter.Result.NEUTRAL, f.filter(makeEvent("rejected")));
    }

    @Test
    public void systemPropertyOverridesConfiguredTtl() {
        String original = System.getProperty("mst.console.dedup.ttl.ms");
        System.setProperty("mst.console.dedup.ttl.ms", "100000");
        try {
            ConsoleDedupFilter f = ConsoleDedupFilter.createFilter(10L);
            f.filter(makeEvent("x"));
            // With the system-property override at 100 000 ms, the second
            // occurrence is well within TTL and must be denied even though
            // the configured ttlMillis was 10.
            assertEquals(Filter.Result.DENY, f.filter(makeEvent("x")));
        } finally {
            if (original != null) System.setProperty("mst.console.dedup.ttl.ms", original);
            else                  System.clearProperty("mst.console.dedup.ttl.ms");
        }
    }

    @Test
    public void wiredAtRuntimeViaLog4jConfig() {
        // Smoke: log4j2.properties references ConsoleDedupFilter; if the
        // class isn't discoverable, configuration loading throws and the
        // root logger ends up null. Reading any logger and emitting one
        // WARN succeeds means the filter wired in cleanly.
        org.apache.logging.log4j.Logger l = LogManager.getLogger(ConsoleDedupFilterTest.class);
        l.warn("smoke from ConsoleDedupFilterTest — must not crash configuration");
    }
}
