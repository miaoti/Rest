import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Simple debug test to check OpenAPI file configuration
 */
public class OpenAPIDebugTest {
    
    public static void main(String[] args) {
        System.out.println("=== OpenAPI Configuration Debug Test ===");
        
        try {
            // Load properties from the trainticket-demo.properties file
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/My-Example/trainticket-demo.properties");
            props.load(fis);
            fis.close();
            
            // Check OpenAPI path configuration
            String openApiPath = props.getProperty("smart.input.fetch.openapi.spec.path");
            System.out.println("OpenAPI path from properties: " + openApiPath);
            
            if (openApiPath != null) {
                File openApiFile = new File(openApiPath);
                System.out.println("File exists: " + openApiFile.exists());
                System.out.println("File absolute path: " + openApiFile.getAbsolutePath());
                System.out.println("File size: " + openApiFile.length() + " bytes");
            } else {
                System.out.println("OpenAPI path is null!");
            }
            
            // Also check the OAS path
            String oasPath = props.getProperty("oas.path");
            System.out.println("\nOAS path from properties: " + oasPath);
            
            if (oasPath != null) {
                File oasFile = new File(oasPath);
                System.out.println("OAS file exists: " + oasFile.exists());
                System.out.println("OAS file absolute path: " + oasFile.getAbsolutePath());
                System.out.println("OAS file size: " + oasFile.length() + " bytes");
            }
            
        } catch (Exception e) {
            System.err.println("Error during debug test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
