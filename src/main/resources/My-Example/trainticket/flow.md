### MST Mode End-to-End Flow (TestGenerationAndExecution.java)

```mermaid
flowchart TD
    A[Start RESTest MST] --> B[Read properties]
    B --> C{generator == "MST"?}
    C -- No --> Z[Classic modes (RT/CBT/FT/ART/LLM)]
    C -- Yes --> D[Init FaultDetectionTracker + load injected faults]
    D --> E[Load OpenAPI spec (oas.path)]
    E --> F[Load multi-service YAML (conf.path) → serviceConfigs]
    F --> G[Build serviceSpecs map (service → spec)]
    G --> H[Extract scenarios from traces (TraceFile)]
    H --> I[Deduplicate scenarios]
    I --> J{root.api.registry.path set?}
    J -- Yes --> K[Init RootApiRegistry; register trees; save]
    J -- No --> L[Skip registry]
    K --> M
    L --> M
    M[Propagate MST props → System.setProperty]
    M --> M1[testsperoperation | test.variants.per.scenario]
    M --> M2[mst.generate.only.first.step]
    M --> M3[smart.input.fetch.* + llm.* + auth.*]
    M3 --> N[Create MultiServiceTestCaseGenerator(useLLM=true)]
    N --> O[Configure MultiServiceRESTAssuredWriter(className,id, baseUrl)]
    O --> P[generator.generate()]
    P --> Q[statsReportManager.setTestCases]
    Q --> R[writer.write(testCases) → multiple .java files]
    R --> S{experiment.execute?}
    S -- true --> T[executeGeneratedTestsWithJUnit]
    T --> T1[Clean old test-classes; setup Allure for IDE]
    T1 --> T2[Compile tests (JavaCompiler → fallback Maven)]
    T2 --> T3[Add target/test-classes to classpath]
    T3 --> T4[Load classes; JUnitCore + AllureJunit4]
    T4 --> T5[Run; log results]
    T5 --> U{allure.report?}
    U -- true --> V[AllureReportManager.generateReport]
    T5 --> W[Generate Fault Detection Report]
    S -- false --> X[Skip execution]
    V --> Y[StatsReportManager.generateReport(id, execute)]
    W --> Y
    X --> Y
    Y --> AA[End]
```

### MST Test Case and Input Generation (MultiServiceTestCaseGenerator)

```mermaid
flowchart TD
    A[generate()] --> B[Group scenarios by root API]
    B --> C[Generate shared parameter pools per root API]
    C --> D[For each scenario → generateScenarioVariants]
    D --> E[get variantCount from System properties]
    E --> F[For v in 1..variantCount → build MultiServiceTestCase]
    F --> G[Traverse trace tree DFS]
    G --> H{Is span HTTP op?}
    H -- No --> H1[skip; visit children] --> G
    H -- Yes --> I[Load service op config (verb+path)]
    I --> J{Step is first business step?}
    J -- Yes --> K[For each parameter]
    K --> K1[Try Smart Fetch]
    K1 -->|success| K2[use value]
    K1 -->|fail/disabled| K3[LLM fallback]
    K2 --> L
    K3 --> L[Collect path/query/header/body maps]
    J -- No --> M[For each parameter]
    M --> M1[Check context dependency (previous output)]
    M1 -->|found| L
    M1 -->|not found| M2[Check input reuse]
    M2 -->|found| L
    M2 -->|not found| M3[Check trace value]
    M3 -->|found| L
    M3 -->|not found| M4[Try Smart Fetch → else LLM]
    M4 --> L[Collect path/query/header/body maps]
    L --> N{Body selection}
    N -- step 1 → gen body from fields
    N -- step >1 → prefer trace body else gen from fields
    N --> O[Expected status: config → else successful trace → else 200]
    O --> P[Create StepCall + capture outputs]
    P --> Q[Update context with outputs and "input.*" values]
    Q --> R{mst.generate.only.first.step?}
    R -- true --> S[stop traversal]
    R -- false --> T[visit children]
    T --> G
    S --> U[Finalize variant (rename by first business API)]
    U --> V[next variant]
```

### Shared Parameter Pool Generation (per root API)

```mermaid
flowchart LR
    A[Identify first business op (verb+path)] --> B[Load op TestParameters]
    B --> C[For each parameter]
    C --> D[Smart Fetch up to 15 values]
    D --> E[If <15, get LLM seed values]
    E --> F[Semantic expand (Word2Vec/BERT) to needed count]
    F --> G[Pool: smart + expanded + fallback]
```

### Smart Input Fetching Flow (SmartInputFetcher)

```mermaid
flowchart TD
    A[fetchSmartInput(ParameterInfo)] --> B{enabled?}
    B -- No --> C[Fallback to LLM]
    B -- Yes --> D[Random < smart.fetch.percentage?]
    D -- No --> C[Fallback to LLM]
    D -- Yes --> E[Clear invalid cached]
    E --> F[Next diverse cached value?]
    F -- Yes --> G[Return cached]
    F -- No --> H[Fetch from smart sources]
    H --> I[Load mappings from registry]
    I --> J{Empty and discovery enabled?}
    J -- Yes --> K[Discover mappings (patterns + LLM)]
    J -- No --> L[Proceed]
    K --> L
    L --> M[Rank by score; limit max.candidates]
    M --> N[Iterate candidates → call endpoint]
    N --> O[Validate value; update success; cache]
    O -->|valid| P[Return]
    O -->|invalid| N
    N -->|none valid| C[Fallback to LLM]
```

### LLM Communication Path (LLMService)

```mermaid
flowchart LR
    A[generateText(system,user,maxTokens,temp)] --> B{llm.enabled && config valid?}
    B -- No --> C[Return null]
    B -- Yes --> D[Log request (LLMCommunicationLogger)]
    D --> E{model.type}
    E -- GEMINI --> F[GeminiApiClient]
    E -- LOCAL  --> G[Local HTTP endpoint]
    E -- OLLAMA --> H[OllamaApiClient]
    F --> I
    G --> I
    H --> I[Result]
    I --> J[Log response]
    J --> K[Return text]
```

### Writer and Execution Flow (MultiServiceRESTAssuredWriter + IntelliJ runner)

```mermaid
flowchart TD
    A[writer.write(testCases)] --> B[Group by scenarioName]
    B --> C[Create package dir: packageName/testClassName]
    C --> D[Emit one .java per scenario]
    D --> E[JUnit + REST-assured code; optional Allure + Jaeger attachments]
    E --> F[executeGeneratedTestsWithJUnit]
    F --> G[Clean target/test-classes (isolation)]
    G --> H[Setup Allure results dir; clean old results]
    H --> I[Compile tests via JavaCompiler; fallback Maven]
    I --> J[Add target/test-classes to context classloader]
    J --> K[Load classes; JUnitCore + AllureJunit4]
    K --> L[Run tests; log results]
```

### Conditions, Flags, and Inputs (key branches)

- **generator == MST**: switches to multi-service flow
- **testsperoperation / test.variants.per.scenario**: number of variants per scenario
- **mst.generate.only.first.step**: generate only first business step (writer handles login as step 0)
- **smart.input.fetch.enabled**: enables Smart Fetch system
- **smart.input.fetch.percentage**: probability Smart Fetch vs LLM
- **smart.input.fetch.registry.path**: registry used for mappings and learning
- **smart.input.fetch.llm.discovery.enabled**: allows LLM-based mapping discovery
- **smart.input.fetch.max.candidates**: max endpoints tried per parameter
- **smart.input.fetch.dependency.resolution.enabled**: resolve parameter dependencies
- **smart.input.fetch.discovery.timeout.ms**: discovery request timeout
- **smart.input.fetch.cache.enabled / ttl**: cache values and TTL
- **llm.enabled**: enables LLM; **llm.model.type**: gemini/local/ollama
- **llm.gemini.*, llm.local.*, llm.ollama.***: backend-specific
- **llm.rate.limit.retry.enabled / max.retries**: retry policy
- **root.api.registry.path**: enables Root API registry population from scenarios
- **allure.report**: generate Allure report after execution
- **experiment.execute**: execute tests vs only generate
- **deletepreviousresults**: clean allure/data outputs before run
- **fault.detection.injected.faults.path / fault.detection.report.dir**: fault injection & reporting

### Notes on Data/Status Selection

- First business step: parameters prefer Smart Fetch → LLM fallback; body from generated fields
- Subsequent steps: prefer dependencies (prev outputs → inputs) → trace → Smart Fetch → LLM → fallback
- Expected status: config → successful status from trace (if config 200) → default 200


