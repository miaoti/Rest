package es.us.isa.restest.inputs;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the schema-aware fault-applicability matrix on
 * {@link InvalidInputType#appliesTo(String)}.
 *
 * <p>The matrix was added to fix the boolean-OVERFLOW / boolean-BOUNDARY /
 * boolean-SEMANTIC mis-labelling that D10 NIFP surfaced as label-vs-value
 * purity failures (see debug/inputs/measurements/.../d10_summary.json).
 */
public class InvalidInputTypeApplicabilityTest {

    @Test
    public void overflowDoesNotApplyToBoolean() {
        assertFalse(InvalidInputType.OVERFLOW.appliesTo("boolean"));
        assertFalse(InvalidInputType.OVERFLOW.appliesTo("BOOLEAN"));
    }

    @Test
    public void overflowAppliesToStringIntegerNumber() {
        assertTrue(InvalidInputType.OVERFLOW.appliesTo("string"));
        assertTrue(InvalidInputType.OVERFLOW.appliesTo("integer"));
        assertTrue(InvalidInputType.OVERFLOW.appliesTo("number"));
    }

    @Test
    public void boundaryDoesNotApplyToBoolean() {
        assertFalse(InvalidInputType.BOUNDARY_VIOLATION.appliesTo("boolean"));
    }

    @Test
    public void specialCharactersOnlyAppliesToStringAndArray() {
        assertTrue(InvalidInputType.SPECIAL_CHARACTERS.appliesTo("string"));
        assertTrue(InvalidInputType.SPECIAL_CHARACTERS.appliesTo("array"));
        assertFalse(InvalidInputType.SPECIAL_CHARACTERS.appliesTo("boolean"));
        assertFalse(InvalidInputType.SPECIAL_CHARACTERS.appliesTo("integer"));
        assertFalse(InvalidInputType.SPECIAL_CHARACTERS.appliesTo("number"));
    }

    @Test
    public void regexMismatchOnlyAppliesToString() {
        assertTrue(InvalidInputType.REGEX_MISMATCH.appliesTo("string"));
        assertFalse(InvalidInputType.REGEX_MISMATCH.appliesTo("integer"));
        assertFalse(InvalidInputType.REGEX_MISMATCH.appliesTo("boolean"));
    }

    @Test
    public void semanticMismatchDoesNotApplyToBoolean() {
        assertFalse(InvalidInputType.SEMANTIC_MISMATCH.appliesTo("boolean"));
        assertTrue(InvalidInputType.SEMANTIC_MISMATCH.appliesTo("string"));
        assertTrue(InvalidInputType.SEMANTIC_MISMATCH.appliesTo("integer"));
    }

    @Test
    public void emptyInputAppliesOnlyToValueBearingContainers() {
        assertTrue(InvalidInputType.EMPTY_INPUT.appliesTo("string"));
        assertTrue(InvalidInputType.EMPTY_INPUT.appliesTo("array"));
        assertTrue(InvalidInputType.EMPTY_INPUT.appliesTo("object"));
        assertFalse(InvalidInputType.EMPTY_INPUT.appliesTo("integer"));
        assertFalse(InvalidInputType.EMPTY_INPUT.appliesTo("boolean"));
    }

    @Test
    public void typeMismatchAndNullInputApplyEverywhere() {
        for (String t : new String[]{"string", "integer", "number", "boolean", "array", "object"}) {
            assertTrue("TYPE_MISMATCH should apply to " + t,
                    InvalidInputType.TYPE_MISMATCH.appliesTo(t));
            assertTrue("NULL_INPUT should apply to " + t,
                    InvalidInputType.NULL_INPUT.appliesTo(t));
        }
    }

    @Test
    public void nullSchemaTypeDefaultsToApplicable() {
        // When type is unknown, we don't drop fault types — be conservative and keep coverage.
        for (InvalidInputType t : InvalidInputType.values()) {
            assertTrue(t.name() + " should default to applicable for null type",
                    t.appliesTo(null));
        }
    }
}
