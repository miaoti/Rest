package es.us.isa.restest.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validation companion for {@link MstConfig}. Three responsibilities:
 *
 * <ol>
 *   <li><b>Unknown-key scan.</b> Walks {@link System#getProperties()} and warns
 *   (or, under {@code mst.config.strict=true}, aborts) when a key starts with
 *   one of our owned namespaces but is not in the whitelist below — catches
 *   typos like {@code smart.input.fetch.percetage} that would otherwise
 *   silently fall back to a default.</li>
 *   <li><b>Range checks.</b> Numeric properties whose values must lie in a
 *   specific interval ({@code faulty.ratio}, {@code smart.input.fetch.percentage}
 *   in [0,1]; gap micros must be non-negative).</li>
 *   <li><b>Documented conflict detection.</b> Logs (INFO level) when settings
 *   interact in a non-obvious way; today only the
 *   {@code mst.generate.only.first.step} vs {@code scenario.shattering.enabled}
 *   precedence is surfaced.</li>
 * </ol>
 */
public final class MstConfigValidator {

    private static final Logger logger = LogManager.getLogger(MstConfigValidator.class);

    /**
     * Hard-coded whitelist of every known MIST/MST key. Keep in sync with the
     * sub-classes of {@link MstConfig}. Any key matching one of
     * {@link #NAMESPACES} but absent here triggers the typo warning.
     *
     * <p>Maintenance contract: when you add a parse call in any sub-class of
     * {@link MstConfig}, add its key here too. Forgetting only weakens the
     * typo-detection UX for that one key; everything else keeps working.
     */
    private static final Set<String> KNOWN_KEYS;
    static {
        Set<String> keys = new HashSet<>(Arrays.asList(
                // Core
                "mst.generate.only.first.step",
                "mist.noun.map.path",
                // SmartFetch
                "smart.input.fetch.enabled",
                "smart.input.fetch.percentage",
                "smart.input.fetch.registry.path",
                // Llm
                "mist.llm.cache.path",
                "llm.response.validation.enabled",
                "llm.response.validation.only.2xx",
                "llm.response.validation.include.rca",
                // Faulty
                "faulty.ratio",
                "faulty.round-robin",
                "negative.input.generation.mode",
                // ScenarioMerge
                "trace.merge.max.session.gap.micros",
                "trace.merge.max.roots.per.scenario",
                // ScenarioShattering
                "scenario.shattering.enabled",
                // SoftErrorCache
                "soft.error.cache.enabled",
                "soft.error.cache.path",
                // StatusCodeExploration
                "status.code.exploration.enabled",
                "status.code.exploration.max.per.test",
                "status.code.exploration.max.per.round",
                // Enhancer
                "test.enhancer.enabled",
                "test.enhancer.rounds",
                "test.enhancer.skip.5xx",
                // Jaeger
                "jaeger.enabled",
                "jaeger.base.url",
                "jaeger.lookback",
                // Validator's own switch
                "mst.config.strict"
        ));
        KNOWN_KEYS = Collections.unmodifiableSet(keys);
    }

    /**
     * Property namespaces we own. A System property starting with one of these
     * is expected to appear in {@link #KNOWN_KEYS}; otherwise it is almost
     * certainly a typo and a warning is emitted. We intentionally do NOT own
     * the generic {@code llm.} prefix (used by the LLM backend client for
     * model URLs, API keys, etc.) — only the narrower
     * {@code llm.response.validation.} subspace.
     */
    private static final List<String> NAMESPACES = Collections.unmodifiableList(Arrays.asList(
            "mst.",
            "mist.",
            "smart.input.fetch.",
            "scenario.shattering.",
            "faulty.",
            "trace.merge.",
            "test.enhancer.",
            "status.code.exploration.",
            "soft.error.cache.",
            "jaeger.",
            "negative.input.generation.",
            "llm.response.validation."
    ));

    private MstConfigValidator() {
        // utility class
    }

    /**
     * Run all three validation passes against {@code cfg}. Throws on range
     * violations and (in strict mode) on unknown keys; otherwise logs.
     */
    public static void validate(MstConfig cfg) {
        boolean strict = Boolean.parseBoolean(System.getProperty("mst.config.strict", "false"));

        // 1. Unknown-key scan
        boolean foundUnknown = false;
        for (String key : System.getProperties().stringPropertyNames()) {
            for (String ns : NAMESPACES) {
                if (key.startsWith(ns) && !KNOWN_KEYS.contains(key)) {
                    logger.warn("MstConfig: unknown property '{}' (typo?). Strict mode = {}", key, strict);
                    foundUnknown = true;
                    break;
                }
            }
        }
        if (foundUnknown && strict) {
            throw new IllegalStateException(
                    "MstConfig: unknown properties found; mst.config.strict=true rejects the run.");
        }

        // 2. Range checks
        double faultyRatio = cfg.faulty().ratio();
        if (faultyRatio < 0.0 || faultyRatio > 1.0) {
            throw new IllegalArgumentException(
                    "faulty.ratio must be in [0,1], got " + faultyRatio);
        }
        long gapMicros = cfg.scenarioMerge().maxSessionGapMicros();
        if (gapMicros < 0) {
            throw new IllegalArgumentException(
                    "trace.merge.max.session.gap.micros must be >= 0, got " + gapMicros);
        }
        double smartFetchPct = cfg.smartFetch().percentage();
        if (smartFetchPct < 0.0 || smartFetchPct > 1.0) {
            throw new IllegalArgumentException(
                    "smart.input.fetch.percentage must be in [0,1], got " + smartFetchPct);
        }

        // 3. Documented conflict (INFO, not fatal)
        if (cfg.core().generateOnlyFirstStep() && cfg.scenarioShattering().enabled()) {
            logger.info("MstConfig: mst.generate.only.first.step=true takes precedence — " +
                    "scenario.shattering.enabled is effectively ignored.");
        }
    }
}
