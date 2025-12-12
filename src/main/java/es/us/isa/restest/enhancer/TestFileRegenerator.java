package es.us.isa.restest.enhancer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Regenerates test files with enhanced parameter values.
 * 
 * This class parses existing test Java files, identifies parameter assignments,
 * and replaces them with LLM-suggested improved values.
 */
public class TestFileRegenerator {
    
    private static final Logger log = LogManager.getLogger(TestFileRegenerator.class);
    
    // Track which tests have been enhanced
    private final Map<String, EnhancementInfo> enhancedTests = new HashMap<>();
    
    /**
     * Regenerate a test file with enhanced parameter values.
     * 
     * @param testFilePath Path to the Java test file
     * @param testMethodName Name of the test method to modify
     * @param enhancedParams Map of parameter name to new value
     * @param originalFailure Original failure information for Allure attachment
     * @return true if regeneration was successful
     */
    public boolean regenerateTestFile(String testFilePath, String testMethodName,
                                      Map<String, String> enhancedParams, 
                                      FailedTestResult originalFailure) {
        
        log.info("🔄 Regenerating test file: {} (method: {})", testFilePath, testMethodName);
        log.info("   Enhanced parameters: {}", enhancedParams);
        
        try {
            Path path = Paths.get(testFilePath);
            if (!Files.exists(path)) {
                log.error("Test file not found: {}", testFilePath);
                return false;
            }
            
            // Read original content
            String content = Files.readString(path);
            String originalContent = content;
            
            // Find the test method
            int methodStart = findMethodStart(content, testMethodName);
            if (methodStart < 0) {
                log.error("Could not find test method: {}", testMethodName);
                return false;
            }
            
            int methodEnd = findMethodEnd(content, methodStart);
            if (methodEnd < 0) {
                log.error("Could not find end of test method: {}", testMethodName);
                return false;
            }
            
            String methodContent = content.substring(methodStart, methodEnd);
            String modifiedMethod = methodContent;
            
            // Replace each parameter value
            for (Map.Entry<String, String> param : enhancedParams.entrySet()) {
                modifiedMethod = replaceParameterValue(modifiedMethod, param.getKey(), param.getValue());
            }
            
            // Add enhancement marker at the beginning of the method
            modifiedMethod = addEnhancementMarker(modifiedMethod, testMethodName, 
                    originalFailure, enhancedParams);
            
            // Reconstruct the file
            String newContent = content.substring(0, methodStart) + 
                               modifiedMethod + 
                               content.substring(methodEnd);
            
            // Write back to file
            Files.writeString(path, newContent);
            
            // Track enhancement
            enhancedTests.put(testFilePath + "#" + testMethodName, 
                    new EnhancementInfo(testFilePath, testMethodName, enhancedParams, originalFailure));
            
            log.info("✅ Successfully regenerated test file");
            return true;
            
        } catch (IOException e) {
            log.error("Failed to regenerate test file: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Replace a parameter value in the method content.
     */
    private String replaceParameterValue(String methodContent, String paramName, String newValue) {
        String originalContent = methodContent;
        
        // Pattern 1: JSON body in Java string - \"paramName\":\"oldValue\" (escaped quotes in Java strings)
        // This is the main pattern used in generated test code like: String requestBody1 = "{\"stationList\":\"value\"}";
        String escapedJsonPattern = "\\\\\"" + Pattern.quote(paramName) + "\\\\\"\\s*:\\s*\\\\\"[^\\\\]*\\\\\"";
        String escapedReplacement = "\\\\\"" + paramName + "\\\\\":\\\\\"" + escapeJavaForJson(newValue) + "\\\\\"";
        methodContent = methodContent.replaceAll(escapedJsonPattern, escapedReplacement);
        
        // Pattern 2: Body field assignment - bodyFields.put("paramName", "oldValue")
        String bodyPattern = "bodyFields\\.put\\(\"" + Pattern.quote(paramName) + "\",\\s*\"[^\"]*\"\\)";
        methodContent = methodContent.replaceAll(bodyPattern, 
                "bodyFields.put(\"" + paramName + "\", \"" + escapeJava(newValue) + "\")");
        
        // Pattern 3: Regular JSON (not escaped) - "paramName":"oldValue"
        String jsonPattern = "\"" + Pattern.quote(paramName) + "\"\\s*:\\s*\"[^\"]*\"";
        methodContent = methodContent.replaceAll(jsonPattern, 
                "\"" + paramName + "\":\"" + escapeJava(newValue) + "\"");
        
        // Pattern 4: Path parameter - pathParams.put("paramName", "oldValue")
        String pathPattern = "pathParams\\.put\\(\"" + Pattern.quote(paramName) + "\",\\s*\"[^\"]*\"\\)";
        methodContent = methodContent.replaceAll(pathPattern, 
                "pathParams.put(\"" + paramName + "\", \"" + escapeJava(newValue) + "\")");
        
        // Pattern 5: Query parameter - queryParams.put("paramName", "oldValue")
        String queryPattern = "queryParams\\.put\\(\"" + Pattern.quote(paramName) + "\",\\s*\"[^\"]*\"\\)";
        methodContent = methodContent.replaceAll(queryPattern, 
                "queryParams.put(\"" + paramName + "\", \"" + escapeJava(newValue) + "\")");
        
        // Pattern 6: Allure parameter reporting
        String allurePattern = "Allure\\.parameter\\(\"🔴 Invalid Parameters\",\\s*\"" + 
                Pattern.quote(paramName) + "=[^\"]*\"\\)";
        methodContent = methodContent.replaceAll(allurePattern, 
                "Allure.parameter(\"🔴 Invalid Parameters (ENHANCED)\", \"" + 
                paramName + "=" + escapeJava(newValue) + "\")");
        
        // Log if any changes were made
        if (!methodContent.equals(originalContent)) {
            log.debug("   Replaced {} with new value", paramName);
        }
        
        return methodContent;
    }
    
    /**
     * Escape string for use inside a JSON value that's inside a Java string literal.
     * This handles double escaping needed for: String s = "{\"key\":\"value\"}";
     */
    private String escapeJavaForJson(String s) {
        if (s == null) return "";
        // First escape for JSON, then escape for Java string
        return s.replace("\\", "\\\\\\\\")  // Backslash needs 4 escapes in replacement
                .replace("\"", "\\\\\\\"")   // Quote needs escaped quote in JSON, then escaped for Java
                .replace("\n", "\\\\n")
                .replace("\r", "\\\\r")
                .replace("\t", "\\\\t");
    }
    
    /**
     * Add enhancement marker to the test method.
     */
    private String addEnhancementMarker(String methodContent, String testMethodName,
                                       FailedTestResult originalFailure, 
                                       Map<String, String> enhancedParams) {
        
        // Find the position after the method declaration line
        int insertPos = methodContent.indexOf('{') + 1;
        if (insertPos <= 0) return methodContent;
        
        StringBuilder marker = new StringBuilder();
        marker.append("\n        // ═══════════════════════════════════════════════════════════════════════\n");
        marker.append("        // 🔧 ENHANCED TEST - Modified by Test Case Enhancer\n");
        marker.append("        // Original Status: ").append(originalFailure.getActualStatusCode()).append("\n");
        marker.append("        // Enhanced Parameters: ").append(enhancedParams.keySet()).append("\n");
        marker.append("        // ═══════════════════════════════════════════════════════════════════════\n");
        marker.append("        Allure.label(\"enhancement\", \"ENHANCED\");\n");
        marker.append("        Allure.addAttachment(\"📝 Original Failure\", \"text/plain\", ");
        marker.append("\"Original Status: ").append(originalFailure.getActualStatusCode());
        marker.append("\\nOriginal Response: ").append(escapeJava(truncate(originalFailure.getResponseBody(), 200)));
        marker.append("\\nEnhanced Parameters: ").append(escapeJava(enhancedParams.toString())).append("\");\n");
        
        return methodContent.substring(0, insertPos) + marker + methodContent.substring(insertPos);
    }
    
    /**
     * Find the start position of a test method.
     */
    private int findMethodStart(String content, String methodName) {
        // Look for @Test annotation followed by method declaration
        Pattern pattern = Pattern.compile(
                "@Test\\s*(?:\\([^)]*\\))?\\s*\\n\\s*public\\s+void\\s+" + 
                Pattern.quote(methodName) + "\\s*\\([^)]*\\)\\s*(?:throws[^{]+)?\\{",
                Pattern.MULTILINE
        );
        
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.start();
        }
        
        // Fallback: just look for method signature
        Pattern fallback = Pattern.compile(
                "public\\s+void\\s+" + Pattern.quote(methodName) + "\\s*\\(",
                Pattern.MULTILINE
        );
        
        Matcher fallbackMatcher = fallback.matcher(content);
        if (fallbackMatcher.find()) {
            // Go back to find @Test annotation
            int methodSig = fallbackMatcher.start();
            int searchStart = Math.max(0, methodSig - 100);
            String before = content.substring(searchStart, methodSig);
            int testAnnotation = before.lastIndexOf("@Test");
            if (testAnnotation >= 0) {
                return searchStart + testAnnotation;
            }
            return methodSig;
        }
        
        return -1;
    }
    
    /**
     * Find the end position of a method (matching closing brace).
     */
    private int findMethodEnd(String content, int methodStart) {
        int braceCount = 0;
        boolean inMethod = false;
        
        for (int i = methodStart; i < content.length(); i++) {
            char c = content.charAt(i);
            
            if (c == '{') {
                braceCount++;
                inMethod = true;
            } else if (c == '}') {
                braceCount--;
                if (inMethod && braceCount == 0) {
                    return i + 1;  // Include the closing brace
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Escape string for Java string literal.
     */
    private String escapeJava(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() > maxLen ? s.substring(0, maxLen) + "..." : s;
    }
    
    /**
     * Get all tests that were enhanced.
     */
    public Map<String, EnhancementInfo> getEnhancedTests() {
        return new HashMap<>(enhancedTests);
    }
    
    /**
     * Information about an enhanced test.
     */
    public static class EnhancementInfo {
        public final String testFilePath;
        public final String testMethodName;
        public final Map<String, String> enhancedParams;
        public final FailedTestResult originalFailure;
        
        public EnhancementInfo(String testFilePath, String testMethodName,
                              Map<String, String> enhancedParams, 
                              FailedTestResult originalFailure) {
            this.testFilePath = testFilePath;
            this.testMethodName = testMethodName;
            this.enhancedParams = enhancedParams;
            this.originalFailure = originalFailure;
        }
    }
}

