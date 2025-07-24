package es.us.isa.restest.inputs.smart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import es.us.isa.restest.inputs.llm.LLMParameterGenerator;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.specification.OpenAPIParameter;
import es.us.isa.restest.specification.OpenAPISpecificationVisitor;
import io.swagger.v3.oas.models.Operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Enhanced LLM Parameter Generator that integrates Smart Input Fetching
 * Intelligently chooses between smart fetching and traditional LLM generation
 */
public class SmartLLMParameterGenerator extends LLMParameterGenerator {
    
    private static final Logger logger = LogManager.getLogger(SmartLLMParameterGenerator.class);
    
    private SmartInputFetcher smartFetcher;
    private SmartInputFetchConfig config;
    private boolean initialized = false;
    private Random random = new Random();
    
    public SmartLLMParameterGenerator() {
        super();
        initializeSmartFetching();
    }
    
    /**
     * Initialize the Smart Input Fetching system from properties
     */
    private void initializeSmartFetching() {
        try {
            // Read configuration from system properties
            Map<String, String> properties = loadPropertiesFromSystem();
            
            config = SmartInputFetchConfig.fromProperties(properties);
            
            if (config.isEnabled()) {
                // Get base URL from properties (defaulting to localhost)
                String baseUrl = properties.getOrDefault("base.url", "http://localhost:8080");
                
                smartFetcher = new SmartInputFetcher(config, baseUrl);
                initialized = true;
                
                logger.info("SmartLLMParameterGenerator initialized with smart fetching enabled");
            } else {
                logger.info("Smart input fetching is disabled, using traditional LLM generation");
            }
            
        } catch (Exception e) {
            logger.warn("Failed to initialize Smart Input Fetching, falling back to traditional LLM: {}", 
                       e.getMessage());
            initialized = false;
        }
    }
    
    @Override
    public JsonNode nextValue() {
        // If smart fetching is not available or disabled, use parent's LLM generation
        if (!initialized || !config.isEnabled()) {
            return super.nextValue();
        }
        
        try {
            // Build parameter info from available data
            ParameterInfo parameterInfo = createParameterInfo();
            
            // Decide whether to use smart fetching based on configured percentage
            boolean useSmartFetching = random.nextDouble() < config.getSmartFetchPercentage();
            
            if (useSmartFetching) {
                // Try smart fetching first
                String smartValue = smartFetcher.fetchSmartInput(parameterInfo);
                
                if (smartValue != null && !smartValue.trim().isEmpty()) {
                    logger.debug("Smart fetching provided value '{}' for parameter '{}'", 
                               smartValue, parameterInfo.getName());
                    return TextNode.valueOf(smartValue);
                }
            }
            
        } catch (Exception e) {
            logger.debug("Smart fetching failed for parameter '{}', falling back to LLM: {}", 
                        getParameterName(), e.getMessage());
        }
        
        // Fall back to traditional LLM generation
        return super.nextValue();
    }
    
    @Override
    public String nextValueAsString() {
        JsonNode node = nextValue();
        return (node != null) ? node.asText() : "";
    }
    
    /**
     * Load properties from system properties and environment variables
     */
    private Map<String, String> loadPropertiesFromSystem() {
        Map<String, String> properties = new HashMap<>();
        
        // Load from system properties
        System.getProperties().entrySet().stream()
                .filter(entry -> entry.getKey().toString().startsWith("smart.input.fetch"))
                .forEach(entry -> properties.put(entry.getKey().toString(), entry.getValue().toString()));
        
        // Also check for base.url which we need
        if (System.getProperty("base.url") != null) {
            properties.put("base.url", System.getProperty("base.url"));
        }
        
        // Load from environment variables (converting format)
        System.getenv().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("SMART_INPUT_FETCH"))
                .forEach(entry -> {
                    String key = entry.getKey().toLowerCase().replace("_", ".");
                    properties.put(key, entry.getValue());
                });
        
        return properties;
    }
    
    /**
     * Get the smart fetching configuration (for testing/debugging)
     */
    public SmartInputFetchConfig getConfig() {
        return config;
    }
    
    /**
     * Check if smart fetching is initialized and enabled
     */
    public boolean isSmartFetchingEnabled() {
        return initialized && config != null && config.isEnabled();
    }
    
    /**
     * Get the smart fetcher instance (for testing/debugging)
     */
    public SmartInputFetcher getSmartFetcher() {
        return smartFetcher;
    }
    
    /**
     * Manually set the smart fetcher (useful for testing)
     */
    public void setSmartFetcher(SmartInputFetcher smartFetcher) {
        this.smartFetcher = smartFetcher;
        this.initialized = (smartFetcher != null);
    }
    
    /**
     * Manually set the configuration (useful for testing)
     */
    public void setConfig(SmartInputFetchConfig config) {
        this.config = config;
        if (config != null && config.isEnabled() && smartFetcher == null) {
            // Re-initialize with new config
            initializeSmartFetching();
        }
    }
    
    /**
     * Create ParameterInfo object from available parameter data
     * This replicates the logic from the parent's buildParameterInfo method
     */
    private ParameterInfo createParameterInfo() {
        ParameterInfo pinfo = new ParameterInfo();
        
        // Use altParameterName if set, else the normal param name
        String finalParamName = (getAltParameterName() != null) ? getAltParameterName() : getParameterName();
        pinfo.setName(finalParamName);
        
        // "type" from the parent
        pinfo.setType(getParameterType());
        
        // We guess the paramIn. If your testConf.yaml has "in" somewhere,
        // you may store it in the parent. For now let's just guess "query".
        String paramIn = "query";  // or "path", "header", etc.
        
        // 1) Find the Operation from the OAS
        Operation openApiOp = findOperation(
                getSpec().getSpecification(), // The 'OpenAPI' object
                getOperationPath(),
                getOperationMethod().toLowerCase()
        );
        
        // 2) If we found the Operation, find the parameter features
        if (openApiOp != null && finalParamName != null) {
            OpenAPIParameter paramObj = OpenAPISpecificationVisitor.findParameterFeatures(openApiOp, finalParamName, paramIn);
            
            if (paramObj != null) {
                pinfo.setInLocation(paramObj.getIn());
                pinfo.setFormat(paramObj.getFormat());
                pinfo.setRegex(paramObj.getPattern());
                pinfo.setDescription(paramObj.getDescription());
                pinfo.setSchemaType(paramObj.getType());
                
                // Set the example if available
                if (paramObj.getExample() != null) {
                    pinfo.setSchemaExample(paramObj.getExample().toString());
                }
                
                logger.debug("Found param in OAS => in: {}, type: {}, format: {}, pattern: {}, example: {}, description: {}",
                        paramObj.getIn(),
                        paramObj.getType(),
                        paramObj.getFormat(),
                        paramObj.getPattern(),
                        paramObj.getExample(),
                        paramObj.getDescription());
            } else {
                logger.warn("Could NOT find param '{}' with in='{}' in operation {} {}",
                        finalParamName, paramIn, getOperationMethod(), getOperationPath());
            }
        } else {
            logger.warn("No Operation found for path='{}', method='{}', or paramName null", getOperationPath(), getOperationMethod());
        }
        
        return pinfo;
    }
    
    /**
     * Helper method: find the correct OAS Operation from OpenAPI spec
     * given the path + method.
     */
    private Operation findOperation(io.swagger.v3.oas.models.OpenAPI openApi, String path, String method) {
        if (openApi == null || openApi.getPaths() == null || path == null) {
            return null;
        }
        io.swagger.v3.oas.models.PathItem pi = openApi.getPaths().get(path);
        if (pi == null) return null;
        
        switch (method.toLowerCase()) {
            case "get":    return pi.getGet();
            case "post":   return pi.getPost();
            case "put":    return pi.getPut();
            case "delete": return pi.getDelete();
            case "patch":  return pi.getPatch();
            default:       return null;
        }
    }
} 