package es.us.isa.restest.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Pins the contract of {@link LoggerStream#LoggerStream(org.apache.logging.log4j.Logger,
 * Level, java.io.OutputStream, boolean)} — when {@code mirrorToStream=false},
 * writes go to the logger only and the backing {@link java.io.OutputStream}
 * is never touched. This is the structural fix that lets log4j thresholds
 * decide what reaches the terminal without {@code System.out.println}
 * spam bypassing the gate.
 *
 * <p>Symmetrically, {@code mirrorToStream=true} (and the legacy 3-arg
 * constructor) must still write to the backing stream so existing
 * callers that depend on the dual-destination behaviour keep working.
 */
public class LoggerStreamMirrorTest {

    @Test
    public void mirrorFalseDoesNotWriteToBackingStream() throws Exception {
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream wrapped = new PrintStream(new LoggerStream(
                LogManager.getLogger("test-stdout"),
                Level.INFO,
                captured,
                /*mirrorToStream*/ false));
        wrapped.println("this should not appear in captured");
        wrapped.flush();
        assertEquals("backing stream must be untouched when mirror is off",
                0, captured.size());
    }

    @Test
    public void mirrorTrueWritesToBackingStream() throws Exception {
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream wrapped = new PrintStream(new LoggerStream(
                LogManager.getLogger("test-stdout"),
                Level.INFO,
                captured,
                /*mirrorToStream*/ true));
        wrapped.println("present");
        wrapped.flush();
        String s = captured.toString();
        assertTrue("backing stream must receive the write when mirror is on: '" + s + "'",
                s.contains("present"));
    }

    @Test
    public void legacyConstructorMirrorsByDefault() throws Exception {
        // The 3-arg constructor preserves the historical mirror=true behavior
        // so any caller that relied on the dual destination keeps working.
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        PrintStream wrapped = new PrintStream(new LoggerStream(
                LogManager.getLogger("test-stdout"),
                Level.INFO,
                captured));
        wrapped.println("legacy");
        wrapped.flush();
        assertTrue(captured.toString().contains("legacy"));
    }
}
