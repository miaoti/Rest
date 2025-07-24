import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Standalone debug test for Smart Input Fetching configuration
 */
public class SmartFetchDebug {
    
    public static void main(String[] args) {
        System.out.println("=== Smart Input Fetching Debug Test ===");
        
        // 1. Load properties from the trainticket-demo.properties file
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/My-Example/trainticket-demo.properties");
            props.load(fis);
            fis.close();
            System.out.println("✅ Successfully loaded properties file");
        } catch (IOException e) {
            System.err.println("❌ Failed to load properties file: " + e.getMessage());
            return;
        }
        
        // 2. Check smart input fetching properties
        System.out.println("\n=== Smart Input Fetching Properties ===");
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
        
        Map<String, String> smartConfig = new HashMap<>();
        for (String property : smartProperties) {
            String value = props.getProperty(property);
            if (value != null) {
                smartConfig.put(property, value);
                System.out.println("✅ " + property + " = " + value);
            } else {
                System.out.println("❌ " + property + " = NOT FOUND");
            }
        }
        
        // 3. Test percentage logic simulation
        System.out.println("\n=== Percentage Logic Test ===");
        String percentageStr = smartConfig.get("smart.input.fetch.percentage");
        if (percentageStr != null) {
            try {
                double percentage = Double.parseDouble(percentageStr);
                System.out.println("Configured percentage: " + percentage);
                
                int smartFetchCount = 0;
                int totalTests = 1000;
                
                for (int i = 0; i < totalTests; i++) {
                    double randomValue = Math.random();
                    if (randomValue < percentage) {
                        smartFetchCount++;
                    }
                }
                
                double actualPercentage = (double) smartFetchCount / totalTests;
                System.out.println("Expected smart fetch rate: " + (percentage * 100) + "%");
                System.out.println("Actual smart fetch rate: " + (actualPercentage * 100) + "%");
                System.out.println("Smart fetch attempts: " + smartFetchCount + "/" + totalTests);
                
                if (Math.abs(actualPercentage - percentage) < 0.05) {
                    System.out.println("✅ Percentage logic working correctly");
                } else {
                    System.out.println("❌ Percentage logic may have issues");
                }
                
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid percentage format: " + percentageStr);
            }
        } else {
            System.err.println("❌ smart.input.fetch.percentage not found");
        }
        
        // 4. Test system properties (as used in MST mode)
        System.out.println("\n=== System Properties Test ===");
        for (String property : smartProperties) {
            String value = smartConfig.get(property);
            if (value != null) {
                System.setProperty(property, value);
                System.out.println("✅ Set system property: " + property + " = " + value);
            }
        }
        
        // Verify system properties
        boolean systemEnabled = Boolean.parseBoolean(System.getProperty("smart.input.fetch.enabled", "false"));
        System.out.println("\nSystem property smart.input.fetch.enabled: " + systemEnabled);
        
        if (systemEnabled) {
            System.out.println("✅ Smart Input Fetching should be enabled in MST mode");
        } else {
            System.out.println("❌ Smart Input Fetching will be disabled in MST mode");
        }
        
        // 5. Check if registry file exists
        String registryPath = smartConfig.get("smart.input.fetch.registry.path");
        if (registryPath != null) {
            java.io.File registryFile = new java.io.File(registryPath);
            System.out.println("\n=== Registry File Check ===");
            System.out.println("Registry path: " + registryPath);
            System.out.println("Registry file exists: " + registryFile.exists());
            if (!registryFile.exists()) {
                System.out.println("ℹ️  Registry file will be created on first use");
            }
        }
        
        System.out.println("\n=== Debug Test Complete ===");
    }
}