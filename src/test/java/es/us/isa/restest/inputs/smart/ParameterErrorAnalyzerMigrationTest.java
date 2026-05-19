package es.us.isa.restest.inputs.smart;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the one-shot {@code target/} -&gt; {@code .mist/} migration that
 * {@link ParameterErrorAnalysisCache#getInstance(String)} performs on first
 * load. The migration must preserve accumulated LLM verdicts across the
 * cache-location change to {@code .mist/}, without clobbering
 * pre-populated new-location files and without surprising users who have
 * their own custom cache paths.
 *
 * <p>Each test uses {@link TemporaryFolder} to isolate filesystem state; the
 * {@link ParameterErrorAnalysisCache} singleton map is keyed by file path,
 * so different temp-folder paths give independent caches without any reset
 * hook.
 */
public class ParameterErrorAnalyzerMigrationTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    /**
     * Minimal verdict map: one key whose value is a CachedVerdict POJO. We
     * use a real verdict body so we can verify the load() round-trip via
     * {@code cache.get(...)} after the migration.
     */
    private static final String SAMPLE_JSON =
            "{\"svcA|op1|400|null\":{\"isParameterError\":true,"
            + "\"parameterName\":\"foo\",\"errorType\":\"VALIDATION_ERROR\"}}";

    private static final String OTHER_JSON =
            "{\"svcB|op2|500|null\":{\"isParameterError\":false,"
            + "\"parameterName\":null,\"errorType\":null}}";

    @Test
    public void legacyTargetFileMigratedToMist() throws IOException {
        Path projectDir = tmp.newFolder("p1").toPath();
        Path legacy = projectDir.resolve("target/parameter-error-analysis-cache.json");
        Files.createDirectories(legacy.getParent());
        Files.write(legacy, SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));

        Path target = projectDir.resolve(".mist/parameter-error-analysis-cache.json");
        assertFalse(".mist/ file must not exist before migration", Files.exists(target));

        ParameterErrorAnalysisCache cache = ParameterErrorAnalysisCache.getInstance(target.toString());

        assertTrue("Migrated file must exist at .mist/", Files.exists(target));
        assertFalse("Legacy target/ file must be gone after migration", Files.exists(legacy));
        assertEquals("Migrated content must round-trip byte-for-byte",
                SAMPLE_JSON, new String(Files.readAllBytes(target), StandardCharsets.UTF_8));

        Optional<ParameterErrorAnalysisCache.CachedVerdict> v = cache.get("svcA|op1|400|null");
        assertTrue("Loaded cache must contain the verdict from the migrated file", v.isPresent());
        assertNotNull(v.get());
        assertTrue(v.get().isParameterError());
        assertEquals("foo", v.get().getParameterName());
    }

    @Test
    public void bothFilesPresentNewOneWins() throws IOException {
        Path projectDir = tmp.newFolder("p2").toPath();
        Path legacy = projectDir.resolve("target/parameter-error-analysis-cache.json");
        Files.createDirectories(legacy.getParent());
        Files.write(legacy, OTHER_JSON.getBytes(StandardCharsets.UTF_8));

        Path target = projectDir.resolve(".mist/parameter-error-analysis-cache.json");
        Files.createDirectories(target.getParent());
        Files.write(target, SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));

        ParameterErrorAnalysisCache cache = ParameterErrorAnalysisCache.getInstance(target.toString());

        assertEquals("Existing .mist/ file must be untouched",
                SAMPLE_JSON, new String(Files.readAllBytes(target), StandardCharsets.UTF_8));
        assertTrue("Legacy target/ file must still exist", Files.exists(legacy));
        assertEquals("Legacy target/ content must be untouched",
                OTHER_JSON, new String(Files.readAllBytes(legacy), StandardCharsets.UTF_8));
        assertTrue("Cache must contain the verdict from .mist/, not from target/",
                cache.get("svcA|op1|400|null").isPresent());
        assertFalse("Cache must NOT contain the verdict from target/",
                cache.get("svcB|op2|500|null").isPresent());
    }

    @Test
    public void userOverriddenPathDoesNotTriggerMigration() throws IOException {
        Path projectDir = tmp.newFolder("p3").toPath();
        Path legacy = projectDir.resolve("target/some-custom.json");
        Files.createDirectories(legacy.getParent());
        Files.write(legacy, SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));

        Path custom = projectDir.resolve("foo/some-custom.json");
        assertFalse("Custom path must not exist before getInstance", Files.exists(custom));

        ParameterErrorAnalysisCache.getInstance(custom.toString());

        assertTrue("Legacy file must still exist (no migration fired)",
                Files.exists(legacy));
        assertEquals("Legacy content must be untouched",
                SAMPLE_JSON, new String(Files.readAllBytes(legacy), StandardCharsets.UTF_8));
        assertFalse("Custom path file must NOT have been created by migration",
                Files.exists(custom));
    }
}
