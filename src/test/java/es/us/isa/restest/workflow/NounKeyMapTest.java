package es.us.isa.restest.workflow;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link NounKeyMap}: default-map loading, per-SUT overlay,
 * and malformed-YAML failure semantics.
 */
public class NounKeyMapTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void fromDefaultLoadsBundledTrainticketMap() {
        NounKeyMap map = NounKeyMap.fromDefault();

        assertEquals("orderId", map.keyFor("orders"));
        assertNotNull("trains must resolve via the bundled default map", map.keyFor("trains"));
        assertNull("unknown nouns should return null", map.keyFor("unknownNoun"));
    }

    @Test
    public void fromPathOverridesDefault() throws Exception {
        File override = tmp.newFile("override.yaml");
        try (FileWriter w = new FileWriter(override)) {
            w.write("orders: customOrderId\n");
        }

        NounKeyMap map = NounKeyMap.fromPath(override.toPath());

        assertEquals("override entry must win over the default",
                "customOrderId", map.keyFor("orders"));
        assertNotNull("trains must still resolve via the default fallback",
                map.keyFor("trains"));
    }

    @Test
    public void malformedYamlThrows() throws Exception {
        File bad = tmp.newFile("bad.yaml");
        try (FileWriter w = new FileWriter(bad)) {
            w.write(": not valid yaml :\n");
        }

        Path path = bad.toPath();
        try {
            NounKeyMap.fromPath(path);
            fail("Malformed YAML must surface as a RuntimeException, not a silent empty map");
        } catch (RuntimeException expected) {
            // Pass — exact message wording is implementation detail.
        }
    }
}
