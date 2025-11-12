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
    H --> J{root api registry path set}
    J -->|Yes| K[Init RootApiRegistry and register ALL scenarios]
    J -->|No| L[Skip registry]
    K --> I[Deduplicate scenarios for test generation]
    L --> I
    I --> M[After deduplication: unique scenarios for tests]
    M[Propagate MST properties]
    M --> M1[testsperoperation or test variants per scenario]
    M --> M2[mst generate only first step]
    M --> M3[smart input fetch and llm and auth]
    M --> M4[faulty ratio and faulty round-robin]
    M1 --> N[Create MST generator use LLM]
    M2 --> N
    M3 --> N
    M4 --> N
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

**Critical Design Decision: Registry Before Deduplication**

The MST flow registers **ALL scenarios** to the Root API Registry BEFORE deduplication:

1. **Extract scenarios from traces** → Get all workflow patterns from Jaeger
2. **Register ALL scenarios** → Root API Registry learns from every trace (even duplicates)
   - Why? Because different traces with the same root API may have different execution patterns, timings, or data flows
   - The registry benefits from seeing all variations to build a comprehensive understanding
3. **Then deduplicate scenarios** → Remove scenarios with the same ROOT API for test generation
   - Why? To avoid generating redundant test cases that would waste resources
   - **Deduplication is based on ROOT API ONLY** (HTTP method + path of first business API call)
   - Different downstream workflows (success vs failure traces) are treated as duplicates if they share the same root API

**Example:**
- 2 traces both start with `POST /api/v1/adminorder` (same root API)
  - Trace 1: Admin order success → downstream calls (station, route, database)
  - Trace 2: Admin order failure → error handling calls
- Both are registered in the Root API Registry (learning from both success and failure patterns)
- But only 1 scenario generates test cases (avoiding redundant tests for the same root API)
- Result: 15 test cases instead of 30 (2 × 15)

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
    K --> K0{Is target faulty param}
    K0 -->|Yes| K00[Use faulty value and lock]
    K0 -->|No| K1[Try Smart Fetch]
    K1 -->|success| K2[use value]
    K1 -->|fail or disabled| K3[LLM fallback]
    K00 --> L
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
flowchart TD
    A[Identify first business operation] --> B[Load operation test parameters]
    B --> C[For each parameter]
    C --> D[Smart Fetch up to 15 values]
    D --> E[If less than limit get LLM seed values]
    E --> F[Semantic expand to needed count]
    F --> G[Pool smart plus expanded plus fallback]
    
    C --> H[Generate comprehensive InvalidInputPool]
    H --> I1[TYPE_MISMATCH: Ask LLM for wrong types]
    I1 --> I2[Parse typed values: integer:55, boolean:true]
    I2 --> I3[Add defaults based on param type]
    I3 --> I4[REGEX_MISMATCH: Ask LLM for pattern violations]
    I4 --> I5[SEMANTIC_MISMATCH: Ask LLM for meaningless values]
    I5 --> I6[OVERFLOW: Ask LLM for huge values]
    I6 --> I7[EMPTY_INPUT: Add empty string, whitespace]
    I7 --> I8[NULL_INPUT: Add null, 'null', 'NULL']
    I8 --> I9[SPECIAL_CHARACTERS: SQL injection, XSS]
    I9 --> I10[BOUNDARY_VIOLATION: Off-by-one errors]
    I10 --> J[Store InvalidInputPool by root API key]
    J --> K[Pool tracks usage for round-robin]
```

### Negative Test Selection with 8 Fault Types (Configurable Strategy)

```mermaid
flowchart TD
    A[Read faulty.ratio and faulty.round-robin] --> B[Calculate negative test count]
    B --> C[Randomly select which variants are negative]
    C --> D[Initialize parameter rotation list from pool keys]
    D --> E[For each variant]
    E --> F{Is negative variant}
    F -->|No| G[Generate positive test]
    F -->|Yes| H{faulty.round-robin}
    H -->|true| I[ROUND-ROBIN MODE]
    H -->|false| J[RANDOM MODE]
    
    I --> I1[Get param at rotation index]
    I1 --> I2[Get InvalidInputPool for param]
    I2 --> I3[Call pool.getNextRoundRobin]
    I3 --> I4{Value available?}
    I4 -->|Yes| I5[Got typed value e.g. Integer 55]
    I4 -->|No| I6[All values exhausted]
    I6 --> G[Generate positive test instead]
    I5 --> I7[Convert to string based on type]
    I7 --> I8[Track invalid param in test case]
    I8 --> K[Increment rotation index]
    
    J --> J1[Select 1-3 params randomly]
    J1 --> J2[Get InvalidInputPool for param]
    J2 --> J3[Call pool.getRandomValue]
    J3 --> J4[Got typed value can repeat]
    J4 --> J5[Convert to string based on type]
    J5 --> J6[Track invalid param in test case]
    
    K --> L[For each parameter in operation]
    J6 --> L
    L --> M{Is target invalid param}
    M -->|Yes| N[Use invalid value LOCKED]
    M -->|No| O[Use normal smart fetch or LLM]
    N --> P[Log fault type and value]
    O --> Q[Continue to next parameter]
    P --> Q
    Q --> R{More parameters}
    R -->|Yes| L
    R -->|No| S[Complete negative test]
```

**Strategies**:
- **Round-Robin** (faulty.round-robin=true, default): 
  - ONE invalid param per test, cycling through all params
  - For each param, cycles through all 8 fault types and their values
  - NO REPETITION until all invalid values exhausted
  - **All 8 Fault Types (with examples)**:
    1. **TYPE_MISMATCH**: Wrong data type
       - Example: String param gets `Integer(55)` or `Boolean(true)`
       - Example: Number param gets `"not_a_number"` or `Boolean(false)`
    2. **REGEX_MISMATCH**: Pattern violation
       - Example: Email param gets `"invalid@format"` (missing domain)
       - Example: Phone param gets `"123"` (wrong format)
       - Example: Date param gets `"not-a-date"` (invalid format)
    3. **SEMANTIC_MISMATCH**: Meaningless/impossible value
       - Example: Age param gets `-5` (negative age)
       - Example: Birthdate param gets `"2050-01-01"` (future date)
       - Example: Status param gets `"INVALID_STATUS"` (not in enum)
    4. **OVERFLOW**: Exceeds limits
       - Example: String param gets `"AAAA..."` (1000+ characters)
       - Example: Number param gets `9999999999` (beyond max)
       - Example: Path param gets `tripId_1234567890_1234567890_...` (very long)
    5. **EMPTY_INPUT**: Empty values ( !ONLY for REQUIRED params)
       - Example: Required string gets `""` (empty string)
       - Example: Required string gets `"   "` (whitespace only)
       - Example: Required array gets `[]` (empty array)
    6. **NULL_INPUT**: Null values ( !ONLY for REQUIRED params)
       - Example: Required param gets `null` (actual null)
       - Example: Required param gets `"null"` (string "null")
       - Example: Required param gets `"NULL"` (string "NULL")
    7. **SPECIAL_CHARACTERS**: Injection attempts
       - Example: SQL injection: `"' OR '1'='1"`
       - Example: XSS attack: `"<script>alert('XSS')</script>"`
       - Example: Path traversal: `"../../../etc/passwd"`
       - Example: Command injection: `"; rm -rf /"`
    8. **BOUNDARY_VIOLATION**: Off-by-one errors
       - Example: Min=1 param gets `0` (min-1)
       - Example: Max=100 param gets `101` (max+1)
       - Example: MinLength=5 param gets `"abcd"` (length 4)
       - Example: MaxLength=10 param gets `"12345678901"` (length 11)
  - **Example Round-Robin Sequence**:
    - Test 1: paramA=TYPE_MISMATCH(Integer(55))
    - Test 2: paramB=REGEX_MISMATCH("invalid@format")
    - Test 3: paramA=SEMANTIC_MISMATCH(-5)
    - Test 4: paramC=OVERFLOW("AAAA..." (1000 chars))
    - Test 5: paramA=EMPTY_INPUT("")  (if paramA is required)
    - Test 6: paramB=NULL_INPUT(null)  (if paramB is required)
    - Test 7: paramA=SPECIAL_CHARACTERS("' OR '1'='1")
    - Test 8: paramC=BOUNDARY_VIOLATION(0)  (if min=1)
    - Test 9: paramA=TYPE_MISMATCH(Boolean(true))  (next value for paramA)
    - ... continues until all invalid values exhausted
  - When exhausted: generates positive tests
  
- **Random** (faulty.round-robin=false):
  - 1-3 randomly selected invalid params per test
  - Random fault type and value selection
  - CAN REPEAT values across tests
  - Test 1: [paramA=TYPE_MISMATCH(55), paramC=NULL_INPUT(null)]
  - Test 2: [paramB=SPECIAL_CHARACTERS("' OR '1'='1")]
  - Test 3: [paramA=TYPE_MISMATCH(55), paramB=EMPTY_INPUT(""), paramC=OVERFLOW("AAAA...")]

### Negative Test Reporting (Allure Integration)

```mermaid
flowchart LR
    A[Test case marked as negative] --> B[Track invalid parameters during generation]
    B --> C[Writer checks if test is negative]
    C --> D[Add Allure parameter Test Type NEGATIVE]
    D --> E[Add Allure parameter Invalid Parameters list]
    E --> F[Add Allure description with invalid values and fault types]
    F --> G[Report displays in Allure with warning icon]
    G --> H[Test expects 4XX/5XX error response]
    H --> I[If 2XX received test FAILS]
```

### 8 Invalid Input Types (Comprehensive Coverage)

```mermaid
flowchart TD
    A[Invalid Input Types] --> B1[TYPE_MISMATCH]
    A --> B2[REGEX_MISMATCH]
    A --> B3[SEMANTIC_MISMATCH]
    A --> B4[OVERFLOW]
    A --> B5[EMPTY_INPUT *]
    A --> B6[NULL_INPUT *]
    A --> B7[SPECIAL_CHARACTERS]
    A --> B8[BOUNDARY_VIOLATION]
    
    B1 --> C1[Wrong data type<br/>String param gets Integer 55]
    B2 --> C2[Pattern violation<br/>Email without @ symbol]
    B3 --> C3[Meaningless value<br/>Age = -5, impossible date]
    B4 --> C4[Exceeds limits<br/>10000 char string, MAX_INT]
    B5 --> C5[Empty values<br/>Empty string, whitespace, []<br/>ONLY for REQUIRED params]
    B6 --> C6[Null values<br/>null, 'null', 'NULL'<br/>ONLY for REQUIRED params]
    B7 --> C7[Injection attempts<br/>SQL injection, XSS, traversal]
    B8 --> C8[Boundary errors<br/>Off-by-one, min-1, max+1]
    
    Note1[* Empty/Null inputs are VALID for optional parameters<br/>They are only generated for required parameters]
```

**Important: Optional Parameter Handling**
- **EMPTY_INPUT** and **NULL_INPUT** are only generated for **required parameters** (`required: true`)
- For **optional parameters** (`required: false` or not specified), null/empty values are **valid** and should NOT be used for negative testing
- This ensures negative tests only target true violations, not legitimate optional parameter behavior

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
- faulty.ratio: percentage of test variants that should be intentionally faulty (e.g., 0.1 = 10%)
- faulty.round-robin: true (default) = one param per test cycling, false = 1-3 random params per test
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


