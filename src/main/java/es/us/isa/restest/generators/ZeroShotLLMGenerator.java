package es.us.isa.restest.generators;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import es.us.isa.restest.inputs.llm.ParameterInfo;
import org.json.JSONObject;
import org.json.JSONArray;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A "zero-shot" style generator that queries a Large Language Model (LLM)
 * to produce realistic sample values for *any* parameter concept,
 * without enumerating categories like IP, city, country, etc.
 */
public class ZeroShotLLMGenerator {

    // optional: param name -> cached list of values
    private final Map<String, List<String>> cache = new ConcurrentHashMap<>();

    /**
     * Generate multiple candidate values for a parameter.
     * If the param is "ip_address", the LLM might produce valid IPs.
     * If "city", it might produce city names, etc.
     */
    public List<String> generateParameterValues(ParameterInfo param, int howMany) {
        System.out.println("*** ZeroShotLLMGenerator.generateParameterValues called for: " + param.getName() + " (howMany=" + howMany + ")");
        
        // 1) check cache
        if (cache.containsKey(param.getName())) {
            System.out.println("*** Found cached value for: " + param.getName());
            return cache.get(param.getName());
        }

        // 2) build prompt
        String prompt = buildPrompt(param, howMany);

        // 3) call the LLM
        String rawOutput = callLLM(prompt);
        System.out.println("*** LLM Raw output: " + rawOutput);

        // 4) parse the lines
        List<String> values = parseLines(rawOutput);

        // 5) filter by regex if present
        if (param.getRegex() != null && !param.getRegex().isEmpty()) {
            values.removeIf(val -> !val.matches(param.getRegex()));
        }

        // fallback if empty
        if (values.isEmpty()) {
            values = Collections.singletonList("");
        }

        // store in cache
        cache.put(param.getName(), values);
        return values;
    }

    /**
     * Build a textual prompt describing the parameter and how many examples we want.
     */
    private String buildPrompt(ParameterInfo param, int howMany) {
        StringBuilder promptBuilder = new StringBuilder();
        
        // Clear introduction with context
        promptBuilder.append("You are an API testing assistant that generates realistic parameter values.\n\n");
        
        // Parameter details
        promptBuilder.append("Parameter Information:\n");
        promptBuilder.append("- Name: ").append(safeStr(param.getName())).append("\n");
        
        String description = safeStr(param.getDescription());
        if (!description.isEmpty()) {
            promptBuilder.append("- Description: ").append(description).append("\n");
        }
        
        promptBuilder.append("- Location: ").append(safeStr(param.getInLocation())).append("\n");
        promptBuilder.append("- Data Type: ").append(safeStr(param.getType())).append("\n");
        
        String format = safeStr(param.getFormat());
        if (!format.isEmpty()) {
            promptBuilder.append("- Format: ").append(format).append("\n");
        }
        
        String example = safeStr(param.getSchemaExample());
        if (!example.isEmpty()) {
            promptBuilder.append("- Example: ").append(example).append("\n");
        }
        
        String regex = safeStr(param.getRegex());
        if (!regex.isEmpty()) {
            promptBuilder.append("- Pattern: ").append(regex).append("\n");
        }
        
        // Clear task instructions with emphasis on formatting
        promptBuilder.append("\nTask: Generate ").append(howMany).append(" realistic test values for this parameter.\n\n");
        
        promptBuilder.append("CRITICAL FORMATTING REQUIREMENT:\n");
        promptBuilder.append("You MUST return exactly ").append(howMany).append(" separate lines.\n");
        promptBuilder.append("Each line contains exactly ONE value.\n");
        promptBuilder.append("Press ENTER after each value.\n");
        promptBuilder.append("Do NOT put multiple values on the same line.\n\n");
        
        promptBuilder.append("Content Requirements:\n");
        promptBuilder.append("- Values should be appropriate for the parameter type and context\n");
        promptBuilder.append("- Generate diverse, realistic examples that an API might actually receive\n");
        promptBuilder.append("- Consider common use cases and edge cases\n\n");
        
        // Context-specific guidance based on parameter name/type
        String guidance = getContextualGuidance(param);
        if (!guidance.isEmpty()) {
            promptBuilder.append("Domain Guidance: ").append(guidance).append("\n\n");
        }
        
        promptBuilder.append("Example Format (for 3 values):\n");
        promptBuilder.append("Value1\n");
        promptBuilder.append("Value2\n");
        promptBuilder.append("Value3\n\n");
        
        promptBuilder.append("Now generate your ").append(howMany).append(" values, one per line:");
        
        return promptBuilder.toString();
    }
    
    /**
     * Provide context-specific guidance based on parameter characteristics
     */
    private String getContextualGuidance(ParameterInfo param) {
        String name = safeStr(param.getName()).toLowerCase();
        String type = safeStr(param.getType()).toLowerCase();
        
        // Station/location related
        if (name.contains("station") || name.contains("location") || name.contains("place")) {
            return "Generate realistic station/location names like train stations, bus stops, or landmarks.";
        }
        
        // ID related
        if (name.contains("id") && (name.contains("login") || name.contains("user"))) {
            return "Generate realistic user login IDs/usernames with mix of letters and numbers.";
        }
        
        // Date/time related
        if (name.contains("date") || name.contains("time") || type.contains("date")) {
            return "Generate valid date/time values in appropriate format.";
        }
        
        // List related
        if (name.contains("list") || type.contains("array")) {
            return "Generate single values that would be elements in a list, not the entire list.";
        }
        
        // Password related
        if (name.contains("password") || name.contains("pass")) {
            return "Generate realistic password patterns with appropriate complexity.";
        }
        
        // Number related
        if (type.contains("int") || type.contains("number")) {
            return "Generate realistic numeric values appropriate for the context.";
        }
        
        return "";
    }


    /**
     * This method is currently a stub. In a real system,
     * you'd do an API call to ChatGPT / local LLM / etc.
     */
    /**
     * This method calls the OpenAI ChatGPT API (model "gpt-3.5-turbo")
     * to produce multiple line-separated answers.
     *
     * Replace "YOUR_OPENAI_API_KEY" with your real key from
     * https://platform.openai.com/account/api-keys
     *
     * If you don't have an API key, you'll need to sign up for
     * an OpenAI account, which includes a free trial with credits.
     */
    private String callLLMGPT(String prompt) {
        final String OPENAI_API_KEY = "sk-proj-13vRlyGryVRNXe3f6MHpGaJhSWoPtXlnIjFI0Y8zAdOt47t8GxLrlmFWSrRrEsTyoR3AN6SHXxT3BlbkFJTt6UwbBtBQsI_XObKpD6csvDBOUgjInkA";
        final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

        String systemContent =
                "You are an AI system that generates parameter values for API testing. " +
                        "CRITICAL: When asked to generate N values, you MUST return exactly N lines. " +
                        "Each line contains exactly one value. Use line breaks between values. " +
                        "Do NOT put multiple values on the same line separated by spaces or commas. " +
                        "Do NOT add explanations, numbering, or extra formatting.";

        // ✅ Use org.json to build the request safely
        JSONArray messages = new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemContent))
                .put(new JSONObject().put("role", "user").put("content", prompt));

        JSONObject requestBody = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", messages)
                .put("max_tokens", 200)
                .put("temperature", 0.7)
                .put("n", 1);

        // Build and send request
        okhttp3.OkHttpClient httpClient = new okhttp3.OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                requestBody.toString(), okhttp3.MediaType.parse("application/json"));

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(OPENAI_API_URL)
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();

        try (okhttp3.Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("[ChatGPT] API call failed with code " + response.code());
                String err = response.body() != null ? response.body().string() : "";
                System.err.println("[ChatGPT] Response body: " + err);
                return "";
            }

            String responseBody = response.body() != null ? response.body().string() : "";
//            System.out.println("This is the response body: " + responseBody);
            return extractContentFromResponse(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private String callLLM(String prompt) {
        // Use local gpt4all API instead of external services
        final String LOCAL_LLM_API_URL = "http://localhost:4891/v1/chat/completions";

        String systemContent =
                "You are an AI system that generates parameter values for API testing. " +
                        "CRITICAL: When asked to generate N values, you MUST return exactly N lines. " +
                        "Each line contains exactly one value. Use line breaks between values. " +
                        "Do NOT put multiple values on the same line separated by spaces or commas. " +
                        "Do NOT add explanations, numbering, or extra formatting.";

        // Build the request body compatible with OpenAI API format (which gpt4all supports)
        JSONArray messages = new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemContent))
                .put(new JSONObject().put("role", "user").put("content", prompt));

        JSONObject requestBody = new JSONObject()
                .put("model", "llama-3-8b-instruct")
                .put("messages", messages)
                .put("max_tokens", 200)
                .put("temperature", 0.7);

        System.out.println("[Local LLM] Sending request to: " + LOCAL_LLM_API_URL);
        System.out.println("[Local LLM] Request body: " + requestBody.toString());

        try {
            // Build HTTP client with longer timeout
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)  // Increased to 90 seconds
                    .build();

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(LOCAL_LLM_API_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("[Local LLM] Response code: " + response.code());
                String responseBody = response.body().string();
                System.out.println("[Local LLM] Response body: " + responseBody);
                
                if (!response.isSuccessful()) {
                    System.out.println("[Local LLM] HTTP error: " + response.code() + " - " + response.message());
                    return generateFallbackValue();
                }

                JSONObject jsonResponse = new JSONObject(responseBody);

                if (jsonResponse.has("choices")) {
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    if (choices.length() > 0) {
                        JSONObject choice = choices.getJSONObject(0);
                        JSONObject message = choice.getJSONObject("message");
                        String content = message.getString("content").trim();
                        
                        System.out.println("[Local LLM] Successfully extracted content: " + content);
                        return content;
                    }
                }
                
                System.out.println("[Local LLM] No choices in response, using fallback");
                return generateFallbackValue();
            }

        } catch (Exception e) {
            System.out.println("[Local LLM] Error calling local LLM API: " + e.getMessage());
            e.printStackTrace();
            return generateFallbackValue();
        }
    }

    private String generateFallbackValue() {
        // Generate simple fallback values when LLM is unavailable
        String fallback = "test" + System.currentTimeMillis() % 1000;
        System.out.println("*** FALLBACK VALUE GENERATED: " + fallback);
        return fallback;
    }

    private String extractFromGeminiResponse(String responseJson) {
        try {
            JSONObject json = new JSONObject(responseJson);
            JSONArray candidates = json.getJSONArray("candidates");
            if (!candidates.isEmpty()) {
                JSONObject first = candidates.getJSONObject(0);
                JSONObject content = first.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (!parts.isEmpty()) {
                    //System.out.println("Here is the output:" + parts.getJSONObject(0).optString("text", ""));
                    return parts.getJSONObject(0).optString("text", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    /**
     * ChatGPT responses are in JSON. We need to parse out:
     *   response.choices[0].message.content
     *
     * We'll do a quick substring approach or a small JSON parse.
     * For a robust solution, use Jackson or Gson.
     */
    private String extractContentFromResponse(String responseJson) {
        // Example JSON (truncated for demonstration):
        // {
        //   "id": "chatcmpl-123",
        //   "object": "chat.completion",
        //   "choices": [
        //     {
        //       "index": 0,
        //       "message": {
        //         "role": "assistant",
        //         "content": "line1\nline2\nline3"
        //       },
        //       ...
        //     }
        //   ],
        //   ...
        // }

        // For brevity, let's do a naive parse.
        // But it's better to use a JSON library.
        // We'll search for: "content": "
        String contentMarker = "\"content\":";
        int ix = responseJson.indexOf(contentMarker);
        if (ix < 0) {
            return "";
        }
        // skip contentMarker
        int start = responseJson.indexOf("\"", ix + contentMarker.length());
        if (start < 0) return "";
        // next quote
        int end = responseJson.indexOf("\"", start + 1);
        if (end < 0) return "";

        // This only gets up to the next quote, doesn't handle escapes or multiple lines well
        // We'll do a more robust approach below:

        // Instead, let's do a quick JSON parse with minimal libs:
        // you can do this if you have a small JSON library.
        // Or parse with substring if we trust there are no edge cases.
        // We'll do a minimal approach here.
        // For demonstration, let's do a basic approach:

        // This is a minimal approach, but let's do something a bit better:

        try {
            // Use org.json or another library if you prefer
            JSONObject obj = new JSONObject(responseJson);
            JSONArray choices = obj.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject msg = firstChoice.getJSONObject("message");
                return msg.optString("content", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Escape quotes or backslashes so we can embed user text in JSON.
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }


    private List<String> parseLines(String raw) {
        if (raw == null || raw.isEmpty()) {
            return Collections.emptyList();
        }
        String[] arr = raw.split("\\r?\\n");
        List<String> lines = new ArrayList<>();
        for (String line : arr) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                lines.add(trimmed);
            }
        }
        return lines;
    }

    private String safeStr(String s) {
        return (s == null ? "" : s);
    }
}
