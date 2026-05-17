package es.us.isa.restest.analysis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Per-signature cache of {@link TraceErrorAnalyzer#generateIntelligentAnalysis}
 * LLM responses.
 * <p>
 * Keyed by the sorted set of root-cause failure signatures
 * ({@code service|operation|status|exception} per failure, joined by {@code ;;}),
 * which captures the dimensions that actually determine the LLM's diagnosis.
 * Two traces that fail in the same way reuse the same diagnostic text instead
 * of paying for a fresh LLM call each time. On the canonical benchmark run,
 * this single hot path accounted for 9,145 LLM calls and ~6 hours of LLM time
 * across a 36-hour run, with only ~10 unique failure-mode signatures.
 * <p>
 * Cached value is the final formatted output string (after
 * {@code formatLLMResponse}), so a cache hit produces byte-identical content
 * to a fresh LLM call. The cache is opaque to consumers (Allure attachments,
 * CLI prints) — no decoration or "cached" marker is added.
 * <p>
 * Thread-safe via {@link ConcurrentHashMap}; persisted to a JSON file so the
 * cache survives across enhancement rounds within a JVM run. Mirrors the
 * design of {@link es.us.isa.restest.inputs.smart.ParameterErrorAnalysisCache}
 * and {@link es.us.isa.restest.validation.SoftErrorRuleCache}.
 */
public class IntelligentAnalysisCache {

    private static final Logger logger = LogManager.getLogger(IntelligentAnalysisCache.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<String, IntelligentAnalysisCache> instances = new ConcurrentHashMap<>();

    private final String filePath;
    private final ConcurrentHashMap<String, String> analyses = new ConcurrentHashMap<>();

    private IntelligentAnalysisCache(String filePath) {
        this.filePath = filePath;
        load();
    }

    /**
     * Get (or create) a singleton cache instance backed by the given file path.
     */
    public static synchronized IntelligentAnalysisCache getInstance(String filePath) {
        return instances.computeIfAbsent(filePath, IntelligentAnalysisCache::new);
    }

    public Optional<String> get(String key) {
        String v = analyses.get(key);
        if (v != null) {
            logger.debug("Intelligent-analysis cache HIT for key={}", key);
        }
        return Optional.ofNullable(v);
    }

    public void put(String key, String analysis) {
        analyses.put(key, analysis);
        save();
        logger.info("Intelligent-analysis cache STORE for key={}", key);
    }

    public int size() {
        return analyses.size();
    }

    private void load() {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            logger.info("No existing intelligent-analysis cache at {}; starting fresh", filePath);
            return;
        }
        try (Reader reader = new FileReader(filePath)) {
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> loaded = GSON.fromJson(reader, mapType);
            if (loaded != null) {
                analyses.putAll(loaded);
                logger.info("Loaded {} intelligent-analysis cache entries from {}", analyses.size(), filePath);
            }
        } catch (Exception e) {
            logger.warn("Could not load intelligent-analysis cache from {}: {}", filePath, e.getMessage());
        }
    }

    private synchronized void save() {
        try {
            Path parent = Paths.get(filePath).getParent();
            if (parent != null) Files.createDirectories(parent);
            try (Writer writer = new FileWriter(filePath)) {
                GSON.toJson(analyses, writer);
            }
        } catch (IOException e) {
            logger.warn("Could not save intelligent-analysis cache to {}: {}", filePath, e.getMessage());
        }
    }
}
