import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple test to verify Smart Input Fetching is working
 */
public class SmartFetchingTest {
    
    public static void main(String[] args) {
        System.out.println("=== Smart Input Fetching Integration Test ===");
        
        try {
            // 1. Load properties
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/My-Example/trainticket-demo.properties");
            props.load(fis);
            fis.close();
            
            // 2. Set system properties (as done in TestGenerationAndExecution)
            String[] smartProperties = {
                "smart.input.fetch.enabled",
                "smart.input.fetch.percentage",
                "smart.input.fetch.registry.path",
                "smart.input.fetch.llm.discovery.enabled",
                "smart.input.fetch.max.candidates",
                "smart.input.fetch.dependency.resolution.enabled",
                "smart.input.fetch.discovery.timeout.ms",
                "smart.input.fetch.cache.enabled",
                "smart.input.fetch.cache.ttl.seconds",
                "base.url"
            };
            
            for (String property : smartProperties) {
                String value = props.getProperty(property);
                if (value != null) {
                    System.setProperty(property, value);
                }
            }
            
            System.out.println("✅ System properties configured");
            
            // 3. Test if SmartLLMParameterGenerator can be loaded
            try {
                Class<?> smartGenClass = Class.forName("es.us.isa.restest.inputs.smart.SmartLLMParameterGenerator");
                System.out.println("✅ SmartLLMParameterGenerator class found");
                
                // Try to create an instance
                Object smartGen = smartGenClass.getDeclaredConstructor().newInstance();
                System.out.println("✅ SmartLLMParameterGenerator instance created");
                
                // Check if smart fetching is enabled
                java.lang.reflect.Method isEnabledMethod = smartGenClass.getMethod("isSmartFetchingEnabled");
                boolean isEnabled = (Boolean) isEnabledMethod.invoke(smartGen);
                System.out.println("Smart fetching enabled: " + isEnabled);
                
                if (isEnabled) {
                    System.out.println("🎯 SUCCESS: Smart Input Fetching is properly configured and enabled!");
                } else {
                    System.out.println("❌ Smart Input Fetching is not enabled");
                }
                
            } catch (ClassNotFoundException e) {
                System.err.println("❌ SmartLLMParameterGenerator class not found: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ Error creating SmartLLMParameterGenerator: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 4. Test SmartInputFetchConfig directly
            System.out.println("\n=== Testing SmartInputFetchConfig ===");
            try {
                Class<?> configClass = Class.forName("es.us.isa.restest.inputs.smart.SmartInputFetchConfig");
                
                // Create properties map
                Map<String, String> configProps = new HashMap<>();
                for (String property : smartProperties) {
                    String value = props.getProperty(property);
                    if (value != null) {
                        configProps.put(property, value);
                    }
                }
                
                // Call fromProperties method
                java.lang.reflect.Method fromPropsMethod = configClass.getMethod("fromProperties", Map.class);
                Object config = fromPropsMethod.invoke(null, configProps);
                
                // Check if enabled
                java.lang.reflect.Method isEnabledMethod = configClass.getMethod("isEnabled");
                boolean isEnabled = (Boolean) isEnabledMethod.invoke(config);
                
                // Get percentage
                java.lang.reflect.Method getPercentageMethod = configClass.getMethod("getSmartFetchPercentage");
                double percentage = (Double) getPercentageMethod.invoke(config);
                
                System.out.println("✅ SmartInputFetchConfig created successfully");
                System.out.println("   - Enabled: " + isEnabled);
                System.out.println("   - Percentage: " + (percentage * 100) + "%");
                
            } catch (Exception e) {
                System.err.println("❌ Error testing SmartInputFetchConfig: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (IOException e) {
            System.err.println("❌ Failed to load properties: " + e.getMessage());
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}