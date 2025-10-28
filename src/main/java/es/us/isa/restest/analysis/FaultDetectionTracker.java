package es.us.isa.restest.analysis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe singleton tracker for monitoring injected fault detection during test execution.
 * Tracks which faults from injected-faults.json are detected by test cases and generates
 * comprehensive coverage reports.
 */
public class FaultDetectionTracker {
    
    private static final Logger logger = LogManager.getLogger(FaultDetectionTracker.class);
    private static final FaultDetectionTracker INSTANCE = new FaultDetectionTracker();
    
    // Thread-safe data structures
    private final Map<String, InjectedFault> injectedFaults = new ConcurrentHashMap<>();
    private final Map<String, List<FaultDetection>> detectedFaults = new ConcurrentHashMap<>();
    private final Set<String> allTestCases = ConcurrentHashMap.newKeySet();
    
    // Metadata
    private String experimentName;
    private long trackingStartTime;
    private boolean initialized = false;
    
    private FaultDetectionTracker() {
        this.trackingStartTime = System.currentTimeMillis();
    }
    
    public static FaultDetectionTracker getInstance() {
        return INSTANCE;
    }
    
    /**
     * Load injected faults from the JSON registry file
     */
    public synchronized void loadInjectedFaults(String jsonPath) {
        try {
            logger.info("Loading injected faults from: {}", jsonPath);
            
            String content = new String(Files.readAllBytes(Paths.get(jsonPath)));
            JSONObject root = new JSONObject(content);
            
            if (!root.has("injected_faults")) {
                logger.error("JSON file does not contain 'injected_faults' array");
                return;
            }
            
            JSONArray faults = root.getJSONArray("injected_faults");
            injectedFaults.clear();
            
            for (int i = 0; i < faults.length(); i++) {
                JSONObject faultJson = faults.getJSONObject(i);
                InjectedFault fault = new InjectedFault(
                    faultJson.getString("faultName"),
                    faultJson.getString("service"),
                    faultJson.getString("api")
                );
                injectedFaults.put(fault.faultName, fault);
            }
            
            logger.info("Loaded {} injected faults from registry", injectedFaults.size());
            initialized = true;
            
        } catch (IOException e) {
            logger.error("Failed to load injected faults file: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to parse injected faults JSON: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Record a detected fault from a test execution
     */
    public synchronized void recordDetectedFault(String faultName, String testClassName, 
                                                 String testMethodName, long timestamp, 
                                                 String responseBody) {
        if (!initialized) {
            logger.warn("FaultDetectionTracker not initialized. Call loadInjectedFaults() first.");
            return;
        }
        
        // Track test case
        String fullTestName = testClassName + "." + testMethodName;
        allTestCases.add(fullTestName);
        
        // Check if this is a known injected fault
        if (!injectedFaults.containsKey(faultName)) {
            logger.debug("Detected fault '{}' is not in the injected faults registry", faultName);
            return;
        }
        
        // Record the detection
        FaultDetection detection = new FaultDetection(
            faultName, 
            testClassName, 
            testMethodName, 
            timestamp, 
            responseBody
        );
        
        detectedFaults.computeIfAbsent(faultName, k -> new ArrayList<>()).add(detection);
        
        logger.info("✅ Detected injected fault: {} in test: {}", faultName, fullTestName);
    }
    
    /**
     * Record a test case execution (for tracking total test count)
     */
    public synchronized void recordTestCase(String testClassName, String testMethodName) {
        String fullTestName = testClassName + "." + testMethodName;
        allTestCases.add(fullTestName);
    }
    
    /**
     * Generate comprehensive fault detection report
     */
    public synchronized void generateReport(String reportDir, String experimentName) {
        if (!initialized) {
            logger.warn("FaultDetectionTracker not initialized. Cannot generate report.");
            return;
        }
        
        try {
            // Create report directory if needed
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
                logger.info("Created fault detection report directory: {}", reportDir);
            }
            
            // Generate report filename with timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String timestamp = dateFormat.format(new Date());
            String reportFileName = String.format("fault-detection-summary-%s-%s.txt", 
                experimentName, timestamp);
            String reportPath = reportDir + "/" + reportFileName;
            
            // Write report
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportPath))) {
                writeReport(writer, experimentName, timestamp);
            }
            
            logger.info("✅ Fault detection report generated: {}", reportPath);
            
        } catch (IOException e) {
            logger.error("Failed to generate fault detection report: {}", e.getMessage(), e);
        }
    }
    
    private void writeReport(PrintWriter writer, String experimentName, String timestamp) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Calculate statistics
        int totalFaults = injectedFaults.size();
        int detectedCount = detectedFaults.size();
        int undetectedCount = totalFaults - detectedCount;
        double detectionRate = totalFaults > 0 ? (detectedCount * 100.0 / totalFaults) : 0.0;
        
        // Header
        writer.println("=".repeat(80));
        writer.println("                    FAULT DETECTION SUMMARY REPORT");
        writer.println("=".repeat(80));
        writer.println();
        writer.println("Experiment:         " + experimentName);
        writer.println("Generated:          " + displayFormat.format(new Date()));
        writer.println("Tracking Started:   " + displayFormat.format(new Date(trackingStartTime)));
        writer.println("Total Test Cases:   " + allTestCases.size());
        writer.println();
        
        // Fault Coverage Summary
        writer.println("=" + "=".repeat(79));
        writer.println("FAULT COVERAGE SUMMARY");
        writer.println("=" + "=".repeat(79));
        writer.println();
        writer.printf("Total Injected Faults:    %d%n", totalFaults);
        writer.printf("Detected Faults:          %d (%.1f%%)%n", detectedCount, detectionRate);
        writer.printf("Undetected Faults:        %d (%.1f%%)%n", undetectedCount, 100.0 - detectionRate);
        writer.println();
        
        // Progress bar visualization
        writer.println("Detection Progress:");
        int barLength = 50;
        int filledLength = (int) (barLength * detectionRate / 100.0);
        String bar = "█".repeat(filledLength) + "░".repeat(barLength - filledLength);
        writer.printf("[%s] %.1f%%%n", bar, detectionRate);
        writer.println();
        
        // Detected Faults Section
        if (!detectedFaults.isEmpty()) {
            writer.println("=" + "=".repeat(79));
            writer.println("DETECTED FAULTS (" + detectedCount + ")");
            writer.println("=" + "=".repeat(79));
            writer.println();
            
            int faultNum = 1;
            List<String> sortedFaultNames = new ArrayList<>(detectedFaults.keySet());
            Collections.sort(sortedFaultNames);
            
            for (String faultName : sortedFaultNames) {
                InjectedFault injectedFault = injectedFaults.get(faultName);
                List<FaultDetection> detections = detectedFaults.get(faultName);
                
                writer.printf("%d. %s%n", faultNum++, faultName);
                writer.printf("   Service:       %s%n", injectedFault.service);
                writer.printf("   API:           %s%n", injectedFault.api);
                writer.printf("   Detections:    %d time(s)%n", detections.size());
                writer.println();
                
                // List all detections with details
                for (int i = 0; i < detections.size(); i++) {
                    FaultDetection detection = detections.get(i);
                    writer.printf("   Detection #%d:%n", i + 1);
                    writer.printf("     Test Class:  %s%n", detection.testClassName);
                    writer.printf("     Test Method: %s%n", detection.testMethodName);
                    writer.printf("     Timestamp:   %s%n", displayFormat.format(new Date(detection.timestamp)));
                    writer.println();
                }
                
                writer.println("-".repeat(80));
                writer.println();
            }
        }
        
        // Undetected Faults Section
        Set<String> undetectedFaultNames = new HashSet<>(injectedFaults.keySet());
        undetectedFaultNames.removeAll(detectedFaults.keySet());
        
        if (!undetectedFaultNames.isEmpty()) {
            writer.println("=" + "=".repeat(79));
            writer.println("UNDETECTED FAULTS (" + undetectedCount + ")");
            writer.println("=" + "=".repeat(79));
            writer.println();
            
            List<String> sortedUndetected = new ArrayList<>(undetectedFaultNames);
            Collections.sort(sortedUndetected);
            
            int faultNum = 1;
            for (String faultName : sortedUndetected) {
                InjectedFault fault = injectedFaults.get(faultName);
                writer.printf("%d. %s%n", faultNum++, faultName);
                writer.printf("   Service:       %s%n", fault.service);
                writer.printf("   API:           %s%n", fault.api);
                writer.println();
            }
        }
        
        // Test Cases Summary
        if (!allTestCases.isEmpty()) {
            writer.println("=" + "=".repeat(79));
            writer.println("TEST CASES EXECUTED (" + allTestCases.size() + ")");
            writer.println("=" + "=".repeat(79));
            writer.println();
            
            List<String> sortedTestCases = new ArrayList<>(allTestCases);
            Collections.sort(sortedTestCases);
            
            for (int i = 0; i < sortedTestCases.size(); i++) {
                writer.printf("%d. %s%n", i + 1, sortedTestCases.get(i));
            }
            writer.println();
        }
        
        // Footer
        writer.println("=" + "=".repeat(79));
        writer.println("                          END OF REPORT");
        writer.println("=" + "=".repeat(79));
    }
    
    /**
     * Reset tracker state (useful for multiple test runs)
     */
    public synchronized void reset() {
        detectedFaults.clear();
        allTestCases.clear();
        trackingStartTime = System.currentTimeMillis();
        logger.info("FaultDetectionTracker reset for new test run");
    }
    
    /**
     * Get detection statistics
     */
    public synchronized Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInjectedFaults", injectedFaults.size());
        stats.put("detectedFaults", detectedFaults.size());
        stats.put("undetectedFaults", injectedFaults.size() - detectedFaults.size());
        stats.put("detectionRate", injectedFaults.size() > 0 
            ? (detectedFaults.size() * 100.0 / injectedFaults.size()) : 0.0);
        stats.put("totalTestCases", allTestCases.size());
        return stats;
    }
    
    // Inner classes for data structures
    
    private static class InjectedFault {
        final String faultName;
        final String service;
        final String api;
        
        InjectedFault(String faultName, String service, String api) {
            this.faultName = faultName;
            this.service = service;
            this.api = api;
        }
    }
    
    private static class FaultDetection {
        final String faultName;
        final String testClassName;
        final String testMethodName;
        final long timestamp;
        final String responseBody;
        
        FaultDetection(String faultName, String testClassName, String testMethodName, 
                      long timestamp, String responseBody) {
            this.faultName = faultName;
            this.testClassName = testClassName;
            this.testMethodName = testMethodName;
            this.timestamp = timestamp;
            this.responseBody = responseBody;
        }
    }
}

