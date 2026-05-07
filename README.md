# RESTest-MST — Trace-Driven, LLM-Assisted Multi-Service Test Generation

> An MST (Multi-Service Testing) mode on top of [RESTest](https://github.com/isa-group/RESTest) that turns OpenTelemetry/Jaeger traces and OpenAPI specs into runnable, cross-service workflow tests with LLM-generated parameters, fault injection, and Allure reports. Submitted to **ICSME 2026 — Tool Demonstration and Data Showcase Track**.

---

## Quick Start (5 commands)

Replays the bundled TrainTicket demo end-to-end (compile → generate → execute → Allure report).

```bash
# 1. Build
mvn clean install -DskipTests

# 2. Provide an LLM API key (DeepSeek shown; see "LLM backends" below for alternatives)
export DEEPSEEK_API_KEY=sk-...

# 3. Run MST generation + execution against the included TrainTicket demo
java -jar target/restest.jar src/main/resources/My-Example/trainticket-demo.properties

# 4. Render the Allure report
allure/bin/allure generate target/allure-results -o target/allure-report --clean

# 5. Open it
allure/bin/allure open target/allure-report
```

The fault-detection report lands under `logs/fault-detection-reports/`; CSV stats under `target/test-data/`.

---

## What this does

For each microservice scenario reconstructed from a Jaeger trace, RESTest-MST emits one JUnit class that:

1. **logs in once per JVM** (configurable; see *Auth strategy*),
2. **replays each root API in order**, wiring data between steps via cross-trace data-dependency inference and a JIT producer-binding registry built from the OpenAPI spec,
3. **injects 8 categories of invalid inputs** (TYPE_MISMATCH, REGEX_MISMATCH, SEMANTIC_MISMATCH, OVERFLOW, EMPTY/NULL, SPECIAL_CHARACTERS, BOUNDARY_VIOLATION) for the configured `faulty.ratio`,
4. **runs LLM soft-error validation** on 2xx responses and **caches the rule** so each API only consults the LLM ~2 times instead of once per test,
5. **explores untriggered status codes** (401/403/404/409/…) via auth-manipulation and LLM-suggested input mutations.

Full pipeline (Phase 1 cross-trace merging → Phase 2 session merging → Phase 2.5 dedup → Phase 3 component shattering → Phase 4 baseline decomposition → variant generation) is documented in [`src/main/resources/My-Example/trainticket/flow.md`](src/main/resources/My-Example/trainticket/flow.md).

---

## Requirements

| | Version |
|---|---|
| JDK | 11 (compile target is `-source 11 -target 11`) |
| Maven | 3.6+ |
| Allure CLI | bundled in `allure/` (Java 8+ required by Allure itself) |
| LLM backend | one of: Ollama (local), DeepSeek / OpenAI-compatible HTTP, Google Gemini |
| Target system | reachable HTTP base URL + an OpenAPI spec + Jaeger traces |

The TrainTicket demo points at a public deployment (`http://129.62.148.112:32677`); replace the `base.url` and `jaeger.base.url` keys in the config to use your own.

---

## Configuration layout

Configuration is **split in two files** so MST-specific keys never leak into the classic RT/CBT/ART/FT/LLM RESTest flows.

```
src/main/resources/My-Example/
├── trainticket-demo.properties      # RESTest-core only (~30 keys) + mst.config.path pointer
└── trainticket-mst.properties       # MST-only (~70 keys: LLM, smart fetch, jaeger,
                                     #   fault detection, soft-error cache, enhancer,
                                     #   status-code exploration, root-API registry,
                                     #   trace merging, auth strategy, ...)
```

`trainticket-demo.properties` is the file you pass on the command line. When `generator=MST`, [`MstConfig`](src/main/java/es/us/isa/restest/configuration/multiservice/MstConfig.java) loads the file referenced by `mst.config.path` and pushes every key onto System properties. Classic generators never touch the MST file.

### Bring your own microservice system

1. Place your OpenAPI spec at `src/main/resources/<your-system>/openapi.yaml`.
2. Drop one or more Jaeger trace JSON/JSONL files into `src/main/resources/<your-system>/test-trace/`.
3. Generate the test configuration once:
   ```bash
   java -cp target/restest.jar es.us.isa.restest.main.MicroserviceConfBuilderMain
   ```
   (Edit the input/output paths at the top of the file or wrap it in your own main.)
4. Copy `trainticket-demo.properties` + `trainticket-mst.properties` to a sibling directory and update the `oas.path`, `conf.path`, `base.url`, `jaeger.base.url`, `mst.config.path` keys.

---

## LLM backends

Set `llm.model.type` in `*-mst.properties` to one of `local`, `gemini`, `ollama`. The unused backends can be left in the file with `*.enabled=false` — only the selected one is contacted.

### Ollama (fully local, default in flow.md examples)

```properties
llm.model.type=ollama
llm.ollama.url=http://localhost:11434
llm.ollama.model=qwen2.5-coder:14b
```

Start the daemon (`ollama serve`) and pull the model (`ollama pull qwen2.5-coder:14b`). No API key required.

### DeepSeek / OpenAI / any OpenAI-compatible endpoint

```properties
llm.model.type=local
llm.local.url=https://api.deepseek.com/v1/chat/completions
llm.local.model=deepseek-chat
llm.local.api.key=${DEEPSEEK_API_KEY}
```

The `${VAR}` syntax is resolved at startup — see *API keys* below.

### Google Gemini

```properties
llm.model.type=gemini
llm.gemini.api.key=${GEMINI_API_KEY}
llm.gemini.model=gemini-2.0-flash-exp
llm.gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models
```

---

## API keys

`llm.local.api.key`, `llm.gemini.api.key`, and any other secret-bearing key supports `${VAR}` and `${VAR:default}` placeholder syntax. The resolver tries, in order:

1. `System.getenv("VAR")` — `export DEEPSEEK_API_KEY=sk-...` before launching.
2. `System.getProperty("VAR")` — `-DDEEPSEEK_API_KEY=sk-...` on the JVM command line (handy for IntelliJ run configs).
3. `.api_keys/VAR` (or `~/.restest/api_keys/VAR`) — a file containing only the secret. The `.api_keys/` directory is gitignored.
4. The literal `default` after `:` in `${VAR:default}`.

A missing key resolves to the empty string, in which case the request is sent unauthenticated rather than literally containing the placeholder text. The full resolution logic lives in [`LLMConfig.resolveEnvPlaceholder`](src/main/java/es/us/isa/restest/llm/LLMConfig.java).

---

## Auth strategy for the *target* system

The generated tests use [`MstAuthHandler`](src/main/java/es/us/isa/restest/auth/MstAuthHandler.java) to obtain and stamp tokens. Configured by `auth.*` keys in the MST file:

| `auth.mode` | Effect |
|---|---|
| `none` | No `Authorization` header on any request. |
| `static_token` | Use `auth.static.token` verbatim; never call `/login`. |
| `per_jvm` (default) | Lazy login on first request, cached for the whole JVM. ~2400 logins → ~1. |
| `per_test` | Legacy: fresh login per test method. |

Endpoints matching `auth.skip.path.patterns` (CSV of regex, e.g. `^/actuator,^/api/v1/users/login`) are sent without an `Authorization` header regardless of mode. On HTTP 401, [`MstAuthRefreshFilter`](src/main/java/es/us/isa/restest/auth/MstAuthRefreshFilter.java) invalidates the cached token and retries once — disabled automatically for tests that intentionally manipulate auth (e.g. `INVALID_TOKEN` exploration tests).

---

## Outputs

| Path | Content |
|---|---|
| `src/test/java/<package>/<TestClassName>_<timestamp>/` | Generated JUnit test sources |
| `target/test-classes/<package>/...` | Compiled `.class` files |
| `target/allure-results/` | Raw Allure JSON (per test) |
| `target/allure-report/` | Rendered HTML report (after `allure generate`) |
| `logs/fault-detection-reports/` | Injected-fault detection summary (matched against `injectedFaults/injected-faults.json`) |
| `target/soft-error-rule-cache.json` | LLM-derived soft-error rules per API (grows incrementally; safe to delete) |
| `target/test-data/` | CSV stats (test cases, results, time) |

---

## Repository data showcase

The TrainTicket dataset bundled with the tool, all under `src/main/resources/My-Example/trainticket/`:

| Asset | Description |
|---|---|
| `merged_openapi_spec 1.yaml` | 265-operation merged OpenAPI spec across 41 TrainTicket microservices |
| `real-system-conf.yaml` | Auto-generated MST test configuration (do not edit by hand — re-run `MicroserviceConfBuilderMain`) |
| `test-trace/*.json` | OpenTelemetry traces used to mine workflow scenarios |
| `injectedFaults/injected-faults.json` | Ground-truth fault registry for detection-rate evaluation |
| `flow.md` | Full algorithm documentation (extraction → merging → shattering → generation) |

---

## Repository layout (top level)

```
src/main/java/es/us/isa/restest/
├── auth/                       MstAuthHandler, MstAuthRefreshFilter
├── configuration/multiservice/ MstConfig, MicroserviceTestConfigurationGenerator, ...
├── generators/                 MultiServiceTestCaseGenerator (MST), classic generators
├── workflow/                   TraceWorkflowExtractor, scenario merging
├── writers/restassured/        MultiServiceRESTAssuredWriter (MST), RESTAssuredWriter (classic)
├── enhancer/                   Test-case enhancer (LLM-driven failure recovery)
├── analysis/                   FaultDetectionTracker, TraceErrorAnalyzer
└── main/                       TestGenerationAndExecution (entry point), MicroserviceConfBuilderMain
```

For classic RESTest modes (RT/CBT/ART/FT/LLM), see the upstream wiki: <https://github.com/isa-group/RESTest/wiki>.

---

## Citation

```bibtex
@inproceedings{RESTestMST2026,
  title     = {{RESTest-MST: Trace-Driven, LLM-Assisted Multi-Service Test Generation}},
  author    = {<authors>},
  booktitle = {Proceedings of the 42nd IEEE International Conference on Software Maintenance and Evolution},
  series    = {ICSME '26},
  publisher = {IEEE},
  year      = {2026},
  note      = {Tool Demonstration and Data Showcase Track}
}
```

This work extends RESTest; please also cite:

```bibtex
@inproceedings{MartinLopez2021Restest,
  title     = {{RESTest: Automated Black-Box Testing of RESTful Web APIs}},
  author    = {Alberto Martin-Lopez and Sergio Segura and Antonio Ruiz-Cort\'{e}s},
  booktitle = {Proceedings of the 30th ACM SIGSOFT International Symposium on Software Testing and Analysis},
  series    = {ISSTA '21},
  publisher = {Association for Computing Machinery},
  year      = {2021}
}
```

---

## License

Distributed under the [GNU Lesser General Public License v3.0](LICENSE), inherited from RESTest. Includes Allure Framework © Qameta Software OÜ under the Apache 2.0 License.
