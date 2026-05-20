package es.us.isa.restest.main;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Pins the {@link MistPathResolver} contract that bit the user:
 * {@code smart.input.fetch.openapi.spec.path} (and every other key
 * listed in {@code MST_INPUT_PATH_KEYS}) must be path-resolved by
 * {@link MistPathResolver#resolveInputPaths(java.util.Properties, java.io.File)}
 * — otherwise the MstMain path leaves it CWD-relative and the
 * generator hits "OpenAPI spec file not found" the moment the user
 * runs from the project root rather than the module directory.
 */
public class MistPathResolverInputKeysTest {

    @Test
    public void smartInputFetchOpenApiSpecPathIsResolved() throws IOException {
        // Build a .properties file inside a temp directory and ask the
        // resolver to rewrite its paths. The smart.input.fetch.openapi.spec.path
        // value is relative; after resolveInputPaths it must be absolute and
        // point under the .properties file's parent directory.
        Path tmpDir = Files.createTempDirectory("mist-path-resolver-test");
        try {
            File propsFile = tmpDir.resolve("my-app.properties").toFile();
            Files.writeString(propsFile.toPath(),
                    "smart.input.fetch.openapi.spec.path=oas/spec.yaml\n");

            Properties props = new Properties();
            try (java.io.FileInputStream in = new java.io.FileInputStream(propsFile)) {
                props.load(in);
            }
            assertEquals("oas/spec.yaml", props.getProperty("smart.input.fetch.openapi.spec.path"));

            MistPathResolver.resolveInputPaths(props, propsFile);

            String resolved = props.getProperty("smart.input.fetch.openapi.spec.path");
            assertTrue("must be absolute: " + resolved, new File(resolved).isAbsolute());
            assertTrue("must point under properties dir: " + resolved,
                    resolved.replace('\\', '/').endsWith("/oas/spec.yaml"));
        } finally {
            // best-effort cleanup
            try { Files.walk(tmpDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(p -> p.toFile().delete()); }
            catch (Exception ignored) { /* nothing */ }
        }
    }

    @Test
    public void everyMstInputKeyIsAlsoInTheGlobalSet() throws Exception {
        // Reflective inspection of the package-private INPUT_PATH_KEYS so the
        // invariant "MST_INPUT_PATH_KEYS subset of INPUT_PATH_KEYS" is
        // explicit. Without it, an MST-only key can be added in one place
        // and silently ignored by the resolver in the other.
        java.lang.reflect.Field f = MistPathResolver.class.getDeclaredField("INPUT_PATH_KEYS");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Set<String> globalKeys = (java.util.Set<String>) f.get(null);
        for (String mstKey : MistPathResolver.MST_INPUT_PATH_KEYS) {
            assertTrue("MST key '" + mstKey + "' must also appear in INPUT_PATH_KEYS",
                    globalKeys.contains(mstKey));
        }
    }
}
