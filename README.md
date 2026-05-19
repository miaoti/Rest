# MIST — Microservice Integration & Scenario Tester

> **MIST** (Microservice Integration & Scenario Tester) turns OpenTelemetry/Jaeger traces and OpenAPI specs into runnable, cross-service workflow tests for microservice REST APIs. Built on the MST mode of [RESTest](https://github.com/isa-group/RESTest), MIST adds a single-fault Sniper Strategy, a Root API Mode that drives only entry-point APIs, and a Trace-as-Oracle layer. Submitted to **ICSME 2026 — Tool Demonstration and Data Showcase Track**.

---

## Inputs (every run needs these)

A single MIST run is fully described by one core `.properties` file. That file points at — and these are the four logical inputs you control:

| Input | Configured via | Bundled demo value |
|---|---|---|
| **OpenAPI spec** of the system under test | `oas.path` | `src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml` |
| **MST test configuration** (auto-generated from the spec) | `conf.path` | `src/main/resources/My-Example/trainticket/real-system-conf.yaml` |
| **Jaeger / OpenTelemetry traces** (single file *or* directory of `.json` / `.jsonl`) | `trace.file.path` | `src/main/resources/My-Example/trainticket/test-trace` |
| **Target system base URL** | `base.url` | `http://129.62.148.112:32677` |

Two more sit in the MIST-mode properties file (`trainticket-mst.properties`):

| Input | Configured via | Where to put the secret |
|---|---|---|
| **LLM backend** (Ollama / OpenAI-compatible / Gemini) | `llm.model.type` + `llm.<backend>.*` | env var `${VAR}` resolved at startup — see *API keys* |
| **Injected-faults registry** (optional, for detection-rate evaluation) | `fault.detection.injected.faults.path` | `src/main/resources/My-Example/trainticket/injectedFaults/injected-faults.json` |

The bundled demo ships every input above pre-staged for TrainTicket. Pick a Quick Start path below depending on whether you have an LLM API key handy.

> **Note on the two launchers.** As of the Path-B rebuild the project is a Maven reactor with `mist-cli`, `mist-core`, `mist-llm`, and `mist-restest-adapter`. The **primary** entry point is `mist-cli/target/mist.jar` (`Main-Class: io.mist.cli.MistMain`); the legacy `mist-restest-adapter/target/restest.jar` (`Main-Class: es.us.isa.restest.main.TestGenerationAndExecution`) still works as a thin delegation and is preserved for the ICSME 2026 demo workflow. Under `-Drandom.seed=42` the two jars produce byte-identical scenario files (see `docs/mst-plans/STAGE_1D_VERIFICATION.md`).

---

## Quick Start A — bundled demo, fully local LLM (no API key)

Best for first-time validation that the tool works on your machine. Uses Ollama, so nothing leaves your laptop.

```bash
# 1. Build the whole reactor (fat JARs for both launch paths)
mvn clean install -DskipTests

# 2. Start Ollama and pull a model (one-time; see https://ollama.com/download)
ollama serve &
ollama pull qwen2.5-coder:14b

# 3. Switch the bundled demo to Ollama (one-time edit; see snippet below)
#    open src/main/resources/My-Example/trainticket-mst.properties and set:
#       llm.model.type=ollama
#       llm.ollama.enabled=true
#       llm.openai_compatible.enabled=false

# 4. Generate + execute against the bundled TrainTicket demo
java -jar mist-cli/target/mist.jar src/main/resources/My-Example/trainticket-demo.properties
#    (Legacy equivalent: java -jar mist-restest-adapter/target/restest.jar <same .properties>)

# 5. Render the Allure report
allure/bin/allure generate target/allure-results -o target/allure-report --clean && \
allure/bin/allure open target/allure-report
```

## Quick Start B — bundled demo, hosted LLM API (DeepSeek shown)

Same demo, faster generation. Substitute Gemini / OpenAI / any OpenAI-compatible endpoint by adjusting the env-var name and the `llm.*` keys (see *LLM backends* below).

```bash
# 1. Build
mvn clean install -DskipTests

# 2. Provide an API key (resolved by ${DEEPSEEK_API_KEY} placeholder in the MST file)
export DEEPSEEK_API_KEY=sk-...

# 3. The bundled demo is already wired for DeepSeek (llm.model.type=openai_compatible,
#    llm.openai_compatible.url=https://api.deepseek.com/v1/chat/completions). Just run:
java -jar mist-cli/target/mist.jar src/main/resources/My-Example/trainticket-demo.properties

# 4-5. Same Allure rendering as above
allure/bin/allure generate target/allure-results -o target/allure-report --clean && \
allure/bin/allure open target/allure-report
```

## Quick Start C — your own microservice system

Five edits, then the same `java -jar` command.

```bash
# 1. Build (one-time)
mvn clean install -DskipTests

# 2. Drop your inputs anywhere under src/main/resources/<your-system>/:
#       openapi.yaml
#       test-trace/*.json     (one or more Jaeger / OTel traces)

# 3. Generate the MST test configuration from your spec (one-time per spec change).
#    Edit the input/output paths at the top of MicroserviceConfBuilderMain
#    or wrap it in your own main, then:
java -cp mist-restest-adapter/target/restest.jar es.us.isa.restest.main.MicroserviceConfBuilderMain

# 4. Copy the bundled property files as a template and update FOUR keys:
#       oas.path           → your openapi.yaml
#       conf.path          → the YAML produced by step 3
#       trace.file.path    → your test-trace directory
#       base.url           → your system's HTTP entry point
#    plus mst.config.path so the core file points at your MST file.

# 5. Same launch command, pointed at YOUR core properties file:
java -jar mist-cli/target/mist.jar src/main/resources/<your-system>/system-demo.properties
```

After any run, the fault-detection report lands under `logs/fault-detection-reports/`, CSV stats under `target/test-data/`.

---

## What this does

For each microservice scenario reconstructed from a Jaeger trace, MIST emits one JUnit class that:

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

`trainticket-demo.properties` is the file you pass on the command line. When `generator=MST`, [`MstConfig`](src/main/java/es/us/isa/restest/configuration/multiservice/MstConfig.java) loads the file referenced by `mst.config.path` and pushes every key onto System properties. Classic generators never touch the MST file. (Bringing your own system is covered in *Quick Start C* above.)

---

## LLM backends

Set `llm.model.type` in `*-mst.properties` to one of `openai_compatible`, `gemini`, or `ollama`. The unused backends can be left in the file with `*.enabled=false` — only the selected one is contacted.

> **Heads-up on naming.** Earlier versions called the OpenAI-compatible backend `local` (`llm.model.type=local`, `llm.local.*`). That was misleading — DeepSeek, OpenAI, and the like are *remote hosted APIs*, not local models. The new canonical name is `openai_compatible`, but the old `local` value and `llm.local.*` keys are still accepted as deprecated aliases (you'll see a one-time deprecation warning on startup).

### Ollama (fully local, default in flow.md examples)

```properties
llm.model.type=ollama
llm.ollama.url=http://localhost:11434
llm.ollama.model=qwen2.5-coder:14b
```

Start the daemon (`ollama serve`) and pull the model (`ollama pull qwen2.5-coder:14b`). No API key required.

### OpenAI-compatible HTTP endpoint (DeepSeek, OpenAI, OpenRouter, Together, ...)

Anything that implements OpenAI's `/v1/chat/completions` request/response shape works here — that includes DeepSeek (the bundled default), OpenAI itself, OpenRouter, Together, Groq, Mistral's hosted API, and self-hosted OpenAI shims like `gpt4all` or `llama.cpp --api`. To swap providers, change just the URL, model name, and API-key env var below.

```properties
llm.model.type=openai_compatible
llm.openai_compatible.url=https://api.deepseek.com/v1/chat/completions
llm.openai_compatible.model=deepseek-chat
llm.openai_compatible.api.key=${DEEPSEEK_API_KEY}
```

For a self-hosted server (e.g. `gpt4all` on localhost), leave `api.key` empty:

```properties
llm.openai_compatible.url=http://localhost:4891/v1/chat/completions
llm.openai_compatible.model=Meta-Llama-3-8B-Instruct.Q4_0.gguf
llm.openai_compatible.api.key=
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

`llm.openai_compatible.api.key`, `llm.gemini.api.key`, and any other secret-bearing key supports `${VAR}` and `${VAR:default}` placeholder syntax. The resolver tries, in order:

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
| `merged_openapi_spec 1.yaml` | 265-operation merged OpenAPI spec; MIST's black-box scope covers the 37 REST-exposed services |
| `real-system-conf.yaml` | Auto-generated MIST test configuration (do not edit by hand — re-run `MicroserviceConfBuilderMain`) |
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
@inproceedings{MIST2026,
  title     = {{MIST: Trace-Driven, LLM-Assisted Multi-Service Test Generation}},
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
