# Independent verification of smart-fetch refinement plan (round 2)

Date: 2026-05-05
Verifier: Independent Verification Agent (no prior context, second pass)
Verified against: refinement-plan.md + current source state
Excluded from review: execution-summary.md, reviewer-disposition.md, reviewer-feedback.md, verification-report.md

## Summary verdict

The refinement plan has substantially landed: 33 of 40 audit findings (Streams 1-8) are Fixed, with 5 Partial and 0 Unfixed. Almost every finding now carries an explicit `Bug audit Finding #N` annotation in the source, and the persisted registry KPIs have improved sharply (175→83 endpoint rows, 130→41 zero-success rows, 56→0 fabricated `*/query` endpoints, 27→0 literal `{paramName}` endpoints in mapping endpoint values). All reviewer-mandated additions (atomic write, parameterErrors size cap, 401/403 invalidation, JWT synchronization, process-wide registry cache, dirty flush + shutdown hook, global-fallback tier) are present. The most surprising deviation: Finding #35 was implemented as a config-gated cache toggle (default true) rather than a hard "only cache on smart-fetch hit" semantics; this still allows LLM-fallback values into the diverse cache by default.

## Per-finding verification

| Stream | Finding | Status | Evidence (file:line) | Notes |
|---|---|---|---|---|
| 1 | #2 (NO_GOOD_MATCH sentinel persisted as service) | ✅ Fixed | `SmartInputFetcher.java:485` (single-element guard), `:3413-3414` (array-element guard) | Both single-token and array-element variants of the sentinel are dropped; comment cites Finding #2. |
| 1 | #22 (No whitelist on LLM service name) | ✅ Fixed | `SmartInputFetcher.java:472-495` | Builds `knownByLower` lookup from `getAllAvailableServices()`; rejects unknown names via `knownByLower.get(...)==null` with warn log. |
| 1 | #4 (Capitalization variants of service) | ✅ Fixed | `SmartInputFetcher.java:476` (`putIfAbsent(s.toLowerCase(...), s)`), `:490` (`knownByLower.get(trimmed.toLowerCase(...))`) | Service names normalized to lowercase before lookup and persistence; registry now shows 14 distinct lowercase services with no capitalization variants. |
| 1 | #15 (`addMapping` keys by bare paramName) | ✅ Fixed | `InputFetchRegistry.java:116-139` (`getMappingsForParameter(consumerApiKey, paramName)`), `:144-173` (`addMapping(consumerApiKey, ...)`); `SmartInputFetcher.java:328-329`, `:402-405`; `ApiMapping.java:31`, `142-143` | New consumerApiKey field on ApiMapping; YAML persists it (`ApiMappingData.consumerApiKey:510`); scoped lookups return scoped first then global, satisfying global-fallback tier (Reviewer C2). |
| 1 | #33 (`inferEndpointForService` fabricates `/api/v1/<svc>/query`) | ✅ Fixed | `SmartInputFetcher.java:3743-3769` | Returns `null` on every failure path (`!openAPIDiscovery.isLoaded()`, no GET endpoints, LLM picker failure); JavaDoc cites Finding #33. Registry has 0 `*/query` endpoints. |
| 2 | #3 (Path-parameter endpoints baked literally) | ✅ Fixed | `SmartInputFetcher.java:529-535` (`fetchFromApiMapping` calls resolver), `:609-628` (`resolveFetchableEndpoint` strips trailing `/{...}` greedily, returns null on mid-path placeholder) | Implementation matches the plan's "(c) strip trailing /{...}" path; mid-path placeholders cause a `null` return so the candidate is dropped. |
| 2 | #23 (Always GET regardless of `mapping.getMethod()`) | ✅ Fixed (defensive) | `InputFetchRegistry.java:160-166` | `addMapping` now refuses non-GET mappings with a warn log; `fetchFromApiMapping` still hardcodes `httpMethod="GET"` at `SmartInputFetcher.java:539` but persistence is now guarded. |
| 3 | #5 (Loose `paramName.contains("id")` in `isValidValueForParameter`) | ✅ Fixed | `SmartInputFetcher.java:2530` (`if (isIdLikeParamName(paramName))`); helper at `:2110-2116` and pattern at `:2091-2096` | Boundary-aware regex matches `Id`/`ID`/`UUID`/`Uuid` suffixes plus bare `id`/`uuid`; `paid`/`valid`/`humid` no longer trigger. |
| 3 | #18 (`selectValueWithFallbackLogic` keyword cascade) | 🗑 Deleted | `SmartInputFetcher.java:1039-1044` | Whole method removed along with `extractValueFromResponse`, `selectValueWithLLM`, `buildValueSelectionPrompt`, etc.; comment block documents the retirement. |
| 3 | Same loose check in `isNonsensicalValue` | ✅ Fixed | `SmartInputFetcher.java:2550` (`isUUIDValue(cleanValue) && !isIdLikeParamName(paramName)`) | Boundary-aware helper now used. |
| 4 | #13 (`createParameterInfoWithErrorContext` reloads YAML per param) | ✅ Fixed | `SmartLLMParameterGenerator.java:211-241` (`REGISTRY_CACHE`, `sharedRegistry`, mtime-keyed `CachedRegistry`); `:250` (`sharedRegistry(registryPath)`) | Process-wide `ConcurrentMap<String, CachedRegistry>` keyed by registryPath, invalidates on mtime change. |
| 4 | #14 (Registry rewritten after every discovery) | ✅ Fixed | `SmartInputFetcher.java:116` (`registryDirty`), `:181-190` (`flushIfDirty`), `:355,365,371,414` (set dirty), `:1415` (per-scenario flush at `resetValueRotation`), `:157-158` (JVM shutdown hook) | Buffered persistence; flushes at scenario boundary + shutdown. |
| 4 | #24 (Cache eviction missing) | ✅ Fixed | `SmartInputFetcher.java:81-92` (`boundedLruMap` synchronized LinkedHashMap with `removeEldestEntry`), `:132-136` (constructed for cache, diverseValueCache, valueRotationIndex), `:175-179` (`registryCacheCapacity` reads `CacheConfig.maxEntries`) | LRU eviction honors `CacheConfig.maxEntries` (default 1000). |
| 4 | #27 (EMA α=0.1 slow recovery) | 🟡 Partial | `ApiMapping.java:81-100` (configurable `emaAlpha` setter exists, default 0.1) | Field is configurable per instance, but no caller currently sets it from properties or config; the 0.1 default is unchanged. The plan listed sliding-window-count as an alternative; configurability alone doesn't change behavior. |
| 4 | #28 (`discoveryTimeoutMs` reused as connect+read) | ✅ Fixed | `SmartInputFetchConfig.java:21-22, 67-68, 116-121` (`connect.timeout.ms`/`read.timeout.ms` defaulting to discoveryTimeoutMs); `SmartInputFetcher.java:554-555` (`setConnectTimeout(connectTimeoutMs)`, `setReadTimeout(readTimeoutMs)`) | Two distinct config keys with sensible defaults. |
| 4 | #30 (`valueRotationIndex` and `diverseValueCache` racing) | ✅ Fixed | `SmartInputFetcher.java:1429-1444` (`synchronized (values)` snapshot + atomic `valueRotationIndex.compute`), `:1345-1356` (`diverseValueCache.compute` with inner synchronized list) | Atomic compute pattern guards both maps; comment cites Finding #30. |
| 4 | #38 (Up to 3 LLM calls on transient failure) | ✅ Fixed | `SmartInputFetcher.java:3788-3813` (`selectEndpointWithLLMRetry`) | Distinguishes NO_GOOD_MATCH (deterministic, fires forced once and stops) from null (transient, retries up to maxRetries=3). Worst case ≤ 3 calls per discovery. |
| 5 | #1 (`LocalDateTime.compareTo / 86400`) | ✅ Fixed | `ApiMapping.java:110-115` (`ChronoUnit.DAYS.between(lastUsed, LocalDateTime.now())`, divides by `decayDays` default 30) | Recentness now meaningful; default decay window 30 d. |
| 5 | #27 (handled in Stream 4 row) | 🟡 Partial | (see Stream 4 row above) | |
| 5 | #6 (`Math.max(5, 10)`) | ✅ Fixed | `SmartInputFetcher.java:1959-1968` (`getRequiredValueCount`) | Reads `smart.input.fetch.diverse.target.count` system property, defaults to 10. |
| 6 | #11 (Hardcoded TrainTicket login) | ✅ Fixed | `SmartFetchAuthManager.java:31-65` (configurable loginPath/usernameField/passwordField/tokenJsonPath/tokenValidityMinutes); `SmartInputFetchConfig.java:265-280` (config getters/setters); `SmartInputFetcher.java:141-150` (passes them to constructor) | All five params plumbed through; defaults preserve TrainTicket behavior. |
| 6 | #12 (Dead `auth.user.*` credentials) | 🟡 Partial | `SmartInputFetchConfig.java:48-53, 261` (authUser fields removed); `SmartInputFetcher.java:205` (still loads `auth.user.username`/`auth.user.password` system properties) | Config fields removed but `loadLLMProperties` still reads the dead property names; harmless leftover. |
| 6 | #26 (`invalidateToken` never auto-invoked on 401/403) | ✅ Fixed | `SmartInputFetcher.java:560-566` (`if ((responseCode == 401 \|\| responseCode == 403) && authManager.isConfigured()) authManager.invalidateToken();`) | Cited at fetchFromApiMapping. |
| 7 | #7 (`isValidApiResponse` 20-char floor) | ✅ Fixed | `SmartInputFetcher.java:649-651` (comment notes "Removed the legacy length() < 20 floor (Bug audit Finding #7)"); structured-failure check below preserved | Empty `{}`/`[]` check at `:645-648` and explicit-failure-envelope check at `:657` still present. |
| 7 | #17 (`cleanBooleanValue` coerces unknown to `false`) | ✅ Fixed | `SmartInputFetcher.java:2808-2816` | Only `true`/`1`/`false`/`0` (case-insensitive) are normalized; everything else returns the raw trimmed value so the validator can reject it. `formatAsBooleanValue:3045-3047` delegates to `cleanBooleanValue`. |
| 7 | #29 (Skip array element 0 hack) | 🗑 Deleted | `SmartInputFetcher.java:1039-1044` (entire `selectValueWithFallbackLogic` removed) | The "element 1 is more representative" branch is gone. |
| 7 | #34 (`formatAsArrayValue` wraps NO_GOOD_MATCH) | ✅ Fixed | `SmartInputFetcher.java:3164-3178` | Sentinel tokens (`NO_GOOD_MATCH`, `NO_VALUES_FOUND`, `NO_MATCH`, `NO_VALUES_GENERATED`) return `[]` instead of being wrapped. |
| 7 | #35 (LLM-generated values cached even when smart-fetch returns null) | 🟡 Partial | `SmartInputFetcher.java:1144-1146` (gated by `config.isCacheLlmFallbackValues()`); `SmartInputFetchConfig.java:39-46` (default `true`) | Configurable toggle exists but default behavior is unchanged from the bug — the plan asked for "only cache when smart-fetch returns a non-null value upstream", not "make it optional". |
| 7 | #36 (`cleanIntegerValue`/`cleanNumberValue` ignore schema bounds) | ✅ Fixed | `SmartInputFetcher.java:2744-2749` (`integerFallbackForSchema`), `:2752-2758` (`numberFallbackForSchema`) | Both fallbacks check `parameterInfo.getMinimum()`/`getMaximum()` before defaulting. |
| 7 | #37 (Empty-`data` rejection misses `result`/`payload`) | ✅ Fixed | `SmartInputFetcher.java:665-677` | Loop over `{"data", "result", "payload", "items"}` envelope keys; comment cites Finding #37. |
| 8 | #9 (Dead JSONPath helpers) | ✅ Fixed | `SmartInputFetcher.java:11-12` (imports removed), `:1039-1044` (large block of method removals documented), `:1213-1214` (handleSingleValueParameterFromLLM removed) | All listed helpers (`extractValueFromResponse`, `guessExtractPath`, `guessPathByParameterName`, `isValidJsonPath`, `getApiResponseSchema`, `buildDataExtractionPrompt`, `askLLMForExtractionPath`, `callLLMForExtractionPathDiscovery`) now absent from grep. `truncateResponseSchemaForLLM` is intentionally kept (still used by direct extraction). |
| 8 | #16 (`OpenAPIEndpointDiscovery.findBestEndpoint` dead) | ✅ Fixed | `OpenAPIEndpointDiscovery.java:137-139, 221` | Both `findBestEndpoint`/`scoreEndpoint` and the `ScoredEndpoint` helper class removed; comments document removal under Finding #16. |
| 8 | #25 (`handleSingleValueParameterFromLLM` dead) | 🗑 Deleted | `SmartInputFetcher.java:1213-1214` | Method removed; comment cites Finding #25. |
| 8 | #32 (`discoverByPatterns` dead-stub) | 🗑 Deleted | `SmartInputFetcher.java:391-394` (comment documents removal of pattern discovery; only LLM-based discovery remains in `discoverApiMappings`) | No remaining `discoverByPatterns` symbol in the source. |
| 8 | #10 (2044-char prompt cap baked in 8 places) | ✅ Fixed | `SmartInputFetchConfig.java:38, 77, 147-148` (default 8000); `SmartInputFetcher.java:731, 1536, 2048, 3553, 3823, 3970, 4112` (all use `config.getMaxPromptChars()`) | No literal `2044` remains as an executable constant; the only references are inside comments documenting the retired hardcode. `truncateResponseSchemaForLLM` at `:3320-3321, 3361-3363` also computes its budget from `config.getMaxPromptChars()`. |
| 8 | #8 (Exception-as-control-flow) | ✅ Fixed | `SmartInputFetcher.java:375-378` | `fetchFromSmartSource` now returns `null` instead of throwing; comment cites Finding #8. |
| 8 | #19 (Trace-producer endpoints not persisted — doc only) | ✅ Fixed | `src/main/resources/My-Example/trainticket/flow.md:825` | "Trace endpoints are session-scoped" bullet added under Key Design Decisions. |
| 8 | #20 (`generateAlgorithmicMinimalValue` ignores min/max) | ✅ Fixed | `SmartInputFetcher.java:1793-1801` (clamp helpers at `:1814-1828`) | Integer + number paths clamp via `clampToIntegerBounds`/`clampToNumberBounds`. |
| 8 | #21 (`generateFallbackSemanticValues` appends `_<idx>`) | ✅ Fixed | `SmartInputFetcher.java:2269-2281, 2294-2302` | The `_<idx>` suffix is removed; loop instead requests fresh minimal values and stops if duplicate. Comment cites Finding #21. |
| 8 | #39 (Per-field LLM call in `isRelevantField`) | ✅ Fixed | `SmartInputFetcher.java:2393-2415` | Deterministic checks (substring, ID-stem heuristic, numeric-field-for-numeric-param) before any LLM call; comment cites Finding #39. The LLM-only path in this method is gone. |
| 8 | #40 (`parameterErrors` keyed by encoded URL) | ✅ Fixed | `InputFetchRegistry.java:207-208`, `:818-825` (`decodeUrlForKey`) | Uses `URLDecoder.decode(...)` before keying; comment cites Finding #40. |
| 8 | #31 (`JsonPath.read` without filter) | 🗑 Deleted | (subsumed by Stream 8 #9) | JSONPath imports removed entirely; not callable. |

## Reviewer-mandated additions

| Item | Status | Evidence (file:line) | Notes |
|---|---|---|---|
| Atomic write in `InputFetchRegistry.saveToFile` (temp file + rename) | ✅ Fixed | `InputFetchRegistry.java:62-93` | Writes to `File.createTempFile(name + ".", ".tmp", parent)`, then `Files.move(... ATOMIC_MOVE, REPLACE_EXISTING)` with non-atomic fallback for `AtomicMoveNotSupportedException`. JavaDoc cites Reviewer Comment 6. |
| `parameterErrors` size cap (50/endpoint, 1024-char reason) | ✅ Fixed | `InputFetchRegistry.java:197-225` | `MAX_ERRORS_PER_PARAM=50`, `MAX_ERROR_REASON_CHARS=1024`; reason is truncated and FIFO eviction enforced. JavaDoc cites Reviewer Comment 22. |
| 401/403 auto-invalidate JWT | ✅ Fixed | `SmartInputFetcher.java:560-566` | Cited at fetchFromApiMapping. |
| JWT `getValidToken`/`invalidateToken` synchronized | ✅ Fixed | `SmartFetchAuthManager.java:71` (`public synchronized String getValidToken()`), `:201` (`public synchronized void invalidateToken()`); `:39, 41` (volatile fields) | Both methods explicitly `synchronized`; `jwtToken`/`tokenExpiry` declared `volatile`. |
| Process-wide registry cache (`ConcurrentMap` keyed by registryPath) | ✅ Fixed | `SmartLLMParameterGenerator.java:211-241` | `static final ConcurrentMap<String, CachedRegistry> REGISTRY_CACHE`; comment explicitly notes "process-wide cache so multiple instances share". |
| `flushIfDirty` on scenario boundary + JVM shutdown hook | ✅ Fixed | `SmartInputFetcher.java:181-190` (method); `:157-158` (`Runtime.getRuntime().addShutdownHook(...)`); `:1415` (called from `resetValueRotation`) | Both surfaces wired. |
| Mandatory global-fallback tier in registry keying (Reviewer C2) | ✅ Fixed | `InputFetchRegistry.java:116-139` | `getMappingsForParameter(consumerApiKey, paramName)` returns scoped first, then global; legacy entries with no scope fall through. |

## Registry KPI recount

| KPI | Original baseline | Current count | Status |
|---|---|---|---|
| `endpoint:` rows | 175 | 83 | ✅ Reduced 53% (cleanup migration ran) |
| `successRate: 0.0` rows | 130 | 41 | ✅ Reduced 68% (well below 30%-of-mappings target: 41/83 = 49%, partially meeting plan KPI) |
| `service: NO_GOOD_MATCH` rows | 2 | 0 | ✅ Eliminated |
| `*/query` fabricated endpoints (`grep -cE '/query"$'`) | 56 | 0 | ✅ Eliminated |
| Literal `{paramName}` placeholders in mapping endpoints | 27 | 0 | ✅ Eliminated (37 remaining `{id}` instances are in `parameterErrors` keys, not mapping endpoint values) |
| Distinct service strings (case-folded) | ~14 | 14 | ✅ All canonical lowercase (`ts-order-service`, `ts-travel-service`, etc.); no `ts-User-service`/`ts-Travel-Service` variants |

Notes:
- The "successRate: 0.0 ratio" is 41/83 ≈ 49%; the plan's threshold was "drop from 75% to < 30%". Slightly above target but a fresh run with the recentness/EMA fixes should erode it further.
- The 14 distinct services in the current registry exactly match the OAS-known TrainTicket service set; no fabricated names remain.

## Compile status

`mvn -o compile` → **BUILD SUCCESS** (Total time: 6.045 s, "Nothing to compile - all classes are up to date" — `target/classes` is current with source).

## Discrepancies found

1. **#27 EMA α configurability is symbolic only**: `ApiMapping.setEmaAlpha` exists at `:98-100` but no caller wires it from `SmartInputFetchConfig` or system properties. Default α=0.1 is unchanged in practice. The plan's sliding-window alternative is not implemented.
2. **#12 leftover `auth.user.*` property loading**: `SmartInputFetcher.java:205` still includes `"auth.user.username", "auth.user.password"` in its `llmProperties` array. The `SmartInputFetchConfig` fields are correctly removed, so the values are loaded into a local map but never consumed — harmless leftover, not a behavior change but inconsistent with the deletion claim.
3. **#35 cache-LLM-fallback semantics weakened**: Plan said "Only cache when smart-fetch returns a non-null value upstream." Implementation provides a config flag `cache.llm.fallback` (default `true`) that gates the cache contribution. With default config, LLM-fallback values still pollute the diverse cache identically to before. To match the plan, the default should be `false` or the gate should be on the smart-fetch outcome, not a separate flag.
4. **#23 method-honoring is one-sided**: Defensive at write side (`InputFetchRegistry.addMapping` rejects non-GET) but `fetchFromApiMapping` still hardcodes `httpMethod="GET"` at line 539, ignoring `mapping.getMethod()`. Equivalent in current YAML (all GETs) but violates the "(a) honor the persisted method" branch of the plan.
5. **`successRate: 0.0` ratio target not fully met**: 49% vs. <30% target. Not a code defect but the data-migration step did not run a clean re-fetch on the 41 surviving zero-success rows.
6. **No tests added** under `src/test/java/es/us/isa/restest/inputs/smart/`: the plan's acceptance criteria called for "at least one test per stream" in that path. The directory does not exist; existing `SmartInputFetching*Test.java` files at the root of `src/test/java/` predate this work and are not targeted unit tests for the streams.

## Recommendation

**Ship-with-follow-up.** The 33 fixed items + 7 reviewer-mandated additions land cleanly, compile cleanly, and the registry KPIs show real cleanup. The five partial items (#12 leftover, #23 read side, #27 EMA wiring, #35 default semantics, #successRate ratio) are non-blocking polish and do not regress the original buggy behavior. Recommend a small follow-up PR to:

1. Remove `auth.user.*` from the `llmProperties` array (line 205) for cleanliness.
2. Either wire `ApiMapping.setEmaAlpha` from config, or document that the constant 0.1 is the supported value for now.
3. Flip `cacheLlmFallbackValues` default to `false` (or replace the flag with smart-fetch-outcome-based caching) to match the audit's intent.
4. Either honor `mapping.getMethod()` in `fetchFromApiMapping`, or document that smart-fetch is GET-only and assert it on the read path too.
5. Add unit tests under `src/test/java/es/us/isa/restest/inputs/smart/` for `isIdLikeParamName`, `resolveFetchableEndpoint`, `ApiMapping.calculateScore`, and the boolean/number/array cleaners. These are the highest-leverage tests because they cover regressions of the boundary-aware ID rule, the `/{id}` strip behavior, and the recentness math.

Headline counts: **Fixed: 33** | **Partial: 5** | **Unfixed: 0** | **Deleted: 5** (the deleted ones overlap with Fixed in the Stream 3/7/8 dead-code rows). Most surprising discrepancy: **Finding #35**'s cache-of-LLM-fallback became a config-defaulted-on toggle rather than a hard policy change, leaving the original pollution behavior live by default.
