import es.us.isa.restest.util.LLMCommunicationLogger;
import es.us.isa.restest.util.SystemResourceMonitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Debug test to verify resource monitoring is working
 */
public class TestResourceMonitoringDebug {
    
    public static void main(String[] args) {
        System.out.println("=== Resource Monitoring Debug Test ===\n");
        
        try {
            // Load your actual properties file
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/My-Example/trainticket-demo.properties"));
            
            System.out.println("📋 **Loaded Properties:**");
            
            // Check resource monitoring properties specifically
            String[] monitoringProps = {
                "llm.resource.monitoring.enabled",
                "llm.resource.monitoring.interval.ms", 
                "llm.resource.monitoring.include.cpu",
                "llm.resource.monitoring.include.detailed.memory"
            };
            
            for (String prop : monitoringProps) {
                String value = properties.getProperty(prop);
                System.out.println("  " + prop + " = " + value);
            }
            
            // Check communication logging properties
            String[] loggingProps = {
                "llm.communication.logging.enabled",
                "llm.communication.logging.include.metadata",
                "llm.communication.logging.include.content"
            };
            
            System.out.println("\n📋 **Communication Logging Properties:**");
            for (String prop : loggingProps) {
                String value = properties.getProperty(prop);
                System.out.println("  " + prop + " = " + value);
            }
            
            // Check model configuration
            System.out.println("\n🔧 **Model Configuration:**");
            System.out.println("  llm.model.type = " + properties.getProperty("llm.model.type"));
            System.out.println("  llm.ollama.enabled = " + properties.getProperty("llm.ollama.enabled"));
            System.out.println("  llm.local.enabled = " + properties.getProperty("llm.local.enabled"));
            System.out.println("  llm.gemini.enabled = " + properties.getProperty("llm.gemini.enabled"));
            
            // Test SystemResourceMonitor initialization
            System.out.println("\n🔍 **Testing SystemResourceMonitor:**");
            try {
                SystemResourceMonitor monitor = SystemResourceMonitor.getInstance(properties);
                System.out.println("  ✅ SystemResourceMonitor created successfully");
                System.out.println("  ✅ Enabled: " + monitor.isEnabled());
                
                // Test shouldMonitor for different model types
                String[] modelTypes = {"OLLAMA", "LOCAL", "GEMINI"};
                for (String modelType : modelTypes) {
                    boolean shouldMonitor = monitor.shouldMonitor(modelType);
                    System.out.println("  📊 shouldMonitor(" + modelType + ") = " + shouldMonitor);
                }
                
            } catch (Exception e) {
                System.out.println("  ❌ Failed to create SystemResourceMonitor: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test LLMCommunicationLogger initialization
            System.out.println("\n📝 **Testing LLMCommunicationLogger:**");
            try {
                // Filter properties like LLMService does
                Properties loggerProps = new Properties();
                for (String key : properties.stringPropertyNames()) {
                    if (key.startsWith("llm.communication.logging.") || key.startsWith("llm.resource.monitoring.")) {
                        loggerProps.setProperty(key, properties.getProperty(key));
                    }
                }
                
                System.out.println("  📋 Properties passed to logger:");
                for (String key : loggerProps.stringPropertyNames()) {
                    System.out.println("    " + key + " = " + loggerProps.getProperty(key));
                }
                
                LLMCommunicationLogger logger = LLMCommunicationLogger.getInstance(loggerProps);
                System.out.println("  ✅ LLMCommunicationLogger created successfully");
                System.out.println("  ✅ Enabled: " + logger.isEnabled());
                
                // Test a mock request
                System.out.println("\n🧪 **Testing Mock LLM Request:**");
                String modelType = properties.getProperty("llm.model.type", "unknown").toUpperCase();
                System.out.println("  Using model type: " + modelType);
                
                LLMCommunicationLogger.LLMRequestContext context = logger.logRequest(
                    modelType,
                    "test-model",
                    "Test system prompt",
                    "Test user prompt",
                    "http://test:1234",
                    "test metadata"
                );
                
                System.out.println("  ✅ Request logged");
                
                // Simulate some processing time
                Thread.sleep(2000);
                
                // Log response
                logger.logResponse(context, "Test response", true, null);
                System.out.println("  ✅ Response logged");
                
                logger.close();
                
            } catch (Exception e) {
                System.out.println("  ❌ Failed to test LLMCommunicationLogger: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("\n✅ **Debug test completed!**");
            System.out.println("📁 Check logs/llm-communications/ for the test log file");
            
        } catch (IOException e) {
            System.err.println("❌ Failed to load properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
