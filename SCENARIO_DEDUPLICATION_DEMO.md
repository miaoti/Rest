# Enhanced Scenario Deduplication System

## Overview

The enhanced scenario deduplication system refines test generation by comparing step sequences across traces and eliminating redundant scenarios. This saves computational resources and focuses testing on unique workflow patterns.

## Key Features

### 1. **Detailed Scenario Analysis**
- Analyzes each trace file to extract workflow scenarios
- Compares step sequences (HTTP method + path) across scenarios
- Groups scenarios by identical step signatures

### 2. **Intelligent Deduplication Logic**
- **Signature-Based Comparison**: Creates unique signatures from step sequences
- **Representative Selection**: Keeps the first scenario from each group as representative
- **Resource Optimization**: Eliminates duplicate scenarios to save test generation time

### 3. **Comprehensive Logging**
- Shows which scenarios are unique vs. duplicates
- Provides detailed step-by-step analysis
- Reports resource savings achieved

## Example Scenario Analysis

Based on the TrainTicket trace files:

### Trace Files Available:
1. `user_order_get.json` - User order retrieval workflow
2. `admin_order_create.json` - Admin order creation workflow  
3. `admin_add_route_success.json` - Successful route addition workflow
4. `admin_add_route_failed.json` - Failed route addition workflow
5. `admin_price_create.json` - Price creation workflow

### Deduplication Process:

```
=== SCENARIO DEDUPLICATION ANALYSIS ===
Starting with 5 scenarios from traces

Scenario 1 (from traces: [trace_001]): signature = GET /api/v1/orderservice/order → POST /api/v1/orderservice/order
Scenario 2 (from traces: [trace_002]): signature = POST /api/v1/adminorderservice/adminorder → GET /api/v1/orderservice/order
Scenario 3 (from traces: [trace_003]): signature = POST /api/v1/adminrouteservice/adminroute → GET /api/v1/routeservice/routes
Scenario 4 (from traces: [trace_004]): signature = POST /api/v1/adminrouteservice/adminroute → GET /api/v1/routeservice/routes
Scenario 5 (from traces: [trace_005]): signature = POST /api/v1/adminpriceservice/adminprice → GET /api/v1/priceservice/prices

=== DEDUPLICATION RESULTS ===
✓ UNIQUE: Scenario 1 will generate tests (no duplicates)
✓ UNIQUE: Scenario 2 will generate tests (no duplicates)
✓ KEPT: Scenario 3 as representative for 2 identical scenarios
  → Steps: ts-admin-route-service[POST /api/v1/adminrouteservice/adminroute] → ts-route-service[GET /api/v1/routeservice/routes]
  → Traces included: [trace_003]
✗ ELIMINATED: Scenarios 4 (identical step sequences)
    Eliminated scenario 4 had traces: [trace_004]
✓ UNIQUE: Scenario 5 will generate tests (no duplicates)

=== FINAL SUMMARY ===
Original scenarios: 5
Unique scenarios for test generation: 4
Duplicate scenarios eliminated: 1
Resource savings: 20.0% fewer test cases to generate
```

## Benefits

### 1. **Resource Efficiency**
- Eliminates redundant test generation
- Reduces computation time and memory usage
- Focuses testing on unique workflow patterns

### 2. **Intelligent Analysis**
- Distinguishes between truly different workflows
- Preserves scenarios with different step sequences
- Maintains trace metadata for debugging

### 3. **Clear Reporting**
- Detailed logging shows exactly which scenarios are kept/eliminated
- Provides reasoning for each deduplication decision
- Reports quantitative savings achieved

## Implementation Details

### Enhanced WorkflowScenarioUtils.deduplicateBySteps()

The enhanced method provides:

1. **Signature Generation**: Creates unique signatures from step sequences
2. **Grouping Logic**: Groups scenarios by identical signatures
3. **Representative Selection**: Selects first scenario from each group
4. **Detailed Logging**: Provides comprehensive analysis reports

### Key Code Enhancements:

```java
// Group scenarios by signature
Map<String, ScenarioGroup> signatureGroups = new LinkedHashMap<>();
for (int i = 0; i < scenarios.size(); i++) {
    WorkflowScenario scenario = scenarios.get(i);
    String signature = buildSignature(scenario);
    
    log.info("Scenario {} (from traces: {}): signature = {}", 
             i + 1, scenario.getTraceIds(), signature);
    
    ScenarioGroup group = signatureGroups.computeIfAbsent(signature, 
        k -> new ScenarioGroup(signature));
    group.addScenario(scenario, i + 1);
}

// Analyze and report duplicates
for (ScenarioGroup group : signatureGroups.values()) {
    if (group.size() > 1) {
        log.info("✓ KEPT: Scenario {} as representative for {} identical scenarios", 
                 group.getRepresentativeIndex(), group.size());
        log.info("✗ ELIMINATED: Scenarios {} (identical step sequences)", 
                 group.getDuplicateIndices());
    }
}
```

## Usage in Test Generation

The deduplication system is automatically integrated into the test generation pipeline:

```java
// In TestGenerationAndExecution.java
List<WorkflowScenario> scenarios = TraceWorkflowExtractor.extractScenarios(TraceFile);
scenarios = WorkflowScenarioUtils.deduplicateBySteps(scenarios);  // ← Enhanced deduplication
```

This ensures that only unique scenarios proceed to test case generation, optimizing the entire testing process.

## Conclusion

The enhanced scenario deduplication system provides:
- **Intelligent duplicate detection** based on step sequences
- **Comprehensive logging** for transparency and debugging
- **Resource optimization** through elimination of redundant scenarios
- **Seamless integration** into existing test generation workflow

This refinement addresses your specific requirement: *"if two scenarios have exactly the same step calls, it will ignore the second one"* while providing detailed analysis and reporting of the deduplication process. 