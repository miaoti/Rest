# MIST — Microservice Integration & Scenario Tester

> **MIST** turns OpenTelemetry / Jaeger traces and OpenAPI specs into
> runnable, cross-service workflow tests for microservice REST APIs.
> Its design contributions are a *Trace Shape Oracle* (per-API
> invariants checked against live traces), an *Adaptive Fault Taxonomy*
> (a registry of fault categories that can be mined per system), and
> a *Sniper Strategy* generation engine that produces one fault per
> negative variant with full per-fault attribution. Submitted to
> **ICSME 2026 — Tool Demonstration and Data Showcase Track**.

---

## Architecture at a glance

MIST ships as a four-module Maven reactor (Stage 1.C of
[`PATH_B_REBUILD_PLAN.md`](debug/Conference-refinement/PATH_B_REBUILD_PLAN.md)):

```
mist-parent (root pom.xml, packaging=pom)
├── mist-core              the contribution — no RESTest dependency
│                          (Trace Shape Oracle + Adaptive Fault Taxonomy)
├── mist-llm               placeholder for the future LLM dispatch module
├── mist-restest-adapter   RESTest internals MIST treats as a library:
│                          spec parser, MST conf model, writers, generators,
│                          smart-fetch, enhancer, …
└── mist-cli               user-facing entry points
                           ├── io.mist.cli.MistMain          ← run the tool
                           └── io.mist.cli.MistConfGenMain   ← gen MST conf
```

`mist-core` has no edges to the legacy `es.us.isa.restest.*` classes;
`mist-restest-adapter` keeps the RESTest-derived code that still drives
the test-generation pipeline. `MistMain` / `MistRunner` in `mist-cli`
wire the two together.

---

## Inputs (every run needs these)

A single MIST run is fully described by one **core `.properties`** file.
All paths inside that file are **resolved relative to the file itself**,
so launching MIST from the repo root, from a module subdirectory, or
from an IntelliJ play-button all produce the same result.

| Input | Key | Bundled demo value (relative to the .properties file) |
|---|---|---|
| **OpenAPI spec** of the system under test | `oas.path` | `trainticket/merged_openapi_spec 1.yaml` |
| **MST test configuration** (generated once from the spec by `MistConfGenMain`) | `conf.path` | `trainticket/real-system-conf.yaml` |
| **Jaeger / OpenTelemetry traces** (single file *or* directory of `.json` / `.jsonl`) | `trace.file.path` | `trainticket/test-trace` |
| **Target system base URL** | `base.url` | `http://129.62.148.112:32677` |
| **MST-mode overlay** (extra MIST-only keys) | `mst.config.path` | `trainticket-mst.properties` |

Two more keys sit in the MIST-mode overlay:

| Input | Key | Where to put the secret |
|---|---|---|
| **LLM backend** (Ollama / OpenAI-compatible / Gemini) | `llm.model.type` + `llm.<backend>.*` | env var `${VAR}` resolved at startup — see *API keys* |
| **Injected-faults registry** (optional, for detection-rate evaluation) | `fault.detection.injected.faults.path` | `trainticket/injectedFaults/injected-faults.json` |

The bundled demo ships every input above pre-staged for TrainTicket.
Pick a Quick Start path below depending on whether you have an LLM
API key handy.

> **Two launchers, one runner.** Both
> `mist-cli/target/mist.jar` (`Main-Class: io.mist.cli.MistMain`) and
> `mist-restest-adapter/target/restest.jar`
> (`Main-Class: es.us.isa.restest.main.TestGenerationAndExecution`)
> end at the same `MistRunner.run()`. Under `-Drandom.seed=42` they
> produce byte-identical scenario files; see
> [`docs/mst-plans/STAGE_1D_VERIFICATION.md`](docs/mst-plans/STAGE_1D_VERIFICATION.md)
> for the verification record. Use `mist.jar` for new work;
> `restest.jar` is preserved for the ICSME 2026 demo workflow.

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
#    open mist-restest-adapter/src/main/resources/My-Example/trainticket-mst.properties
#    and set:
#       llm.model.type=ollama
#       llm.ollama.enabled=true
#       llm.openai_compatible.enabled=false

# 4. Generate + execute against the bundled TrainTicket demo (run from repo root)
java -jar mist-cli/target/mist.jar \
     mist-restest-adapter/src/main/resources/My-Example/trainticket-demo.properties
#    (Legacy entry, byte-identical output under the same seed:
#       java -jar mist-restest-adapter/target/restest.jar <same .properties>)

# 5. Render the Allure report
allure/bin/allure generate target/allure-results -o target/allure-report --clean && \
allure/bin/allure open target/allure-report
```

In IntelliJ, the pre-shipped run configuration **"MIST: Demo
(bundled TrainTicket)"** does the same thing with one click —
working directory is fixed to `$PROJECT_DIR$` so the play-button
behaves like the CLI.

## Quick Start B — bundled demo, hosted LLM API (DeepSeek shown)

Same demo, faster generation. Substitute Gemini / OpenAI / any
OpenAI-compatible endpoint by adjusting the env-var name and the
`llm.*` keys (see *LLM backends* below).

```bash
# 1. Build
mvn clean install -DskipTests

# 2. Provide an API key (resolved by ${DEEPSEEK_API_KEY} placeholder in the MST file)
export DEEPSEEK_API_KEY=sk-...

# 3. The bundled demo is already wired for DeepSeek
#    (llm.model.type=openai_compatible,
#     llm.openai_compatible.url=https://api.deepseek.com/v1/chat/completions).
#    Just run:
java -jar mist-cli/target/mist.jar \
     mist-restest-adapter/src/main/resources/My-Example/trainticket-demo.properties

# 4-5. Same Allure rendering as Quick Start A.
```

## Quick Start C — your own microservice system

Generate a conf, point a `.properties` file at your spec, run.

```bash
# 1. Build (one-time)
mvn clean install -DskipTests

# 2. Drop your inputs anywhere — for example, alongside the bundled demo:
#       <yourdir>/openapi.yaml
#       <yourdir>/test-trace/*.json   (one or more Jaeger / OTel traces)

# 3. Generate the MST test configuration from your spec (one-time per spec change).
#    No source edits required — pass paths positionally:
java -cp mist-cli/target/mist.jar io.mist.cli.MistConfGenMain \
     <yourdir>/openapi.yaml \
     <yourdir>/your-mst-conf.yaml

# 4. Copy the bundled .properties files as a starting point:
#       cp mist-restest-adapter/src/main/resources/My-Example/trainticket-demo.properties \
#          <yourdir>/system-demo.properties
#       cp mist-restest-adapter/src/main/resources/My-Example/trainticket-mst.properties \
#          <yourdir>/system-mst.properties
#    Then in <yourdir>/system-demo.properties update FIVE keys (paths are
#    resolved relative to the .properties file, so write them relative to
#    <yourdir>):
#       oas.path           → openapi.yaml
#       conf.path          → your-mst-conf.yaml         (from step 3)
#       trace.file.path    → test-trace/
#       base.url           → your system's HTTP entry point
#       mst.config.path    → system-mst.properties      (the MST overlay)

# 5. Launch:
java -jar mist-cli/target/mist.jar <yourdir>/system-demo.properties
```

In IntelliJ, the **"MIST: Generate MST Conf From OAS"** and
**"MIST: Demo (bundled TrainTicket)"** run configurations are
templates you can copy + edit for your own SUT.

After any run, the fault-detection report lands under
`logs/fault-detection-reports/`, CSV stats under `target/test-data/`,
and the generated JUnit sources under the directory you pointed
`test.target.dir` at.

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

Configuration is **split into two files** — the core `.properties` you
pass on the command line plus an MST overlay it references via
`mst.config.path`. The split keeps the ~70 MIST-specific keys out of
sight of the classic generators living in `mist-restest-adapter`
(RT / CBT / ART / FT / LLM, the RESTest modes the adapter still
carries as a library).

```
mist-restest-adapter/src/main/resources/My-Example/
├── trainticket-demo.properties        # core (~30 keys) + mst.config.path pointer
├── trainticket-demo-noexec.properties # core, but experiment.execute=false and
│                                      # network-dependent toggles off — smoke
│                                      # profile for sandboxes
├── trainticket-mst.properties         # MST overlay (~70 keys: LLM, smart fetch,
│                                      # jaeger, fault detection, enhancer,
│                                      # status-code exploration, root-API
│                                      # registry, trace merging, auth, …)
└── trainticket-mst-noexec.properties  # MST overlay for the no-exec profile
                                       # (jaeger / smart-fetch / llm / enhancer /
                                       # status-code-exploration all disabled)
```

`mst.config.path` itself and every INPUT-path key inside both files
(`oas.path`, `conf.path`, `trace.file.path`,
`fault.detection.injected.faults.path`, the various registry paths) are
resolved **relative to the .properties file's own directory** by
[`MistPathResolver`](mist-restest-adapter/src/main/java/es/us/isa/restest/main/MistPathResolver.java)
at startup. This is why the bundled values look like
`trainticket/merged_openapi_spec 1.yaml` rather than the old
`src/main/resources/My-Example/trainticket/…` form — the path no
longer depends on where the user launches MIST from.

OUTPUT paths (`test.target.dir`, `allure.results.dir`,
`data.tests.dir`, the various `.mist/*-cache.json` keys) keep the
Maven convention of being relative to the JVM CWD; the run
configurations under
[`.idea/runConfigurations/`](.idea/runConfigurations) pin CWD to
`$PROJECT_DIR$` so the IDE matches the CLI.

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

A missing key resolves to the empty string, in which case the request is sent unauthenticated rather than literally containing the placeholder text. The full resolution logic lives in [`LLMConfig.resolveEnvPlaceholder`](mist-restest-adapter/src/main/java/es/us/isa/restest/llm/LLMConfig.java).

---

## Auth strategy for the *target* system

The generated tests use [`MstAuthHandler`](mist-restest-adapter/src/main/java/es/us/isa/restest/auth/MstAuthHandler.java) to obtain and stamp tokens. Configured by `auth.*` keys in the MST file:

| `auth.mode` | Effect |
|---|---|
| `none` | No `Authorization` header on any request. |
| `static_token` | Use `auth.static.token` verbatim; never call `/login`. |
| `per_jvm` (default) | Lazy login on first request, cached for the whole JVM. ~2400 logins → ~1. |
| `per_test` | Legacy: fresh login per test method. |

Endpoints matching `auth.skip.path.patterns` (CSV of regex, e.g. `^/actuator,^/api/v1/users/login`) are sent without an `Authorization` header regardless of mode. On HTTP 401, [`MstAuthRefreshFilter`](mist-restest-adapter/src/main/java/es/us/isa/restest/auth/MstAuthRefreshFilter.java) invalidates the cached token and retries once — disabled automatically for tests that intentionally manipulate auth (e.g. `INVALID_TOKEN` exploration tests).

---

## Outputs

| Path (relative to JVM CWD) | Content |
|---|---|
| `<test.target.dir>/<package>/<TestClassName>_<timestamp>/` | Generated JUnit test sources (default `src/test/java`) |
| `target/test-classes/<package>/...`            | Compiled `.class` files |
| `target/allure-results/`                       | Raw Allure JSON (per test) |
| `target/allure-report/`                        | Rendered HTML report (after `allure generate`) |
| `logs/fault-detection-reports/`                | Injected-fault detection summary, matched against `injectedFaults/injected-faults.json` |
| `.mist/llm-call-cache.json`                    | SHA-256-keyed cache of LLM responses — replays make `-Drandom.seed` runs reproducible |
| `.mist/parameter-error-analysis-cache.json`    | Parameter-error analyser cache |
| `.mist/intelligent-analysis-cache.json`        | Trace error analyser intelligent cache |
| `.mist/trace-shape-invariants.json`            | Phase 2 Trace Shape Oracle persisted invariants |
| `target/test-data/`                            | CSV stats (test cases, results, time) |
| `target/mist-mined-fault-types.yaml`           | Phase 3 mined SUT-specific fault categories (when `mist.fault.mining.enabled=true`) |

> The legacy `target/soft-error-rule-cache.json` is gone — its contract
> moved into the Phase 2 `ResponseEnvelopeInvariant` and the persisted
> invariant store at `.mist/trace-shape-invariants.json`. See Phase 2.E
> of [`PATH_B_REBUILD_PLAN.md`](debug/Conference-refinement/PATH_B_REBUILD_PLAN.md).

---

## Repository data showcase

The TrainTicket dataset bundled with the tool, all under
`mist-restest-adapter/src/main/resources/My-Example/trainticket/`:

| Asset | Description |
|---|---|
| `merged_openapi_spec 1.yaml` | 265-operation merged OpenAPI spec; MIST's black-box scope covers the 37 REST-exposed services |
| `real-system-conf.yaml` | Auto-generated MIST test configuration. Do not edit by hand — re-run `io.mist.cli.MistConfGenMain <spec> <out>` |
| `test-trace/*.json` | OpenTelemetry traces used to mine workflow scenarios |
| `injectedFaults/injected-faults.json` | Ground-truth fault registry for detection-rate evaluation |
| `flow.md` | Full algorithm documentation (extraction → merging → shattering → generation) |

---

## Repository layout (post-Path-B)

```
mist-parent (root pom.xml, packaging=pom)
├── mist-core/
│   └── src/main/java/io/mist/core/
│       ├── oracle/shape/              Trace Shape Oracle (Phase 2):
│       │                              SpanTreeShape / StatusPropagation /
│       │                              TimingEnvelope / ResponseEnvelope
│       │                              invariants + Learner + Oracle + Verdict
│       └── fault/                     Adaptive Fault Taxonomy (Phase 3):
│                                      FaultType + FaultTypeRegistry +
│                                      ApplicabilityMatrix + FaultMiner
├── mist-llm/                          (placeholder for future LLM module)
├── mist-restest-adapter/
│   └── src/main/java/es/us/isa/restest/
│       ├── auth/                      MstAuthHandler, MstAuthRefreshFilter
│       ├── configuration/multiservice/ MicroserviceTestConfigurationGenerator, …
│       ├── generators/                MultiServiceTestCaseGenerator (MST),
│       │                              classic RT / CBT / ART / FT / LLM generators
│       ├── workflow/                  TraceWorkflowExtractor, scenario merging,
│       │                              WorkflowPipeline + Phase 2.5–4 stages
│       ├── writers/restassured/       MultiServiceRESTAssuredWriter,
│       │                              RESTAssuredWriter
│       ├── enhancer/                  Test-case enhancer + status-code exploration
│       ├── analysis/                  FaultDetectionTracker, TraceErrorAnalyzer
│       └── main/                      TestGenerationAndExecution (legacy entry),
│                                      MistRunner, MistRunResult, MistPathResolver
└── mist-cli/
    └── src/main/java/io/mist/cli/
        ├── MistMain                    → java -jar mist-cli/target/mist.jar
        └── MistConfGenMain             → MST conf generator (replaces the old
                                          MicroserviceConfBuilderMain)
```

The classic RESTest modes (`RT / CBT / ART / FT / LLM`) still live in
`mist-restest-adapter` and run via the legacy
`es.us.isa.restest.main.TestGenerationAndExecution`. They are not the
recommended entry for MIST work but stay alive as a library surface.

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

MIST is built on top of RESTest internals (loaded as a library
dependency from `mist-restest-adapter`); please also cite:

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
