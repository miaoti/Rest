package es.us.isa.restest.generators;

import es.us.isa.restest.inputs.InvalidInputPool;
import es.us.isa.restest.inputs.InvalidInputType;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Locks in the SUT-agnostic coverage enrichments that target two classes of injected
 * faults missed by the previous generator: (a) path-located parameters where empty
 * path segments never reach the @PathVariable handler, (b) string parameters whose
 * "comma-separated list" semantics aren't expressed via type:array.
 *
 * <p>The tests inspect the pool directly via reflection on {@code valuesByType} —
 * there is no public accessor for the full value list, and counts alone aren't
 * specific enough to prove the right shapes were added.</p>
 */
public class HardcodedInvalidInputGeneratorCoverageTest {

    private final HardcodedInvalidInputGenerator gen = new HardcodedInvalidInputGenerator();

    /** Fix A: empty "" must be omitted for path-located required strings. */
    @Test
    public void pathLocatedString_omitsPureEmptyFromEmptyPool() {
        ParameterInfo p = stringParam("tripId", "path", true, null, null);
        InvalidInputPool pool = gen.generateInvalidInputPool(p);

        List<Object> empties = valuesFor(pool, InvalidInputType.EMPTY_INPUT);
        assertFalse("path-located param must not include pure-empty \"\" — Spring routes /foo/ to a different handler",
                empties.contains(""));
        // Whitespace variants are still present.
        assertTrue(empties.contains(" "));
        assertTrue(empties.contains("\t"));
    }

    /** Mirror: body-located required strings still get "". No regression on the common case. */
    @Test
    public void bodyLocatedString_includesPureEmptyInEmptyPool() {
        ParameterInfo p = stringParam("name", "body", true, null, null);
        InvalidInputPool pool = gen.generateInvalidInputPool(p);

        List<Object> empties = valuesFor(pool, InvalidInputType.EMPTY_INPUT);
        assertTrue("body-located param keeps pure-empty \"\" — the controller's null/empty check is the target",
                empties.contains(""));
        assertTrue(empties.contains(" "));
    }

    /** Fix B: a description naming "comma-separated" triggers element-level mutation. */
    @Test
    public void csvDescription_addsElementMutationsToBoundaryPool() {
        ParameterInfo p = stringParam(
                "stationList", "body", true,
                "Comma-separated list of station names (e.g., \"Shanghai,Beijing\")",
                "Grand Central,Penn,Union,Times Sq,Broadway");
        InvalidInputPool pool = gen.generateInvalidInputPool(p);

        List<Object> boundary = valuesFor(pool, InvalidInputType.BOUNDARY_VIOLATION);
        assertTrue("expected at least one CSV variant with a 1-char interior element",
                boundary.stream().anyMatch(v -> v instanceof String && isCsvWithShortElement((String) v)));
        assertTrue("expected at least one CSV variant with a long (>50 char) interior element",
                boundary.stream().anyMatch(v -> v instanceof String && isCsvWithLongElement((String) v)));
    }

    /** Fix B: a description naming "comma-separated" adds empty/whitespace element variants. */
    @Test
    public void csvDescription_addsEmptyAndWhitespaceElementMutations() {
        ParameterInfo p = stringParam(
                "tags", "body", true,
                "comma-separated tag names", "alpha,bravo,charlie");
        InvalidInputPool pool = gen.generateInvalidInputPool(p);

        List<Object> empties = valuesFor(pool, InvalidInputType.EMPTY_INPUT);
        assertTrue("expected CSV variant with empty interior element (a,,c)",
                empties.stream().anyMatch(v -> v instanceof String && containsEmptyInteriorElement((String) v)));
        assertTrue("expected CSV variant with whitespace-only interior element",
                empties.stream().anyMatch(v -> v instanceof String && containsWhitespaceOnlyInteriorElement((String) v)));
    }

    /** Detection by example: 3+ commas implies a list shape even without description hints. */
    @Test
    public void csvDetectedByExampleCommaCount() {
        ParameterInfo p = stringParam(
                "ids", "body", true,
                null,
                "id-1,id-2,id-3,id-4");
        InvalidInputPool pool = gen.generateInvalidInputPool(p);

        List<Object> boundary = valuesFor(pool, InvalidInputType.BOUNDARY_VIOLATION);
        assertTrue("3+ commas in example should activate CSV-element mutation",
                boundary.stream().anyMatch(v -> v instanceof String && isCsvWithShortElement((String) v)));
    }

    /** No-CSV signal: a regular string with no list hints must NOT get element mutations. */
    @Test
    public void plainString_doesNotGetCsvElementMutations() {
        ParameterInfo p = stringParam(
                "username", "body", true,
                "User account name", "alice");
        InvalidInputPool pool = gen.generateInvalidInputPool(p);

        List<Object> boundary = valuesFor(pool, InvalidInputType.BOUNDARY_VIOLATION);
        for (Object v : boundary) {
            if (v instanceof String && ((String) v).contains(",")) {
                assertFalse("plain string should not get CSV element mutations: " + v,
                        isCsvWithShortElement((String) v));
            }
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────────────

    private static ParameterInfo stringParam(String name, String location, boolean required,
                                             String description, String example) {
        ParameterInfo p = new ParameterInfo();
        p.setName(name);
        p.setInLocation(location);
        p.setType("string");
        p.setRequired(required);
        p.setDescription(description);
        p.setSchemaExample(example);
        return p;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> valuesFor(InvalidInputPool pool, InvalidInputType type) {
        try {
            Field f = InvalidInputPool.class.getDeclaredField("valuesByType");
            f.setAccessible(true);
            Map<InvalidInputType, List<Object>> m = (Map<InvalidInputType, List<Object>>) f.get(pool);
            return m.get(type);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("test reflection failed", e);
        }
    }

    /** A CSV variant where some interior (not-first, not-last) element has length 1. */
    private static boolean isCsvWithShortElement(String s) {
        String[] parts = s.split(",", -1);
        if (parts.length < 3) return false;
        for (int i = 1; i < parts.length - 1; i++) {
            if (parts[i].length() == 1) return true;
        }
        return false;
    }

    /** A CSV variant where some interior element has length > 50. */
    private static boolean isCsvWithLongElement(String s) {
        String[] parts = s.split(",", -1);
        if (parts.length < 3) return false;
        for (int i = 1; i < parts.length - 1; i++) {
            if (parts[i].length() > 50) return true;
        }
        return false;
    }

    private static boolean containsEmptyInteriorElement(String s) {
        String[] parts = s.split(",", -1);
        if (parts.length < 3) return false;
        for (int i = 1; i < parts.length - 1; i++) {
            if (parts[i].isEmpty()) return true;
        }
        return false;
    }

    private static boolean containsWhitespaceOnlyInteriorElement(String s) {
        String[] parts = s.split(",", -1);
        if (parts.length < 3) return false;
        for (int i = 1; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty() && parts[i].trim().isEmpty()) return true;
        }
        return false;
    }
}
