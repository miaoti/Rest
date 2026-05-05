# Fresh deep code review — Smart Input Fetch subsystem

Date: 2026-05-05
Reviewer: Independent Code Review Agent (no audit context)
Files in scope:
- `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java` (4199 lines)
- `src/main/java/es/us/isa/restest/inputs/smart/InputFetchRegistry.java` (835)
- `src/main/java/es/us/isa/restest/inputs/smart/SmartLLMParameterGenerator.java` (378)
- `src/main/java/es/us/isa/restest/inputs/smart/ParameterErrorAnalyzer.java` (488)
- `src/main/java/es/us/isa/restest/inputs/smart/SmartFetchAuthManager.java` (254)
- `src/main/java/es/us/isa/restest/inputs/smart/OpenAPIEndpointDiscovery.java` (221)
- `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetchConfig.java` (291)
- `src/main/java/es/us/isa/restest/inputs/smart/CacheConfig.java` (42)
- `src/main/java/es/us/isa/restest/inputs/smart/ServicePattern.java` (68)
- `src/main/java/es/us/isa/restest/inputs/smart/ApiMapping.java` (168)
- `src/main/java/es/us/isa/restest/inputs/smart/ParameterError.java` (79)

Integration points cross-checked: `MultiServiceTestCaseGenerator` (lines 200-244, 800-960, 2330-2370, 2980-3009).
Persisted state cross-checked: `src/main/resources/My-Example/trainticket/input-fetch-registry.yaml` (49 977 lines, 1.6 MB).
Design intent cross-checked: `src/main/resources/My-Example/trainticket/flow.md` (Smart Input Fetching Flow section).

Excluded from input: every file under `debug/inputs/smart_fetch/` (prior audits, plans, reviewer feedback, execution summaries).

## Summary verdict

`SmartInputFetcher` is functional and shows clear evidence of iterative fixes (many "Bug audit Finding #N" comments justify recent changes), but it is now a 4 200-line god class with deeply intertwined responsibilities — value extraction, formatting, validation, LLM orchestration, prompt engineering, schema inference, caching, and HTTP plumbing all live in the same file. The biggest risks I see are **(1) configuration silently dropping on the floor** — `CacheConfig.maxEntries` from YAML never reaches the cache, and `llm.local.api.key` (a property the docs call out as critical for DeepSeek) is filtered out by an explicit allowlist before the LLMService is constructed, **(2) the consumer-scoping fix (Finding #15) does not actually work for the persisted registry** because the existing 49 977-line YAML has zero `consumerApiKey` entries and the SmartLLMParameterGenerator path never sets `apiName` on its ParameterInfo, so every read falls into the global tier, **(3) brittle name-substring heuristics** that contradict the boundary-aware ID rule the rest of the file is careful to use (`paramName.contains("route")`, `contains("number")`, `contains("price")`). Concurrency is mostly OK (`Collections.synchronizedMap` does override `compute`), but the cache and registry have several read-side TOCTOU races and unbounded static caches.

Top-3 risk areas: (1) silent config drop (Findings F1, F2, F12); (2) scoping fix is paper-only (Findings F3, F11); (3) heuristic validators contradict each other (Findings F4, F5, F6, F8).

What surprised me: a JWT prefix is logged at debug level (F13); `ServicePattern` is fully dead code shipped as 5 default patterns initialized in the registry (F18); the algorithmic last-resort string fallback can return values like `dist1234d` (a hash-derived char appended) that have nothing to do with the parameter (F19); and the file imports six okhttp3 types plus `org.json.JSONArray`/`JSONObject` plus `TimeUnit`/`Pattern`/`ConcurrentHashMap` — all unused (F22). 

## Findings (severity-ordered, then file-ordered)

### Finding F1: `CacheConfig.maxEntries` from YAML is never honored — caches always default to 1000

**Severity:** High
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:132-136` and `:175-179`
**Category:** Logical / Config

**Code excerpt:**
```java
// Cache constructed in ctor at line 132:
int cacheCap = config.isCacheEnabled()
        ? Math.max(1, registryCacheCapacity()) : 1;
this.cache = boundedLruMap(cacheCap);
this.diverseValueCache = boundedLruMap(cacheCap);
this.valueRotationIndex = boundedLruMap(cacheCap);
this.openAPIDiscovery = new OpenAPIEndpointDiscovery();
// ... (auth setup)
loadRegistry();   // line 152

// registryCacheCapacity:
private int registryCacheCapacity() {
    return registry != null && registry.getCacheConfig() != null
            ? registry.getCacheConfig().getMaxEntries()
            : 1000;
}
```

**What is wrong:** `registryCacheCapacity()` reads `this.registry`, but the cache is built *before* `loadRegistry()` runs. So `registry` is always `null` at that point, and the method always returns the literal `1000`. The Javadoc on `registryCacheCapacity()` (line 172) admits this in passing — "Defaults to 1000 if the registry has not been loaded yet (the cache is constructed before `loadRegistry()` runs)" — but the cache is *unconditionally* constructed before `loadRegistry()`, so the YAML's `cache.maxEntries` has zero effect.

**Why it matters:** The YAML claims to be the system of record for cache configuration. Operators who increase `cache.maxEntries` to e.g. 5 000 to accommodate a long soak test will see no behavior change and may attribute eviction artifacts to other causes.

**Suggested fix:** Either (a) call `loadRegistry()` before constructing the caches, or (b) defer cache construction until after registry load, or (c) read `cache.maxEntries` directly from `SmartInputFetchConfig` (and delete the YAML-side `CacheConfig`, since `SmartInputFetchConfig` is the actual source of truth for *every other* config knob).

---

### Finding F2: `llm.local.api.key` is silently filtered out before the LLMService is constructed

**Severity:** High
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:195-218`
**Category:** Config / Doc

**Code excerpt:**
```java
private Map<String, String> loadLLMProperties() {
    Map<String, String> properties = new HashMap<>();
    String[] llmProperties = {
        "llm.enabled", "llm.model.type",
        "llm.local.enabled", "llm.local.url", "llm.local.model",
        "llm.gemini.enabled", "llm.gemini.api.key", "llm.gemini.model", "llm.gemini.api.url",
        "llm.ollama.enabled", "llm.ollama.url", "llm.ollama.model",
        ...
    };
    for (String prop : llmProperties) {
        String value = System.getProperty(prop);
        if (value != null) properties.put(prop, value);
    }
    ...
}
```

**What is wrong:** The allowlist enumerates every LLM-related system property the SmartInputFetcher is willing to forward to `LLMService.getInstance(...)`. `llm.local.api.key` — which `LLMConfig.java:99` reads for OpenAI-compatible auth (DeepSeek, OpenAI, etc., per `flow.md` lines 851-862) — is missing from the list. Same for `llm.local.api.timeout` and any future option. A user setting `-Dllm.local.api.key=$DEEPSEEK_API_KEY` will see the key dropped on the floor and DeepSeek will return 401, with no log indicating why.

**Why it matters:** This is the difference between "DeepSeek as documented in flow.md works" and "every smart fetch that hits the LLM silently fails 401". The bug is invisible — the SmartInputFetcher just sees `null` from `llmService.generateText` and falls back to algorithmic generation, which still succeeds.

**Suggested fix:** Replace the explicit allowlist with a prefix filter (any `llm.*` and `auth.*` system property) — or, better, drop `loadLLMProperties()` entirely and use the same `loadPropertiesFromSystem()` pattern that `SmartLLMParameterGenerator:103-125` uses (which forwards every `smart.input.fetch*` property without enumerating them).

---

### Finding F3: Consumer-scoping (Bug audit Finding #15) is paper-only — every persisted entry is global, and the SmartLLMParameterGenerator never sets `apiName`

**Severity:** High
**Location:** `src/main/resources/My-Example/trainticket/input-fetch-registry.yaml` (49 977 lines, 0 `consumerApiKey:` entries) and `src/main/java/es/us/isa/restest/inputs/smart/SmartLLMParameterGenerator.java:291-353` (`createParameterInfo()` does not call `setApiName`)
**Category:** Logical / YAML / Doc

**What is wrong:** Two related symptoms:
1. The shipped registry YAML has **zero** entries with `consumerApiKey:` — `grep -c "consumerApiKey:" input-fetch-registry.yaml = 0`. So even though `InputFetchRegistry.getMappingsForParameter(consumerApiKey, parameterName)` (lines 116-139) is implemented to return scoped mappings ahead of global ones, every existing learned mapping has scope=null and lands in the global tier. The new fix only takes effect for mappings learned in *future* runs.
2. `SmartLLMParameterGenerator.createParameterInfo()` (line 291) builds a `ParameterInfo` without calling `setApiName`. The MultiServiceTestCaseGenerator path has a `createParameterInfoWithContext()` helper (line 1313 of `MultiServiceTestCaseGenerator`) that *does* set apiName, but the `SmartLLMParameterGenerator` (which is the path RESTest's classic LLM generator inheritance chain uses) does not. So discoveries triggered through that path register with scope=null too, indefinitely diluting the "fix".

**Why it matters:** The whole point of Finding #15 was to stop two operations that share a parameter named `id` from polluting each other's candidate list. As shipped, that pollution still happens for every existing learning *and* for half the future writes.

**Suggested fix:** (a) Run a one-time migration script that populates `consumerApiKey` on existing entries by joining the registry with the OAS so we can guess the original consumer for each saved mapping, *or* document the registry as a fresh start and clear it. (b) Add `info.setApiName(...)` to `SmartLLMParameterGenerator.createParameterInfo()` using `getOperationMethod() + " " + getOperationPath()`.

---

### Finding F4: `paramName.contains("route")` causes false positives in the LLM-response validator

**Severity:** High
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1604`
**Category:** Logical / API

**Code excerpt:**
```java
if (isIdLikeParamName(paramName) || paramName.contains("route")) {
    // IDs should be reasonable length and format
    if (cleanResponse.length() < 3 || cleanResponse.length() > 50) { ... return false; }
    if (cleanResponse.contains(" ") && cleanResponse.split(" ").length > 3) { ... return false; }
}
```

**What is wrong:** The rest of `SmartInputFetcher` is careful to use the boundary-aware `isIdLikeParamName()` (line 2110) precisely so that English words like `paid`, `valid`, `humid`, `void`, `aid` do NOT match — a regression specifically called out as "pipeline-bug-audit Finding #5". This validator sneaks in a raw substring match on `"route"` that fires on any of `routedById`, `traceRouteUrl`, `getrouteName`, `enroute`, `routeBigDescription`. Those are not necessarily ID-shaped fields; rejecting a 60-char station name because the param is called `routeDescription` is the same bug pattern the boundary-aware rule was introduced to fix.

**Why it matters:** Treating `routeDescription` (a string parameter that may legitimately exceed 50 chars or contain spaces) as if it were `routeId` rejects valid LLM output and pushes the system into algorithmic fallback unnecessarily. Reduces fetch quality without observable signal.

**Suggested fix:** Drop the `|| paramName.contains("route")` clause. If a route-id parameter has the bare name `"route"` it's already covered by the bare-id check inside `isIdLikeParamName`.

---

### Finding F5: `paramName.contains("number")` matches `phoneNumber`/`trainNumber` and forces digit-presence on string parameters

**Severity:** High
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1619`
**Category:** Logical

**Code excerpt:**
```java
if (paramName.contains("price") || paramName.contains("rate") || paramName.contains("number")) {
    // Should be numeric or at least contain numbers
    if (!cleanResponse.matches(".*\\d.*")) {
        log.warn("❌ Numeric parameter '{}' contains no digits: '{}'", paramName, cleanResponse);
        return false;
    }
}
```

**What is wrong:** `phoneNumber` is a string. `trainNumber` per TrainTicket is a string like `G1237` (which luckily has digits, so this passes), but `flightNumber` could be `AA` (American Airlines — no digits). `bigCategoryNumber` matches too. The check assumes any param name containing the substring "number" must be numeric, contradicting `getOpenAPISchemaType()` which is the authoritative source for type classification. Equivalent issues with `contains("price")` (matches `discountPriceLabel`).

**Why it matters:** Same pattern as F4 — substring matching on parameter names where boundary-aware checks already exist, and the schema type is the right answer.

**Suggested fix:** Replace with `isNumericSchemaType(parameterInfo)` (already defined at line 1320). Drop the substring checks entirely.

---

### Finding F6: `isNonsensicalValue` rejects non-numeric values for any parameter whose name contains "distance"/"price"/"rate" — bypasses schema

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:2555-2562`
**Category:** Logical

**Code excerpt:**
```java
// Distance/numeric parameters should not have non-numeric values (but allow values with units)
if ((paramName.contains("distance") || paramName.contains("price") || paramName.contains("rate"))) {
    String numericPart = extractNumericPart(cleanValue);
    if (numericPart == null || numericPart.trim().isEmpty()) {
        log.debug("Rejecting non-numeric value '{}' for numeric parameter '{}'", cleanValue, paramName);
        return true;
    }
}
```

**What is wrong:** Same anti-pattern as F4/F5. The rest of the file has `isNumericSchemaType(parameterInfo)` which uses the OpenAPI schema. Hardcoded English nouns reject values for parameters like `migrationDateRange` (matches "rate"), `priceListDescription` (matches "price"), `distanceUnit` (matches "distance" — but `distanceUnit` could very well be a string `"km"`).

**Why it matters:** Strings legitimately containing a digit-free description for a "distance"-named string parameter are silently rejected, pushing tests toward algorithmic fallback values that are even less realistic.

**Suggested fix:** Gate the check on `isNumericSchemaType(parameterInfo)`, not parameter-name substrings.

---

### Finding F7: `isValidApiResponse` rejects bodies whose top-level data is `null` or `[empty]` but accepts arrays of garbage

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:639-694`
**Category:** Logical

**Code excerpt:**
```java
for (String envelopeKey : new String[] {"data", "result", "payload", "items"}) {
    Object payload = obj.opt(envelopeKey);
    if (payload instanceof org.json.JSONArray
            && ((org.json.JSONArray) payload).isEmpty()) {
        log.debug("API response has empty {} array", envelopeKey);
        return false;
    }
    ...
}
return true;
```

**What is wrong:** The validator returns `true` (response is "valid") in two surprising cases:
1. `{"data": null}` is *accepted* (the `payload` is null, neither array nor object, falls through), but the downstream extractor will inevitably fail to extract anything useful from null.
2. `{"data": [{}]}` (a non-empty array containing one empty object) is also accepted, but extraction will fail for the same reason.
3. Conversely, `{"data": []}` short-circuits to `false` — but a response that is just a top-level array `[]` (no envelope) is *also* `false` via the body=="[]" check at line 645. So an empty top-level array is rejected, but an envelope with `"data": null` is not. Inconsistent.

**Why it matters:** The validator is supposed to filter out useless responses *before* spending an LLM call on extraction. False positives waste LLM budget; false negatives are caught later but with no failed-mapping signal back to `updateSuccessRate`.

**Suggested fix:** Reject `data == null` envelopes the same way `data == []` is rejected; treat single-element arrays of empty objects the same as empty arrays.

---

### Finding F8: Length caps disagree across the validation stack (50 vs. 100 vs. 2000)

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1587, 1606, 2521-2527, 2664`
**Category:** Logical / Doc

**What is wrong:** Four different length caps are sprinkled across the file:
- `isValidLLMResponse:1587` — `cleanResponse.length() > 100` ⇒ "explanation, not value", reject.
- `isValidLLMResponse:1606` — for ID-like params, `length() < 3 || > 50` ⇒ reject.
- `isValidValueForParameter:2521-2527` — `DEFAULT_MAX_VALUE_LENGTH = 2000` (or schema `maxLength + 100`), reject if exceeded.
- `isValidIdValue:2664` — `length() > 50 || contains(" ")` ⇒ reject.

A 60-char station description string returned by the LLM would: pass `isValidValueForParameter` (under 2000), pass `isValidLLMResponse` length-100 check (under 100)... wait, 60 < 100, OK. But a 105-char description would fail `isValidLLMResponse`'s "looks like an explanation" check even though it's a perfectly legitimate value. Meanwhile a 51-char ID would be rejected by `isValidIdValue` but accepted by `isValidLLMResponse` (because 51 < 100 for non-ID params, but 51 > 50 for ID params at line 1606 too... but only when that branch triggers, which depends on the same brittle paramName check called out in F4).

The result is a different cap depending on which validator runs first.

**Why it matters:** Operators trying to figure out why a particular value is being rejected have to read all four call sites to find the active cap. The schema's declared `maxLength` is supposed to be the source of truth, but two of the four caps ignore it.

**Suggested fix:** Delete the magic 100 and 50 from the LLM-response validator and let `isValidValueForParameter` (the only schema-aware one) be the single gate. The ID 50-char cap can stay as an *additional* rule inside `isValidIdValue` since IDs really are short by convention.

---

### Finding F9: TOCTOU races on `cache` and `diverseValueCache` between containsKey/get/remove

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1125-1126, 1366-1369`
**Category:** Concurrency

**Code excerpt:**
```java
// fallbackToLLM, line 1125:
int cachedCountBefore = 0;
String cacheKey = buildCacheKey(parameterInfo);
if (diverseValueCache.containsKey(cacheKey)) {
    cachedCountBefore = diverseValueCache.get(cacheKey).size();   // could NPE
}

// clearInvalidCachedValues, line 1366-1369:
if (cache.containsKey(cacheKey)) {
    CachedValue cachedValue = cache.get(cacheKey);   // could be null
    if (cachedValue != null && !isValidValueForParameter(cachedValue.value, parameterInfo)) {
        cache.remove(cacheKey);
    }
}
```

**What is wrong:** `Collections.synchronizedMap(...)` (the wrapper used by `boundedLruMap`) synchronizes individual method calls, not compound operations. Between `containsKey(k)` returning true and `get(k)` returning a value, another thread can `remove(k)` (or LRU eviction can fire). Line 1126 then calls `.size()` on a returned-null reference — NPE.

The `diverseValueCache.compute` paths (lines 1345, 1377) ARE atomic because `Collections.synchronizedMap` overrides `compute` (JDK 8+ contract). So the rotation logic in `getNextDiverseValue` is fine. But the inline containsKey-then-get pattern at the two sites above is not.

**Why it matters:** Under parallel scenario execution (which `MultiServiceTestCaseGenerator` does enable), this could throw a sporadic NPE. The code is non-deterministic and would only show up in stress runs.

**Suggested fix:** Replace the containsKey+get pattern with a single `getOrDefault` or a single `get` with explicit null-check, or call `compute` and read the size inside the lambda.

---

### Finding F10: `addParameterError` decodes URL keys but `getParameterErrors` does not — read/write asymmetry

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/InputFetchRegistry.java:208 (write), 246, 254 (read)`
**Category:** Logical / YAML

**Code excerpt:**
```java
public void addParameterError(String apiEndpoint, String parameterName, ParameterError error) {
    String key = decodeUrlForKey(apiEndpoint);                                  // decoded
    Map<String, List<ParameterError>> endpointErrors = parameterErrors.computeIfAbsent(key, ...);
    ...
}

public List<ParameterError> getParameterErrors(String apiEndpoint, String parameterName) {
    return parameterErrors.getOrDefault(apiEndpoint, new HashMap<>())              // raw input
            .getOrDefault(parameterName, new ArrayList<>());
}

public Map<String, List<ParameterError>> getParameterErrorsForEndpoint(String apiEndpoint) {
    return parameterErrors.getOrDefault(apiEndpoint, new HashMap<>());              // raw input
}
```

**What is wrong:** `addParameterError` normalizes the key via `decodeUrlForKey` (so `/foo/%25` and `/foo/%` collapse to one bucket — Bug audit Finding #40). The two read methods do *not* apply the same normalization. So a caller asking `getParameterErrors("/foo/%25", ...)` will fail to find errors that were stored under decoded `"/foo/%"`. The same bug Finding #40 was meant to fix on the write side is reintroduced on the read side.

Additionally, `URLDecoder.decode(..., "UTF-8")` (line 821) applies `+ → space` decoding which is correct for query-strings, not paths. So `/foo+bar` becomes `/foo bar` in the storage key, but the literal `/foo+bar` lookup will not find it.

**Why it matters:** The error-context-aware LLM generation feature relies on `getErrorContextForParameter` finding stored errors. The asymmetry undermines that.

**Suggested fix:** Apply `decodeUrlForKey` in `getParameterErrors`, `getParameterErrorsForEndpoint`, and `getErrorContextForParameter` too. Or use a path-segment-aware decoder rather than `URLDecoder` (which is for query strings).

---

### Finding F11: Each `SmartInputFetcher` instance registers an unbounded shutdown hook — last-writer-wins on registry flush

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:157-158`
**Category:** Resource / Concurrency

**Code excerpt:**
```java
Runtime.getRuntime().addShutdownHook(new Thread(this::flushIfDirty,
        "smart-fetch-registry-flush"));
```

**What is wrong:** Every `SmartInputFetcher` instance registers a shutdown hook. There's no de-registration — instances cannot be GC'd while their hook references them. If multiple fetchers are constructed in one JVM (e.g. when `SmartLLMParameterGenerator` is used independently of `MultiServiceTestCaseGenerator`), all of them try to write the same registry path on JVM shutdown. The `saveToFile` is `synchronized` and uses atomic temp-rename, so the file won't be corrupted — but the *last* writer wins, and earlier writers' data is silently lost.

In a production-like test suite that creates fetchers as part of test setup/teardown (e.g. one per test class), the shutdown hook list grows unboundedly with every `SmartInputFetcher.<init>`.

**Why it matters:** Resource leak in long-running test farms; data loss on shutdown when fetchers were learning concurrent disjoint information.

**Suggested fix:** Either move the shutdown hook to a class-level static singleton that holds a registry of all live fetchers (and merges their dirty state), or decouple the dirty-flushing from the per-instance lifecycle entirely (have `MultiServiceTestCaseGenerator` flush at end-of-suite).

---

### Finding F12: `LLMService.getInstance(...)` is a first-init-wins singleton; second caller's properties are silently discarded

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/llm/LLMService.java:94-122` (cited from imports in the smart-fetch package)
**Category:** Coupling / Config

**Code excerpt:**
```java
public static synchronized LLMService getInstance(LLMConfig config) {
    if (instance == null) {
        instance = new LLMService(config);
    }
    return instance;
}
```

**What is wrong:** `SmartInputFetcher` constructor calls `LLMService.getInstance(loadLLMProperties())` with its own filtered set of properties (see F2). If a different code path elsewhere (e.g., `LLMParameterGenerator`) already created the singleton with a different config (different model, different timeout, different rate-limit settings), the SmartInputFetcher silently uses *that* config and ignores its own. There is no check / warning that the configs don't match.

**Why it matters:** Switching the model under test (e.g. local-Ollama → DeepSeek) by setting `-Dllm.model.type=local` works only if `LLMService.getInstance` was *not* yet called by another generator path. Tests are sensitive to construction order.

**Suggested fix:** Either (a) make `LLMService` take config per-call (`generateText(config, prompt)`), (b) detect config mismatch on second call and warn, or (c) document that the first caller wins and have a single early-init site.

---

### Finding F13: JWT prefix logged at debug level — credential leak

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartFetchAuthManager.java:141, 148`
**Category:** Security

**Code excerpt:**
```java
log.info("✅ Admin login successful, JWT token obtained (expires: {})", tokenExpiry);
log.debug("JWT token: {}...", jwtToken.substring(0, Math.min(20, jwtToken.length())));   // <— line 141
...
log.error("❌ Admin login failed with HTTP {}: {}", responseCode, responseBody);          // <— line 148
```

**What is wrong:** Two leaks:
1. Line 141 logs the first 20 chars of the JWT. JWTs use base64url; the header (typically `eyJhbGciOiJI…` for HS256) is fixed, but the first ~20 chars of the *payload* contain user-identifying claims (sub, iss, sometimes email). For systems with weak JWT signing keys, this prefix may reduce the brute-force search space.
2. Line 148 logs the entire login-failed response body. If a server uses an error format that echoes the password ("invalid password 'foo'") or returns a sensitive recovery hint, it ends up in the error log.

**Why it matters:** Test infrastructure logs are typically less protected than production logs. A leaked JWT prefix or password echo in CI artifacts is a real exposure.

**Suggested fix:** Drop the JWT debug log entirely (the expiry timestamp at line 140 is enough to confirm acquisition). Truncate `responseBody` to 200 chars and redact any value following `password` substring.

---

### Finding F14: `readResponse` uses platform default charset and concatenates lines without newlines

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:3290-3299`
**Category:** Logical / Resource

**Code excerpt:**
```java
private String readResponse(HttpURLConnection conn) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }
}
```

**What is wrong:** Two bugs in nine lines:
1. `new InputStreamReader(stream)` (no charset) uses the platform default encoding. On a Linux CI box that's UTF-8, but on a Windows dev machine it's CP1252, and the resulting bytes-to-chars decoding will mangle non-ASCII responses (e.g., Chinese station names — TrainTicket is a Chinese-translated demo service that *does* have UTF-8 Mandarin content). Should be `StandardCharsets.UTF_8`.
2. `response.append(line)` strips the line separator. JSON is whitespace-insensitive so this happens to work, but if the response is a multi-line JSONP wrapper or contains embedded newlines inside a string, those newlines are silently lost.
3. Error responses (4xx/5xx with a JSON body) are ignored — `getInputStream` throws on 4xx, and `readResponse` is only invoked after the success-code check, so the error body is discarded. The auth manager (`SmartFetchAuthManager.readResponse`) DOES handle this correctly using `getErrorStream()` fallback. Inconsistent.

**Why it matters:** UTF-8 mangling causes the smart fetch to return broken station names, which then fail downstream validation and trigger fallback. The bug is silent and locale-dependent.

**Suggested fix:** Use `new InputStreamReader(stream, StandardCharsets.UTF_8)`. Read the entire stream as one buffer rather than line-by-line. Mirror the auth manager's error-stream fallback so 4xx bodies aren't lost.

---

### Finding F15: `cleanNumberValue` does not clamp to schema min/max, breaking parity with `clampToNumberBounds`

**Severity:** Medium
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:2776-2798`
**Category:** Logical

**Code excerpt:**
```java
private String cleanNumberValue(String value, ParameterInfo parameterInfo) {
    if (value == null || value.trim().isEmpty()) {
        return numberFallbackForSchema(parameterInfo);
    }
    try {
        String cleanValue = value.replaceAll("[^0-9.-]", "");
        if (cleanValue.isEmpty() || cleanValue.equals("-") || cleanValue.equals(".")) {
            return numberFallbackForSchema(parameterInfo);
        }
        double doubleValue = Double.parseDouble(cleanValue);
        return String.valueOf(doubleValue);          // <— no clamp
    } catch (NumberFormatException e) {
        return numberFallbackForSchema(parameterInfo);
    }
}
```

**What is wrong:** When the LLM emits e.g. `"99999999.0"` for a parameter declared with `maximum: 1000`, this method parses the number successfully and returns `"9.9999999E7"`. The companion algorithmic generator uses `clampToNumberBounds` (line 1823) to respect the bounds; the cleaner does not. The same pattern exists for `cleanIntegerValue` (line 2723) — `parseIntegerLiteral` returns the parsed long without clamping.

Worse: the regex `[^0-9.-]` does not validate sane formatting. `"-1.2.3"` has multiple dots but survives the strip and parses as `NumberFormatException` → falls into fallback. `"1-2"` becomes `"1-2"` → also throws. So strange inputs *do* trigger the fallback, but legitimate-but-out-of-bounds values pass through.

**Why it matters:** Cleaned values that exceed schema bounds will be rejected downstream by `isValidValueForParameter` (which DOES check bounds via `clampToIntegerBounds`/`clampToNumberBounds`... actually no, those are only used when synthesizing a fallback, not when validating. So the bound is leaked into the test as an invalid value).

**Suggested fix:** After parsing, apply `clampToNumberBounds(doubleValue, parameterInfo)` for `cleanNumberValue` and `clampToIntegerBounds(parsed, parameterInfo)` for `cleanIntegerValue`.

---

### Finding F16: `isUUIDValue` fast-path returns false for length != 36 — misses lower-case versions of common variants

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:2587-2592`
**Category:** Logical

**Code excerpt:**
```java
private boolean isUUIDValue(String value) {
    if (value == null || value.length() != 36) {
        return false;
    }
    return value.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
}
```

**What is wrong:** `length() != 36` rejects:
- braced UUIDs `"{47e2a130-0000-0000-0000-000000000000}"` (38 chars)
- urn-prefixed `"urn:uuid:47e2a130-..."` (45 chars)
- compact (no-dash) `"47e2a13000000000000000000000"` (32 chars)

UUIDs from real systems sometimes appear in these forms. The strict equality is fine if you want to reject them, but the function name `isUUIDValue` suggests it identifies UUID-shaped values broadly.

**Why it matters:** `isNonsensicalValue` uses this to reject UUID-shaped strings on non-ID parameters. A station name like `"city-47e2a130-0000-0000-0000-000000000000"` from a poorly-cleaned response would pass through.

**Suggested fix:** Either accept the variant forms and normalize, or rename to `isStandard36CharUUID` to make the contract explicit.

---

### Finding F17: `getRequiredValueCount` ignores its `parameterInfo` argument and reads from `System.getProperty` instead of `SmartInputFetchConfig`

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1959-1968`
**Category:** API / Config

**Code excerpt:**
```java
private int getRequiredValueCount(ParameterInfo parameterInfo) {
    String configured = System.getProperty("smart.input.fetch.diverse.target.count");
    if (configured != null) {
        try {
            int n = Integer.parseInt(configured.trim());
            if (n > 0) return n;
        } catch (NumberFormatException ignored) { }
    }
    return 10;
}
```

**What is wrong:** Two issues: (a) `parameterInfo` is unused — the signature lies. (b) The configuration knob is read straight from `System.getProperty` rather than from `SmartInputFetchConfig`. Every other knob in this class flows through `SmartInputFetchConfig`. This single property bypasses the config object, meaning it's not visible in `config.toString()` and not reflected in `SmartInputFetchConfig.fromProperties()`.

**Why it matters:** A user setting the property through a properties-file-based test runner that doesn't push to `System.getProperties` will see no effect. Surprising and inconsistent.

**Suggested fix:** Either remove the unused parameter or branch on parameter type (e.g., closed-domain booleans need only 2 values). Move the property to `SmartInputFetchConfig` like everything else.

---

### Finding F18: `ServicePattern` class is fully dead code — loaded into the registry, persisted, but never queried

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/ServicePattern.java`, `InputFetchRegistry.java:307-326`
**Category:** Dead code

**What is wrong:** `ServicePattern.matches()` has zero callers in the smart-fetch package or anywhere else in production (`grep -rn "\\.matches(" /smart/` shows only regex/string matches that are not on `ServicePattern` instances). The `InputFetchRegistry` constructor adds five default patterns (station, user, train, route, order) on lines 307-326; the YAML round-trip persists them; but nothing ever calls `pattern.matches(parameterName)`. The patterns are dead state.

The comment at SmartInputFetcher.java:391 says "Pattern discovery was a no-op stub that always returned an empty list and emitted misleading warning logs (Bug audit Finding #32 + Reviewer C12). It is now removed; LLM-based discovery is the only supported discovery path." — but the *data* of `ServicePattern` was not removed. The five default patterns still get bootstrapped into every fresh registry.

**Why it matters:** The persisted registry YAML carries useless `servicePatterns:` entries. The `ServicePattern` class is shipped maintenance burden. New callers may see `getServicePatterns()` and assume it does something.

**Suggested fix:** Delete `ServicePattern.java`, `InputFetchRegistry.servicePatterns`, the bootstrap code in `initializeDefaults`, and the `ServicePatternData` YAML DTO. Migration: drop the `servicePatterns:` block from existing YAMLs (Jackson would silently ignore unknown fields if the top-level model uses `@JsonIgnoreProperties(ignoreUnknown = true)` — verify or add).

---

### Finding F19: `generateAlgorithmicStringValue` returns hash-derived gibberish for unit-bearing parameters

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1834-1865`
**Category:** Logical / Quality

**Code excerpt:**
```java
private String generateAlgorithmicStringValue(String paramName) {
    String cleanName = paramName.replaceAll("[^a-zA-Z0-9]", "");
    int hashValue = Math.abs(paramName.hashCode());
    StringBuilder result = new StringBuilder();
    if (cleanName.length() > 0) {
        int prefixLength = Math.min(cleanName.length(), Math.max(1, cleanName.length() / 3));
        result.append(cleanName.substring(0, prefixLength).toLowerCase());
    }
    result.append(hashValue % 10000);
    String lowerName = paramName.toLowerCase();
    if (lowerName.contains("distance") || lowerName.contains("length") || lowerName.contains("size")) {
        char unitChar = (char)('a' + (hashValue % 26));
        result.append(unitChar);
    }
    return result.toString();
}
```

**What is wrong:** For a string-typed parameter named `distance`, this can return `"d1234x"`. The trailing single letter (`'a' + hash%26`) is meant to be a "unit-like suffix", but a single random letter is not a unit. For most LLM-failed cases this becomes the value that ends up in the test: not realistic enough to mock as data, not fault-shaped enough to be a deliberate fault probe.

The whole function is the absolute-last-resort fallback (when LLM fails AND minimal-LLM fails AND algorithmic generation is the only path), so its quality may not matter much in steady state. But the comment claim of "no hardcoded strings" is a bit dishonest — `'a'`, `'z'`, ranges over 26 lowercase Latin letters are arguably more hardcoded than a clean default.

**Why it matters:** Quality artifact more than a correctness bug. For tests that DO take this path (logs would show "LLM failed → algorithmic fallback"), the resulting test value is essentially noise.

**Suggested fix:** Drop the unit-char suffix; just emit `cleanName + hash`. Or fall back to the schema's `example` if available before going algorithmic.

---

### Finding F20: `addMapping` does not deduplicate against existing mappings — registry can accumulate duplicates across runs

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/InputFetchRegistry.java:156-173`
**Category:** Logical / YAML

**Code excerpt:**
```java
public void addMapping(String consumerApiKey, String parameterName, ApiMapping mapping) {
    if (mapping == null) return;
    String method = mapping.getMethod();
    if (method != null && !"GET".equalsIgnoreCase(method)) { ... return; }
    if (consumerApiKey != null && !consumerApiKey.isEmpty()) {
        mapping.setConsumerApiKey(consumerApiKey);
    }
    parameterMappings.computeIfAbsent(parameterName, k -> new ArrayList<>()).add(mapping);
    log.debug(...);
}
```

**What is wrong:** No `.contains()` check before `.add()`. If the discovery path runs multiple times for the same `(consumerApiKey, paramName, endpoint, service)` (which shouldn't happen often per the gating in `fetchFromSmartSource:334`, but can if mappings are rebuilt e.g. after a registry reload), duplicate entries accumulate. `ApiMapping.equals` (line 146) checks `endpoint+method+service+extractPath`, so the dedup *could* be done — but isn't.

**Why it matters:** Registry growth over many runs. With 49 977 lines already, the file isn't tiny.

**Suggested fix:** Replace `.add(mapping)` with `if (!list.contains(mapping)) list.add(mapping);` — but note this requires `ApiMapping.equals` to also include `consumerApiKey` (currently it doesn't, so two mappings differing only in scope would compare equal).

---

### Finding F21: `InputFetchRegistry.removeMapping` does not honor `consumerApiKey` scope

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/InputFetchRegistry.java:178-188`
**Category:** API

**Code excerpt:**
```java
public boolean removeMapping(String parameterName, ApiMapping mapping) {
    List<ApiMapping> mappings = parameterMappings.get(parameterName);
    if (mappings != null) {
        boolean removed = mappings.remove(mapping);
        ...
    }
    return false;
}
```

**What is wrong:** The companion `addMapping(consumerApiKey, parameterName, mapping)` was retrofitted with consumer scoping. `removeMapping` was not. Since `ApiMapping.equals` doesn't include `consumerApiKey`, calling `removeMapping(...)` will remove the *first matching* mapping regardless of which consumer scope it was registered under. There's no caller of `removeMapping` in production code (zero hits in `grep`), so this is academic — but if someone tries to use it for cleanup, they'd be in for a surprise.

**Why it matters:** Latent API hazard. Consider just deleting `removeMapping` since nothing uses it.

**Suggested fix:** Either delete or add the `consumerApiKey` parameter and filter accordingly.

---

### Finding F22: Multiple unused imports in SmartInputFetcher.java

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:21-34`
**Category:** Dead code

**What is wrong:** Imports declared but never used (verified by grep):
- Line 21 AND 26 — `java.util.stream.Collectors` imported twice (duplicate).
- Line 23 — `java.util.concurrent.ConcurrentHashMap` (mentioned only in a Javadoc comment).
- Line 24 — `java.util.concurrent.TimeUnit` (zero references).
- Line 25 — top-level `java.util.regex.Pattern` (every actual use is fully qualified).
- Line 28 — `okhttp3.MediaType`.
- Line 29 — `okhttp3.OkHttpClient`.
- Line 30 — `okhttp3.Request`.
- Line 31 — `okhttp3.RequestBody`.
- Line 32 — `okhttp3.Response`.
- Line 33 — `org.json.JSONArray` (only used as `org.json.JSONArray` fully-qualified at line 667-668).
- Line 34 — `org.json.JSONObject` (only used fully-qualified at lines 656, 700, 707).

**Why it matters:** Cosmetic only — the okhttp3 imports suggest a planned migration to OkHttp that never happened (the file uses `HttpURLConnection` everywhere). New maintainers may waste time looking for okhttp call sites.

**Suggested fix:** Run an IDE "organize imports" pass. Decide whether to migrate to OkHttp or commit to HttpURLConnection.

---

### Finding F23: `baseUrl` is not normalized for trailing slash; can produce `//path` URLs

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:118-120, 536`
**Category:** Logical

**Code excerpt:**
```java
public SmartInputFetcher(SmartInputFetchConfig config, String baseUrl) {
    this.config = config;
    this.baseUrl = baseUrl;     // no normalization
    ...
}
...
String url = baseUrl + resolved;   // line 536
```

**What is wrong:** `SmartFetchAuthManager` strips the trailing slash from `baseUrl` (line 52 of that file). `SmartInputFetcher` does not. If a user passes `http://localhost:8080/`, the resulting URL becomes `http://localhost:8080//api/v1/...`. Most servers treat this as the same path, but RFC 3986 considers them distinct, and reverse proxies (nginx) may issue a redirect.

**Why it matters:** Cosmetic at best, redirect-loop at worst.

**Suggested fix:** Normalize at constructor time: `this.baseUrl = baseUrl != null && baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;` — same code as `SmartFetchAuthManager` already has.

---

### Finding F24: Static `REGISTRY_CACHE` is unbounded and shared mutable state across tests

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartLLMParameterGenerator.java:211-241`
**Category:** Resource / Testability

**Code excerpt:**
```java
private static final java.util.concurrent.ConcurrentMap<String, CachedRegistry> REGISTRY_CACHE =
        new java.util.concurrent.ConcurrentHashMap<>();
...
static InputFetchRegistry sharedRegistry(String registryPath) {
    if (registryPath == null || registryPath.isEmpty()) return null;
    java.io.File registryFile = new java.io.File(registryPath);
    if (!registryFile.exists()) return null;
    long mtime = registryFile.lastModified();
    CachedRegistry cached = REGISTRY_CACHE.get(registryPath);
    if (cached != null && cached.lastModified == mtime) {
        return cached.registry;
    }
    ...
}
```

**What is wrong:** The cache is process-wide static and grows unboundedly with the number of distinct `registryPath` values seen. There's no eviction. More problematic: the *cached `InputFetchRegistry` instance* is a mutable object that may be shared across multiple `SmartLLMParameterGenerator` instances and (transitively) across the SmartInputFetcher's separately-loaded copy. If one mutates `parameterMappings` while another iterates, you get a `ConcurrentModificationException`. The class is not thread-safe; the cache makes that latent hazard real.

For tests: tests that mutate the registry cannot rely on a clean slate without explicitly clearing `REGISTRY_CACHE`.

**Why it matters:** Memory leak for many-path scenarios; cross-test contamination; concurrent mutation hazard if used in parallel.

**Suggested fix:** Either (a) bound the cache (e.g., LRU with capacity 64), (b) clone-on-read so mutations don't propagate, or (c) document explicit cache-reset for tests.

---

### Finding F25: `loadOpenAPISpec` has an unused-return error path that hides config errors

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1241-1262`
**Category:** Error-handling

**What is wrong:** The OpenAPI spec is loaded inside a try/catch. On failure, `log.error(...)` runs but the SmartInputFetcher continues with `openAPIDiscovery.isLoaded() == false`. Subsequently, `inferEndpointForService` (line 3743) returns null when discovery is unavailable, and the `discoverByLLM` flow drops the candidate. Net effect: **all** LLM-discovery silently fails when the OpenAPI spec is missing, and the only signal is one early ERROR log line plus subsequent "OpenAPI discovery unavailable" debug entries per param.

**Why it matters:** A misconfigured OpenAPI path turns smart-fetch into pure-LLM mode without operator-visible alarm.

**Suggested fix:** When the configured `openApiSpecPath` is non-empty but the file is missing or unparseable, throw a startup exception or escalate the log level to FATAL — don't silently degrade.

---

### Finding F26: `flow.md` claims "Trace endpoints are session-scoped" but `fetchFromApiMapping` calls `cacheValue` for them

**Severity:** Low
**Location:** `flow.md:825` vs. `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:308-316`
**Category:** Flow.md-discrepancy

**Code excerpt (flow.md line 825):**
> Trace endpoints are session-scoped: Trace-observed `ApiMapping` objects are never added to `registry.addMapping()` and are never persisted to YAML.

**Code excerpt (SmartInputFetcher.java line 308-316):**
```java
ApiMapping traceMapping = new ApiMapping(endpoint, "trace-observed", "DIRECT_EXTRACTION");
traceMapping.setPriority(10);
try {
    String value = fetchFromApiMapping(traceMapping, parameterInfo);
    if (value != null && !value.trim().isEmpty() && isValidValueForParameter(value, parameterInfo)) {
        cacheValue(parameterInfo, value);    // <— caches the value (correctly), but also feeds diverseValueCache
        ...
    }
}
```

**What is wrong:** The flow.md is technically correct that trace-mappings aren't persisted to the registry YAML — but `cacheValue` (line 1275) feeds the *value* into both `cache` and `diverseValueCache` (which IS the in-memory pool used across scenarios). So values fetched from a *trace-observed-this-scenario* endpoint are reused in the next scenario via the diverse-cache rotation, even though the producer is tied to a specific scenario's workflow. That's exactly what `flow.md`'s `resetValueRotation()` claim was trying to prevent.

**Why it matters:** Cross-scenario leakage of trace-observed values. The `resetValueRotation` only resets the cursor, not the contents.

**Suggested fix:** Either also clear `diverseValueCache` in `resetValueRotation()`, or bypass the diverse cache for trace-observed fetches.

---

### Finding F27: Default admin password `222222` is a hardcoded shipped credential

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetchConfig.java:155`
**Category:** Security

**Code excerpt:**
```java
config.authAdminPassword = properties.getOrDefault("auth.admin.password", "222222");
```

**What is wrong:** A real password literal in the source repo. While obviously a TrainTicket-demo default, the config layer should not have a default password at all — operators should be forced to set it explicitly. Forgotten overrides will silently use this in production-like deployments.

**Why it matters:** Source-controlled credentials are flagged by many secret-scanners. Even a "demo" credential is an anti-pattern.

**Suggested fix:** Default to empty string and have `SmartFetchAuthManager.isConfigured()` treat empty as "auth disabled". Document the test demo's expected credentials in the properties file, not in code.

---

### Finding F28: `clampToInteger/NumberBounds` is only used by the algorithmic-fallback path, not by the value-validator

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:1814-1829, 2494-2536`
**Category:** Logical

**What is wrong:** `clampToIntegerBounds`/`clampToNumberBounds` are invoked only in `generateAlgorithmicMinimalValue` (line 1798, 1801). `isValidValueForParameter` does not check the schema's `minimum`/`maximum` against the candidate value. So an LLM-emitted value of `-99999` for a parameter with `minimum: 0` passes validation, gets cached as a "valid" value, and is returned to the test runner — which then sends a 400 from the SUT. The success-rate signal absorbs the failure but the candidate stays in the diverse cache.

**Why it matters:** Schema constraints `minimum`/`maximum` are documented contract terms; the fetcher is supposed to honor them as part of its "realistic value" promise.

**Suggested fix:** Add a numeric-bound check inside `isValidValueForParameter` for `integer`/`number` schema types. Reject values outside `[minimum, maximum]` rather than silently clamp (clamping in the validator would mask LLM errors).

---

### Finding F29: `getOpenAPISchemaType` falls through to LLM inference when the parameter has no declared type — adds an LLM call to the hot path

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:2916-2940` (inferSchemaTypeFromParameterName → askLLMForSchemaTypeInference)
**Category:** Performance / LLM

**What is wrong:** When `ParameterInfo.getSchemaType()` and `getType()` are both null/empty (which can happen for hand-built `ParameterInfo` objects), `getOpenAPISchemaType` calls `inferSchemaTypeFromParameterName` which makes an LLM call (lines 2945-2971) for every fetch, returning `"string"` as a fallback if the LLM doesn't respond. This is one extra LLM call per parameter PER fetch when types are missing.

For most call sites (the ones routed via `MultiServiceTestCaseGenerator.createParameterInfoWithContext`) the type IS set, so this branch rarely fires — but the `SmartLLMParameterGenerator.createParameterInfo()` path (line 297) calls `pinfo.setType(getParameterType())` from the parent generator which may return null, putting that path on the LLM-per-fetch hot loop.

**Why it matters:** LLM calls are slow (10-100 ms with local models, more with remote APIs). If `pinfo.getType()` is null, every fetch costs an extra LLM round-trip just to discover the type.

**Suggested fix:** Make `inferSchemaTypeFromParameterName` deterministic — strip the LLM call entirely. The existing rules in `getOpenAPISchemaType` (lines 2873-2884) cover 99 % of cases by name-substring; the remaining 1 % can default to "string" without an LLM round-trip.

---

### Finding F30: `findSemanticMatch` and `isRelevantField` make per-field LLM calls for every JSON field they don't deterministically classify

**Severity:** Low
**Location:** `src/main/java/es/us/isa/restest/inputs/smart/SmartInputFetcher.java:939-970, 2393-2415`
**Category:** Performance / LLM

**What is wrong:** `extractValuesFromJsonNode` (line 2348) iterates every key of every JSON object in a response and calls `isRelevantField` for each. While the "isRelevantField" comment claims (line 2389-2391) that the LLM is now only consulted as a last resort, an API response with 30 fields and no deterministic matches still triggers up to 30 LLM calls per fetch (line 2414). For trace-observed endpoints with rich responses, this is the dominant performance cost.

The companion `findSemanticMatch` (line 939) makes one LLM call per fetch when the deterministic substring matches fail. This is at least bounded.

**Why it matters:** A 2 KB JSON response could trigger dozens of LLM calls. Rate-limited APIs (Gemini, OpenAI) will throttle.

**Suggested fix:** Either (a) batch the field-relevance question into one LLM call ("which of these 30 fields is most relevant?"), or (b) use only deterministic field-name matching and skip the LLM fallback entirely.

---

## Cross-cutting observations

**Brittle parameter-name heuristics layered on top of careful boundary-aware checks.** The file went through real effort to introduce `isIdLikeParamName` (boundary-aware) so that English words ending in `id` like `paid`, `valid`, `humid` no longer fire on the ID path. That work is undermined by half a dozen sites that still do `paramName.contains("route")`, `contains("price")`, `contains("rate")`, `contains("number")`, `contains("distance")`, `contains("station")`. They directly contradict the spirit of the boundary fix — and several of them affect *validation* (so the false positive rejects legitimate values).

**Configuration plumbing has two layers that don't agree.** `SmartInputFetchConfig` is the documented config object (44 fields, 88 getter/setters). Yet `getRequiredValueCount` reads `System.getProperty` directly (Finding F17), `loadLLMProperties` uses an explicit allowlist that drops important keys (Finding F2), and `CacheConfig` (a separate class loaded from the YAML) is read after caches are constructed and therefore ignored (Finding F1). Three different ways to "configure" the system with three different bugs.

**Persistence layer treats user data and code data the same.** `ServicePattern` (Finding F18) and the consumer-scoping fix (Finding F3) both reveal that the YAML is hand-rolled DTO-mapped via Jackson without a versioning story. New fields don't get backfilled on load; removed fields aren't migrated; defaults are added on every fresh registry but not on existing ones.

**LLM-as-a-validator instead of LLM-as-a-data-source.** Two methods (`isRelevantField`, `inferSchemaTypeFromParameterName`) call the LLM to make trivial decisions that could be deterministic. Each is wrapped in a "fall back to substring match if LLM fails" cushion, so the fallback IS the implicit deterministic answer. Just commit to the deterministic answer.

**The static caches in `SmartLLMParameterGenerator` do not match the per-instance loaded registry in `SmartInputFetcher`.** Two pathways both load the same YAML, with different mtimes-relative loads, and there is no synchronization between them. A mutation in one (e.g., `addMapping` from discovery) is invisible to the other until file-flush.

## flow.md vs code discrepancies

1. **`flow.md:825`**: Claims trace-observed endpoints' values are session-scoped. But `cacheValue(parameterInfo, value)` in `SmartInputFetcher.java:313` feeds them into the cross-scenario `diverseValueCache`. Discrepancy. (Finding F26.)
2. **`flow.md:826`** (cache key parity): Claims `buildCacheKey` keys on `name + type + location + format + enum + minimum/maximum + minLength/maxLength + regex`. The implementation does include all of those (line 3273-3287), but the doc omits that the SmartLLMParameterGenerator path constructs ParameterInfo without setting those constraints (since `buildParameterInfo` in the parent class only fills a subset). So in practice, the cache-key-parity benefit is partial.
3. **`flow.md:798`** (Priority 0 chain): Says trace endpoints are tried FIRST, then registry mappings. The code does exactly this. No discrepancy.
4. **`flow.md:824`** (JSONPath fully retired): Code matches — `extractPath` is treated as a static `"DIRECT_EXTRACTION"` marker. Confirmed.
5. **`flow.md:1099`** (`cleanIntegerValue` uses `Long.parseLong` with `BigInteger` fallback): Confirmed at line 2762-2770. But the doc doesn't mention that no schema-bound clamping is applied (Finding F15/F28).

## What looks good

- The atomic temp-rename in `InputFetchRegistry.saveToFile` (line 68-93) is well-implemented. The `AtomicMoveNotSupportedException` fallback for cross-device tmpdirs is a nice production-thinking detail.
- `ApiMapping.calculateScore` correctly uses `ChronoUnit.DAYS.between(...)` (line 112) — the comment at line 105-108 explicitly mentions the previous bug, and the fix is right.
- `boundedLruMap` uses access-order LRU correctly (line 86-92), and the eviction is bounded.
- The closed-domain short-circuit in `generateSemanticallySimilarValues` (lines 2014-2025) is a real correctness fix for booleans/enums; the boolean-synonym hallucination it prevents is a real LLM failure mode.
- The `selectEndpointWithLLMRetry` distinction between transient failures (retry) and deterministic `NO_GOOD_MATCH` (don't retry, try forced once) (lines 3788-3813) is the correct retry policy.
- `SmartFetchAuthManager` correctly handles 401/403 invalidation (lines 562-566 of SmartInputFetcher) and uses `synchronized` getValidToken to prevent duplicate logins.
- `extractTokenAtPath` (SmartFetchAuthManager:162-174) is a correct, safe replacement for the previous TrainTicket-specific hardcoded `data.token` extraction.
- `truncateForLog` (line 830) is correctly used in error logging.

## Recommendation

**Iterate.** The code is mostly correct and the recent fixes are real, but several near-term tweaks would substantially raise the quality bar:

Top priority (high-impact, low-cost):
- Fix Finding F1 (CacheConfig.maxEntries — move loadRegistry before cache construction, ~3 lines).
- Fix Finding F2 (LLM property allowlist — switch to prefix filter, ~5 lines).
- Fix Finding F4-F6 (substring heuristics — replace with `isIdLikeParamName` and `isNumericSchemaType`, ~20 lines).
- Fix Finding F13 (drop JWT debug log, redact responseBody log, ~3 lines).
- Fix Finding F14 (UTF-8 charset, ~2 lines).

Medium priority (substantive but larger):
- Address Finding F3 (consumer-scoping is paper-only): write a registry-migration script that backfills `consumerApiKey` from the OAS, OR restart with a clean registry; AND fix `SmartLLMParameterGenerator.createParameterInfo` to set apiName.
- Address Finding F15/F28 (clamp values to schema bounds in cleaner AND validator).
- Address Finding F18 (delete dead `ServicePattern` machinery).
- Decide what to do about the 4200-line `SmartInputFetcher` — split off the LLM-prompt builders into a separate class, the value cleaners (~600 lines: `cleanIntegerValue`, `cleanNumberValue`, `formatAsArrayValue`, etc.) into another, and the validation methods into a third.

Lower priority:
- Findings F9-F12 are real but low-frequency; address as part of a concurrency/resource cleanup pass.
- Findings F16, F19, F22, F23, F25-F27 are hygiene.

Do not ship-as-is for new operators, because the silent config drops (F1, F2) make the system look like it works while ignoring half the user's settings — that's the worst possible mode for a research-oriented test framework where reproducibility depends on every knob being honored.
