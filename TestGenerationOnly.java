import java.io.File;
import java.util.Properties;

public class TestGenerationOnly {
    public static void main(String[] args) {
        System.out.println("Testing test generation with improved naming...");
        
        try {
            // Set system properties to match the configuration
            System.setProperty("test.variants.per.scenario", "10");
            System.setProperty("testsperoperation", "10");
            System.setProperty("experiment.execute", "false");
            
            // Try to run the test generation
            String[] mainArgs = {"src/main/resources/My-Example/trainticket-demo.properties"};
            
            // Use reflection to avoid class loading issues
            Class<?> mainClass = Class.forName("es.us.isa.restest.main.TestGenerationAndExecution");
            java.lang.reflect.Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) mainArgs);
            
        } catch (Exception e) {
            System.out.println("Error running test generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 