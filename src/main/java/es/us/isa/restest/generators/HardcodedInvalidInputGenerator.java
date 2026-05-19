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

        // Generate each fault type only when it is meaningful for the schema type.
        // See InvalidInputType.appliesTo() for the applicability matrix.
        // This prevents nonsense like "OVERFLOW" being applied to a boolean
        // parameter (which has a 2-element domain and cannot overflow), which
        // D10 NIFP previously surfaced as label-vs-value purity failures.
        if (InvalidInputType.TYPE_MISMATCH.appliesTo(paramType))       generateTypeMismatchInputs(param, pool);
        if (InvalidInputType.OVERFLOW.appliesTo(paramType))            generateOverflowInputs(param, pool);
        if (InvalidInputType.EMPTY_INPUT.appliesTo(paramType))         generateEmptyInputs(param, pool);
        if (InvalidInputType.NULL_INPUT.appliesTo(paramType))          generateNullInputs(param, pool);
        if (InvalidInputType.SPECIAL_CHARACTERS.appliesTo(paramType))  generateSpecialCharacterInputs(param, pool);
        if (InvalidInputType.BOUNDARY_VIOLATION.appliesTo(paramType))  generateBoundaryViolationInputs(param, pool);
        if (InvalidInputType.REGEX_MISMATCH.appliesTo(paramType))      generateRegexMismatchInputs(param, pool);
        if (InvalidInputType.SEMANTIC_MISMATCH.appliesTo(paramType))   generateSemanticMismatchInputs(param, pool);
        
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
     *
     * <p>Package-private so {@link ZeroShotLLMGenerator}'s "smart" mode can reuse
     * the static payload set without going through the LLM.
     */
    void generateTypeMismatchInputs(ParameterInfo param, InvalidInputPool pool) {
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
     * Package-private for reuse by {@link ZeroShotLLMGenerator}'s smart mode.
     */
    void generateOverflowInputs(ParameterInfo param, InvalidInputPool pool) {
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
     * Package-private for reuse by {@link ZeroShotLLMGenerator}'s smart mode.
     */
    void generateEmptyInputs(ParameterInfo param, InvalidInputPool pool) {
        // Skip empty inputs for optional parameters - they are valid!
        if (param.getRequired() == null || !param.getRequired()) {
            log.debug("  ⚠️ Skipping EMPTY_INPUT for optional parameter: {}", param.getName());
            return;
        }
        
        log.debug("  📝 Generating EMPTY_INPUT for required parameter: {}", param.getName());
        String paramType = safeStr(param.getType()).toLowerCase();

        // Empty string variations. Skip the pure-empty "" for path-located parameters:
        // empty path segments collapse to a trailing slash and Spring (or any URL
        // router) returns 405/404 before the @PathVariable handler ever runs, so the
        // controller's trim().isEmpty() check is never exercised. Whitespace variants
        // (URL-encoded as %20 etc. by the writer) DO reach the handler.
        if (!isPathLocation(param)) {
            pool.addValue(InvalidInputType.EMPTY_INPUT, "");
        }
        pool.addValue(InvalidInputType.EMPTY_INPUT, " ");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "   ");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "\t");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "\n");
        pool.addValue(InvalidInputType.EMPTY_INPUT, "\r\n");
        pool.addValue(InvalidInputType.EMPTY_INPUT, " \t \n ");

        // For comma-separated string parameters, replacing the WHOLE value tests
        // only the structural validators (empty / single-element). The per-element
        // validators (each element must be non-empty / non-whitespace) are reachable
        // only via element-level mutation. Detection is OpenAPI-spec based and
        // SUT-agnostic; see looksLikeCsv().
        if (looksLikeCsv(param)) {
            String baseline = getCsvBaseline(param);
            pool.addValue(InvalidInputType.EMPTY_INPUT, mutateCsvElement(baseline, ""));
            pool.addValue(InvalidInputType.EMPTY_INPUT, mutateCsvElement(baseline, " "));
            pool.addValue(InvalidInputType.EMPTY_INPUT, mutateCsvElement(baseline, "   "));
        }

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
     * Package-private for reuse by {@link ZeroShotLLMGenerator}'s smart mode.
     */
    void generateNullInputs(ParameterInfo param, InvalidInputPool pool) {
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
     * Package-private for reuse by {@link ZeroShotLLMGenerator}'s smart mode.
     */
    void generateSpecialCharacterInputs(ParameterInfo param, InvalidInputPool pool) {
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

        // CSV element-level special chars: the field as a whole parses fine but a single
        // element carries a hostile payload. Catches per-element sanitisers.
        if (looksLikeCsv(param)) {
            String baseline = getCsvBaseline(param);
            pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, mutateCsvElement(baseline, "<script>"));
            pool.addValue(InvalidInputType.SPECIAL_CHARACTERS, mutateCsvElement(baseline, "'; DROP TABLE x; --"));
        }
    }
    
    /**
     * Generate boundary violation inputs.
     * Package-private for reuse by {@link ZeroShotLLMGenerator}'s smart mode.
     *
     * <p>When the schema declares {@code minimum/maximum/minLength/maxLength} constraints,
     * this method emits precise off-by-one violations ({@code min-1}, {@code max+1},
     * {@code minLength-1}, {@code maxLength+1}).  When no constraints are declared, it
     * falls back to canonical boundaries per type.  This replaces the previous code path
     * where the LLM was asked to produce boundary values without ever being told what
     * the actual boundaries were.
     */
    void generateBoundaryViolationInputs(ParameterInfo param, InvalidInputPool pool) {
        String paramType = safeStr(param.getType()).toLowerCase();

        log.debug("  📝 Generating BOUNDARY_VIOLATION for type: {}", paramType);

        // CSV element-length boundaries (independent of field-level minLength/maxLength).
        // Some APIs validate each comma-separated element separately (e.g. "each station
        // name must be between 2 and 50 characters"). Whole-value boundary mutators never
        // exercise per-element validators because they replace the whole field.
        if (looksLikeCsv(param)) {
            String baseline = getCsvBaseline(param);
            pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, mutateCsvElement(baseline, "X"));
            pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, mutateCsvElement(baseline, "A".repeat(101)));
        }

        // Schema-derived numeric boundaries (off-by-one)
        Number min = param.getMinimum();
        Number max = param.getMaximum();
        Integer minLen = param.getMinLength();
        Integer maxLen = param.getMaxLength();

        // BOUNDARY_VIOLATION is only meaningful when the schema declares a bound
        // to violate. For schemas with no minimum/maximum/minLength/maxLength,
        // any value we emit here would be a label-vs-value mismatch (D10 NIFP
        // surfaced 248/248 such cases as schema-unbounded). Skip silently — the
        // round-robin will simply exercise fewer fault types for that param.
        boolean hasNumericBound = (min != null) || (max != null);
        boolean hasLengthBound = (minLen != null) || (maxLen != null);

        switch (paramType) {
            case "integer":
            case "int":
            case "long":
            case "number":
                if (!hasNumericBound) {
                    log.debug("    ⤷ skipping BOUNDARY_VIOLATION: no min/max declared in schema");
                    return;
                }
                if (min != null && min.longValue() > Long.MIN_VALUE) {
                    pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, min.longValue() - 1);
                }
                if (max != null && max.longValue() < Long.MAX_VALUE) {
                    pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, max.longValue() + 1);
                }
                break;

            case "string":
                if (!hasLengthBound) {
                    log.debug("    ⤷ skipping BOUNDARY_VIOLATION: no minLength/maxLength declared in schema");
                    return;
                }
                // Guard minLen > 0: a minLen of 0 means empty string is valid, so
                // "length - 1 = -1" is meaningless as a boundary violation and would
                // just duplicate EMPTY_INPUT coverage.
                if (minLen != null && minLen > 0) {
                    pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "a".repeat(minLen - 1));
                }
                // Cap the maxLen+1 string at a sane upper bound so we don't blow up the
                // JVM heap when a schema declares maxLength = Integer.MAX_VALUE.
                if (maxLen != null && maxLen > 0 && maxLen < 100_000) {
                    pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, "A".repeat(maxLen + 1));
                }
                break;

            case "double":
            case "float":
                if (!hasNumericBound) {
                    log.debug("    ⤷ skipping BOUNDARY_VIOLATION: no min/max declared in schema");
                    return;
                }
                if (min != null && Double.isFinite(min.doubleValue()) && min.doubleValue() > -Double.MAX_VALUE) {
                    pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, min.doubleValue() - 0.0001);
                }
                if (max != null && Double.isFinite(max.doubleValue()) && max.doubleValue() < Double.MAX_VALUE) {
                    pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, max.doubleValue() + 0.0001);
                }
                break;

            case "array":
                // Array boundary applies when minItems/maxItems is declared. The
                // ParameterInfo model doesn't expose those today; conservatively
                // emit empty + singleton as boundary candidates.
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, Collections.emptyList());
                pool.addValue(InvalidInputType.BOUNDARY_VIOLATION, Collections.singletonList("single"));
                break;

            default:
                // Unknown type without bounds — skip silently.
                return;
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
    /**
     * Returns true when the parameter is located in the URL path. Path-located string
     * params have routing-level constraints distinct from header/body — notably, an
     * empty path segment never reaches the handler.
     */
    private static boolean isPathLocation(ParameterInfo p) {
        if (p == null) return false;
        String loc = p.getInLocation();
        return loc != null && "path".equalsIgnoreCase(loc.trim());
    }

    /**
     * Heuristic: does the parameter look like a comma-separated string list?
     * SUT-agnostic — only inspects OpenAPI-spec primitives carried by {@link ParameterInfo}:
     * <ul>
     *   <li>{@code type:array} — explicit list, trivially CSV-shaped on the wire</li>
     *   <li>{@code description} contains "comma-separated", "comma separated", "csv", or "list of"</li>
     *   <li>{@code example} contains 3+ commas (a single comma might be prose; 3+ implies a list)</li>
     * </ul>
     * False positives are cheap (a few wasted variants); false negatives are the main risk
     * and are mitigated by the LLM's own CSV-aware prompt in ZeroShotLLMGenerator.
     */
    private static boolean looksLikeCsv(ParameterInfo p) {
        if (p == null) return false;
        String type = safeStrStatic(p.getType()).toLowerCase(Locale.ROOT);
        if ("array".equals(type)) return true;
        String desc = safeStrStatic(p.getDescription()).toLowerCase(Locale.ROOT);
        if (desc.contains("comma-separated") || desc.contains("comma separated")
                || desc.contains("csv") || desc.contains("list of")) {
            return true;
        }
        String ex = safeStrStatic(p.getSchemaExample());
        // Count commas. 3+ separators ⇒ 4+ elements ⇒ very likely a list (a sentence
        // describing 4 things rarely uses bare commas with no spaces around them).
        int commas = 0;
        for (int i = 0; i < ex.length(); i++) if (ex.charAt(i) == ',') commas++;
        return commas >= 3;
    }

    /**
     * Produce a baseline CSV string for {@code mutateCsvElement} to operate on.
     * Prefers the parameter's {@code example} (which the OpenAPI author intended as a
     * valid sample). Falls back to a synthetic 3-element CSV when no example is provided.
     */
    private static String getCsvBaseline(ParameterInfo p) {
        String ex = safeStrStatic(p == null ? null : p.getSchemaExample());
        if (ex.contains(",")) return ex;
        // Synthetic baseline. Generic placeholder words — not SUT-specific.
        return "alpha,bravo,charlie";
    }

    /**
     * Replace one interior element of a CSV string with {@code replacement} and return
     * the rejoined value. Interior (not first/last) so the structural validators
     * (insufficient-count, missing-start, missing-end) don't fire first and mask the
     * per-element validator we're actually targeting.
     */
    private static String mutateCsvElement(String baseline, String replacement) {
        if (baseline == null || baseline.isEmpty()) return replacement;
        String[] parts = baseline.split(",", -1);
        if (parts.length < 3) {
            // Too few parts to mutate interior; pad with placeholders then mutate the middle.
            String[] padded = new String[Math.max(3, parts.length)];
            for (int i = 0; i < padded.length; i++) {
                padded[i] = i < parts.length ? parts[i] : ("filler" + i);
            }
            parts = padded;
        }
        int mid = parts.length / 2;
        parts[mid] = replacement;
        return String.join(",", parts);
    }

    /** Null-safe trim helper. {@link #safeStr} is instance-bound; this is a static twin. */
    private static String safeStrStatic(String s) {
        return s == null ? "" : s;
    }

    private String safeStr(String s) {
        return s != null ? s : "";
    }
}

