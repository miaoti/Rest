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
}

