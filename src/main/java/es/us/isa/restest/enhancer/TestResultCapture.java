package es.us.isa.restest.enhancer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe utility for capturing test execution results.
 * Generated tests call these static methods to store response data
 * that can later be collected by the Test Case Enhancer.
 * 
 * Uses ThreadLocal storage to ensure thread-safety in parallel test execution.
 */
public class TestResultCapture {
    
    private static final Logger log = LogManager.getLogger(TestResultCapture.class);
    
    // ThreadLocal storage for current test context
    private static final ThreadLocal<TestContext> currentContext = new ThreadLocal<>();
    
    // Global storage for all captured results (thread-safe)
    private static final Map<String, FailedTestResult> capturedResults = new ConcurrentHashMap<>();
    
    // Flag to enable/disable capture
    private static volatile boolean captureEnabled = false;
    
    /**
     * Enable result capture (called before test execution starts).
     */
    public static void enableCapture() {
        captureEnabled = true;
        capturedResults.clear();
        log.info("Test result capture ENABLED");
    }
    
    /**
     * Disable result capture and return all captured results.
     * NOTE: Results are NOT cleared - call clearResults() when done with exploration.
     */
    public static Map<String, FailedTestResult> disableCaptureAndGetResults() {
        captureEnabled = false;
        Map<String, FailedTestResult> results = new ConcurrentHashMap<>(capturedResults);
        // Don't clear here - results needed for status code exploration
        log.info("Test result capture DISABLED. Captured {} results", results.size());
        return results;
    }
    
    /**
     * Get a snapshot of captured results without modifying state.
     * Use this for status code exploration after disableCaptureAndGetResults().
     */
    public static Map<String, FailedTestResult> getResultsSnapshot() {
        return new ConcurrentHashMap<>(capturedResults);
    }
    
    /**
     * Explicitly clear all captured results.
     * Call this after status code exploration is complete.
     */
    public static void clearResults() {
        int count = capturedResults.size();
        capturedResults.clear();
        log.debug("Cleared {} captured results", count);
    }
    
    /**
     * Check if capture is enabled.
     */
    public static boolean isCaptureEnabled() {
        return captureEnabled;
    }
    
    /**
     * Set the current test being executed.
     * Called at the beginning of each test method.
     */
    public static void setCurrentTest(String testClassName, String testMethodName) {
        if (!captureEnabled) return;
        
        TestContext context = new TestContext();
        context.testClassName = testClassName;
        context.testMethodName = testMethodName;
        context.startTime = System.currentTimeMillis();
        currentContext.set(context);
        
        log.debug("Started capturing test: {}.{}", testClassName, testMethodName);
    }
    
    /**
     * Set test metadata.
     */
    public static void setTestMetadata(String endpoint, String httpMethod, 
                                       String serviceName, boolean isNegativeTest) {
        if (!captureEnabled) return;
        
        TestContext context = currentContext.get();
        if (context != null) {
            context.endpoint = endpoint;
            context.httpMethod = httpMethod;
            context.serviceName = serviceName;
            context.isNegativeTest = isNegativeTest;
        }
    }
    
    /**
     * Add a parameter snapshot for the current test.
     */
    public static void addParameter(String name, String value, String type, 
                                   String location, String description, String example) {
        addParameter(name, value, type, location, description, example, false);
    }
    
    /**
     * Add a parameter snapshot with required flag for the current test.
     */
    public static void addParameter(String name, String value, String type, 
                                   String location, String description, String example, boolean required) {
        if (!captureEnabled) return;
        
        TestContext context = currentContext.get();
        if (context != null) {
            ParameterSnapshot param = ParameterSnapshot.builder()
                    .name(name)
                    .value(value)
                    .type(type)
                    .location(location)
                    .description(description)
                    .example(example)
                    .required(required)
                    .build();
            context.parameters.add(param);
        }
    }
    
    /**
     * Capture the response from a step execution.
     */
    public static void captureResponse(int statusCode, String responseBody) {
        if (!captureEnabled) return;
        
        TestContext context = currentContext.get();
        if (context != null) {
            context.lastStatusCode = statusCode;
            context.lastResponseBody = responseBody;
        }
    }
    
    /**
     * Mark the current test as failed and store the result.
     */
    public static void markTestFailed(String errorMessage, String failureType) {
        if (!captureEnabled) return;
        
        TestContext context = currentContext.get();
        if (context == null) {
            log.warn("No test context found when marking test as failed");
            return;
        }
        
        String testKey = context.testClassName + "." + context.testMethodName;
        
        FailedTestResult result = FailedTestResult.builder()
                .testClassName(context.testClassName)
                .testMethodName(context.testMethodName)
                .endpoint(context.endpoint)
                .httpMethod(context.httpMethod)
                .serviceName(context.serviceName)
                .negativeTest(context.isNegativeTest)
                .invalidParameters(new ArrayList<>(context.invalidParameters))
                .actualStatusCode(context.lastStatusCode)
                .responseBody(context.lastResponseBody)
                .errorMessage(errorMessage)
                .failureType(failureType)
                .parameters(new ArrayList<>(context.parameters))
                .executionTimestamp(context.startTime)
                .build();
        
        capturedResults.put(testKey, result);
        log.debug("Captured failed test: {} (status: {}, enhanceable: {})", 
                testKey, context.lastStatusCode, result.isEnhanceable());
    }
    
    /**
     * Clear the current test context (called at end of test method).
     */
    public static void clearCurrentTest() {
        currentContext.remove();
    }
    
    /**
     * Get all captured failed test results.
     */
    public static List<FailedTestResult> getAllCapturedResults() {
        return new ArrayList<>(capturedResults.values());
    }
    
    /**
     * Get only enhanceable failures (non-5xx errors).
     */
    public static List<FailedTestResult> getEnhanceableFailures() {
        List<FailedTestResult> enhanceable = new ArrayList<>();
        for (FailedTestResult result : capturedResults.values()) {
            if (result.isEnhanceable()) {
                enhanceable.add(result);
            }
        }
        return enhanceable;
    }
    
    /**
     * Add an invalid parameter (for negative tests).
     * These parameters are intentionally invalid and should NOT be changed during enhancement.
     */
    public static void addInvalidParameter(String paramNameAndValue) {
        if (!captureEnabled) return;
        
        TestContext context = currentContext.get();
        if (context != null) {
            context.invalidParameters.add(paramNameAndValue);
        }
    }
    
    /**
     * Internal context for a single test execution.
     */
    private static class TestContext {
        String testClassName;
        String testMethodName;
        String endpoint;
        String httpMethod;
        String serviceName;
        boolean isNegativeTest;
        int lastStatusCode;
        String lastResponseBody;
        long startTime;
        List<ParameterSnapshot> parameters = new ArrayList<>();
        List<String> invalidParameters = new ArrayList<>();
    }
}

