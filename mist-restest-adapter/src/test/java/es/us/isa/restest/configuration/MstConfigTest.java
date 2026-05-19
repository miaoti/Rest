package es.us.isa.restest.configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Coverage for the {@link MstConfig} POJO: default values, parsing, singleton
 * caching, and the test-only {@link MstConfig#resetForTesting()} hook.
 *
 * <p>Each test must save and restore the System properties it touches so the
 * suite remains order-independent and does not bleed state into unrelated
 * configuration tests.
 */
public class MstConfigTest {

    /**
     * Every key {@link MstConfig} reads. Kept here (instead of importing from
     * the production validator's KNOWN_KEYS) so the tests stay independent of
     * validator internals and instead document the surface area the POJO
     * touches.
     */
    private static final List<String> ALL_KEYS = Arrays.asList(
            "mst.generate.only.first.step",
            "mist.noun.map.path",
            "smart.input.fetch.enabled",
            "smart.input.fetch.percentage",
            "smart.input.fetch.registry.path",
            "mist.llm.cache.path",
            "llm.response.validation.enabled",
            "llm.response.validation.only.2xx",
            "llm.response.validation.include.rca",
            "faulty.ratio",
            "faulty.round-robin",
            "negative.input.generation.mode",
            "trace.merge.max.session.gap.micros",
            "trace.merge.max.roots.per.scenario",
            "scenario.shattering.enabled",
            "soft.error.cache.enabled",
            "soft.error.cache.path",
            "parameter.error.analysis.cache.path",
            "intelligent.analysis.cache.path",
            "status.code.exploration.enabled",
            "status.code.exploration.max.per.test",
            "status.code.exploration.max.per.round",
            "test.enhancer.enabled",
            "test.enhancer.rounds",
            "test.enhancer.skip.5xx",
            "jaeger.enabled",
            "jaeger.base.url",
            "jaeger.lookback",
            "mst.config.strict"
    );

    private Map<String, String> savedProperties;

    @Before
    public void saveAndClear() {
        savedProperties = new HashMap<>();
        for (String key : ALL_KEYS) {
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
        for (String key : ALL_KEYS) {
            System.clearProperty(key);
        }
        for (Map.Entry<String, String> e : savedProperties.entrySet()) {
            System.setProperty(e.getKey(), e.getValue());
        }
        MstConfig.resetForTesting();
    }

    @Test
    public void defaultsWhenNoPropertiesSet() {
        MstConfig cfg = MstConfig.fromSystemProperties();

        // Core
        assertTrue("mst.generate.only.first.step default", cfg.core().generateOnlyFirstStep());
        assertEquals("mist/noun-map.default.yaml", cfg.core().nounMapPath());

        // SmartFetch
        assertFalse(cfg.smartFetch().enabled());
        assertEquals(0.0, cfg.smartFetch().percentage(), 1e-9);
        assertNull(cfg.smartFetch().registryPath());

        // Llm
        assertEquals(".mist/llm-call-cache.json", cfg.llm().cachePath());
        assertFalse(cfg.llm().responseValidationEnabled());
        assertTrue(cfg.llm().responseValidationOnly2xx());
        assertTrue(cfg.llm().responseValidationIncludeRca());

        // Faulty
        assertEquals(0.1, cfg.faulty().ratio(), 1e-9);
        assertTrue(cfg.faulty().roundRobin());
        assertEquals("smart", cfg.faulty().negativeInputGenerationMode());

        // ScenarioMerge
        assertEquals(60_000_000L, cfg.scenarioMerge().maxSessionGapMicros());
        assertEquals(10, cfg.scenarioMerge().maxRootsPerScenario());

        // ScenarioShattering
        assertTrue(cfg.scenarioShattering().enabled());

        // SoftErrorCache
        assertTrue(cfg.softErrorCache().enabled());
        assertEquals(".mist/soft-error-rule-cache.json", cfg.softErrorCache().cachePath());

        // ParameterErrorCache
        assertEquals(".mist/parameter-error-analysis-cache.json",
                cfg.parameterErrorCache().cachePath());

        // IntelligentAnalysisCache
        assertEquals(".mist/intelligent-analysis-cache.json",
                cfg.intelligentAnalysisCache().cachePath());

        // StatusCodeExploration
        assertFalse(cfg.statusCodeExploration().enabled());
        assertEquals(3, cfg.statusCodeExploration().maxPerTest());
        assertEquals(20, cfg.statusCodeExploration().maxPerRound());

        // Enhancer
        assertFalse(cfg.enhancer().enabled());
        assertEquals(1, cfg.enhancer().rounds());
        assertTrue(cfg.enhancer().skip5xx());

        // Jaeger
        assertFalse(cfg.jaeger().enabled());
        assertEquals("http://localhost:16686", cfg.jaeger().baseUrl());
        assertEquals("1h", cfg.jaeger().lookback());
    }

    @Test
    public void parsesIntegerAndDoubleProperties() {
        System.setProperty("faulty.ratio", "0.3");
        System.setProperty("trace.merge.max.session.gap.micros", "999");
        System.setProperty("trace.merge.max.roots.per.scenario", "4");
        System.setProperty("smart.input.fetch.percentage", "0.75");

        MstConfig cfg = MstConfig.fromSystemProperties();

        assertEquals(0.3, cfg.faulty().ratio(), 1e-9);
        assertEquals(999L, cfg.scenarioMerge().maxSessionGapMicros());
        assertEquals(4, cfg.scenarioMerge().maxRootsPerScenario());
        assertEquals(0.75, cfg.smartFetch().percentage(), 1e-9);
    }

    @Test
    public void boooleanCaseInsensitive() {
        // Boolean.parseBoolean accepts "TRUE"/"FALSE"/"True" interchangeably,
        // anything else collapses to false. We rely on this so legacy property
        // files with mixed-case values keep working.
        System.setProperty("mst.generate.only.first.step", "FALSE");
        assertFalse(MstConfig.fromSystemProperties().core().generateOnlyFirstStep());

        System.setProperty("mst.generate.only.first.step", "True");
        assertTrue(MstConfig.fromSystemProperties().core().generateOnlyFirstStep());
    }

    @Test
    public void singletonCachesFirstInstance() {
        MstConfig first = MstConfig.instance();
        MstConfig second = MstConfig.instance();
        assertSame("instance() must return the cached singleton on repeat calls", first, second);
    }

    @Test
    public void resetForTesting() {
        MstConfig before = MstConfig.instance();
        assertEquals(0.1, before.faulty().ratio(), 1e-9); // default

        // Mutate the property and reset — the next instance() call should
        // observe the new value.
        System.setProperty("faulty.ratio", "0.42");
        MstConfig.resetForTesting();

        MstConfig after = MstConfig.instance();
        assertNotNull(after);
        assertEquals(0.42, after.faulty().ratio(), 1e-9);
    }
}
