# Root API Registry System

## Overview

The Root API Registry is a sophisticated system designed for tracking and managing unique Root API endpoints with their complete microservice interaction trees. This system is essential for microservice testing as it:

1. **Prevents Duplicate Root APIs**: Ensures each unique root API endpoint is registered only once
2. **Captures Interaction Trees**: Records the complete downstream service call hierarchies for each root API
3. **Supports Multiple Patterns**: Allows one root API to have multiple interaction trees (common in microservices)
4. **Enables Future Testing**: Provides a persistent registry for targeted API testing campaigns

## Key Components

### 1. RootApiRegistry
- **Purpose**: Main registry class that manages the collection of root APIs
- **Storage**: JSON file format for persistence across test runs
- **Thread-Safe**: Supports concurrent access for multi-threaded test generation
- **Features**:
  - Automatic deduplication of root APIs
  - Tree structure comparison to avoid duplicate patterns
  - Registry statistics and reporting
  - Configurable file path via properties

### 2. RootApiEntry
- **Purpose**: Represents a single root API endpoint
- **Contains**:
  - HTTP method (GET, POST, etc.)
  - API path (e.g., `/api/v1/tickets`)
  - Service name
  - Multiple tree structures for different execution patterns
- **Features**:
  - Tree structure hashing for duplicate detection
  - JSON serialization/deserialization
  - Thread-safe tree management

### 3. ApiTree
- **Purpose**: Represents a specific microservice interaction pattern
- **Structure**: Hierarchical tree showing service call dependencies
- **Contains**:
  - Tree ID and source trace information
  - Root node with complete service call hierarchy
  - Structure hash for pattern comparison
- **Features**:
  - Recursive tree structure representation
  - Service call normalization (removes specific IDs, focuses on patterns)
  - JSON persistence

### 4. TreeNode
- **Purpose**: Individual node in the microservice interaction tree
- **Contains**:
  - Service name
  - Operation/API path
  - HTTP method
  - Child nodes (downstream service calls)

## Configuration

Add the following properties to your test configuration file (e.g., `trainticket-demo.properties`):

```properties
# ROOT API REGISTRY CONFIGURATION
# Enable Root API Registry
root.api.registry.enabled=true

# Path where the registry JSON file will be stored
root.api.registry.path=src/main/resources/My-Example/trainticket/root-api-registry.json

# Enable detailed logging
root.api.registry.debug.logging=true
```

## Integration with MST

The Root API Registry is automatically integrated with the Multi-Service Testing (MST) workflow in `TestGenerationAndExecution.java`:

1. **Trace Processing**: After `TraceWorkflowExtractor` processes trace files
2. **Registry Update**: Root APIs are extracted and registered with their tree structures
3. **Persistence**: Registry is automatically saved to the configured JSON file
4. **Logging**: Detailed statistics are logged for monitoring

## JSON Structure

The registry uses a structured JSON format:

```json
{
  "registry_metadata": {
    "version": "1.0",
    "last_updated": "2025-09-16T10:30:00Z",
    "total_root_apis": 5,
    "total_trees": 12
  },
  "root_apis": {
    "GET /api/v1/tickets": {
      "method": "GET",
      "path": "/api/v1/tickets",
      "service": "ts-ticket-service",
      "trees": [
        {
          "tree_id": "tree_1_admin_add_route_success",
          "source_trace": "admin_add_route_success",
          "structure_hash": "123456789",
          "root": {
            "service": "ts-ticket-service",
            "operation": "GET /api/v1/tickets",
            "http_method": "GET",
            "api_path": "/api/v1/tickets",
            "children": [
              {
                "service": "ts-price-service",
                "operation": "POST /api/v1/price/query",
                "http_method": "POST",
                "api_path": "/api/v1/price/query",
                "children": []
              }
            ]
          }
        }
      ]
    }
  }
}
```

## Benefits

### 1. **Microservice Testing Optimization**
- Focus testing on actual entry points used by real clients
- Understand complete service interaction patterns
- Avoid testing internal APIs that aren't publicly accessible

### 2. **Pattern Analysis**
- Identify common microservice interaction patterns
- Detect when the same root API triggers different downstream flows
- Analyze service dependency relationships

### 3. **Test Campaign Planning**
- Use registered root APIs as basis for comprehensive test suites
- Ensure coverage of all discovered interaction patterns
- Track API usage patterns over time

### 4. **Documentation and Visualization**
- Automatic documentation of microservice interactions
- Clear hierarchy of service dependencies
- Source trace tracking for reproducibility

## Usage Example

```java
// Initialize registry
RootApiRegistry registry = new RootApiRegistry("path/to/registry.json");

// Register root APIs from workflow scenarios
List<WorkflowScenario> scenarios = TraceWorkflowExtractor.extractScenarios(traceDir);
registry.registerRootApisFromScenarios(scenarios);

// Save registry
registry.saveRegistry();

// Get statistics
RootApiRegistry.RegistryStats stats = registry.getStats();
System.out.println("Total Root APIs: " + stats.getTotalRootApis());
System.out.println("Total Trees: " + stats.getTotalTrees());

// Access specific root API
RootApiEntry entry = registry.getRootApiEntry("GET /api/v1/tickets");
for (ApiTree tree : entry.getTrees()) {
    System.out.println("Tree from: " + tree.getSourceTrace());
    // Analyze tree structure...
}
```

## Logging

The system provides comprehensive logging:

- **INFO**: Registry initialization, updates, and statistics
- **DEBUG**: Individual root API registrations and tree additions
- **WARN**: Configuration issues and URL parsing failures
- **ERROR**: File I/O errors and JSON parsing issues

Example log output:
```
INFO  - Initializing Root API Registry at: src/main/resources/My-Example/trainticket/root-api-registry.json
INFO  - Registering new root API: GET /api/v1/tickets
DEBUG - Added new tree 'tree_1_admin_add_route_success' for root API 'GET /api/v1/tickets'
INFO  - Root API Registry updated: 5 total root APIs, 12 total trees
INFO  - Registry saved to: src/main/resources/My-Example/trainticket/root-api-registry.json
```

