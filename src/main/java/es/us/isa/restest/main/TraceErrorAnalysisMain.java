package es.us.isa.restest.main;

import org.json.JSONObject;
import es.us.isa.restest.analysis.TraceErrorAnalyzer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Main class to test the TraceErrorAnalyzer with the admin_add_route_failed.json sample trace.
 * 
 * This demonstrates how the error analysis system identifies root causes of API failures
 * in microservice traces and generates comprehensive error reports.
 */
public class TraceErrorAnalysisMain {
    
    public static void main(String[] args) {
        System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
        System.out.println("                        🔍 TRACE ERROR ANALYSIS DEMONSTRATION                        ");
        System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
        System.out.println();
        
        // Load LLM configuration properties manually for testing
        loadPropertiesForTesting();
        
        try {
            // Path to the sample failed trace
            String tracePath = "src/main/resources/My-Example/trainticket/tests/admin_add_route_failed.json";
            
            System.out.println("📁 Loading trace file: " + tracePath);
            
            // Read the sample failed trace
            String jsonContent = new String(Files.readAllBytes(Paths.get(tracePath)));
            JSONObject traceWrapper = new JSONObject(jsonContent);
            
            // Extract the first trace from the data array
            if (!traceWrapper.has("data") || traceWrapper.getJSONArray("data").length() == 0) {
                System.err.println("❌ No trace data found in the JSON file");
                return;
            }
            
            JSONObject trace = traceWrapper.getJSONArray("data").getJSONObject(0);
            System.out.println("✅ Trace loaded successfully");
            System.out.println("   Trace ID: " + trace.optString("traceID", "Unknown"));
            System.out.println("   Total Spans: " + trace.getJSONArray("spans").length());
            System.out.println();
            
            // Perform error analysis
            System.out.println("🔬 Performing error analysis...");
            TraceErrorAnalyzer.ErrorAnalysisResult result = TraceErrorAnalyzer.analyzeTrace(trace);
            System.out.println("✅ Analysis complete");
            System.out.println();
            
            // Print quick summary
            System.out.println("═══════════════════════════════════════════════════════════════════════════════");
            System.out.println("📊 QUICK ANALYSIS SUMMARY");
            System.out.println("═══════════════════════════════════════════════════════════════════════════════");
            System.out.println("• Has Errors: " + (result.hasErrors() ? "❌ YES" : "✅ NO"));
            System.out.println("• Total Failed Spans: " + result.getAllFailedSpans().size());
            System.out.println("• Root Cause Failures: " + result.getRootCauseFailures().size());
            System.out.println("• Propagated Failures: " + (result.getAllFailedSpans().size() - result.getRootCauseFailures().size()));
            System.out.println();
            
            if (result.hasErrors()) {
                System.out.println("🔍 ROOT CAUSE SUMMARY:");
                for (int i = 0; i < result.getRootCauseFailures().size(); i++) {
                    TraceErrorAnalyzer.FailedSpan failure = result.getRootCauseFailures().get(i);
                    System.out.println("  " + (i + 1) + ". " + failure.getServiceName() + " → " + failure.getOperationName());
                    if (failure.getHttpStatusCode() > 0) {
                        System.out.println("     HTTP Status: " + failure.getHttpStatusCode());
                    }
                    if (!failure.getErrorMessages().isEmpty()) {
                        System.out.println("     Error: " + failure.getErrorMessages().get(0));
                    }
                }
                System.out.println();
            }
            
            // Generate and display the detailed error report
            System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
            System.out.println("                              📋 DETAILED ERROR REPORT                              ");
            System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
            System.out.println();
            
            String errorReport = TraceErrorAnalyzer.generateErrorReport(result);
            System.out.println(errorReport);
            
            // Show how this would appear in Allure report
            System.out.println("\n\n");
            System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
            System.out.println("                           🎯 ALLURE REPORT INTEGRATION                           ");
            System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
            System.out.println();
            System.out.println("This error analysis would be automatically attached to your Allure test reports as:");
            System.out.println();
            if (result.hasErrors()) {
                System.out.println("📎 Attachment 1: \"💥 ERROR ANALYSIS\"");
                System.out.println("   ↳ Contains the detailed error report shown above");
                System.out.println();
                System.out.println("📎 Attachment 2: \"🔗 API Call Trace (FAILED)\"");
                System.out.println("   ↳ Visual hierarchy with ❌ indicators for failed APIs");
                System.out.println();
            } else {
                System.out.println("📎 Attachment 1: \"🔗 API Call Trace (SUCCESS)\"");
                System.out.println("   ↳ Visual hierarchy with ✅ indicators for successful APIs");
                System.out.println();
            }
            System.out.println("📎 Additional attachments: Trace Summary, Raw Data, Query Debug Info");
            System.out.println();
            
            // Get intelligent analysis from TraceErrorAnalyzer
            if (result.hasErrors()) {
                System.out.println("🤖 GETTING INTELLIGENT ANALYSIS...");
                System.out.println("──────────────────────────────────────────────────────────────────────────────────────");
                
                String intelligentAnalysis = TraceErrorAnalyzer.generateIntelligentAnalysis(result, trace);
                if (intelligentAnalysis != null && !intelligentAnalysis.trim().isEmpty()) {
                    System.out.println("🧠 INTELLIGENT INSIGHTS:");
                    System.out.println(intelligentAnalysis);
                } else {
                    System.out.println("⚠️ Intelligent analysis not available (check LLM configuration)");
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error during trace analysis:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n████████████████████████████████████████████████████████████████████████████████████████");
        System.out.println("                               ✅ ANALYSIS COMPLETE                               ");
        System.out.println("████████████████████████████████████████████████████████████████████████████████████████");
    }
    
    /**
     * Load LLM configuration properties manually for testing purposes.
     * In production, these properties are automatically passed by TestGenerationAndExecution.java
     */
    private static void loadPropertiesForTesting() {
        try {
            System.out.println("🔧 Loading LLM configuration from trainticket-demo.properties...");
            
            // Path to the properties file (same as used in TestGenerationAndExecution)
            String propertiesPath = "src/main/resources/My-Example/trainticket-demo.properties";
            
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(propertiesPath)) {
                properties.load(fis);
            }
            
            // Set LLM-related system properties to match what TestGenerationAndExecution does
            String[] llmProperties = {
                "llm.enabled",
                "llm.model.type",
                "llm.local.enabled",
                "llm.local.url", 
                "llm.local.model",
                "llm.gemini.enabled",
                "llm.gemini.api.key",
                "llm.gemini.model",
                "llm.gemini.api.url",
                "llm.ollama.enabled",
                "llm.ollama.url",
                "llm.ollama.model",
                "llm.rate.limit.retry.enabled",
                "llm.rate.limit.max.retries"
            };
            
            int loadedCount = 0;
            for (String property : llmProperties) {
                String value = properties.getProperty(property);
                if (value != null) {
                    System.setProperty(property, value);
                    loadedCount++;
                    System.out.println("   ✅ " + property + " = " + value);
                } else {
                    System.out.println("   ⚠️  " + property + " not found in properties file");
                }
            }
            
            System.out.println("📊 Loaded " + loadedCount + " LLM configuration properties");
            
            // Log the current LLM configuration
            String modelType = System.getProperty("llm.model.type", "unknown");
            String enabled = System.getProperty("llm.enabled", "false");
            System.out.println("🤖 LLM Configuration:");
            System.out.println("   - Enabled: " + enabled);
            System.out.println("   - Model Type: " + modelType);
            
            if ("ollama".equals(modelType)) {
                String ollamaUrl = System.getProperty("llm.ollama.url", "not set");
                String ollamaModel = System.getProperty("llm.ollama.model", "not set");
                System.out.println("   - Ollama URL: " + ollamaUrl);
                System.out.println("   - Ollama Model: " + ollamaModel);
            } else if ("gemini".equals(modelType)) {
                String geminiKey = System.getProperty("llm.gemini.api.key", "not set");
                String geminiModel = System.getProperty("llm.gemini.model", "not set");
                System.out.println("   - Gemini API Key: " + (geminiKey.equals("not set") ? "not set" : "configured"));
                System.out.println("   - Gemini Model: " + geminiModel);
            }
            
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not load properties file for LLM configuration");
            System.err.println("   Error: " + e.getMessage());
            System.err.println("   Intelligent analysis may not be available");
            System.out.println();
        }
    }
}
