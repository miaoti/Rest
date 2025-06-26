package es.us.isa.restest.generators;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import es.us.isa.restest.inputs.llm.ParameterInfo;
import org.json.JSONObject;
import org.json.JSONArray;

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
        // 1) check cache
        if (cache.containsKey(param.getName())) {
            return cache.get(param.getName());
        }

        // 2) build prompt
        String prompt = buildPrompt(param, howMany);

        // 3) call the LLM
        String rawOutput = callLLM(prompt);
        System.out.println(rawOutput);

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
        return String.format(Locale.ROOT,
                "You are a knowledge model. The parameter has the following metadata:\n" +
                        "- Name: %s\n" +
                        "- Description: %s\n" +
                        "- In: %s\n" +
                        "- Type: %s\n" +
                        "- Format: %s\n" +
                        "- Schema Type: %s\n" +
                        "- Schema Example: %s\n" +
                        "- Regex Pattern: %s\n\n" +
                        "Please generate %d realistic example values for this parameter in English.\n" +
                        "The generated result should not based on any of your memory but only randomly generating based on parameter's metadata.\n" +
                        "Return the values only, one per line,make sure only one item per line, there should not have multiple items in one line seperated by comma, do not do something like \" a,b,c\" but like \"a\nb\nc\n\", with no extra commentary or formatting.\n",
                safeStr(param.getName()),
                safeStr(param.getDescription()),
                safeStr(param.getInLocation()),
                safeStr(param.getType()),
                safeStr(param.getFormat()),
                safeStr(param.getSchemaType()),
                safeStr(param.getSchemaExample()),
                safeStr(param.getRegex()),
                howMany
        );
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
        final String OPENAI_API_KEY = "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";
        final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

        String systemContent =
                "You are an AI system that generates only the final lines of values " +
                        "with no extra commentary or numbering. " +
                        "If asked to produce N lines, return exactly N lines.";

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
                "You are an AI system that generates only the final lines of values " +
                        "with no extra commentary or numbering. " +
                        "If asked to produce N lines, return exactly N lines.";

        // Build the request body compatible with OpenAI API format (which gpt4all supports)
        JSONArray messages = new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemContent))
                .put(new JSONObject().put("role", "user").put("content", prompt));

        JSONObject requestBody = new JSONObject()
                .put("model", "llama-3-8b-instruct")  // Use the local Llama 3 8B Instruct model
                .put("messages", messages)
                .put("max_tokens", 200)
                .put("temperature", 0.7)
                .put("stream", false);

        okhttp3.OkHttpClient httpClient = new okhttp3.OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                requestBody.toString(),
                okhttp3.MediaType.parse("application/json")
        );

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(LOCAL_LLM_API_URL)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (okhttp3.Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("[Local LLM] API call failed with code " + response.code());
                String err = response.body() != null ? response.body().string() : "";
                System.err.println("[Local LLM] Response body: " + err);
                return "";
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            System.out.println("[Local LLM] Raw response: " + responseBody);
            return extractContentFromResponse(responseBody);
        } catch (Exception e) {
            System.err.println("[Local LLM] Error calling local LLM API: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
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
