package es.us.isa.restest.fault;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import es.us.isa.restest.inputs.InvalidInputType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Locks in the default-YAML registry semantics and verifies parity with
 * {@link InvalidInputType#appliesTo(String)} on every (oasType x category)
 * pair. The eight default ids must match the legacy enum byte-for-byte.
 */
public class FaultTypeRegistryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String[] OAS_TYPES = {"string", "integer", "number", "boolean", "array", "object"};

    @Test
    public void loadDefaultExposesEightLegacyIds() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        Set<String> expected = new HashSet<>(Arrays.asList(
                "TYPE_MISMATCH",
                "REGEX_MISMATCH",
                "SEMANTIC_MISMATCH",
                "OVERFLOW",
                "EMPTY_INPUT",
                "NULL_INPUT",
                "SPECIAL_CHARACTERS",
                "BOUNDARY_VIOLATION"));
        assertEquals(expected.size(), registry.size());
        for (String id : expected) {
            assertNotNull("registry must expose default id " + id, registry.byId(id));
            assertEquals(FaultType.FaultSource.DEFAULT, registry.byId(id).source());
        }
    }

    @Test
    public void applicableForMatchesLegacyEnumOnEveryOasType() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        for (InvalidInputType legacy : InvalidInputType.values()) {
            FaultType ft = registry.byId(legacy.name());
            assertNotNull("registry must expose default id " + legacy.name(), ft);
            for (String oas : OAS_TYPES) {
                boolean legacyApplies = legacy.appliesTo(oas);
                boolean registryApplies = ft.applicableTo().contains(oas);
                assertEquals(
                        legacy.name() + " applicability mismatch for OAS type '" + oas + "'",
                        legacyApplies, registryApplies);
            }
        }
    }

    @Test
    public void applicableForFiltersByOasTypeAndLocation() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        // REGEX_MISMATCH is the strictest single-OAS-type fault — easiest to assert.
        List<FaultType> stringQuery = registry.applicableFor("string", "query");
        assertTrue("REGEX_MISMATCH should apply to string query",
                stringQuery.stream().anyMatch(ft -> ft.id().equals("REGEX_MISMATCH")));

        List<FaultType> integerQuery = registry.applicableFor("integer", "query");
        assertTrue("REGEX_MISMATCH should NOT apply to integer query",
                integerQuery.stream().noneMatch(ft -> ft.id().equals("REGEX_MISMATCH")));
    }

    @Test
    public void normalizationAcceptsIntAndLongAliases() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        // Legacy enum's appliesTo accepted "int" and "long" — registry normalizes.
        List<FaultType> intResult = registry.applicableFor("int", null);
        List<FaultType> integerResult = registry.applicableFor("integer", null);
        assertEquals(integerResult.size(), intResult.size());
    }

    @Test
    public void overlayAddsMinedTypesAndPreservesDefaults() throws Exception {
        FaultTypeRegistry base = FaultTypeRegistry.loadDefault();
        File overlay = tmp.newFile("mined.yaml");
        try (FileWriter w = new FileWriter(overlay)) {
            w.write("faults:\n");
            w.write("  - id: INVALID_STATION_NAME\n");
            w.write("    displayName: Invalid station name\n");
            w.write("    applicableTo: [string]\n");
            w.write("    applicableLocations: [path, query]\n");
            w.write("    source: MINED\n");
        }
        FaultTypeRegistry merged = base.loadOverlay(overlay.toPath());
        assertEquals(base.size() + 1, merged.size());
        FaultType mined = merged.byId("INVALID_STATION_NAME");
        assertNotNull(mined);
        assertEquals(FaultType.FaultSource.MINED, mined.source());
        assertTrue(mined.applicableTo().contains("string"));
        // Defaults still resolve through the merged registry.
        assertNotNull(merged.byId("TYPE_MISMATCH"));
    }

    @Test
    public void malformedYamlOverlayThrows() throws Exception {
        FaultTypeRegistry base = FaultTypeRegistry.loadDefault();
        File bad = tmp.newFile("bad.yaml");
        try (FileWriter w = new FileWriter(bad)) {
            w.write(": not valid yaml :\n");
        }
        try {
            base.loadOverlay(bad.toPath());
            fail("Malformed YAML must surface as a RuntimeException");
        } catch (RuntimeException expected) {
            // pass
        }
    }

    @Test
    public void unknownByIdReturnsNull() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        assertNull(registry.byId("DOES_NOT_EXIST"));
    }
}
