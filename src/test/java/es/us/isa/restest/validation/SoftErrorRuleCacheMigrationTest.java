package es.us.isa.restest.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the one-shot {@code target/} -&gt; {@code .mist/} migration that
 * {@link SoftErrorRuleCache#getInstance(String)} performs on first load. The
 * migration must preserve hard-won cache state across the cache-location
 * change to {@code .mist/}, without clobbering pre-populated new-location
 * files and without surprising users who have their own custom cache paths.
 *
 * <p>Each test uses {@link TemporaryFolder} to isolate filesystem state; the
 * {@link SoftErrorRuleCache} singleton map is keyed by file path, so different
 * temp-folder paths give independent caches without any reset hook.
 */
public class SoftErrorRuleCacheMigrationTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    /**
     * Minimal {@link SoftErrorRuleCache.SoftErrorRule} JSON. The migration
     * only moves the file; loading still goes through the production GSON
     * path, so we use a real (non-empty) map so we can verify content
     * preservation by re-reading the file after the migration.
     */
    private static final String SAMPLE_JSON =
            "{\"GET /api/x\":{\"fieldChecks\":[],\"failureMessageFields\":[],"
            + "\"nullDataIsFailure\":true,\"knownPatterns\":[]}}";

    private static final String OTHER_JSON =
            "{\"POST /api/y\":{\"fieldChecks\":[],\"failureMessageFields\":[],"
            + "\"nullDataIsFailure\":false,\"knownPatterns\":[]}}";

    @Test
    public void legacyTargetFileMigratedToMist() throws IOException {
        // Arrange: a project-like layout with a legacy target/soft-error...
        // file and no .mist/ directory yet. This is the canonical first-run
        // case after upgrade — the migration must preserve the user's
        // accumulated rule cache.
        Path projectDir = tmp.newFolder("p1").toPath();
        Path legacy = projectDir.resolve("target/soft-error-rule-cache.json");
        Files.createDirectories(legacy.getParent());
        Files.write(legacy, SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));

        Path target = projectDir.resolve(".mist/soft-error-rule-cache.json");
        assertFalse(".mist/ file must not exist before migration", Files.exists(target));

        // Act
        SoftErrorRuleCache cache = SoftErrorRuleCache.getInstance(target.toString());

        // Assert: the file was moved (not copied — the legacy is gone) and the
        // content arrived intact at the new location.
        assertTrue("Migrated file must exist at .mist/", Files.exists(target));
        assertFalse("Legacy target/ file must be gone after migration", Files.exists(legacy));
        assertEquals("Migrated content must round-trip byte-for-byte",
                SAMPLE_JSON, new String(Files.readAllBytes(target), StandardCharsets.UTF_8));
        assertTrue("Loaded cache must contain the api key from the migrated file",
                cache.hasRule("GET /api/x"));
    }

    @Test
    public void bothFilesPresentNewOneWins() throws IOException {
        // Arrange: both the legacy target/ file and a fresh .mist/ file exist
        // with intentionally different content. The migration MUST NOT
        // clobber the new file — the user (or an earlier process) has
        // already started using the new location and any updates there
        // post-date the legacy file.
        Path projectDir = tmp.newFolder("p2").toPath();
        Path legacy = projectDir.resolve("target/soft-error-rule-cache.json");
        Files.createDirectories(legacy.getParent());
        Files.write(legacy, OTHER_JSON.getBytes(StandardCharsets.UTF_8));

        Path target = projectDir.resolve(".mist/soft-error-rule-cache.json");
        Files.createDirectories(target.getParent());
        Files.write(target, SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));

        // Act
        SoftErrorRuleCache cache = SoftErrorRuleCache.getInstance(target.toString());

        // Assert: .mist/ untouched, target/ untouched, in-memory cache
        // matches .mist/ content only.
        assertEquals("Existing .mist/ file must be untouched",
                SAMPLE_JSON, new String(Files.readAllBytes(target), StandardCharsets.UTF_8));
        assertTrue("Legacy target/ file must still exist", Files.exists(legacy));
        assertEquals("Legacy target/ content must be untouched",
                OTHER_JSON, new String(Files.readAllBytes(legacy), StandardCharsets.UTF_8));
        assertTrue("Cache must contain the rule from .mist/, not from target/",
                cache.hasRule("GET /api/x"));
        assertFalse("Cache must NOT contain the rule from target/",
                cache.hasRule("POST /api/y"));
    }

    @Test
    public void userOverriddenPathDoesNotTriggerMigration() throws IOException {
        // Arrange: a custom user-configured path whose parent is NOT named
        // .mist. A sibling target/some-custom.json exists under the same
        // grandparent. The migration must NOT fire on this shape because
        // a custom path signals the user explicitly chose where to put
        // their cache, and silently moving a same-named file into their
        // chosen location would be surprising and possibly destructive.
        Path projectDir = tmp.newFolder("p3").toPath();
        Path legacy = projectDir.resolve("target/some-custom.json");
        Files.createDirectories(legacy.getParent());
        Files.write(legacy, SAMPLE_JSON.getBytes(StandardCharsets.UTF_8));

        Path custom = projectDir.resolve("foo/some-custom.json");
        assertFalse("Custom path must not exist before getInstance", Files.exists(custom));

        // Act
        SoftErrorRuleCache.getInstance(custom.toString());

        // Assert: the legacy file is untouched (no move happened) and the
        // custom path was NOT created by a phantom migration.
        assertTrue("Legacy file must still exist (no migration fired)",
                Files.exists(legacy));
        assertEquals("Legacy content must be untouched",
                SAMPLE_JSON, new String(Files.readAllBytes(legacy), StandardCharsets.UTF_8));
        assertFalse("Custom path file must NOT have been created by migration",
                Files.exists(custom));
    }
}
