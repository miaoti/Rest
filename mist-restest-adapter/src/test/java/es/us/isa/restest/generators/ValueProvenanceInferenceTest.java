package es.us.isa.restest.generators;

import es.us.isa.restest.testcases.MultiServiceTestCase;
import io.mist.core.value.ValueProvenance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Pins the Fix 3 Layer 1 contract: every literal string the generator
 * emits when it cannot resolve a real value must be detectable as a
 * synthetic placeholder, and the resulting {@link MultiServiceTestCase}
 * must report {@link MultiServiceTestCase#hasSyntheticPlaceholder()}
 * true so the writer's resolution-aware classifier reclassifies the
 * test as negative.
 */
public class ValueProvenanceInferenceTest {

    @Test
    public void detectsAllFourSyntheticPrefixes() {
        assertEquals(ValueProvenance.SYNTHETIC_PLACEHOLDER, ValueProvenanceInference.infer("FALLBACK_id_3"));
        assertEquals(ValueProvenance.SYNTHETIC_PLACEHOLDER, ValueProvenanceInference.infer("LLM_EMPTY"));
        assertEquals(ValueProvenance.SYNTHETIC_PLACEHOLDER, ValueProvenanceInference.infer("LLM_EMPTY_userId"));
        assertEquals(ValueProvenance.SYNTHETIC_PLACEHOLDER, ValueProvenanceInference.infer("STEP1_userId_v0"));
        assertEquals(ValueProvenance.SYNTHETIC_PLACEHOLDER, ValueProvenanceInference.infer("VAL_userId"));
    }

    @Test
    public void liveLookingValuesAreNotDetectedAsSynthetic() {
        assertNull(ValueProvenanceInference.infer("user_123"));
        assertNull(ValueProvenanceInference.infer("alice@example.com"));
        assertNull(ValueProvenanceInference.infer("42"));
        assertNull(ValueProvenanceInference.infer("true"));
        assertNull(ValueProvenanceInference.infer(""));
    }

    @Test
    public void nullValueInfersNothing() {
        assertNull(ValueProvenanceInference.infer(null));
    }

    @Test
    public void freshTestCaseHasNoSyntheticPlaceholder() {
        MultiServiceTestCase tc = new MultiServiceTestCase("t1");
        assertFalse(tc.hasSyntheticPlaceholder());
        assertTrue(tc.getParameterProvenance().isEmpty());
    }

    @Test
    public void recordSyntheticPlaceholderFlipsTheFlag() {
        MultiServiceTestCase tc = new MultiServiceTestCase("t2");
        tc.recordParameterProvenance(0, "userId", ValueProvenance.SYNTHETIC_PLACEHOLDER);
        assertTrue(tc.hasSyntheticPlaceholder());
    }

    @Test
    public void recordingOnlyLiveGroundedDoesNotTripTheFlag() {
        MultiServiceTestCase tc = new MultiServiceTestCase("t3");
        tc.recordParameterProvenance(0, "a", ValueProvenance.RESOLVED_LIVE);
        tc.recordParameterProvenance(1, "b", ValueProvenance.RESOLVED_CACHE);
        tc.recordParameterProvenance(2, "c", ValueProvenance.LLM_GENERATED);
        assertFalse(tc.hasSyntheticPlaceholder());
    }

    @Test
    public void mixedProvenanceWithOneSyntheticTripsTheFlag() {
        MultiServiceTestCase tc = new MultiServiceTestCase("t4");
        tc.recordParameterProvenance(0, "a", ValueProvenance.RESOLVED_LIVE);
        tc.recordParameterProvenance(1, "b", ValueProvenance.SYNTHETIC_PLACEHOLDER);
        tc.recordParameterProvenance(2, "c", ValueProvenance.LLM_GENERATED);
        assertTrue(tc.hasSyntheticPlaceholder());
    }

    @Test
    public void provenanceMapReturnedIsUnmodifiable() {
        MultiServiceTestCase tc = new MultiServiceTestCase("t5");
        tc.recordParameterProvenance(0, "a", ValueProvenance.RESOLVED_LIVE);
        try {
            tc.getParameterProvenance().put("0:b", ValueProvenance.SYNTHETIC_PLACEHOLDER);
            org.junit.Assert.fail("expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // ok
        }
    }
}
