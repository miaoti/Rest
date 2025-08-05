import es.us.isa.restest.llm.LLMConfig;
import es.us.isa.restest.llm.LLMService;
import es.us.isa.restest.generators.ZeroShotLLMGenerator;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify LLM integration works with both local and Gemini models
 */
public class LLMIntegrationTest {
    
    private Map<String, String> localProperties;
    private Map<String, String> geminiProperties;
    
    @BeforeEach
    void setUp() {
        // Setup properties for local model
        localProperties = new HashMap<>();
        localProperties.put("llm.enabled", "true");
        localProperties.put("llm.model.type", "local");
        localProperties.put("llm.local.enabled", "true");
        localProperties.put("llm.local.url", "http://localhost:4891/v1/chat/completions");
        localProperties.put("llm.local.model", "llama-3-8b-instruct");
        
        // Setup properties for Gemini model (only Gemini enabled)
        geminiProperties = new HashMap<>();
        geminiProperties.put("llm.enabled", "true");
        geminiProperties.put("llm.model.type", "gemini");
        geminiProperties.put("llm.local.enabled", "false");
        geminiProperties.put("llm.gemini.enabled", "true");
        geminiProperties.put("llm.gemini.api.key", "AIzaSyANJa0k_Ap8JROFtAh7BbxQo3XrVGHLR-c");
        geminiProperties.put("llm.gemini.model", "gemini-2.0-flash-exp");
        geminiProperties.put("llm.gemini.api.url", "https://generativelanguage.googleapis.com/v1beta/models");
    }
    
    @Test
    void testLLMConfigCreation() {
        // Test local config
        LLMConfig localConfig = LLMConfig.fromProperties(localProperties);
        assertNotNull(localConfig);
        assertTrue(localConfig.isEnabled());
        assertEquals(LLMConfig.ModelType.LOCAL, localConfig.getModelType());
        assertTrue(localConfig.isValid());
        
        // Test Gemini config
        LLMConfig geminiConfig = LLMConfig.fromProperties(geminiProperties);
        assertNotNull(geminiConfig);
        assertTrue(geminiConfig.isEnabled());
        assertEquals(LLMConfig.ModelType.GEMINI, geminiConfig.getModelType());
        assertTrue(geminiConfig.isValid());
    }
    
    @Test
    void testLLMServiceCreation() {
        // Test local service creation
        LLMService localService = LLMService.getInstance(localProperties);
        assertNotNull(localService);
        assertTrue(localService.isReady());
        assertEquals(LLMConfig.ModelType.LOCAL, localService.getConfig().getModelType());
        
        // Test Gemini service creation
        LLMService geminiService = LLMService.getInstance(geminiProperties);
        assertNotNull(geminiService);
        assertTrue(geminiService.isReady());
        assertEquals(LLMConfig.ModelType.GEMINI, geminiService.getConfig().getModelType());
    }
    
    @Test
    void testZeroShotLLMGeneratorWithSystemProperties() {
        // Set system properties for testing
        System.setProperty("llm.enabled", "true");
        System.setProperty("llm.model.type", "gemini");
        System.setProperty("llm.gemini.enabled", "true");
        System.setProperty("llm.gemini.api.key", "AIzaSyANJa0k_Ap8JROFtAh7BbxQo3XrVGHLR-c");
        System.setProperty("llm.gemini.model", "gemini-2.0-flash-exp");
        System.setProperty("llm.gemini.api.url", "https://generativelanguage.googleapis.com/v1beta/models");
        
        try {
            // Create generator (should pick up system properties)
            ZeroShotLLMGenerator generator = new ZeroShotLLMGenerator();
            assertNotNull(generator);
            
            // Create a test parameter
            ParameterInfo testParam = new ParameterInfo();
            testParam.setName("userId");
            testParam.setType("string");
            testParam.setDescription("A unique identifier for a user");
            
            // Test parameter generation (this will make actual API call if Gemini is available)
            List<String> values = generator.generateParameterValues(testParam, 3);
            assertNotNull(values);
            // Note: We can't assert specific values since they depend on API availability
            
        } finally {
            // Clean up system properties
            System.clearProperty("llm.enabled");
            System.clearProperty("llm.model.type");
            System.clearProperty("llm.gemini.enabled");
            System.clearProperty("llm.gemini.api.key");
            System.clearProperty("llm.gemini.model");
            System.clearProperty("llm.gemini.api.url");
        }
    }
    
    @Test
    void testConfigValidation() {
        // Test invalid Gemini config (missing API key)
        Map<String, String> invalidGeminiProps = new HashMap<>(geminiProperties);
        invalidGeminiProps.put("llm.gemini.api.key", "");
        
        LLMConfig invalidConfig = LLMConfig.fromProperties(invalidGeminiProps);
        assertFalse(invalidConfig.isValid());
        
        // Test disabled config (should be valid)
        Map<String, String> disabledProps = new HashMap<>();
        disabledProps.put("llm.enabled", "false");
        
        LLMConfig disabledConfig = LLMConfig.fromProperties(disabledProps);
        assertTrue(disabledConfig.isValid()); // Disabled is valid
        assertFalse(disabledConfig.isEnabled());
    }
    
    @Test
    void testModelTypeSelection() {
        // Test case-insensitive model type selection
        Map<String, String> upperCaseProps = new HashMap<>(geminiProperties);
        upperCaseProps.put("llm.model.type", "GEMINI");

        LLMConfig config = LLMConfig.fromProperties(upperCaseProps);
        assertEquals(LLMConfig.ModelType.GEMINI, config.getModelType());

        // Test default to local for unknown types
        Map<String, String> unknownProps = new HashMap<>(localProperties);
        unknownProps.put("llm.model.type", "unknown");

        LLMConfig defaultConfig = LLMConfig.fromProperties(unknownProps);
        assertEquals(LLMConfig.ModelType.LOCAL, defaultConfig.getModelType());
    }

    @Test
    void testParameterCachingWithDifferentTypes() {
        // Set system properties for testing
        System.setProperty("llm.enabled", "true");
        System.setProperty("llm.model.type", "gemini");
        System.setProperty("llm.gemini.enabled", "true");
        System.setProperty("llm.gemini.api.key", "AIzaSyANJa0k_Ap8JROFtAh7BbxQo3XrVGHLR-c");
        System.setProperty("llm.gemini.model", "gemini-2.0-flash-exp");
        System.setProperty("llm.gemini.api.url", "https://generativelanguage.googleapis.com/v1beta/models");

        try {
            ZeroShotLLMGenerator generator = new ZeroShotLLMGenerator();

            // Create two parameters with same name but different types
            ParameterInfo param1 = new ParameterInfo();
            param1.setName("distance");
            param1.setType("string");
            param1.setInLocation("query");
            param1.setDescription("Distance as a string");

            ParameterInfo param2 = new ParameterInfo();
            param2.setName("distance");
            param2.setType("number");
            param2.setInLocation("query");
            param2.setDescription("Distance as a number");

            // Generate values for first parameter
            List<String> values1 = generator.generateParameterValues(param1, 3);
            assertNotNull(values1);

            // Generate values for second parameter (should be different due to different type)
            List<String> values2 = generator.generateParameterValues(param2, 3);
            assertNotNull(values2);

            // Generate values for first parameter again (should use cache)
            List<String> values1Cached = generator.generateParameterValues(param1, 3);
            assertNotNull(values1Cached);

            // The cached values should be the same as the first call
            assertEquals(values1, values1Cached);

            System.out.println("Parameter caching test completed successfully");
            System.out.println("String distance values: " + values1);
            System.out.println("Number distance values: " + values2);

        } finally {
            // Clean up system properties
            System.clearProperty("llm.enabled");
            System.clearProperty("llm.model.type");
            System.clearProperty("llm.gemini.enabled");
            System.clearProperty("llm.gemini.api.key");
            System.clearProperty("llm.gemini.model");
            System.clearProperty("llm.gemini.api.url");
        }
    }

    @Test
    void testOnlyOneModelEnabled() {
        // Test that only Gemini is enabled when configured
        LLMConfig geminiConfig = LLMConfig.fromProperties(geminiProperties);
        assertTrue(geminiConfig.isGeminiEnabled());
        assertFalse(geminiConfig.isLocalEnabled()); // Should be false based on our properties
        assertEquals(LLMConfig.ModelType.GEMINI, geminiConfig.getModelType());

        // Test that only local is enabled when configured
        Map<String, String> localOnlyProps = new HashMap<>(localProperties);
        localOnlyProps.put("llm.gemini.enabled", "false");

        LLMConfig localConfig = LLMConfig.fromProperties(localOnlyProps);
        assertTrue(localConfig.isLocalEnabled());
        assertFalse(localConfig.isGeminiEnabled());
        assertEquals(LLMConfig.ModelType.LOCAL, localConfig.getModelType());
    }
}
