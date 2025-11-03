### MST Mode End-to-End Flow (TestGenerationAndExecution.java)

```mermaid
flowchart TD
    A[Start RESTest MST] --> B[Read properties]
    B --> C{generator equals MST}
    C -->|No| Z[Classic modes RT CBT FT ART LLM]
    C -->|Yes| D[Init fault detection and load injected faults]
    D --> E[Load OpenAPI spec]
    E --> F[Load multi service YAML to serviceConfigs]
    F --> G[Build serviceSpecs map]
    G --> H[Extract scenarios from traces]
    H --> I[Deduplicate scenarios]
    I --> J{root api registry path set}
    J -->|Yes| K[Init RootApiRegistry and register trees]
    J -->|No| L[Skip registry]
    K --> M
    L --> M
    M[Propagate MST properties]
    M --> M1[testsperoperation or test variants per scenario]
    M --> M2[mst generate only first step]
    M --> M3[smart input fetch and llm and auth]
    M1 --> N[Create MST generator use LLM]
    M2 --> N
    M3 --> N
    N --> O[Configure MST writer]
    O --> P[Run generator]
    P --> Q[Set stats test cases]
    Q --> R[Write tests multiple files]
    R --> S{experiment execute}
    S -->|true| T[Execute generated tests]
    T --> T1[Clean test classes and setup Allure]
    T1 --> T2[Compile tests then fallback maven]
    T2 --> T3[Add target/test-classes to classpath]
    T3 --> T4[Load classes and attach Allure]
    T4 --> T5[Run; log results]
    T5 --> U{allure report}
    U -->|true| V[Generate Allure report]
    T5 --> W[Generate fault detection report]
    S -->|false| X[Skip execution]
    V --> Y[Generate stats report]
    W --> Y
    X --> Y
    Y --> AA[End]
```

### MST Test Case and Input Generation (MultiServiceTestCaseGenerator)

```mermaid
flowchart TD
    A[generate] --> B[Group scenarios by root API]
    B --> C[Generate shared parameter pools per root API]
    C --> D[For each scenario generateScenarioVariants]
    D --> E[get variantCount from System properties]
    E --> F[For each v build MultiServiceTestCase]
    F --> G[Traverse trace tree DFS]
    G --> H{Is span HTTP op}
    H -->|No| H1[skip; visit children]
    H1 --> G
    H -->|Yes| I[Load service operation config]
    I --> J{Is first business step}
    J -->|Yes| K[For each parameter]
    K --> K1[Try Smart Fetch]
    K1 -->|success| K2[use value]
    K1 -->|fail or disabled| K3[LLM fallback]
    K2 --> L
    K3 --> L[Collect path/query/header/body maps]
    J -->|No| M[For each parameter]
    M --> M1[Check previous output dependency]
    M1 -->|found| L
    M1 -->|not found| M2[Check input reuse]
    M2 -->|found| L
    M2 -->|not found| M3[Check trace value]
    M3 -->|found| L
    M3 -->|not found| M4[Try Smart Fetch or LLM]
    M4 --> L[Collect path/query/header/body maps]
    L --> N{Body selection}
    N -->|step one| B1[Generate body from fields]
    N -->|step later| B2[Prefer trace body or generate]
    B1 --> O[Expected status logic]
    B2 --> O
    O --> P[Create StepCall and capture outputs]
    P --> Q[Update context with outputs and inputs]
    Q --> R{first step only}
    R -->|true| S[Stop traversal]
    R -->|false| T[Visit children]
    T --> G
    S --> U[Finalize variant rename by first business API]
    U --> V[next variant]
```

### Shared Parameter Pool Generation (per root API)

```mermaid
flowchart LR
    A[Identify first business operation] --> B[Load operation test parameters]
    B --> C[For each parameter]
    C --> D[Smart Fetch up to 15 values]
    D --> E[If less than limit get LLM seed values]
    E --> F[Semantic expand to needed count]
    F --> G[Pool smart plus expanded plus fallback]
```

### Smart Input Fetching Flow (SmartInputFetcher)

```mermaid
flowchart TD
    A[fetchSmartInput] --> B{enabled}
    B -->|No| C[Fallback to LLM]
    B -->|Yes| D[Random below threshold]
    D -->|No| C[Fallback to LLM]
    D -->|Yes| E[Clear invalid cached]
    E --> F[Next diverse cached value?]
    F -->|Yes| G[Return cached]
    F -->|No| H[Fetch from smart sources]
    H --> I[Load mappings from registry]
    I --> J{Empty and discovery enabled}
    J -->|Yes| K[Discover mappings]
    J -->|No| L[Proceed]
    K --> L
    L --> M[Rank by score then limit]
    M --> N[Iterate candidates then call endpoint]
    N --> O[Validate update cache]
    O -->|valid| P[Return]
    O -->|invalid| N
    N -->|none valid| C[Fallback to LLM]
```

### LLM Communication Path (LLMService)

```mermaid
flowchart LR
    A[generateText] --> B{llm enabled and config valid}
    B -->|No| C[Return null]
    B -->|Yes| D[Log LLM request]
    D --> E{model type}
    E -- GEMINI --> F[GeminiApiClient]
    E -- LOCAL --> G[Local HTTP]
    E -- OLLAMA --> H[OllamaApiClient]
    F --> I
    G --> I
    H --> I[Result]
    I --> J[Log LLM response]
    J --> K[Return text]
```

### Writer and Execution Flow (MultiServiceRESTAssuredWriter + IntelliJ runner)

```mermaid
flowchart TD
    A[Write tests] --> B[Group by scenario name]
    B --> C[Create package directory]
    C --> D[Emit one file per scenario]
    D --> E[Add JUnit and REST assured code]
    E --> F[Execute generated tests]
    F --> G[Clean test classes isolation]
    G --> H[Setup Allure results and clean]
    H --> I[Compile tests then maybe Maven]
    I --> J[Add test classes to classloader]
    J --> K[Load classes and attach Allure]
    K --> L[Run tests and log]
```

### Conditions, Flags, and Inputs (key branches)

- generator == MST: switches to multi-service flow
- testsperoperation / test.variants.per.scenario: number of variants per scenario
- mst.generate.only.first.step: generate only first business step (writer handles login as step 0)
- smart.input.fetch.enabled: enables Smart Fetch system
- smart.input.fetch.percentage: probability Smart Fetch vs LLM
- smart.input.fetch.registry.path: registry used for mappings and learning
- smart.input.fetch.llm.discovery.enabled: allows LLM-based mapping discovery
- smart.input.fetch.max.candidates: max endpoints tried per parameter
- smart.input.fetch.dependency.resolution.enabled: resolve parameter dependencies
- smart.input.fetch.discovery.timeout.ms: discovery request timeout
- smart.input.fetch.cache.enabled / ttl: cache values and TTL
- llm.enabled: enables LLM; llm.model.type: gemini/local/ollama
- llm.gemini.*, llm.local.*, llm.ollama.*: backend-specific
- llm.rate.limit.retry.enabled / max.retries: retry policy
- root.api.registry.path: enables Root API registry population from scenarios
- allure.report: generate Allure report after execution
- experiment.execute: execute tests vs only generate
- deletepreviousresults: clean allure/data outputs before run
- fault.detection.injected.faults.path / fault.detection.report.dir: fault injection & reporting

### Notes on Data/Status Selection

- First business step: parameters prefer Smart Fetch -> LLM fallback; body from generated fields
- Subsequent steps: prefer dependencies (prev outputs -> inputs) -> trace -> Smart Fetch -> LLM -> fallback
- Expected status: config -> successful status from trace (if config 200) -> default 200


