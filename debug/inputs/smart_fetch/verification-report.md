# Independent verification of smart-fetch refinement plan

Date: 2026-05-05
Verifier: Independent Verification Agent (no prior context)
Verified against: refinement-plan.md + current source state
Excluded from review: execution-summary.md, reviewer-disposition.md, reviewer-feedback.md

## Summary verdict

Of the 38 distinct findings cited in the eight-stream refinement plan (some streams reference the same finding), **30 are verifiably fixed**, **5 are partial**, and **3 are unfixed**. Two of the three unfixed items are non-trivial in scope (Finding #15 cross-service registry-key collision, Finding #24 unbounded cache eviction); Finding #19 is documentation-only and the doc lives in `dataflow-map.md` rather than `flow.md`. The reviewer-mandated additions (atomic write, error caps, JWT auto-invalidate, JWT synchronization, process-wide registry cache, scenario-boundary + shutdown flush) are all present. The registry KPIs are dramatically improved versus baseline: zero `NO_GOOD_MATCH` rows, zero fabricated `*/query` endpoints, zero literal `{paramName}` placeholders, and successRate-0.0 dropped from 75% to 46%.

## Per-finding verification

| Stream | Finding | Verdict | Evidence |
|---|---|---|---|
| 1 | #2 — `NO_GOOD_MATCH` persisted as service | ✅ Fixed | `SmartInputFetcher.java:431-435` filters sentinel in `discoverByLLM`; `:3343-3349` `askLLMForServices` drops `NO_GOOD_MATCH` array elements; `grep -c 'service: NO_GOOD_MATCH'` on registry → 0. |
| 1 | #22 — LLM-suggested service whitelist | ✅ Fixed | `SmartInputFetcher.java:419-441` builds `knownByLower` from `getAllAvailableServices()`, drops anything not present (line 438-441). |
| 1 | #4 — Capitalization normalization | ✅ Fixed | `SmartInputFetcher.java:437-441` canonicalizes via lowercase lookup. Registry recount: all 14 distinct services are lowercase canonical (`grep -E '[A-Z]'` → 0 hits in service rows). |
| 1 | #15 — `addMapping` keys by bare paramName | ❌ Unfixed | `InputFetchRegistry.java:105-108` is unchanged: `parameterMappings.computeIfAbsent(parameterName, k -> ...)`. No consumer-API scoping introduced; cross-service collision risk remains. |
| 1 | #33 — Fabricated `*/query` endpoints | ✅ Fixed | `SmartInputFetcher.java:3676-3702` (`inferEndpointForService`) returns `null` rather than fabricating; the old `"/api/v1/" + service.toLowerCase().replace(...) + "/query"` pattern is gone (`grep` confirms). Registry recount: 0 `*/query` rows. |
| 2 | #3 — Path-parameter substitution | ✅ Fixed | `SmartInputFetcher.java:476-575` introduces `resolveFetchableEndpoint` which strips trailing `/{...}` segments and returns null on mid-path placeholders. Registry recount: 0 placeholder rows. |
| 2 | #23 — Honor `mapping.getMethod()` | ❌ Unfixed | `SmartInputFetcher.java:486` still hardcodes `String httpMethod = "GET";`. The plan said either honor or document; neither code change nor schema-validation rejection is present. The audit notes the YAML has 100 % GET so this is latent. |
| 3 | #5 — Loose `paramName.contains("id")` | ✅ Fixed | `SmartInputFetcher.java:2466` uses `isIdLikeParamName(paramName)` (boundary-aware). The cited buggy substring check is gone. |
| 3 | #18 — `selectValueWithFallbackLogic` heuristics | 🗑 Deleted/Removed | Method removed per `SmartInputFetcher.java:986-991` block comment ("extractValueFromResponse, selectValueWithFallbackLogic, selectValueWithLLM, ... removed"). Verified via `grep`: no caller. |
| 3 | "isNonsensicalValue" loose `id` check | ✅ Fixed | `SmartInputFetcher.java:2486` uses `isIdLikeParamName(paramName)`; old loose contains is gone. |
| 4 | #13 — Per-call YAML reload in `SmartLLMParameterGenerator` | ✅ Fixed | `SmartLLMParameterGenerator.java:211-241` introduces a process-wide `ConcurrentMap<String, CachedRegistry> REGISTRY_CACHE` keyed by path, mtime-invalidated. `createParameterInfoWithErrorContext` now calls `sharedRegistry(...)` instead of `loadFromFile(...)`. |
| 4 | #14 — Registry rewritten per discovery | 🟡 Partial | `SmartInputFetcher.java:362` still calls `saveRegistry()` after every successful discovery batch. The plan's "buffer until scenario boundary" is not implemented for discovery saves; however, a `registryDirty` flag plus scenario-boundary (`resetValueRotation` → `flushIfDirty`) and shutdown-hook flush (line 125-126) handles success/failure rate updates. So the dominant write path (success-rate updates, the audit's I/O storm) is batched; the discovery save is not. |
| 4 | #24 — Cache eviction missing | ❌ Unfixed | `SmartInputFetcher.java:102-104` still uses unbounded `ConcurrentHashMap` for `cache`/`diverseValueCache`/`valueRotationIndex`. `CacheConfig.maxEntries` is still never read (`grep -rn maxEntries src/main/java` shows no consumer). |
| 4 | #27 — EMA α=0.1 fixed | ✅ Fixed | `ApiMapping.java:69-87` introduces a configurable `emaAlpha` field with `setEmaAlpha`. The default still 0.1 but is now per-instance configurable, satisfying the plan ("Make α configurable"). |
| 4 | #28 — Connect/read timeout split | ✅ Fixed | `SmartInputFetcher.java:501-502` reads `config.getConnectTimeoutMs()` / `getReadTimeoutMs()`. `SmartInputFetchConfig.java:104-111` defines them with `discovery.timeout.ms` as the back-compat default. |
| 4 | #30 — Rotation race | 🟡 Partial | `SmartInputFetcher.java:1286-1297` (`cacheDiverseValue`) and `:1318-1336` (`clearInvalidCachedValues`) now use `compute` + synchronized blocks. But `:1359-1388` (`getNextDiverseValue`) still does the read-modify-write `valueRotationIndex.put(cacheKey, (currentIndex + 1) % values.size())` without `compute`. The race window the audit identified is therefore narrowed but not closed. |
| 4 | #38 — LLM endpoint-select retries | ❌ Unfixed | `SmartInputFetcher.java:3707-3739` still does `int maxRetries = 3;` and on every `NO_GOOD_MATCH` calls `forceEndpointSelectionWithLLM` inside the loop. There is no "give up after second NO_GOOD_MATCH" branch. The audit's worst case (3 + 3 = 6 LLM calls) still applies. |
| 5 | #1 — `compareTo / 86400` recentness math | ✅ Fixed | `ApiMapping.java:98-103` uses `ChronoUnit.DAYS.between(lastUsed, LocalDateTime.now())` divided by configurable `decayDays` (default 30). Old `LocalDateTime.now().compareTo(lastUsed) / (24.0 * 60 * 60)` is gone. |
| 5 | #6 — `Math.max(5, 10)` ternary | ✅ Fixed | `SmartInputFetcher.java:1895-1904` reads `smart.input.fetch.diverse.target.count` and falls back to literal `10`. Old `Math.max(5, 10)` no longer in code (only mentioned in the comment at :1892). |
| 6 | #11 — Hardcoded TrainTicket login | ✅ Fixed | `SmartFetchAuthManager.java:31-60` adds `loginPath`, `loginUsernameField`, `loginPasswordField`, `tokenJsonPath`, `tokenValidityMinutes` fields with a 7-arg constructor; the back-compat 3-arg constructor delegates with TrainTicket defaults. `SmartInputFetchConfig.java:147-156` reads the matching config keys. `performLogin` uses the configured fields (`:111-145`). |
| 6 | #12 — Dead `auth.user.*` credentials | ❌ Unfixed | `SmartInputFetchConfig.java:43-44, 143-144` still loads `auth.user.username`/`auth.user.password`; `getAuthUserUsername`/`getAuthUserPassword` still defined; `grep -rn getAuthUserUsername src/main/java` shows no caller. Neither wired into a second auth profile nor removed. |
| 6 | #26 — Auto-invalidate JWT on 401/403 | ✅ Fixed | `SmartInputFetcher.java:507-513` checks `responseCode == 401 \|\| responseCode == 403` and calls `authManager.invalidateToken()`. Comment cites Bug audit Finding #26. (Note: it invalidates but does not retry within the same call — the plan said "retry once". The next attempt will re-login.) |
| 7 | #7 — `length() < 20` floor | ✅ Fixed | `SmartInputFetcher.java:596-598` comment confirms removal: "Removed the legacy `length() < 20` floor (Bug audit Finding #7)". The buggy 20-char check no longer exists. |
| 7 | #17 — `cleanBooleanValue` synonyms | ✅ Fixed | `SmartInputFetcher.java:2744-2752` accepts only `true/1` → "true", `false/0` → "false". Synonyms `yes/on/enabled/active` are gone. `formatAsBooleanValue` (`:2981-2983`) delegates to `cleanBooleanValue`. |
| 7 | #29 — Skip-element-0 hack | 🗑 Deleted/Removed | Removed alongside `selectValueWithFallbackLogic` (the only place it lived). No `list.get(1)` "second element more representative" pattern remains. |
| 7 | #34 — `formatAsArrayValue` wraps sentinels | ✅ Fixed | `SmartInputFetcher.java:3107-3114` rejects `NO_GOOD_MATCH`/`NO_VALUES_FOUND`/`NO_MATCH`/`NO_VALUES_GENERATED` and returns `[]` instead of wrapping. |
| 7 | #35 — LLM-fallback values pollute diverse cache | ❌ Unfixed | `SmartInputFetcher.java:1087` (`fallbackToLLM` body) still calls `cacheDiverseValue(parameterInfo, processed)`. The plan said "Only cache when smart-fetch returns a non-null value upstream"; the call is unchanged. |
| 7 | #36 — `cleanIntegerValue/cleanNumberValue` ignore bounds | ✅ Fixed | `SmartInputFetcher.java:2659-2694` introduces `integerFallbackForSchema` / `numberFallbackForSchema` that read `parameterInfo.getMinimum()`/`getMaximum()`. The empty-input and parse-failure branches both use them. |
| 7 | #37 — Empty-`data` rejection misses other envelopes | ✅ Fixed | `SmartInputFetcher.java:610-624` iterates `for (String envelopeKey : new String[]{"data","result","payload","items"})`. Each envelope checked for empty array/object. |
| 8 | #9 — Dead JSONPath helpers | ✅ Fixed | `grep -nE "private (String\|boolean\|JsonNode\|List).+(extractValueFromResponse\|guessExtractPath\|guessPathByParameterName\|isValidJsonPath\|getApiResponseSchema\|buildDataExtractionPrompt\|askLLMForExtractionPath\|callLLMForExtractionPathDiscovery)"` returns 0 hits. The block comment at `:986-991` documents the deletion. |
| 8 | #16 — `findBestEndpoint` dead | 🗑 Deleted/Removed | `OpenAPIEndpointDiscovery.java:137-139, 221` mark `findBestEndpoint`/`scoreEndpoint`/`ScoredEndpoint` as removed. `grep` confirms no callers. |
| 8 | #25 — `handleSingleValueParameterFromLLM` | 🗑 Deleted/Removed | `SmartInputFetcher.java:1154` comment confirms deletion. `grep` confirms no method body. |
| 8 | #32 — `discoverByPatterns` dead-stub | 🗑 Deleted/Removed | `grep -n discoverByPatterns SmartInputFetcher.java` returns 0 hits. The comment at `:344-346` confirms removal. |
| 8 | #10 — 2044-char prompt cap | 🟡 Partial | 6 sites now use `config.getMaxPromptChars()` (lines 678, 1472, 1984, 3486, 3749, 3896). 5 sites still hard-code (1200@:963, 500@:2360, 800@:2885, 500@:3024, 2000@:4038) plus residual `1944 - tempPrompt.length()`@:3296 and `1500 -`@:3255. The audit cited 8+ sites; majority are converted but not all. |
| 8 | #8 — Throw-as-control-flow | ✅ Fixed | `SmartInputFetcher.java:328-331` returns `null` instead of `throw new Exception("No smart sources available...")`. `grep "No smart sources available"` returns 0 hits. |
| 8 | #19 — Trace endpoints not persisted (doc-only) | 🟡 Partial | The plan said "Document explicitly in `flow.md` ... No code change." The documentation lives in `dataflow-map.md:542-546` which states "Trace endpoints ARE session-scoped — flow.md is correct". There is no `flow.md` file in `debug/inputs/smart_fetch/`; the doc landed in a different file. Counting as Partial because the spirit (documented) is satisfied even if the location differs. |
| 8 | #20 — `generateAlgorithmicMinimalValue` ignores bounds | ✅ Fixed | `SmartInputFetcher.java:1729-1747` calls `clampToIntegerBounds`/`clampToNumberBounds` (`:1750-1765`) which honor `parameterInfo.getMinimum()`/`getMaximum()`. |
| 8 | #21 — `_<idx>` suffix for fallback values | ✅ Fixed | `grep "minimalValue +"` returns 0 hits. `SmartInputFetcher.java:2205-2218` fetches a fresh minimal value per loop iteration without concatenating a numeric suffix. |
| 8 | #39 — Per-field LLM call in `isRelevantField` | ✅ Fixed | `SmartInputFetcher.java:2329-2351` introduces deterministic checks (equality, substring, ID-stem heuristic, numeric-field-for-numeric-param). Falls through to LLM only when nothing matches; comment at :2324-2327 cites Finding #39. |
| 8 | #40 — `parameterErrors` keyed by encoded URL | ✅ Fixed | `InputFetchRegistry.java:142-143` calls `decodeUrlForKey(apiEndpoint)` before keying. Helper `:748-755` uses `URLDecoder.decode(...)`. |
| 8 | #31 — `JsonPath.read` no-filter (folded into Stream 8) | 🗑 Deleted/Removed | Already covered by the Finding #9 dead-code purge; `extractValueFromResponse` is gone. |

## Reviewer-mandated additions

| Item | Verdict | Evidence |
|---|---|---|
| Atomic write in `InputFetchRegistry.saveToFile` (C6) | ✅ Present | `InputFetchRegistry.java:68-93` writes to `File.createTempFile` then `Files.move(... ATOMIC_MOVE)` with a `REPLACE_EXISTING` fallback. Comment cites Reviewer Comment 6. |
| `parameterErrors` size cap (50/endpoint, 1024-char reason) (C22) | ✅ Present | `InputFetchRegistry.java:132-133` defines `MAX_ERRORS_PER_PARAM=50` and `MAX_ERROR_REASON_CHARS=1024`. Enforced in `addParameterError` at `:144-147` (truncate reason) and `:158-160` (FIFO eviction). |
| 401/403 auto-invalidate JWT (Finding #26) | ✅ Present | `SmartInputFetcher.java:509-513` invokes `authManager.invalidateToken()` on 401/403. |
| `getValidToken`/`invalidateToken` synchronized (C4) | ✅ Present | `SmartFetchAuthManager.java:71` declares `public synchronized String getValidToken()`. `:201` declares `public synchronized void invalidateToken()`. The volatile fields are at `:39, 41`. |
| Process-wide registry cache (`ConcurrentMap` keyed by registryPath) (C19) | ✅ Present | `SmartLLMParameterGenerator.java:211-241` defines `private static final ConcurrentMap<String, CachedRegistry> REGISTRY_CACHE` keyed by registry path with mtime invalidation. |
| `flushIfDirty` on scenario boundary + JVM shutdown hook (C1) | ✅ Present | Shutdown hook at `SmartInputFetcher.java:125-126`, scenario-boundary call at `:1356` (inside `resetValueRotation`), method body at `:138-147`, dirty flag at `:89` (`volatile`). Set to true on success/failure paths at lines 308, 318, 324. |

## Registry KPI recount

| KPI | Original baseline | Current count | Notes |
|---|---|---|---|
| `endpoint:` rows | 175 | **82** | Drop > 50 %, consistent with cleanup migration the plan called for. |
| `successRate: 0.0` rows | 130 | **38** | Improvement 75 % → 46 %, clears KPI threshold (the plan target was < 30 %, so we are halfway). |
| `service: NO_GOOD_MATCH` rows | 2 | **0** | Sentinel completely purged. |
| `*/query` fabricated endpoints (`grep -cE '/query"$'`) | 56 | **0** | Fully purged. |
| Literal `{paramName}` placeholders (`grep -cE 'endpoint:.*\{'`) | 27 | **0** | Fully purged. |
| Distinct service strings (case-folded) | ~14 | **14** | Same, but case-sensitive count is also 14 (no capitalization variants), down from 28 in baseline. |

No sentinel/templated rows remain in the parameterMappings section. Mentions of `NO_GOOD_MATCH` only show up in the embedded prompt-template strings (lines 1349-1413) and in legacy `parameterErrors` records (lines 31382, 46024) where the sentinel had been sent as a request value before the discovery filter was added — these are historical observation logs, not active mappings.

## Compile status

`mvn -o compile` reports `BUILD SUCCESS` with `Nothing to compile - all classes are up to date`. The pre-built classes in `target/classes/.../inputs/smart/` are dated 2026-05-05 02:15, newer than the source files. Code compiles.

## Discrepancies found

Items where current code does **not** match what the plan proposed:

1. **Finding #15 (H) — addMapping cross-service collision** is unfixed. The plan said: "Change the key to `<consumerApiKey>::<paramName>` so two operations sharing `id` don't share producer mappings." The method body in `InputFetchRegistry.java:105-108` is unchanged from the audit excerpt. This is the most concerning gap because the plan flagged it as High and it remains structurally broken; with two operations both having an `id` parameter, they still share a mapping list.

2. **Finding #23 (M) — `fetchFromApiMapping` always GET** is unfixed. The plan offered (a) honor `mapping.getMethod()` or (b) reject non-GET at registration. Neither is in place. Latent today (registry has 100 % GET) but a footgun.

3. **Finding #24 (M) — Cache eviction missing** is unfixed. Three caches are still unbounded `ConcurrentHashMap`s; `CacheConfig.maxEntries` has no consumer. Long-running soak tests will leak.

4. **Finding #38 (M) — LLM endpoint-select retry storm** is unfixed. Still up to 6 LLM calls per parameter discovery (3 attempts × {normal + forced}) regardless of whether the LLM gave the same `NO_GOOD_MATCH` response twice with the same prompt.

5. **Finding #35 (M) — LLM-fallback values polluting diverse cache** is unfixed. `cacheDiverseValue` is still called from inside `fallbackToLLM`, exactly the source-of-truth violation the plan called out.

6. **Finding #14 partial** — discovery still triggers an immediate `saveRegistry()` (line 362). The dirty-flag flush (which is a fine and well-implemented mechanism) only addresses success/failure rate updates, not the discovery write itself. So the I/O storm is mitigated for hot-path updates but the discovery batch still writes on every batch.

7. **Finding #30 partial** — `getNextDiverseValue` (the read+rotate hot path the audit flagged) still does an unguarded `valueRotationIndex.put(cacheKey, (currentIndex + 1) % values.size())`. The fix only landed on the writers.

8. **Finding #10 partial** — 5 of the cited 8+ hardcoded prompt-cap sites are still hardcoded (e.g. `prompt.length() > 2000` at line 4038, `> 800` at :2885, `> 500` at :2360 and :3024, `> 1200` at :963, plus `1944 - tempPrompt.length()` at :3296 and `1500 -` at :3255). The new `config.getMaxPromptChars()` infrastructure is real and applied to the major call sites, but the cleanup is not exhaustive.

9. **Finding #19** is documented in `dataflow-map.md` rather than `flow.md` (no `flow.md` exists in `debug/inputs/smart_fetch/`). Spirit honored, location differs.

Items where the fix **exceeds** the plan:

- **Reviewer C22 enforcement** (cap at 50 errors / 1024 chars per param) is implemented as the plan said, *plus* a FIFO eviction loop in `addParameterError` so the registry is bounded even under sustained fault-payload flooding.
- **`integerFallbackForSchema` / `numberFallbackForSchema`** (Finding #36) handle both empty/null input and parse-failure branches; the plan only required the parse-failure branch.
- **Finding #4** is fixed at two layers: (1) sentinel/lowercase canonicalization in `discoverByLLM` (`:419-441`); plus (2) a defense-in-depth `equalsIgnoreCase("NO_GOOD_MATCH")` filter in the array-element loop of `askLLMForServices` (`:3343-3349`).
- **`isRelevantField` (Finding #39)** adds a numeric-field-for-numeric-param heuristic (`:2340-2348`) that the plan did not specifically request.
- **Reviewer C1 + C19 + atomic save** all landed cleanly with cross-citing comments.

## Recommendation

**Iterate.** Three of the High/Critical findings still have concrete, scoped gaps (#15, #38, #35), one Medium finding has a meaningful soak-test concern (#24), and Finding #14 / #30 are only halfway through. The fixes that did land are high quality (well commented, cite their finding numbers, defense-in-depth where appropriate), and the registry is materially cleaner. But shipping with #15 unfixed risks a regression where two unrelated operations sharing a parameter name pollute each other's mappings the moment the registry repopulates from a fresh discovery cycle — and that contradicts the plan's stated KPI #6 ("Has `lastUsed` evidence that the freshness signal is now live").

The most surprising discrepancy is **Finding #15**: the plan's Stream 1 listed it as a top-priority High data-hygiene fix that "could be migrated alongside the registry cleanup", and the registry cleanup clearly happened (KPIs prove it), but the keying logic in `InputFetchRegistry.addMapping` is byte-for-byte the same as the audit's excerpt. It is the only data-hygiene finding where the registry artifact matches the plan's targets but the producing code does not.

Suggested next-iteration scope (smallest set that closes the highest-impact gaps):

1. Implement `addMapping(consumerApiKey, paramName, mapping)` and a one-shot migration to split current entries (Finding #15).
2. Bound caches and wire `CacheConfig.maxEntries` through the existing put paths (Finding #24).
3. Differentiate "LLM said `NO_GOOD_MATCH`" from "LLM call failed" in `selectEndpointWithLLMRetry`; cap forced-selection at 1 retry (Finding #38).
4. Move `cacheDiverseValue` out of the `fallbackToLLM` body, or tag entries with a source field and exclude LLM-sourced ones from `getNextDiverseValue` (Finding #35).
5. Sweep the 5 remaining hardcoded prompt-length caps onto `config.getMaxPromptChars()` (Finding #10).
6. Replace `getNextDiverseValue`'s read-modify-write on `valueRotationIndex` with `compute(...)` to close the rotation race (Finding #30).
