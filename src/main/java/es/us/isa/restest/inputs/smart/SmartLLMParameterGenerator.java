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
    private Random random = es.us.isa.restest.util.SeededRandom.create("SmartLLMParameterGenerator");
    
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
        // If smart fetching is not available or disabled, use parent's LLM generation with error context
        if (!initialized || !config.isEnabled()) {
            return generateWithErrorContext();
        }
        
        try {
            // Build parameter info from available data
            ParameterInfo parameterInfo = createParameterInfo();

            // Try smart fetching first (percentage check is handled inside SmartInputFetcher)
            String smartValue = smartFetcher.fetchSmartInput(parameterInfo);

            if (smartValue != null && !smartValue.trim().isEmpty()) {
                logger.debug("Smart fetching provided value '{}' for parameter '{}'",
                           smartValue, parameterInfo.getName());
                return TextNode.valueOf(smartValue);
            }

        } catch (Exception e) {
            logger.debug("Smart fetching failed for parameter '{}', falling back to LLM: {}",
                        getParameterName(), e.getMessage());
        }
        
        // Fall back to traditional LLM generation with error context
        return generateWithErrorContext();
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
     * Generate parameter value with error context awareness
     * This method uses the error registry to avoid known problematic values
     */
    private JsonNode generateWithErrorContext() {
        try {
            // Get the parent's fallback value
            JsonNode parentFallbackNode = super.nextValue();

            // Build parameter info with error context
            ParameterInfo pinfo = createParameterInfoWithErrorContext();

            // Ask the LLM for possible values with error awareness
            java.util.List<String> llmValues = getAiDrivenGenerator().generateParameterValues(pinfo);

            // Decide which value to return
            if (llmValues != null && !llmValues.isEmpty()) {
                String chosen = llmValues.get(random.nextInt(llmValues.size()));
                logger.debug("SmartLLMParameterGenerator used error-aware LLM value for '{}' => {}", 
                           pinfo.getName(), chosen);
                return new TextNode(chosen);
            } else {
                logger.debug("SmartLLMParameterGenerator had no LLM output, fallback => {}", 
                           parentFallbackNode.asText());
                return parentFallbackNode;
            }
            
        } catch (Exception e) {
            logger.warn("Error in generateWithErrorContext for parameter '{}': {}", 
                       getParameterName(), e.getMessage());
            return super.nextValue();
        }
    }
    
    /**
     * Path-keyed in-memory cache of {@link InputFetchRegistry}. The previous implementation
     * re-parsed the entire (~1.6 MB / 51 K-line) registry YAML on every parameter generation,
     * burning seconds of wall time per scenario (Bug audit Finding #13).
     *
     * <p>The cache is invalidated when the registry file's mtime changes — sufficient for
     * concurrent reads from a single JVM. Reviewer Comment 19: this is a process-wide
     * cache so multiple {@code SmartLLMParameterGenerator}/{@code SmartInputFetcher}
     * instances share a single view.</p>
     */
    private static final java.util.concurrent.ConcurrentMap<String, CachedRegistry> REGISTRY_CACHE =
            new java.util.concurrent.ConcurrentHashMap<>();

    private static final class CachedRegistry {
        final InputFetchRegistry registry;
        final long lastModified;
        CachedRegistry(InputFetchRegistry r, long m) { this.registry = r; this.lastModified = m; }
    }

    /**
     * Returns the cached registry for the given path, or loads + caches it on miss.
     * Returns {@code null} when the path is unset / file missing / parse fails.
     */
    static InputFetchRegistry sharedRegistry(String registryPath) {
        if (registryPath == null || registryPath.isEmpty()) return null;
        java.io.File registryFile = new java.io.File(registryPath);
        if (!registryFile.exists()) return null;
        long mtime = registryFile.lastModified();
        CachedRegistry cached = REGISTRY_CACHE.get(registryPath);
        if (cached != null && cached.lastModified == mtime) {
            return cached.registry;
        }
        try {
            InputFetchRegistry loaded = InputFetchRegistry.loadFromFile(registryFile);
            REGISTRY_CACHE.put(registryPath, new CachedRegistry(loaded, mtime));
            return loaded;
        } catch (java.io.IOException e) {
            logger.debug("Failed to load registry from {}: {}", registryPath, e.getMessage());
            return null;
        }
    }

    /**
     * Create ParameterInfo object with error context from registry.
     */
    private ParameterInfo createParameterInfoWithErrorContext() {
        ParameterInfo pinfo = createParameterInfo();
        try {
            String registryPath = System.getProperty("smart.input.fetch.registry.path");
            InputFetchRegistry registry = sharedRegistry(registryPath);
            if (registry != null) {
                String apiEndpoint = getOperationPath();
                if (apiEndpoint != null) {
                    String errorContext = registry.getErrorContextForParameter(apiEndpoint, pinfo.getName());
                    if (!errorContext.isEmpty()) {
                        String existingDesc = pinfo.getDescription() != null ? pinfo.getDescription() : "";
                        String enhancedDesc = existingDesc + "\n\n" + errorContext;
                        pinfo.setDescription(enhancedDesc);
                        logger.debug("Added error context for parameter '{}' on endpoint '{}': {} error patterns found",
                                pinfo.getName(), apiEndpoint,
                                registry.getParameterErrors(apiEndpoint, pinfo.getName()).size());
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Could not load error context for parameter '{}': {}", pinfo.getName(), e.getMessage());
        }
        return pinfo;
    }
    
    /**
     * Get access to the AiDrivenLLMGenerator from parent class
     */
    private es.us.isa.restest.generators.AiDrivenLLMGenerator getAiDrivenGenerator() {
        try {
            // Use reflection to access the private aiDriven field from parent
            java.lang.reflect.Field field = getClass().getSuperclass().getDeclaredField("aiDriven");
            field.setAccessible(true);
            return (es.us.isa.restest.generators.AiDrivenLLMGenerator) field.get(this);
        } catch (Exception e) {
            logger.warn("Could not access AiDrivenLLMGenerator from parent: {}", e.getMessage());
            // Create a new instance as fallback
            return new es.us.isa.restest.generators.AiDrivenLLMGenerator();
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
        
        // 1) Find the Operation from the OAS
        Operation openApiOp = findOperation(
                getSpec().getSpecification(), // The 'OpenAPI' object
                getOperationPath(),
                getOperationMethod().toLowerCase()
        );

        // 2) Try every standard parameter location in priority order so path/header/body/cookie
        // parameters are not silently treated as query strings (which would skip their schema
        // constraints — pattern, enum, format, etc.).
        if (openApiOp != null && finalParamName != null) {
            OpenAPIParameter paramObj = null;
            String foundIn = null;
            for (String candidateIn : PARAM_LOCATION_LOOKUP_ORDER) {
                OpenAPIParameter candidate = OpenAPISpecificationVisitor.findParameterFeatures(
                        openApiOp, finalParamName, candidateIn);
                if (candidate != null) {
                    paramObj = candidate;
                    foundIn = candidateIn;
                    break;
                }
            }

            if (paramObj != null) {
                pinfo.setInLocation(paramObj.getIn() != null ? paramObj.getIn() : foundIn);
                pinfo.setFormat(paramObj.getFormat());
                pinfo.setRegex(paramObj.getPattern());
                pinfo.setDescription(paramObj.getDescription());
                pinfo.setSchemaType(paramObj.getType());

                // Set the example if available
                if (paramObj.getExample() != null) {
                    pinfo.setSchemaExample(paramObj.getExample().toString());
                }

                logger.debug("Found param '{}' (in={}) in OAS => type: {}, format: {}, pattern: {}, example: {}, description: {}",
                        finalParamName,
                        pinfo.getInLocation(),
                        paramObj.getType(),
                        paramObj.getFormat(),
                        paramObj.getPattern(),
                        paramObj.getExample(),
                        paramObj.getDescription());
            } else {
                logger.warn("Could NOT find param '{}' in any location for operation {} {}",
                        finalParamName, getOperationMethod(), getOperationPath());
            }
        } else {
            logger.warn("No Operation found for path='{}', method='{}', or paramName null", getOperationPath(), getOperationMethod());
        }

        return pinfo;
    }

    /** Order in which parameter locations are tried when the caller didn't tell us which one. */
    private static final java.util.List<String> PARAM_LOCATION_LOOKUP_ORDER =
            java.util.Arrays.asList("query", "path", "header", "body", "formData", "cookie");
    
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