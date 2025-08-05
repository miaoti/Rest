import es.us.isa.restest.inputs.smart.SmartInputFetcher;
import es.us.isa.restest.inputs.smart.SmartInputFetchConfig;
import es.us.isa.restest.inputs.llm.ParameterInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple test to verify smart fetch behavior
 */
public class SmartFetchTest {
    
    public static void main(String[] args) {
        System.out.println("=== Smart Fetch Test ===");
        
        try {
            // Create test configuration
            Map<String, String> properties = new HashMap<>();
            properties.put("smart.input.fetch.enabled", "true");
            properties.put("smart.input.fetch.percentage", "0.9");
            properties.put("smart.input.fetch.registry.path", "src/main/resources/My-Example/trainticket/input-fetch-registry.yaml");
            properties.put("smart.input.fetch.openapi.spec.path", "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml");
            properties.put("smart.input.fetch.llm.discovery.enabled", "true");
            properties.put("smart.input.fetch.max.candidates", "5");
            properties.put("smart.input.fetch.dependency.resolution.enabled", "true");
            properties.put("smart.input.fetch.discovery.timeout.ms", "5000");
            properties.put("smart.input.fetch.cache.enabled", "true");
            properties.put("smart.input.fetch.cache.ttl.seconds", "300");
            
            SmartInputFetchConfig config = SmartInputFetchConfig.fromProperties(properties);
            SmartInputFetcher fetcher = new SmartInputFetcher(config, "http://129.62.148.112:32677");
            
            // Test parameter info for body parameter
            ParameterInfo paramInfo = new ParameterInfo();
            paramInfo.setName("body");
            paramInfo.setType("array");
            paramInfo.setDescription("Station name list");
            paramInfo.setInLocation("body");
            
            System.out.println("Testing smart fetch with 90% percentage...");
            
            // Test multiple times to see the percentage behavior
            int smartFetchCount = 0;
            int llmFallbackCount = 0;
            int totalTests = 20;
            
            for (int i = 0; i < totalTests; i++) {
                String result = fetcher.fetchSmartInput(paramInfo);
                System.out.println("Test " + (i+1) + ": " + result);
                
                if (result != null && result.contains("id=") && result.contains("name=")) {
                    smartFetchCount++;
                } else {
                    llmFallbackCount++;
                }
            }
            
            System.out.println("\n=== Results ===");
            System.out.println("Smart fetch successes: " + smartFetchCount + "/" + totalTests + " (" + (smartFetchCount * 100.0 / totalTests) + "%)");
            System.out.println("LLM fallbacks: " + llmFallbackCount + "/" + totalTests + " (" + (llmFallbackCount * 100.0 / totalTests) + "%)");
            System.out.println("Expected smart fetch rate: 90%");
            
            if (smartFetchCount >= totalTests * 0.8) { // Allow some variance
                System.out.println("✅ Smart fetch is working correctly!");
            } else {
                System.out.println("❌ Smart fetch rate is too low!");
            }
            
        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
