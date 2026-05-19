package es.us.isa.restest.configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Coverage for {@link MstConfigValidator}: range checks, unknown-key handling,
 * and strict-mode escalation. Tests share the {@link MstConfigTest} pattern of
 * saving and restoring System properties between cases so they remain
 * order-independent.
 */
public class MstConfigValidatorTest {

    private static final List<String> KEYS = Arrays.asList(
            "faulty.ratio",
            "trace.merge.max.session.gap.micros",
            "smart.input.fetch.percentage",
            "mst.config.strict",
            "mst.generate.only.first.step",
            "mst.generrate.only.first.step", // typo, deliberate
            "smart.input.fetch.percetage"     // typo, deliberate
    );

    private Map<String, String> savedProperties;

    @Before
    public void saveAndClear() {
        savedProperties = new HashMap<>();
        for (String key : KEYS) {
            String value = System.getProperty(key);
            if (value != null) {
                savedProperties.put(key, value);
            }
            System.clearProperty(key);
        }
        MstConfig.resetForTesting();
    }

    @After
    public void restore() {
        for (String key : KEYS) {
            System.clearProperty(key);
        }
        for (Map.Entry<String, String> e : savedProperties.entrySet()) {
            System.setProperty(e.getKey(), e.getValue());
        }
        MstConfig.resetForTesting();
    }

    @Test
    public void validConfigDoesNotThrow() {
        // Defaults must pass validation cleanly.
        MstConfig cfg = MstConfig.fromSystemProperties();
        MstConfigValidator.validate(cfg);
    }

    @Test(expected = IllegalArgumentException.class)
    public void outOfRangeFaultyRatioThrows() {
        System.setProperty("faulty.ratio", "2.0");
        MstConfig.fromSystemProperties();
    }

    @Test
    public void unknownKeyWarnsButContinuesByDefault() {
        // Typo: should be mst.generate.only.first.step
        System.setProperty("mst.generrate.only.first.step", "true");
        // No strict mode => the validator logs a warning and returns normally.
        // We assert by absence of an exception; verifying the log line is
        // brittle across log4j configurations.
        MstConfig.fromSystemProperties();
    }

    @Test(expected = IllegalStateException.class)
    public void unknownKeyFailsInStrictMode() {
        System.setProperty("mst.generrate.only.first.step", "true");
        System.setProperty("mst.config.strict", "true");
        MstConfig.fromSystemProperties();
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeSessionGapMicrosThrows() {
        System.setProperty("trace.merge.max.session.gap.micros", "-1");
        MstConfig.fromSystemProperties();
    }
}
