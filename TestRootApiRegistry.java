import es.us.isa.restest.registry.RootApiRegistry;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.util.TraceWorkflowExtractor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Standalone test program to test RootApiRegistry functionality.
 * Reads traces from the configured directory and generates root API registry.
 */
public class TestRootApiRegistry {
    
    public static void main(String[] args) {
        try {
            // Read properties file
            String propertiesFile = "src/main/resources/My-Example/trainticket-demo.properties";
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                props.load(fis);
            }
            
            // Get configuration from properties
            String tracesDir = props.getProperty("trace.file.path");
            String registryPath = props.getProperty("root.api.registry.path");
            
            System.out.println("=== Root API Registry Test ===");
            System.out.println("Properties file: " + propertiesFile);
            System.out.println("Traces directory: " + tracesDir);
            System.out.println("Registry output path: " + registryPath);
            System.out.println();
            
            if (registryPath == null || registryPath.isEmpty()) {
                System.err.println("ERROR: root.api.registry.path not configured in properties file");
                System.exit(1);
            }
            
            // Extract scenarios from all traces in the directory
            System.out.println("Extracting workflows from traces...");
            List<WorkflowScenario> scenarios = TraceWorkflowExtractor.extractScenarios(tracesDir);
            System.out.println("Found " + scenarios.size() + " scenarios");
            System.out.println();
            
            // Create registry and register root APIs
            System.out.println("Initializing Root API Registry...");
            RootApiRegistry registry = new RootApiRegistry(registryPath);
            
            System.out.println("Registering root APIs from scenarios...");
            int registeredApis = registry.registerRootApisFromScenarios(scenarios);
            
            // Get statistics
            RootApiRegistry.RegistryStats stats = registry.getStats();
            System.out.println();
            System.out.println("=== Registration Complete ===");
            System.out.println("Total root APIs: " + stats.getTotalRootApis());
            System.out.println("Total trees: " + stats.getTotalTrees());
            System.out.println();
            
            // List all registered root APIs
            System.out.println("Registered Root APIs:");
            for (String apiKey : registry.getAllRootApiKeys()) {
                System.out.println("  - " + apiKey);
            }
            System.out.println();
            
            // Save registry to file
            System.out.println("Saving registry to: " + registryPath);
            registry.saveRegistry();
            
            System.out.println();
            System.out.println("✓ Test completed successfully!");
            System.out.println("Check the output file: " + registryPath);
            
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read properties file");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("ERROR: Test failed");
            e.printStackTrace();
            System.exit(1);
        }
    }
}

