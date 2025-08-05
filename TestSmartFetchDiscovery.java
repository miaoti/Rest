import es.us.isa.restest.inputs.smart.SmartInputFetcher;
import es.us.isa.restest.inputs.smart.SmartInputFetchConfig;
import es.us.isa.restest.inputs.llm.ParameterInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Test to verify smart fetch discovery system
 */
public class TestSmartFetchDiscovery {
    
    public static void main(String[] args) {
        System.out.println("=== Smart Fetch Discovery Test ===");
        
        try {
            // Create test configuration
            Map<String, String> properties = new HashMap<>();
            properties.put("smart.input.fetch.enabled", "true");
            properties.put("smart.input.fetch.percentage", "1.0"); // 100% for testing
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
            
            // Test parameters that should trigger discovery
            String[] testParams = {
                "distanceList",   // Should match distance pattern
                "endStation",     // Should match station pattern  
                "startStation",   // Should match station pattern
                "stations",       // Should match station pattern
                "id",             // Should match route/id pattern
                "loginId"         // Should match route/id pattern
            };
            
            String[] descriptions = {
                "List of distances for route segments",
                "Destination station name",
                "Origin station name", 
                "List of station names",
                "Route identifier",
                "User login identifier"
            };
            
            for (int i = 0; i < testParams.length; i++) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("Testing parameter: " + testParams[i]);
                System.out.println("Description: " + descriptions[i]);
                System.out.println("=".repeat(50));
                
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(testParams[i]);
                paramInfo.setType("string");
                paramInfo.setDescription(descriptions[i]);
                paramInfo.setInLocation("formData");
                
                try {
                    String result = fetcher.fetchSmartInput(paramInfo);
                    
                    if (result != null && !result.trim().isEmpty()) {
                        System.out.println("✅ SUCCESS: " + result);
                    } else {
                        System.out.println("❌ FAILED: No result returned");
                    }
                    
                } catch (Exception e) {
                    System.out.println("💥 ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
                
                // Small delay to avoid overwhelming the system
                Thread.sleep(1000);
            }
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Test completed. Check registry file for new mappings:");
            System.out.println("src/main/resources/My-Example/trainticket/input-fetch-registry.yaml");
            
        } catch (Exception e) {
            System.err.println("Test setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
