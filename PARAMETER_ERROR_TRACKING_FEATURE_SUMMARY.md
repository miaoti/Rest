# Parameter Error Tracking Feature - Implementation Summary

## 🎯 **Feature Overview**

This feature implements a comprehensive parameter error tracking system that learns from API test failures and improves future test generation by avoiding known problematic parameter values.

## ✅ **Implemented Components**

### 1. **Core Data Structures**

#### `ParameterError.java`
- Stores detailed error information (error type, reason, API endpoint, parameter name, timestamp)
- Provides structured way to capture parameter-specific failure patterns
- Supports deduplication to avoid redundant error storage

#### `InputFetchRegistry.java` (Extended)
- Added `parameterErrors` mapping: `API endpoint → parameter → List<ParameterError>`
- Methods for adding, retrieving, and querying parameter errors
- Error context generation for LLM prompts
- YAML serialization/deserialization for persistence

### 2. **Error Analysis Engine**

#### `ParameterErrorAnalyzer.java`
- **LLM-Powered Analysis**: Uses LLM to analyze trace errors and identify which specific parameter caused the failure
- **Fallback Pattern Matching**: Pattern-based analysis for common error types when LLM is unavailable
- **Trace Integration**: Extracts API endpoint and HTTP method information from Jaeger traces
- **Structured Results**: Returns detailed analysis with parameter name, error type, and reason

### 3. **Test Execution Integration**

#### `MultiServiceRESTAssuredWriter.java` (Enhanced)
- **Parameter Collection**: Collects all parameters used during test execution in `allStepParameters` map
- **Automatic Analysis**: Triggers parameter error analysis after detecting trace errors
- **Registry Updates**: Automatically saves new parameter errors to the registry YAML file
- **Generated Code**: Adds `analyzeAndRecordParameterErrors()` method to generated test classes

### 4. **Smart Parameter Generation**

#### `SmartLLMParameterGenerator.java` (Enhanced)
- **Error-Aware Generation**: Loads error context from registry during parameter generation
- **Enhanced Prompts**: Adds known error patterns to LLM prompts to avoid problematic values
- **Contextual Learning**: Uses historical failure data to guide parameter value selection

## 🔄 **Complete Workflow**

### **Test Generation Phase**
1. **Smart Fetcher & LLM Generator** load error patterns from registry
2. **Parameter Generation** includes error context in prompts: "⚠️ KNOWN ERROR PATTERNS: avoid these patterns..."
3. **Value Selection** avoids known problematic parameter values

### **Test Execution Phase**
1. **Parameter Collection** gathers all parameters used in API calls (`allStepParameters`)
2. **Trace Analysis** analyzes Jaeger traces for errors using `TraceErrorAnalyzer`
3. **Parameter Error Analysis** identifies which parameter caused failures using `ParameterErrorAnalyzer`

### **Error Learning Phase**
1. **LLM Analysis** determines failing parameter and reason (e.g., "PARAMETER: price, ERROR_TYPE: VALIDATION_ERROR, REASON: Negative values not allowed")
2. **Registry Update** stores new error patterns in the input-fetch-registry.yaml
3. **Persistence** saves updated registry for future test runs

### **Future Test Runs**
1. **Context Loading** reads updated registry with new error patterns
2. **Enhanced Generation** uses learned patterns to avoid repeating failures
3. **Continuous Learning** builds knowledge base of parameter constraints

## 📊 **Registry Structure Extension**

The `input-fetch-registry.yaml` file now includes:

```yaml
parameterErrors:
  "/api/v1/orderservice/orders":
    price:
    - errorType: "VALIDATION_ERROR"
      errorReason: "Negative price values not allowed"
      timestamp: [2025, 8, 11, 17, 0, 0, 111222333]
      additionalInfo: "IllegalArgumentException for price = -100.50"
    loginId:
    - errorType: "AUTHORIZATION_ERROR"
      errorReason: "Invalid login ID format"
      timestamp: [2025, 8, 11, 17, 10, 0, 777888999]
      additionalInfo: "Authentication failed for malformed loginId"
```

## 🎯 **Key Benefits**

1. **Self-Learning System**: Tests automatically learn from failures and avoid repeating mistakes
2. **LLM-Enhanced Analysis**: AI-powered identification of exact parameter failure causes
3. **Historical Knowledge**: Builds persistent knowledge base of parameter constraints
4. **Non-Intrusive**: Existing test generation continues working; error tracking is additive
5. **Comprehensive Integration**: Works with Smart Input Fetching, traditional LLM generation, and MST workflows

## 🔧 **Configuration**

The feature uses existing configuration properties:

```properties
# Required: Registry file path
smart.input.fetch.registry.path=src/main/resources/My-Example/trainticket/input-fetch-registry.yaml

# LLM configuration for error analysis
llm.enabled=true
llm.model.type=ollama
llm.ollama.enabled=true
llm.ollama.url=http://localhost:11434
llm.ollama.model=gemma3:4b
```

## 📋 **Generated Test Class Changes**

Generated test classes now include:

1. **Parameter Collection**: `Map<String, String> allStepParameters` collects all test parameters
2. **Error Analysis Method**: `analyzeAndRecordParameterErrors(trace, allStepParameters)` 
3. **Automatic Integration**: Called after trace error detection in `attachJaegerTrace()`
4. **Required Imports**: All necessary imports for error analysis classes

## ✨ **Example Usage**

When a test fails with a trace error:

1. **Trace Analysis** detects API failure in `/api/v1/orderservice/orders`
2. **Parameter Analysis** identifies that `price=-50.0` caused a validation error
3. **LLM Analysis** determines: "Negative price values not allowed"
4. **Registry Update** stores the error pattern for future avoidance
5. **Next Test Run** generates positive price values based on learned constraint

## 🚀 **Status: Fully Implemented**

All components are implemented and integrated. The feature is ready for testing and can be enabled by:

1. Setting `smart.input.fetch.enabled=true` in properties
2. Configuring the LLM service for error analysis
3. Running MST test generation with `generator=MST`

The system will automatically start learning from parameter failures and improving future test generation quality.
