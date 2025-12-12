package es.us.isa.restest.generators;

import es.us.isa.restest.inputs.InvalidInputPool;
import es.us.isa.restest.inputs.InvalidInputType;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Generates hardcoded invalid inputs for negative testing.
 * This is a faster, deterministic alternative to LLM-based generation.
 * 
 * Benefits:
 * - No LLM API calls needed (faster, no cost)
 * - Predictable and reproducible results
 * - Comprehensive coverage of common invalid input patterns
 * 
 * For array parameters, generates BOTH:
 * 1. Invalid array structures (null, empty, wrong type)
 * 2. Arrays with invalid element values (null elements, wrong type elements)
 */
public class HardcodedInvalidInputGenerator {
    
    private static final Logger log = LogManager.getLogger(HardcodedInvalidInputGenerator.class);
    
    /**
     * Generate comprehensive invalid input pool using hardcoded values.
     * No LLM calls - all values are deterministic.
     */
    public InvalidInputPool generateInvalidInputPool(ParameterInfo param) {
        String paramName = param.getName() != null ? param.getName() : "unknown";
        String paramType = param.getType() != null ? param.getType().toLowerCase() : "string";
        
        log.info("🔧 [HARDCODE] Generating invalid input pool for '{}' (type: {})", paramName, paramType);
        
        InvalidInputPool pool = new InvalidInputPool(paramName, paramType);
        
        // Generate each type of invalid input using hardcoded values
        generateTypeMismatchInputs(param, pool);
        generateOverflowInputs(param, pool);
        generateEmptyInputs(param, pool);
        generateNullInputs(param, pool);
        generateSpecialCharacterInputs(param, pool);
        generateBoundaryViolationInputs(param, pool);
        generateRegexMismatchInputs(param, pool);
        generateSemanticMismatchInputs(param, pool);
        
        // Special handling for array types - generate both array-level and element-level invalids
        if ("array".equalsIgnoreCase(paramType)) {
            generateArraySpecificInputs(param, pool);
        }
        
        log.info("✅ [HARDCODE] Generated invalid input pool:\n{}", pool.getPoolSummary());
        
        return pool;
    }
    
    /**
     * Generate type mismatch inputs - wrong data type for the parameter.
     * These are stored as raw objects (Integer, Boolean, etc.) NOT strings.
     */
    private void generateTypeMismatchInputs(ParameterInfo param, InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        log.debug("  📝 Generating TYPE_MISMATCH for type: {}", paramType);
        
        switch (paramType) {
            case "string":
                // String expects text, provide numbers/booleans/null
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 12345);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, -999);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 3.14159);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, false);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList(1, 2, 3));
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Collections.singletonMap("key", "value"));
                break;
                
            case "integer":
            case "int":
            case "long":
            case "number":
                // Number expects integer, provide strings/booleans
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "not_a_number");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "12.34abc");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "NaN");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "infinity");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, false);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList("a", "b"));
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "");
                break;
                
            case "boolean":
            case "bool":
                // Boolean expects true/false, provide strings/numbers
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "yes");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "no");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "maybe");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 1);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 0);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, -1);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "TRUE");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 2);
                break;
                
            case "array":
                // Array expects list, provide primitives
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "not_an_array");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 123);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Collections.singletonMap("key", "value"));
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 3.14);
                break;
                
            case "object":
                // Object expects key-value, provide primitives
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "not_an_object");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 456);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList(1, 2, 3));
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 2.718);
                break;
                
            case "double":
            case "float":
                // Float/double expects decimal, provide strings/booleans
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "not_a_float");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "1.2.3.4");
                pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList(1.0, 2.0));
                pool.addValue(InvalidInputType.TYPE_MISMATCH, "");
                break;
                
            default:
                // Generic type mismatches
                pool.addValue(InvalidInputType.TYPE_MISMATCH, 999);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList("x"));
                pool.addValue(InvalidInputType.TYPE_MISMATCH, Collections.emptyMap());
                break;
        }
    }
    
    /**
     * Generate overflow inputs - values that exceed expected limits.
     */
    private void generateOverflowInputs(ParameterInfo param, InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        log.debug("  📝 Generating OVERFLOW for type: {}", paramType);
        
        switch (paramType) {
            case "string":
                // Very long strings
                pool.addValue(InvalidInputType.OVERFLOW, "A".repeat(1000));
                pool.addValue(InvalidInputType.OVERFLOW, "X".repeat(5000));
                pool.addValue(InvalidInputType.OVERFLOW, "Z".repeat(10000));
                pool.addValue(InvalidInputType.OVERFLOW, "Lorem ipsum dolor sit amet ".repeat(100));
                break;
                
            case "integer":
            case "int":
                pool.addValue(InvalidInputType.OVERFLOW, Integer.MAX_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, Integer.MIN_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, Long.MAX_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, Long.MIN_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, "99999999999999999999");
                break;
                
            case "long":
                pool.addValue(InvalidInputType.OVERFLOW, Long.MAX_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, Long.MIN_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, "999999999999999999999999999999");
                break;
                
            case "double":
            case "float":
            case "number":
                pool.addValue(InvalidInputType.OVERFLOW, Double.MAX_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, Double.MIN_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, -Double.MAX_VALUE);
                pool.addValue(InvalidInputType.OVERFLOW, "1E+309");
                pool.addValue(InvalidInputType.OVERFLOW, "1E-324");
                break;
                
            case "array":
                // Very large arrays
                List<Object> largeArray = new ArrayList<>();
                for (int i = 0; i < 1000; i++) {
                    largeArray.add("item_" + i);
                }
                pool.addValue(InvalidInputType.OVERFLOW, largeArray);
                break;
                
            default:
                pool.addValue(InvalidInputType.OVERFLOW, "A".repeat(5000));
                pool.addValue(InvalidInputType.OVERFLOW, Integer.MAX_VALUE);
                break;
        }
    }
    
    /**
     * Generate empty inputs - empty string, empty array, empty object.
     * ONLY for REQUIRED parameters.
     */
    private void generateEmptyInputs(ParameterInfo param, InvalidInputPool pool) {
        // Skip empty inputs for optional parameters - they are valid!
        if (param.getRequired() == null || !param.getRequired()) {
            log.debug("  ⚠️ Skipping EMPTY_INPUT for optional parameter: {}", param.getName());
            return;
        }
        
        log.debug("  📝 Generating EMPTY_INPUT for required parameter: {}", param.getName());
        String paramType = safeStr(param.getType()).toLowerCase();
        
        // Empty string variations
        pool.addValue(InvalidInputType.EMPTY_INPUT, "");
        pool.addValue(InvalidInputType.EMPTY_INPUT, " ");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "   ");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "\t");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "\n");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "\r\n");
        pool.addValue(InvalidInputType.EMPTY_INPUT, " \t \n ");
        
        // Type-specific empty values
        if ("array".equals(paramType)) {
            pool.addValue(InvalidInputType.EMPTY_INPUT, Collections.emptyList());
            pool.addValue(InvalidInputType.EMPTY_INPUT, "[]");
        } else if ("object".equals(paramType)) {
            pool.addValue(InvalidInputType.EMPTY_INPUT, Collections.emptyMap());
            pool.addValue(InvalidInputType.EMPTY_INPUT, "{}");
        }
    }
    
    /**
     * Generate null inputs.
     * ONLY for REQUIRED parameters.
     */
    private void generateNullInputs(ParameterInfo param, InvalidInputPool pool) {
        // Skip null inputs for optional parameters - they are valid!
        if (param.getRequired() == null || !param.getRequired()) {
            log.debug("  ⚠️ Skipping NULL_INPUT for optional parameter: {}", param.getName());
            return;
        }
        
        log.debug("  📝 Generating NULL_INPUT for required parameter: {}", param.getName());
        
        // Actual null
        pool.addValue(InvalidInputType.NULL_INPUT, null);
        
        // String representations of null (sometimes APIs parse these)
        pool.addValue(InvalidInputType.NULL_INPUT, "null");
        pool.addValue(InvalidInputType.NULL_INPUT, "NULL");
        pool.addValue(InvalidInputType.NULL_INPUT, "null");
        pool.addValue(InvalidInputType.NULL_INPUT, "nil");
        pool.addValue(InvalidInputType.NULL_INPUT, "undefined");
        pool.addValue(InvalidInputType.NULL_INPUT, "None");
    }
    
    /**
     * Generate special character inputs (potential injection attempts).
     */
    private void generateSpecialCharacterInputs(ParameterInfo param, InvalidInputPool pool) {
        log.debug("  📝 Generating SPECIAL_CHARACTERS for: {}", param.getName());
        
        // SQL injection attempts
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "' OR '1'='1");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "'; DROP TABLE users; --");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "1; DELETE FROM users WHERE 1=1");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "' UNION SELECT * FROM passwords --");
        
        // XSS attempts
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "<script>alert('XSS')</script>");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "<img src=x onerror=alert('XSS')>");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "javascript:alert('XSS')");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "<svg onload=alert('XSS')>");
        
        // Path traversal
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "../../../etc/passwd");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "..\\..\\..\\windows\\system32\\config\\sam");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "%2e%2e%2f%2e%2e%2f");
        
        // Command injection
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "; ls -la");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "| cat /etc/passwd");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "`whoami`");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "$(cat /etc/passwd)");
        
        // Special characters that might break parsing
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "!@#$%^&*(){}[]|\\:;\"'<>?,./");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "\\x00\\x01\\x02");
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, "\u0000\u0001\u0002");
    }
    
    /**
     * Generate boundary violation inputs.
     */
    private void generateBoundaryViolationInputs(ParameterInfo param, InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();
        
        log.debug("  📝 Generating BOUNDARY_VIOLATION for type: {}", paramType);
        
        switch (paramType) {
            case "integer":
            case "int":
            case "long":
            case "number":
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, -1);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, 0);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, -999999);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, 999999999);
                break;
                
            case "string":
                // Boundary length violations
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "");  // Zero length
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "a"); // One character
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "A".repeat(256)); // Common max
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "B".repeat(257)); // Just over common max
                break;
                
            case "double":
            case "float":
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, -0.0001);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, 0.0);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, 0.0001);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, -1.0);
                break;
                
            case "array":
                // Boundary array sizes
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, Collections.emptyList());
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, Collections.singletonList("single"));
                break;
                
            default:
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, -1);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, 0);
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "");
                break;
        }
    }
    
    /**
     * Generate regex mismatch inputs based on parameter name patterns.
     */
    private void generateRegexMismatchInputs(ParameterInfo param, InvalidInputPool pool) {
        String paramName = safeStr(param.getName()).toLowerCase();
        
        log.debug("  📝 Generating REGEX_MISMATCH for: {}", paramName);
        
        // Email pattern mismatches
        if (paramName.contains("email") || paramName.contains("mail")) {
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "not_an_email");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "missing@domain");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "@nodomain.com");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "spaces in@email.com");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "double@@at.com");
        }
        // Phone pattern mismatches
        else if (paramName.contains("phone") || paramName.contains("tel") || paramName.contains("mobile")) {
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "not-a-phone");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "123");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "abc-def-ghij");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "++1234567890");
        }
        // Date pattern mismatches
        else if (paramName.contains("date") || paramName.contains("time") || paramName.contains("timestamp")) {
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "not-a-date");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "32-13-2024");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "2024/99/99");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "yesterday");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "00:99:99");
        }
        // UUID pattern mismatches
        else if (paramName.contains("id") || paramName.contains("uuid") || paramName.contains("guid")) {
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "not-a-uuid");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "12345");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "gggggggg-gggg-gggg-gggg-gggggggggggg");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "");
        }
        // URL pattern mismatches
        else if (paramName.contains("url") || paramName.contains("link") || paramName.contains("uri")) {
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "not_a_url");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "htp://wrong-protocol.com");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "://missing-protocol.com");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "http://");
        }
        // Generic regex mismatches
        else {
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "!@#$%");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "   ");
            pool.addValue(InvalidInputType.REGEX_MISMATCH, "\t\n\r");
        }
    }
    
    /**
     * Generate semantic mismatch inputs based on parameter name patterns.
     */
    private void generateSemanticMismatchInputs(ParameterInfo param, InvalidInputPool pool) {
        String paramName = safeStr(param.getName()).toLowerCase();
        
        log.debug("  📝 Generating SEMANTIC_MISMATCH for: {}", paramName);
        
        // Age-related semantic mismatches
        if (paramName.contains("age")) {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, -5);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, 0);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, 200);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, 999);
        }
        // Price/amount semantic mismatches
        else if (paramName.contains("price") || paramName.contains("amount") || paramName.contains("cost")) {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, -100.00);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, -0.01);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, 999999999.99);
        }
        // Quantity semantic mismatches
        else if (paramName.contains("quantity") || paramName.contains("count") || paramName.contains("num")) {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, -1);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, 0);
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, -999);
        }
        // Country/region semantic mismatches
        else if (paramName.contains("country") || paramName.contains("region") || paramName.contains("state")) {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "ZZZ");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "XXX");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "NonExistentCountry");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "123");
        }
        // Station/location semantic mismatches (TrainTicket specific)
        else if (paramName.contains("station") || paramName.contains("city") || paramName.contains("place")) {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "NonExistentStation");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "ZZZZZ");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "12345");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "");
        }
        // Train/trip semantic mismatches (TrainTicket specific)
        else if (paramName.contains("train") || paramName.contains("trip")) {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "NonExistentTrain");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "INVALID_TRIP_ID");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "00000000-0000-0000-0000-000000000000");
        }
        // Generic semantic mismatches
        else {
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "INVALID_VALUE");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "NON_EXISTENT");
            pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, "ZZZZZZZZZ");
        }
    }
    
    /**
     * Generate array-specific invalid inputs.
     * This covers BOTH:
     * 1. Invalid array structures (wrong type, malformed)
     * 2. Arrays with invalid element values (null elements, wrong type elements)
     */
    private void generateArraySpecificInputs(ParameterInfo param, InvalidInputPool pool) {
        log.debug("  📝 Generating ARRAY-SPECIFIC invalid inputs for: {}", param.getName());
        
        // === ARRAY STRUCTURE LEVEL INVALIDS ===
        // These test the array container itself
        
        // Null array (already covered in NULL_INPUT but adding for completeness)
        pool.addValue(InvalidInputType.TYPE_MISMATCH, null);
        
        // Non-array types where array expected
        pool.addValue(InvalidInputType.TYPE_MISMATCH, "this is a string not an array");
        pool.addValue(InvalidInputType.TYPE_MISMATCH, 12345);
        pool.addValue(InvalidInputType.TYPE_MISMATCH, 3.14159);
        pool.addValue(InvalidInputType.TYPE_MISMATCH, true);
        pool.addValue(InvalidInputType.TYPE_MISMATCH, Collections.singletonMap("key", "value")); // Object instead of array
        
        // Malformed array strings
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "[");
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "]");
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "[,]");
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "[,,]");
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "[[[]]]");
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "[1,2,3");
        pool.addValue(InvalidInputType.REGEX_MISMATCH, "1,2,3]");
        
        // === ARRAY ELEMENT LEVEL INVALIDS ===
        // These test invalid VALUES inside the array
        
        // Array with null elements
        pool.addValue(InvalidInputType.NULL_INPUT, Arrays.asList(null, null, null));
        pool.addValue(InvalidInputType.NULL_INPUT, Arrays.asList("valid", null, "valid"));
        pool.addValue(InvalidInputType.NULL_INPUT, Arrays.asList(null));
        
        // Array with empty string elements
        pool.addValue(InvalidInputType.EMPTY_INPUT, Arrays.asList("", "", ""));
        pool.addValue(InvalidInputType.EMPTY_INPUT, Arrays.asList("valid", "", "valid"));
        pool.addValue(InvalidInputType.EMPTY_INPUT, Arrays.asList(" ", "  ", "   "));
        
        // Array with mixed types (type mismatch at element level)
        pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList("string", 123, true, null));
        pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList(1, "two", 3, "four"));
        
        // Array with special characters in elements
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, Arrays.asList("<script>alert('XSS')</script>", "normal"));
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, Arrays.asList("' OR '1'='1", "normal"));
        pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, Arrays.asList("../../../etc/passwd"));
        
        // Array with overflow elements
        pool.addValue(InvalidInputType.OVERFLOW, Arrays.asList("A".repeat(1000), "B".repeat(1000)));
        pool.addValue(InvalidInputType.OVERFLOW, Arrays.asList(Integer.MAX_VALUE, Integer.MIN_VALUE));
        
        // Nested arrays (potentially invalid depending on schema)
        pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4)));
        pool.addValue(InvalidInputType.TYPE_MISMATCH, Arrays.asList(Arrays.asList(Arrays.asList("deep"))));
        
        // Array with semantic mismatches
        pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, Arrays.asList("INVALID_ID_1", "INVALID_ID_2"));
        pool.addValue(InvalidInputType.SEMANTIC_MISMATCH, Arrays.asList("NonExistent1", "NonExistent2"));
    }
    
    /**
     * Safe string conversion - returns empty string if null.
     */
    private String safeStr(String s) {
        return s != null ? s : "";
    }
}

