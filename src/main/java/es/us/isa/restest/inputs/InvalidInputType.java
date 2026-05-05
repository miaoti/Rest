package es.us.isa.restest.inputs;

/**
 * Types of invalid inputs for negative testing
 */
public enum InvalidInputType {
    /**
     * Type mismatch - e.g., string when expecting integer, integer when expecting string
     */
    TYPE_MISMATCH("Type Mismatch", "Wrong data type"),
    
    /**
     * Regex pattern mismatch - value doesn't match expected pattern
     */
    REGEX_MISMATCH("Regex Mismatch", "Does not match required pattern"),
    
    /**
     * Semantic mismatch - type is correct but meaning is wrong
     * e.g., negative age, invalid email format, wrong country code
     */
    SEMANTIC_MISMATCH("Semantic Mismatch", "Semantically invalid value"),
    
    /**
     * Overflow - value exceeds expected range or length
     * e.g., very long strings, numbers beyond max value
     */
    OVERFLOW("Overflow", "Value exceeds limits"),
    
    /**
     * Empty input - empty string, empty array, empty object
     */
    EMPTY_INPUT("Empty Input", "Required field left empty"),
    
    /**
     * Null input - null value when not allowed
     */
    NULL_INPUT("Null Input", "Null value provided"),
    
    /**
     * Special characters - injection attempts, SQL injection, XSS
     */
    SPECIAL_CHARACTERS("Special Characters", "Malicious or special characters"),
    
    /**
     * Boundary violation - just outside valid range
     * e.g., minLength-1, maxValue+1
     */
    BOUNDARY_VIOLATION("Boundary Violation", "Just outside valid boundary");
    
    private final String displayName;
    private final String description;
    
    InvalidInputType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }

    /**
     * Whether this fault type is meaningful for a given OpenAPI schema type.
     *
     * <p>A negative test labelled {@code OVERFLOW} for a {@code boolean} parameter
     * is nonsensical — there is no overflow notion for a 2-element domain — and the
     * value generated for it (e.g. {@code "A".repeat(5000)}) is really a
     * {@code TYPE_MISMATCH}. The negative-pool round-robin used to fire every
     * fault type at every parameter; this filter keeps the fault label aligned
     * with the kind of badness a value can actually carry against the schema.
     *
     * <p>The matrix below is conservative — when in doubt we say "applicable"
     * to avoid silently dropping coverage. The cases we deliberately exclude
     * are the ones surfaced by D10 NIFP as label-vs-value mismatches in
     * production runs.
     */
    public boolean appliesTo(String oasType) {
        if (oasType == null) {
            return true;
        }
        String t = oasType.toLowerCase();
        switch (this) {
            case TYPE_MISMATCH:
            case NULL_INPUT:
                return true;
            case EMPTY_INPUT:
                // "Empty" is only meaningful for value-bearing containers (string, array, object).
                return t.equals("string") || t.equals("array") || t.equals("object");
            case OVERFLOW:
                // Numeric/string/array can overflow; boolean and object cannot.
                return t.equals("string") || t.equals("integer") || t.equals("int")
                        || t.equals("long") || t.equals("number") || t.equals("double")
                        || t.equals("float") || t.equals("array");
            case BOUNDARY_VIOLATION:
                return t.equals("string") || t.equals("integer") || t.equals("int")
                        || t.equals("long") || t.equals("number") || t.equals("double")
                        || t.equals("float") || t.equals("array");
            case SPECIAL_CHARACTERS:
                // Special-character payloads are textual; firing them at numeric or
                // boolean params produces a TYPE_MISMATCH wearing a SPECIAL label.
                return t.equals("string") || t.equals("array");
            case REGEX_MISMATCH:
                // Regex patterns only constrain strings.
                return t.equals("string");
            case SEMANTIC_MISMATCH:
                // Booleans have a 2-element domain — no "semantic" gradient.
                return !t.equals("boolean");
            default:
                return true;
        }
    }
}

