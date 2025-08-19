# System Resource Monitoring for Local LLM Models

## Overview

This system provides comprehensive **CPU and memory monitoring** during Local LLM model communications. The monitoring system automatically tracks system resources **ONLY for local models** (GPT4All, Ollama) and **NOT for online API calls** (Gemini).

## 🎯 Key Features

- **Real-time CPU usage monitoring** during local LLM calls
- **Detailed memory usage tracking** (heap, non-heap, total memory)
- **Configurable monitoring intervals** (default: 1 second)
- **Automatic log file creation** with timestamps
- **Thread-safe operation** for concurrent LLM calls
- **Smart model detection** - only monitors local models
- **Integration with existing LLM communication logging**

## 🏗️ Architecture

### Components

1. **`SystemResourceMonitor`** - Core monitoring class
   - Tracks CPU and memory usage
   - Creates timestamped log files
   - Manages monitoring lifecycle

2. **`LLMCommunicationLogger`** - Enhanced with monitoring integration
   - Starts/stops monitoring based on model type
   - Correlates resource data with LLM requests
   - Cross-references between communication and resource logs

3. **Configuration Properties** - Flexible monitoring settings
   - Enable/disable monitoring
   - Adjust monitoring intervals
   - Configure log locations and formats

## 📁 Directory Structure

```
logs/
├── llm-communications/           # LLM request/response logs
│   └── llm-communication-20250819-154500.log
└── system-resources/             # Resource monitoring logs (NEW)
    └── resource-monitor-20250819-154500.log
```

## ⚙️ Configuration

### Basic Configuration

Add these properties to your configuration file:

```properties
# Enable resource monitoring
llm.resource.monitoring.enabled=true

# Log directory
llm.resource.monitoring.dir=logs/system-resources

# Monitoring interval (in milliseconds)
llm.resource.monitoring.interval.ms=1000

# Include CPU monitoring
llm.resource.monitoring.include.cpu=true

# Include detailed memory information
llm.resource.monitoring.include.detailed.memory=true
```

### Model-Specific Behavior

| Model Type | Resource Monitoring | Log Message |
|------------|-------------------|-------------|
| **LOCAL** (GPT4All) | ✅ **ENABLED** | `Resource Monitoring: ✅ ENABLED (Local Model)` |
| **OLLAMA** | ✅ **ENABLED** | `Resource Monitoring: ✅ ENABLED (Local Model)` |
| **GEMINI** (API) | ⏭️ **SKIPPED** | `Resource Monitoring: ⏭️ SKIPPED (Online Model)` |

## 🚀 Usage Examples

### 1. Using with LOCAL Model (GPT4All)

```properties
# Configuration for GPT4All monitoring
llm.model.type=local
llm.local.enabled=true
llm.local.url=http://localhost:4891
llm.local.model=Meta-Llama-3-8B-Instruct.Q4_0.gguf

# Resource monitoring will be ENABLED
llm.resource.monitoring.enabled=true
```

### 2. Using with OLLAMA Model

```properties
# Configuration for Ollama monitoring  
llm.model.type=ollama
llm.ollama.enabled=true
llm.ollama.url=http://localhost:11434
llm.ollama.model=gemma2:2b

# Resource monitoring will be ENABLED
llm.resource.monitoring.enabled=true
```

### 3. Using with GEMINI API

```properties
# Configuration for Gemini API
llm.model.type=gemini
llm.gemini.enabled=true
llm.gemini.api.key=YOUR_API_KEY_HERE

# Resource monitoring will be SKIPPED (no local computation)
llm.resource.monitoring.enabled=true
```

## 📊 Sample Resource Monitor Log

```
################################################################################
# SYSTEM RESOURCE MONITORING LOG
# Session ID: RES-1692454500123-1
# Started: 2025-08-19 15:45:00.123
# Configuration:
#   - Monitoring Interval: 1000 ms
#   - Include CPU Usage: true
#   - Include Detailed Memory: true
#   - Include System Info: true
# Note: Only monitors LOCAL and OLLAMA models, not online APIs
################################################################################

================================================================================
🔍 RESOURCE MONITORING START #1
================================================================================
Timestamp: 2025-08-19 15:45:01.234
Session ID: RES-1692454500123-1
Monitoring ID: 1
LLM Request ID: 42
Model Type: LOCAL
Model Name: Meta-Llama-3-8B-Instruct.Q4_0.gguf
Monitoring Interval: 1000 ms

💻 SYSTEM INFORMATION:
  OS: Windows 10 10.0
  Architecture: amd64
  Java Version: 11.0.19
  Available Processors: 8
  Max Memory: 8.0 GB
  Total Memory: 2.1 GB
  Free Memory: 1.2 GB

--------------------------------------------------------------------------------
📊 RESOURCE MONITORING DATA:
⏱️  [2025-08-19 15:45:01.234] CPU: 15.67% | Memory: 45.23% (3.6 GB/8.0 GB) | Heap: 856.3 MB | Non-Heap: 89.4 MB
⏱️  [2025-08-19 15:45:02.235] CPU: 89.45% | Memory: 52.18% (4.2 GB/8.0 GB) | Heap: 1.1 GB | Non-Heap: 92.1 MB
⏱️  [2025-08-19 15:45:03.236] CPU: 92.33% | Memory: 56.89% (4.6 GB/8.0 GB) | Heap: 1.3 GB | Non-Heap: 94.8 MB
⏱️  [2025-08-19 15:45:04.237] CPU: 78.12% | Memory: 54.67% (4.4 GB/8.0 GB) | Heap: 1.2 GB | Non-Heap: 96.2 MB
⏱️  [2025-08-19 15:45:05.238] CPU: 23.45% | Memory: 48.34% (3.9 GB/8.0 GB) | Heap: 967.5 MB | Non-Heap: 91.7 MB
--------------------------------------------------------------------------------
🏁 RESOURCE MONITORING END
Timestamp: 2025-08-19 15:45:05.999
LLM Request ID: 42
================================================================================
```

## 📊 Sample Communication Log with Monitoring

```
🚀 LLM REQUEST #42
================================================================================
Timestamp: 2025-08-19 15:45:01.234
Session ID: LLM-1692454500123-1
Request ID: 42
Model Type: LOCAL
Model Name: Meta-Llama-3-8B-Instruct.Q4_0.gguf
Endpoint: http://localhost:4891
Resource Monitoring: ✅ ENABLED (Local Model)
Additional Metadata: maxTokens=200, temperature=0.70
--------------------------------------------------------------------------------
📝 SYSTEM PROMPT:
You are a helpful assistant for REST API testing.
--------------------------------------------------------------------------------
👤 USER PROMPT:
Generate test data for a train booking API endpoint.
--------------------------------------------------------------------------------

🎯 LLM RESPONSE #42
Timestamp: 2025-08-19 15:45:05.999
Status: ✅ SUCCESS
Response Time: 4765 ms
Resource Monitoring: 🔍 COMPLETED - Check system-resources logs for details
--------------------------------------------------------------------------------
🤖 LLM RESPONSE:
Here's test data for your train booking API:
{
  "departure": "New York",
  "destination": "Boston",
  "date": "2025-08-20",
  "passengers": 2
}
--------------------------------------------------------------------------------
================================================================================
```

## 🔧 Configuration Options

### Complete Configuration Reference

```properties
# Resource Monitoring Configuration
# =================================

# Enable/disable monitoring (default: true)
llm.resource.monitoring.enabled=true

# Log directory (default: logs/system-resources)
llm.resource.monitoring.dir=logs/system-resources

# File prefix (default: resource-monitor)
llm.resource.monitoring.file.prefix=resource-monitor

# Monitoring interval in milliseconds (default: 1000)
# Values: 500ms (detailed), 1000ms (balanced), 2000ms (light)
llm.resource.monitoring.interval.ms=1000

# Include detailed memory info (default: true)
llm.resource.monitoring.include.detailed.memory=true

# Include CPU usage (default: true)
llm.resource.monitoring.include.cpu=true

# Include system info at monitoring start (default: true)
llm.resource.monitoring.include.system.info=true
```

### Performance Tuning

#### For Detailed Analysis
```properties
llm.resource.monitoring.interval.ms=500
llm.resource.monitoring.include.detailed.memory=true
llm.resource.monitoring.include.cpu=true
```

#### For Production Use
```properties
llm.resource.monitoring.interval.ms=2000
llm.resource.monitoring.include.detailed.memory=false
llm.resource.monitoring.include.cpu=true
```

#### For Minimal Overhead
```properties
llm.resource.monitoring.interval.ms=5000
llm.resource.monitoring.include.detailed.memory=false
llm.resource.monitoring.include.cpu=false
```

## 🧪 Testing

### Run the Test Program

```bash
java TestResourceMonitoring
```

This will test monitoring with all three model types and show expected behavior:

1. **LOCAL model**: Resource monitoring enabled
2. **OLLAMA model**: Resource monitoring enabled  
3. **GEMINI API**: Resource monitoring skipped

### Expected Test Output

```
=== System Resource Monitoring Test ===

🎯 **NEW FEATURE: System Resource Monitoring for Local LLM Models**
This feature monitors CPU and memory usage during LLM communications
ONLY for local models (LOCAL/OLLAMA), NOT for online APIs (GEMINI).

🔧 **Test Configuration:**
- Resource monitoring enabled: true
- Monitoring interval: 500ms
- Log directory: logs/system-resources

🧪 **Test 1: Local Model (Should Monitor Resources)**
  Model Type: LOCAL
  Model Name: Meta-Llama-3-8B-Instruct.Q4_0.gguf
  Endpoint: http://localhost:4891
  ✅ Test completed for LOCAL

🧪 **Test 2: Ollama Model (Should Monitor Resources)**
  Model Type: OLLAMA
  Model Name: gemma2:2b
  Endpoint: http://localhost:11434
  ✅ Test completed for OLLAMA

🧪 **Test 3: Gemini API (Should Skip Monitoring)**
  Model Type: GEMINI
  Model Name: gemini-2.0-flash-exp
  Endpoint: https://generativelanguage.googleapis.com
  ✅ Test completed for GEMINI

✅ **Test Completed Successfully!**

📁 **Check the following directories for logs:**
- LLM Communications: logs/llm-communications/
- Resource Monitoring: logs/system-resources/
```

## 🔍 Troubleshooting

### Common Issues

1. **No resource monitoring logs created**
   - Check if `llm.resource.monitoring.enabled=true`
   - Verify you're using LOCAL or OLLAMA model type
   - Ensure log directory exists and is writable

2. **CPU usage shows -1.0**
   - This is normal on some systems where CPU usage cannot be determined
   - Memory monitoring will still work correctly

3. **High resource usage during monitoring**
   - Increase monitoring interval: `llm.resource.monitoring.interval.ms=2000`
   - Disable detailed features: `llm.resource.monitoring.include.detailed.memory=false`

### Log Location Issues

If logs are not appearing in expected locations:

```bash
# Check if directories exist
ls -la logs/
ls -la logs/system-resources/

# Create directories if missing
mkdir -p logs/system-resources
```

## 🚀 Integration with Your Project

### 1. Add Configuration

Add resource monitoring settings to your properties file:

```properties
# Copy from resource-monitoring-config.properties
llm.resource.monitoring.enabled=true
llm.resource.monitoring.dir=logs/system-resources
llm.resource.monitoring.interval.ms=1000
```

### 2. Verify Setup

Run a simple test to verify monitoring works:

```java
Properties props = new Properties();
props.load(new FileInputStream("your-config.properties"));

LLMCommunicationLogger logger = LLMCommunicationLogger.getInstance(props);
// Your existing LLM code will now include resource monitoring
```

### 3. Monitor Results

Check the new log directory:
- `logs/system-resources/` - Resource monitoring data
- `logs/llm-communications/` - Enhanced communication logs

## 📈 Benefits

1. **Performance Analysis**: Track resource usage patterns during LLM inference
2. **Capacity Planning**: Understand resource requirements for different models
3. **Debugging**: Identify resource bottlenecks during LLM operations
4. **Optimization**: Compare resource usage between different local models
5. **Monitoring**: Real-time insight into system load during AI workloads

## ⚠️ Important Notes

- **Local Models Only**: Monitoring automatically skips online APIs (Gemini)
- **Thread-Safe**: Safe to use with concurrent LLM requests
- **Configurable Impact**: Adjust monitoring frequency to balance detail vs. overhead
- **Automatic Cleanup**: Resources are properly cleaned up when monitoring ends
- **Cross-Platform**: Works on Windows, Linux, and macOS

## 🔮 Future Enhancements

Potential improvements for future versions:

- GPU usage monitoring for CUDA-enabled models
- Network I/O monitoring for API calls
- Disk I/O monitoring for model loading
- Historical resource usage analysis
- Resource usage alerts and thresholds
- Integration with monitoring dashboards

---

**Ready to monitor your local LLM models? Start with the provided configuration files and test program!** 🚀
