import es.us.isa.restest.inputs.smart.SmartInputFetcher;
import es.us.isa.restest.inputs.smart.SmartInputFetchConfig;
import es.us.isa.restest.inputs.llm.ParameterInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Test the improved smart fetch system with better prompts and message length handling
 */
public class TestImprovedSmartFetch {
    
    public static void main(String[] args) {
        System.out.println("=== Improved Smart Fetch Test ===");
        
        try {
            // Create test configuration
            Map<String, String> properties = new HashMap<>();
            properties.put("smart.input.fetch.enabled", "true");
            properties.put("smart.input.fetch.percentage", "1.0"); // 100% for testing
            properties.put("smart.input.fetch.registry.path", "src/main/resources/My-Example/trainticket/input-fetch-registry.yaml");
            properties.put("smart.input.fetch.openapi.spec.path", "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml");
            properties.put("smart.input.fetch.llm.discovery.enabled", "true");
            properties.put("smart.input.fetch.max.candidates", "3");
            properties.put("smart.input.fetch.dependency.resolution.enabled", "true");
            properties.put("smart.input.fetch.discovery.timeout.ms", "10000");
            properties.put("smart.input.fetch.cache.enabled", "false"); // Disable cache for testing
            
            SmartInputFetchConfig config = SmartInputFetchConfig.fromProperties(properties);
            SmartInputFetcher fetcher = new SmartInputFetcher(config, "http://129.62.148.112:32677");
            
            // Test parameters that should trigger discovery with improved prompts
            String[] testParams = {
                "distanceList",   // Should match distance pattern and get route services
                "endStation",     // Should match station pattern and get station service
                "loginId",        // Should match id pattern and get user/auth services
                "priceInfo"       // Should trigger LLM discovery for price services
            };
            
            String[] descriptions = {
                "List of distances between stations",
                "Destination station name",
                "User login identifier", 
                "Price information for booking"
            };
            
            for (int i = 0; i < testParams.length; i++) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("🧪 Testing parameter: " + testParams[i]);
                System.out.println("📝 Description: " + descriptions[i]);
                System.out.println("=".repeat(60));
                
                ParameterInfo paramInfo = new ParameterInfo();
                paramInfo.setName(testParams[i]);
                paramInfo.setType("string");
                paramInfo.setDescription(descriptions[i]);
                paramInfo.setInLocation("formData");
                
                try {
                    long startTime = System.currentTimeMillis();
                    String result = fetcher.fetchSmartInput(paramInfo);
                    long endTime = System.currentTimeMillis();
                    
                    if (result != null && !result.trim().isEmpty()) {
                        System.out.println("✅ SUCCESS: " + result);
                        System.out.println("⏱️  Time: " + (endTime - startTime) + "ms");
                        
                        // Check if result looks realistic
                        if (result.contains("id=") || result.contains("name=") || 
                            result.matches("\\d+") || result.matches("[a-zA-Z]+")) {
                            System.out.println("🎯 Result looks realistic!");
                        } else {
                            System.out.println("⚠️  Result might be fallback value");
                        }
                    } else {
                        System.out.println("❌ FAILED: No result returned");
                    }
                    
                } catch (Exception e) {
                    System.out.println("💥 ERROR: " + e.getMessage());
                    e.printStackTrace();
                }
                
                // Small delay to avoid overwhelming the system
                Thread.sleep(2000);
            }
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🏁 Test completed!");
            System.out.println("📋 Check registry file for new mappings:");
            System.out.println("   src/main/resources/My-Example/trainticket/input-fetch-registry.yaml");
            System.out.println("📊 Expected improvements:");
            System.out.println("   - Cleaner LLM responses (no extra explanations)");
            System.out.println("   - Proper priority ordering for discovered services");
            System.out.println("   - Better handling of large API responses");
            System.out.println("   - More accurate service selection");
            
        } catch (Exception e) {
            System.err.println("Test setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
