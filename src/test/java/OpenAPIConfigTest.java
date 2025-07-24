package es.us.isa.restest.test;

import es.us.isa.restest.inputs.smart.SmartInputFetchConfig;
import es.us.isa.restest.inputs.smart.OpenAPIEndpointDiscovery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Test to verify OpenAPI configuration and service loading
 */
public class OpenAPIConfigTest {
    
    @Test
    @DisplayName("Test OpenAPI Configuration Loading")
    void testOpenAPIConfigurationLoading() {
        // Create test properties that simulate system properties
        Map<String, String> testProperties = new HashMap<>();
        testProperties.put("smart.input.fetch.enabled", "true");
        testProperties.put("smart.input.fetch.openapi.spec.path", "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml");
        testProperties.put("oas.path", "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml");
        
        // Create configuration from properties
        SmartInputFetchConfig config = SmartInputFetchConfig.fromProperties(testProperties);
        
        // Verify configuration
        assertTrue(config.isEnabled(), "Smart input fetching should be enabled");
        assertEquals("src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml", 
                    config.getOpenApiSpecPath(), "OpenAPI spec path should be set correctly");
        
        System.out.println("✅ Configuration loaded successfully:");
        System.out.println("   - Enabled: " + config.isEnabled());
        System.out.println("   - OpenAPI Spec Path: " + config.getOpenApiSpecPath());
    }
    
    @Test
    @DisplayName("Test OpenAPI Service Discovery")
    void testOpenAPIServiceDiscovery() {
        try {
            // Load OpenAPI specification and discover services
            String openApiPath = "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml";
            Map<String, Set<String>> serviceEndpoints = OpenAPIEndpointDiscovery.loadFromFile(openApiPath);
            
            // Verify services were loaded
            assertNotNull(serviceEndpoints, "Service endpoints should not be null");
            assertFalse(serviceEndpoints.isEmpty(), "Should have discovered services");
            
            System.out.println("✅ OpenAPI services discovered:");
            for (String service : serviceEndpoints.keySet()) {
                System.out.println("   - Service: " + service + " (" + serviceEndpoints.get(service).size() + " endpoints)");
            }
            
            // Check for expected services
            assertTrue(serviceEndpoints.containsKey("ts-station-service") || 
                      serviceEndpoints.keySet().stream().anyMatch(s -> s.contains("station")),
                      "Should contain station-related service");
                      
        } catch (Exception e) {
            fail("Failed to load OpenAPI specification: " + e.getMessage());
        }
    }
}