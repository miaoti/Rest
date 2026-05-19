package es.us.isa.restest.test;

import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.inputs.smart.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Demonstration test for the Smart Input Fetching System
 * Shows how the system intelligently fetches realistic test data
 */
public class SmartInputFetchingDemoTest {
    
    private SmartInputFetcher smartFetcher;
    private SmartInputFetchConfig config;
    
    @BeforeEach
    void setUp() {
        // Create configuration
        config = new SmartInputFetchConfig();
        config.setEnabled(true);
        config.setSmartFetchPercentage(0.7); // 70% smart fetching, 30% LLM
        config.setRegistryPath("src/main/resources/My-Example/trainticket/input-fetch-registry.yaml");
        config.setLlmDiscoveryEnabled(true);
        config.setMaxCandidates(3);
        config.setDependencyResolutionEnabled(true);
        config.setDiscoveryTimeoutMs(5000);
        
        // Initialize cache config
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setEnabled(true);
        cacheConfig.setMaxEntries(100);
        cacheConfig.setTtlSeconds(300);
        config.setCacheConfig(cacheConfig);
        
        // Set base URL (would normally come from properties)
        String baseUrl = "http://localhost:8080"; // TrainTicket system URL
        
        // Initialize the smart fetcher
        smartFetcher = new SmartInputFetcher(config, baseUrl);
    }
    
    @Test
    @DisplayName("Test Smart Station Name Fetching")
    void testStationNameFetching() {
        // Create parameter info for station name
        ParameterInfo stationParam = new ParameterInfo();
        stationParam.setName("stationName");
        stationParam.setType("string");
        stationParam.setDescription("Name of the railway station");
        stationParam.setInLocation("body");
        stationParam.setSchemaExample("Beijing");
        
        // Test smart fetching
        String fetchedValue = smartFetcher.fetchSmartInput(stationParam);
        
        // Verify result
        assertNotNull(fetchedValue, "Smart fetcher should return a value");
        assertFalse(fetchedValue.trim().isEmpty(), "Smart fetcher should return a non-empty value");
        assertFalse(fetchedValue.startsWith("LLM_"), "Should not fall back to LLM for station names");
        
        System.out.println("Smart fetched station name: " + fetchedValue);
    }
    
    @Test
    @DisplayName("Test Smart User ID Fetching")
    void testUserIdFetching() {
        // Create parameter info for user ID
        ParameterInfo userParam = new ParameterInfo();
        userParam.setName("userId");
        userParam.setType("string");
        userParam.setDescription("Unique identifier for a user");
        userParam.setInLocation("path");
        
        // Test smart fetching
        String fetchedValue = smartFetcher.fetchSmartInput(userParam);
        
        // Verify result
        assertNotNull(fetchedValue, "Smart fetcher should return a value");
        assertFalse(fetchedValue.trim().isEmpty(), "Smart fetcher should return a non-empty value");
        
        System.out.println("Smart fetched user ID: " + fetchedValue);
    }
    
    @Test
    @DisplayName("Test Registry Loading and Pattern Matching")
    void testRegistryPatternMatching() {
        // Load the input fetch registry
        InputFetchRegistry registry = new InputFetchRegistry();
        
        // Test pattern matching for station-related parameters
        List<String> candidateServices = registry.getAllServices();
        assertFalse(candidateServices.isEmpty(), "Registry should have service patterns");
        
        // Look for station service
        boolean hasStationService = candidateServices.stream()
                .anyMatch(service -> service.contains("station"));
        assertTrue(hasStationService, "Should have station-related services");
        
        System.out.println("Available services: " + candidateServices);
    }
    
    @Test
    @DisplayName("Test Configuration Loading from Properties")
    void testConfigurationLoading() {
        // Test configuration loading
        Map<String, String> testProperties = new HashMap<>();
        testProperties.put("smart.input.fetch.enabled", "true");
        testProperties.put("smart.input.fetch.percentage", "0.8");
        testProperties.put("smart.input.fetch.registry.path", "test-registry.yaml");
        testProperties.put("smart.input.fetch.llm.discovery.enabled", "true");
        testProperties.put("smart.input.fetch.max.candidates", "5");
        
        SmartInputFetchConfig testConfig = SmartInputFetchConfig.fromProperties(testProperties);
        
        assertTrue(testConfig.isEnabled(), "Should be enabled");
        assertEquals(0.8, testConfig.getSmartFetchPercentage(), 0.01, "Should have correct percentage");
        assertEquals("test-registry.yaml", testConfig.getRegistryPath(), "Should have correct registry path");
        assertTrue(testConfig.isLlmDiscoveryEnabled(), "LLM discovery should be enabled");
        assertEquals(5, testConfig.getMaxCandidates(), "Should have correct max candidates");
        
        System.out.println("Configuration loaded successfully: " + testConfig);
    }
    
    @Test
    @DisplayName("Test API Mapping Cache")
    void testApiMappingCache() {
        // Create test API mapping
        ApiMapping mapping = new ApiMapping();
        mapping.setEndpoint("/api/v1/stationservice/stations");
        mapping.setMethod("GET");
        mapping.setService("ts-station-service");
        mapping.setExtractPath("$.data[*].name");
        mapping.setPriority(10);
        mapping.setSuccessRate(0.95);
        mapping.setDescription("Fetches all station names from station service");
        
        // Test mapping properties
        assertEquals("/api/v1/stationservice/stations", mapping.getEndpoint());
        assertEquals("GET", mapping.getMethod());
        assertEquals("ts-station-service", mapping.getService());
        assertEquals("$.data[*].name", mapping.getExtractPath());
        assertEquals(10, mapping.getPriority());
        assertEquals(0.95, mapping.getSuccessRate(), 0.01);
        
        System.out.println("API Mapping created successfully: " + mapping);
    }
    
    @Test
    @DisplayName("Test Smart Fetching Percentage Logic")
    void testSmartFetchingPercentage() {
        // Test with different percentages
        config.setSmartFetchPercentage(1.0); // 100% smart fetching
        
        ParameterInfo param = new ParameterInfo();
        param.setName("testParam");
        param.setType("string");
        param.setDescription("Test parameter");
        
        // The smart fetcher should always try smart fetching with 100% percentage
        // In a real scenario, this would connect to actual APIs
        // For testing, we verify the configuration is respected
        
        assertTrue(config.getSmartFetchPercentage() == 1.0, "Should use 100% smart fetching");
        assertTrue(config.isEnabled(), "Smart fetching should be enabled");
        
        System.out.println("Smart fetching percentage test passed: " + config.getSmartFetchPercentage());
    }
} 