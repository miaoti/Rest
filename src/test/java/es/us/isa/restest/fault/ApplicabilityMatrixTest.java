package es.us.isa.restest.fault;

import org.junit.Test;

import es.us.isa.restest.inputs.InvalidInputType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Parity table between {@link InvalidInputType#appliesTo(String)} and the
 * registry-driven {@link ApplicabilityMatrix}. Phase 3.C of
 * PATH_B_REBUILD_PLAN.md moves the applicability matrix from code into data;
 * this test guarantees the data carries the same semantics on every
 * (oasType x category) pair.
 */
public class ApplicabilityMatrixTest {

    private static final String[] OAS_TYPES = {"string", "integer", "number", "boolean", "array", "object"};

    @Test
    public void parityWithLegacyEnumOnEveryOasType() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        ApplicabilityMatrix matrix = new ApplicabilityMatrix(registry);
        for (InvalidInputType legacy : InvalidInputType.values()) {
            FaultType ft = registry.byId(legacy.name());
            for (String oas : OAS_TYPES) {
                boolean legacyApplies = legacy.appliesTo(oas);
                boolean matrixApplies = matrix.applies(ft, oas, null);
                assertEquals(
                        legacy.name() + " applicability mismatch for OAS type '" + oas + "'",
                        legacyApplies, matrixApplies);
            }
        }
    }

    @Test
    public void nullOasTypeDefaultsToApplicableForAllDefaults() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        ApplicabilityMatrix matrix = new ApplicabilityMatrix(registry);
        for (FaultType ft : registry.values()) {
            assertTrue(ft.id() + " should apply when oasType is null",
                    matrix.applies(ft, null, null));
        }
    }

    @Test
    public void locationFilterIsHonoured() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        ApplicabilityMatrix matrix = new ApplicabilityMatrix(registry);
        FaultType regex = registry.byId("REGEX_MISMATCH");
        // All default locations are accepted.
        assertTrue(matrix.applies(regex, "string", "query"));
        assertTrue(matrix.applies(regex, "string", "path"));
        // Unknown location filters out.
        assertFalse(matrix.applies(regex, "string", "no-such-location"));
    }

    @Test
    public void aliasedOasTypesNormalize() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        ApplicabilityMatrix matrix = new ApplicabilityMatrix(registry);
        FaultType overflow = registry.byId("OVERFLOW");
        // Legacy enum accepted "int" and "long" as integer aliases.
        assertTrue(matrix.applies(overflow, "int", null));
        assertTrue(matrix.applies(overflow, "long", null));
        assertTrue(matrix.applies(overflow, "double", null));
        assertTrue(matrix.applies(overflow, "float", null));
    }

    @Test
    public void nullFaultTypeIsNotApplicable() {
        FaultTypeRegistry registry = FaultTypeRegistry.loadDefault();
        ApplicabilityMatrix matrix = new ApplicabilityMatrix(registry);
        assertFalse(matrix.applies(null, "string", "query"));
    }
}
