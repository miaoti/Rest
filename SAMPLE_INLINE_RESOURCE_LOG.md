# Sample LLM Communication Log with Inline Resource Monitoring

This shows how the resource monitoring data will appear **directly in your LLM communication logs** in the `logs/llm-communications/` directory.

## Sample Log Output

```
################################################################################
# LLM COMMUNICATION LOG
# Session ID: LLM-1692459600123-1
# Started: 2025-08-19 16:30:00.123
# Configuration:
#   - Include Response Time: true
#   - Include Content: true
#   - Include Metadata: true
#   - Max Content Length: -1
#   - Log Level: INFO
################################################################################

================================================================================
🚀 LLM REQUEST #1
================================================================================
Timestamp: 2025-08-19 16:30:05.234
Session ID: LLM-1692459600123-1
Request ID: 1
Model Type: LOCAL
Model Name: llama-3-8b-instruct
Endpoint: http://localhost:4891/v1/chat/completions
Additional Metadata: maxTokens=200, temperature=0.70
Resource Monitoring: ✅ ENABLED (Local Model)
📊 Initial Resource Usage: CPU: 12.3% | Memory: 45.2% (3.6 GB/8.0 GB) | Heap: 856.3 MB
--------------------------------------------------------------------------------
📝 SYSTEM PROMPT:
You are a helpful assistant for REST API testing. Generate realistic parameter values for train booking systems.
--------------------------------------------------------------------------------
👤 USER PROMPT:
Generate a valid train station name for the parameter 'departureStation' in a train booking API.
--------------------------------------------------------------------------------

🎯 LLM RESPONSE #1
Timestamp: 2025-08-19 16:30:08.987
Status: ✅ SUCCESS
Response Time: 3753 ms
📊 Final Resource Usage: CPU: 8.7% | Memory: 47.8% (3.8 GB/8.0 GB) | Heap: 912.7 MB
--------------------------------------------------------------------------------
🤖 LLM RESPONSE:
New York Penn Station
--------------------------------------------------------------------------------
================================================================================

================================================================================
🚀 LLM REQUEST #2
================================================================================
Timestamp: 2025-08-19 16:30:15.123
Session ID: LLM-1692459600123-1
Request ID: 2
Model Type: LOCAL
Model Name: llama-3-8b-instruct
Endpoint: http://localhost:4891/v1/chat/completions
Additional Metadata: maxTokens=200, temperature=0.70
Resource Monitoring: ✅ ENABLED (Local Model)
📊 Initial Resource Usage: CPU: 8.7% | Memory: 47.8% (3.8 GB/8.0 GB) | Heap: 912.7 MB
--------------------------------------------------------------------------------
📝 SYSTEM PROMPT:
You are a helpful assistant for REST API testing. Generate realistic parameter values for train booking systems.
--------------------------------------------------------------------------------
👤 USER PROMPT:
Generate a valid date for the parameter 'departureDate' in a train booking API. Format: YYYY-MM-DD
--------------------------------------------------------------------------------

🎯 LLM RESPONSE #2
Timestamp: 2025-08-19 16:30:18.456
Status: ✅ SUCCESS
Response Time: 3333 ms
📊 Final Resource Usage: CPU: 15.2% | Memory: 49.1% (3.9 GB/8.0 GB) | Heap: 967.2 MB
--------------------------------------------------------------------------------
🤖 LLM RESPONSE:
2025-08-25
--------------------------------------------------------------------------------
================================================================================

================================================================================
🚀 LLM REQUEST #3
================================================================================
Timestamp: 2025-08-19 16:30:25.789
Session ID: LLM-1692459600123-1
Request ID: 3
Model Type: GEMINI
Model Name: gemini-2.0-flash-exp
Endpoint: https://generativelanguage.googleapis.com/v1beta/models
Additional Metadata: maxTokens=200, temperature=0.70
Resource Monitoring: ⏭️ SKIPPED (Online Model)
--------------------------------------------------------------------------------
📝 SYSTEM PROMPT:
You are a helpful assistant for REST API testing. Generate realistic parameter values for train booking systems.
--------------------------------------------------------------------------------
👤 USER PROMPT:
Generate a valid passenger count for the parameter 'passengerCount' in a train booking API.
--------------------------------------------------------------------------------

🎯 LLM RESPONSE #3
Timestamp: 2025-08-19 16:30:26.234
Status: ✅ SUCCESS
Response Time: 445 ms
--------------------------------------------------------------------------------
🤖 LLM RESPONSE:
2
--------------------------------------------------------------------------------
================================================================================
```

## Key Features of Inline Resource Monitoring

### 1. **Initial Resource Usage** (at request start)
```
📊 Initial Resource Usage: CPU: 12.3% | Memory: 45.2% (3.6 GB/8.0 GB) | Heap: 856.3 MB
```

### 2. **Final Resource Usage** (at response completion)
```
📊 Final Resource Usage: CPU: 8.7% | Memory: 47.8% (3.8 GB/8.0 GB) | Heap: 912.7 MB
```

### 3. **Model-Specific Behavior**
- **LOCAL/OLLAMA models**: Show resource monitoring data
- **GEMINI (online API)**: Show "Resource Monitoring: ⏭️ SKIPPED (Online Model)"

### 4. **Resource Data Format**
- **CPU**: Percentage usage (when available)
- **Memory**: Percentage and absolute values (used/total)
- **Heap**: Java heap memory usage (when detailed monitoring enabled)

## Configuration Impact

With your current settings in `trainticket-demo.properties`:

```properties
llm.model.type=local
llm.resource.monitoring.enabled=true
llm.resource.monitoring.include.detailed.memory=true
llm.resource.monitoring.include.cpu=true
```

You'll see:
- ✅ Resource monitoring for LOCAL model calls
- CPU and memory data in each request/response pair
- Detailed heap memory information
- All data in your existing `logs/llm-communications/` directory

## Benefits

1. **Single Log File**: All data in one place - no need to correlate separate files
2. **Request Correlation**: Resource usage directly tied to specific LLM requests
3. **Performance Analysis**: See resource impact of each individual LLM call
4. **Before/After Comparison**: Initial vs final resource usage for each request
5. **Model Comparison**: See resource differences between local and online models

This approach gives you immediate visibility into how much CPU and memory each LLM call consumes, directly in your communication logs! 🎯
