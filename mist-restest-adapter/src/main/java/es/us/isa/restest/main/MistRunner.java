package es.us.isa.restest.main;

import es.us.isa.restest.analysis.FaultDetectionTracker;
import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationIO;
import es.us.isa.restest.configuration.pojos.Auth;
import es.us.isa.restest.configuration.pojos.TestConfiguration;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.coverage.CoverageGatherer;
import es.us.isa.restest.coverage.CoverageMeter;
import es.us.isa.restest.enhancer.FailedTestCollector;
import es.us.isa.restest.enhancer.FailedTestResult;
import es.us.isa.restest.enhancer.StatusCodeExplorationEnhancer;
import es.us.isa.restest.enhancer.TestCaseEnhancer;
import es.us.isa.restest.enhancer.TestFileRegenerator;
import es.us.isa.restest.enhancer.TestResultCapture;
import es.us.isa.restest.generators.AbstractTestCaseGenerator;
import es.us.isa.restest.generators.MultiServiceTestCaseGenerator;
import io.mist.llm.LLMService;
import es.us.isa.restest.registry.RootApiRegistry;
import es.us.isa.restest.reporting.AllureReportManager;
import es.us.isa.restest.reporting.StatsReportManager;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.ConsoleProgressBar;
import es.us.isa.restest.util.IDGenerator;
import es.us.isa.restest.util.PropertyManager;
import es.us.isa.restest.util.RESTestException;
import es.us.isa.restest.util.Timer;
import es.us.isa.restest.workflow.TraceWorkflowExtractor;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowScenarioUtils;
import es.us.isa.restest.writers.IWriter;
import es.us.isa.restest.writers.restassured.MultiServiceRESTAssuredWriter;
import io.mist.core.oracle.shape.ShapeInvariantStore;
import io.mist.core.oracle.shape.TraceShapeLearner;
import io.mist.core.oracle.shape.TraceShapeOracle;
import io.qameta.allure.junit4.AllureJunit4;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static es.us.isa.restest.util.FileManager.createDir;
import static es.us.isa.restest.util.FileManager.deleteDir;

/**
 * Lifted MST-only entry point. Stage 1.A of {@code PATH_B_REBUILD_PLAN.md}
 * moves every MST-specific code path out of {@link TestGenerationAndExecution}
 * into this class. Behavior is preserved verbatim; only the static-to-instance
 * rewrite of previously-static fields differs.
 */
public final class MistRunner {

    private static final Logger logger = LogManager.getLogger(MistRunner.class.getName());

    private final es.us.isa.restest.configuration.MstConfig config;
    private final Path workdir;
    private final Inputs inputs;

    // Previously-static MST-only state lifted from TestGenerationAndExecution.
    private MultiServiceRESTAssuredWriter mstWriter = null;
    private final List<MultiServiceTestCase> generatedMSTTestCases = new ArrayList<>();
    private long testGenerationStartTime = 0;
    private OpenAPISpecification spec;

    /**
     * Trace Shape Oracle instance for this MistRunner invocation. Cold start
     * trains from the labelled seed corpus; subsequent invocations reuse the
     * {@code .mist/trace-shape-invariants.json} file. See
     * {@link #bootstrapTraceShapeOracle()}.
     */
    private TraceShapeOracle traceShapeOracle = null;
    private Path traceShapeStorePath = null;

    public MistRunner(es.us.isa.restest.configuration.MstConfig config, Path workdir, Inputs inputs) {
        this.config = Objects.requireNonNull(config, "config");
        this.workdir = Objects.requireNonNull(workdir, "workdir");
        this.inputs = Objects.requireNonNull(inputs, "inputs");
        // NO System.getProperty calls in this constructor.
    }

    /**
     * Holds the MIST-core scalars that MistRunner needs. These are parsed by
     * {@link TestGenerationAndExecution#readParameterValues()} (or by
     * {@code MistMain} directly) before delegating to {@link #run()} so
     * MistRunner does not have to re-parse args.
     */
    public static final class Inputs {
        private final String testClassName;
        private final String targetDirJava;
        private final String packageName;
        private final String experimentName;
        private final String oasPath;
        private final String confPath;
        private final String propertiesFilePath;
        private final String mstPropertiesFilePath;
        private final String traceFilePath;
        private final Integer numTestCases;
        private final Float faultyRatio;
        private final Boolean executeTestCases;
        private final Boolean allureReports;
        private final Boolean enableCSVStats;
        private final Boolean enableInputCoverage;
        private final Boolean enableOutputCoverage;
        private final Boolean deletePreviousResults;
        private final Boolean logToFile;
        private final boolean checkTestCases;
        private final String proxy;

        private Inputs(Builder b) {
            this.testClassName = b.testClassName;
            this.targetDirJava = b.targetDirJava;
            this.packageName = b.packageName;
            this.experimentName = b.experimentName;
            this.oasPath = b.oasPath;
            this.confPath = b.confPath;
            this.propertiesFilePath = b.propertiesFilePath;
            this.mstPropertiesFilePath = b.mstPropertiesFilePath;
            this.traceFilePath = b.traceFilePath;
            this.numTestCases = b.numTestCases;
            this.faultyRatio = b.faultyRatio;
            this.executeTestCases = b.executeTestCases;
            this.allureReports = b.allureReports;
            this.enableCSVStats = b.enableCSVStats;
            this.enableInputCoverage = b.enableInputCoverage;
            this.enableOutputCoverage = b.enableOutputCoverage;
            this.deletePreviousResults = b.deletePreviousResults;
            this.logToFile = b.logToFile;
            this.checkTestCases = b.checkTestCases;
            this.proxy = b.proxy;
        }

        public static Builder builder() { return new Builder(); }

        public static final class Builder {
            private String testClassName;
            private String targetDirJava;
            private String packageName;
            private String experimentName;
            private String oasPath;
            private String confPath;
            private String propertiesFilePath;
            private String mstPropertiesFilePath;
            private String traceFilePath;
            private Integer numTestCases;
            private Float faultyRatio;
            private Boolean executeTestCases;
            private Boolean allureReports;
            private Boolean enableCSVStats;
            private Boolean enableInputCoverage;
            private Boolean enableOutputCoverage;
            private Boolean deletePreviousResults;
            private Boolean logToFile;
            private boolean checkTestCases;
            private String proxy;

            public Builder testClassName(String v)         { this.testClassName = v; return this; }
            public Builder targetDirJava(String v)         { this.targetDirJava = v; return this; }
            public Builder packageName(String v)           { this.packageName = v; return this; }
            public Builder experimentName(String v)        { this.experimentName = v; return this; }
            public Builder oasPath(String v)               { this.oasPath = v; return this; }
            public Builder confPath(String v)              { this.confPath = v; return this; }
            public Builder propertiesFilePath(String v)    { this.propertiesFilePath = v; return this; }
            public Builder mstPropertiesFilePath(String v) { this.mstPropertiesFilePath = v; return this; }
            public Builder traceFilePath(String v)         { this.traceFilePath = v; return this; }
            public Builder numTestCases(Integer v)         { this.numTestCases = v; return this; }
            public Builder faultyRatio(Float v)            { this.faultyRatio = v; return this; }
            public Builder executeTestCases(Boolean v)     { this.executeTestCases = v; return this; }
            public Builder allureReports(Boolean v)        { this.allureReports = v; return this; }
            public Builder enableCSVStats(Boolean v)       { this.enableCSVStats = v; return this; }
            public Builder enableInputCoverage(Boolean v)  { this.enableInputCoverage = v; return this; }
            public Builder enableOutputCoverage(Boolean v) { this.enableOutputCoverage = v; return this; }
            public Builder deletePreviousResults(Boolean v){ this.deletePreviousResults = v; return this; }
            public Builder logToFile(Boolean v)            { this.logToFile = v; return this; }
            public Builder checkTestCases(boolean v)       { this.checkTestCases = v; return this; }
            public Builder proxy(String v)                 { this.proxy = v; return this; }

            public Inputs build() { return new Inputs(this); }
        }
    }

    /**
     * Replaces the MST main block formerly at L199-L326 of
     * {@link TestGenerationAndExecution#main(String[])}. Returns an exit code
     * via {@link MistRunResult} instead of calling {@code System.exit}; the
     * caller in {@code TestGenerationAndExecution.main} is responsible for
     * forwarding the exit code.
     */
    public MistRunResult run() throws Exception {
        // Create target directory if it does not exist
        createDir(inputs.targetDirJava);

        // Trace Shape Oracle (Phase 2.F): bootstrap before the writer is built
        // so the writer can be aware of the on-disk invariant store. The oracle
        // itself is consulted at test-execution time by code emitted into the
        // generated test class, which re-reads the same JSON file.
        traceShapeOracle = bootstrapTraceShapeOracle();

        // RESTest runner
        AbstractTestCaseGenerator generator = createMstGenerator();
        IWriter writer = createMstWriter();
        StatsReportManager statsReportManager = createMstStatsReportManager();
        AllureReportManager reportManager = createMstAllureReportManager();

        // MST mode: generate once and write multiple files
        logger.info("Running MST mode - generating multi-service test files");
        logger.info("ISOLATION MODE: Each run will generate and execute only newly generated tests");

        // 🔥 FIX: Store the start timestamp BEFORE test generation begins
        // This is used to filter which test files to compile/execute (instead of hardcoded 5 minutes)
        testGenerationStartTime = System.currentTimeMillis();
        logger.info("Test generation start timestamp: {} (will be used for file filtering)", testGenerationStartTime);

        // Generate unique test class name
        String id = IDGenerator.generateTimeId();
        String className = inputs.testClassName + "_" + id;
        logger.info("Generated unique test identifier: {}", className);

        // 🔍 FAULT DETECTION: Initialize tracker with injected faults
        logger.info("🔍 Initializing Fault Detection Tracker...");
        String faultsJsonPath = resolveInputPath(readParameterValue("fault.detection.injected.faults.path"));
        if (faultsJsonPath == null || faultsJsonPath.isEmpty()) {
            // Default path if not configured
            faultsJsonPath = resolveInputPath(
                    "src/main/resources/My-Example/trainticket/injectedFaults/injected-faults.json");
        }
        FaultDetectionTracker.getInstance().reset();
        FaultDetectionTracker.getInstance().loadInjectedFaults(faultsJsonPath);
        logger.info("🔍 Fault Detection Tracker initialized from: {}", faultsJsonPath);

        // Set up writer
        if (writer instanceof MultiServiceRESTAssuredWriter) {
            ((MultiServiceRESTAssuredWriter) writer).setClassName(className);
            ((MultiServiceRESTAssuredWriter) writer).setTestId(id);
            // Phase 2.F: hand the writer the Trace Shape Oracle so the emitted
            // tests carry a verdict block alongside the existing TraceErrorAnalyzer.
            ((MultiServiceRESTAssuredWriter) writer).setTraceShapeOracle(traceShapeOracle, traceShapeStorePath);
            // Store writer for status code exploration during enhancement
            mstWriter = (MultiServiceRESTAssuredWriter) writer;
        }

        // Generate test cases
        logger.info("Generating tests");
        Timer.startCounting(Timer.TestStep.TEST_SUITE_GENERATION);
        Collection<TestCase> testCases = generator.generate();
        Timer.stopCounting(Timer.TestStep.TEST_SUITE_GENERATION);

        // Store MST test cases for status code exploration (used during enhancement)
        generatedMSTTestCases.clear();
        for (TestCase tc : testCases) {
            if (tc instanceof MultiServiceTestCase) {
                generatedMSTTestCases.add((MultiServiceTestCase) tc);
            }
        }
        logger.info("📋 Stored {} MST test cases for status code exploration", generatedMSTTestCases.size());

        // Pass test cases to the statistic report manager (CSV writing, coverage)
        if (statsReportManager != null) {
            statsReportManager.setTestCases(testCases);
        }

        // Write test cases using MultiServiceRESTAssuredWriter (creates multiple files)
        logger.info("Writing {} test cases to multiple files in folder structure", testCases.size());
        logger.info("TARGET: All test files will be in timestamped package: {}.{}", inputs.packageName, className);
        writer.write(testCases);

        // Execute tests if enabled
        if (Boolean.TRUE.equals(inputs.executeTestCases)) {
            logger.info("Executing generated test cases");
            logger.info("ISOLATION: Only executing tests from current run (timestamp: {})", id);

            // For MST mode, find the actual generated test classes and execute them individually
            String actualPackageName = inputs.packageName + "." + className;

            // Check if Test Case Enhancer is enabled. We use the FQN here
            // because this file already imports the legacy
            // es.us.isa.restest.configuration.multiservice.MstConfig
            // (Properties-file loader), and the new typed POJO lives at
            // es.us.isa.restest.configuration.MstConfig.
            es.us.isa.restest.configuration.MstConfig.Enhancer enhancerCfg =
                    es.us.isa.restest.configuration.MstConfig.instance().enhancer();
            boolean enhancerEnabled = enhancerCfg.enabled();
            int enhancerRounds = enhancerCfg.rounds();
            boolean skip5xx = enhancerCfg.skip5xx();

            if (enhancerEnabled) {
                logger.info("═══════════════════════════════════════════════════════════════════════════");
                logger.info("🔧 TEST CASE ENHANCER ENABLED - {} enhancement round(s) configured", enhancerRounds);
                logger.info("═══════════════════════════════════════════════════════════════════════════");

                // Execute with enhancement loop
                executeWithEnhancement(actualPackageName, className, enhancerRounds, skip5xx, id);
            } else {
                // Standard single execution
                executeGeneratedTestsWithJUnit(actualPackageName, className);
            }

            // Generate Allure report using the existing AllureReportManager
            if (Boolean.TRUE.equals(inputs.allureReports) && reportManager != null) {
                logger.info("Generating Allure report using AllureReportManager");
                logger.info("ALLURE REPORT: Will contain only results from current run");
                logger.info("NOTE: Multiple test classes from this run will appear as separate test suites in one report");
                reportManager.generateReport();
            }

            // 🔍 FAULT DETECTION: Generate fault detection report
            logger.info("🔍 Generating Fault Detection Report...");
            String faultReportDir = readParameterValue("fault.detection.report.dir");
            if (faultReportDir == null || faultReportDir.isEmpty()) {
                // Default directory if not configured
                faultReportDir = "logs/fault-detection-reports";
            }
            FaultDetectionTracker.getInstance()
                .generateReport(faultReportDir, inputs.experimentName + "_" + id);

            // Log detection statistics
            Map<String, Object> stats = FaultDetectionTracker.getInstance().getStatistics();
            logger.info("🔍 Fault Detection Summary:");
            logger.info("   - Total Injected Faults: {}", stats.get("totalInjectedFaults"));
            logger.info("   - Detected Faults: {}", stats.get("detectedFaults"));
            logger.info("   - Detection Rate: {}%", String.format("%.1f", ((Number) stats.get("detectionRate")).doubleValue()));
            logger.info("   - Report saved to: {}", faultReportDir);
        }

        // Generate reports
        if (statsReportManager != null) {
            logger.info("Generating CSV data");
            statsReportManager.generateReport(id, Boolean.TRUE.equals(inputs.executeTestCases));
        }

        logger.info("Iteration 1. {} test cases generated.", testCases.size());
        logger.info("SUMMARY: Generated and executed only newly created tests from timestamp: {}", id);
        logger.info("Stopped after 1 iterations (max.iterations limit reached)");

        Timer.stopCounting(Timer.TestStep.ALL);

        return MistRunResult.builder()
                .exitCode(0)
                .testCaseCount(testCases.size())
                .runId(id)
                .build();
    }

    /**
     * Lifted MST case from {@code TestGenerationAndExecution.createGenerator()}
     * (L431-L575). Sets {@link #spec}.
     */
    private AbstractTestCaseGenerator createMstGenerator() throws RESTestException, java.io.IOException {
        AbstractTestCaseGenerator gen;

        // multi‑service
        // 1. OpenAPI spec (single file or already merged)
        spec = new OpenAPISpecification(inputs.oasPath);

        TestConfiguration emptyTc = new TestConfiguration();
        emptyTc.setOperations(new ArrayList<>());      // ← avoid NPE in preconditions

        Auth emptyAuth = new Auth();                   // no API‑keys, just present
        TestConfigurationObject dummyPrimaryConf = new TestConfigurationObject();
        dummyPrimaryConf.setTestConfiguration(emptyTc);
        dummyPrimaryConf.setAuth(emptyAuth);

        // 3. Load the **multi‑service** YAML
        FileInputStream in = new FileInputStream(inputs.confPath);
        Map<String, TestConfigurationObject> serviceConfigs =
                MicroserviceTestConfigurationIO.loadMultiServiceConfiguration(in);
        in.close();

        if (serviceConfigs.isEmpty()) {
            throw new RESTestException(
                    "No services found in configuration file " + inputs.confPath);
        }

        // 4. Build a map service‑name → spec (single‑spec variant)
        Map<String, OpenAPISpecification> serviceSpecs = new LinkedHashMap<>();
        for (String svc : serviceConfigs.keySet()) {
            serviceSpecs.put(svc, spec);
        }

        // 5. Get the recorded workflows from the trace file. The entry-point
        // (TestGenerationAndExecution / MistMain) already resolved
        // inputs.traceFilePath against the .properties file's directory, so
        // this value is absolute regardless of how MIST was launched. We only
        // fall through to readParameterValue when the entry point did not set
        // inputs.traceFilePath at all (legacy paths).
        String tracePath = inputs.traceFilePath;
        if (tracePath == null || tracePath.trim().isEmpty()) {
            tracePath = resolveInputPath(readParameterValue("trace.file.path"));
        }
        logger.info("MST trace input: {}", tracePath);
        List<WorkflowScenario> scenarios =
                        TraceWorkflowExtractor.extractScenarios(tracePath);

        // 5.5. Register root APIs with their tree structures in the registry
        // ⚠️ IMPORTANT: Register BEFORE deduplication to capture ALL trace patterns
        String registryPath = resolveInputPath(readParameterValue("root.api.registry.path"));
        if (registryPath != null && !registryPath.isEmpty()) {
            logger.info("Initializing Root API Registry at: {}", registryPath);
            RootApiRegistry registry = new RootApiRegistry(registryPath);

            logger.info("Registering {} scenarios (BEFORE deduplication) to capture all trace patterns", scenarios.size());
            registry.registerRootApisFromScenarios(scenarios);
            registry.saveRegistry();

            RootApiRegistry.RegistryStats stats = registry.getStats();
            logger.info("Root API Registry updated: {} total root APIs, {} total trees",
                   stats.getTotalRootApis(), stats.getTotalTrees());

            // Optionally log all registered root APIs for debugging
            if (logger.isDebugEnabled()) {
                logger.debug("Registered Root APIs:");
                for (String apiKey : registry.getAllRootApiKeys()) {
                    logger.debug("  - {}", apiKey);
                }
            }
        } else {
            logger.warn("Root API Registry path not configured. Set 'root.api.registry.path' property to enable registry.");
        }

        // 5.6. NOW deduplicate scenarios for test generation (to avoid redundant tests)
        logger.info("Deduplicating scenarios for test generation (to avoid redundant tests)...");
        scenarios = WorkflowScenarioUtils.deduplicateBySteps(scenarios);
        logger.info("After deduplication: {} unique scenarios will generate test cases", scenarios.size());


        // Pass configuration parameters as system properties for the generator
        String variantsPerScenario = readParameterValue("test.variants.per.scenario");
        if (variantsPerScenario != null) {
            System.setProperty("test.variants.per.scenario", variantsPerScenario);
        }
        if (inputs.numTestCases != null) {
            System.setProperty("testsperoperation", inputs.numTestCases.toString());
        }
        // New: pass step generation control for MST (only first business step)
        String onlyFirstStep = readParameterValue("mst.generate.only.first.step");
        if (onlyFirstStep != null) {
            System.setProperty("mst.generate.only.first.step", onlyFirstStep);
            logger.info("MST first-step-only mode: {}", onlyFirstStep);
        }

        // CRITICAL: Pass Smart Input Fetching configuration to MST generator
        passSmartInputFetchingProperties();

        // Pass faulty.ratio to MST generator
        if (inputs.faultyRatio != null) {
            System.setProperty("faulty.ratio", inputs.faultyRatio.toString());
            logger.info("MST faulty ratio: {}", inputs.faultyRatio);
        }

        // Pass faulty.round-robin strategy to MST generator
        String faultyRoundRobin = readParameterValue("faulty.round-robin");
        if (faultyRoundRobin != null) {
            System.setProperty("faulty.round-robin", faultyRoundRobin);
            logger.info("MST faulty round-robin mode: {}", faultyRoundRobin);
        }

        // Pass negative input generation mode (smart | llm | hardcode) to the MST generator.
        //   smart    — LLM only for REGEX_MISMATCH + SEMANTIC_MISMATCH; static/schema for the rest
        //   llm      — LLM for all 6 contextual fault types (research/ablation mode, much slower)
        //   hardcode — fully deterministic, zero LLM calls (fastest)
        String negativeInputMode = readParameterValue("negative.input.generation.mode");
        if (negativeInputMode != null) {
            String modeLc = negativeInputMode.trim().toLowerCase();
            System.setProperty("negative.input.generation.mode", modeLc);
            String desc;
            switch (modeLc) {
                case "llm":      desc = "LLM for all contextual types (slow; research/ablation)"; break;
                case "hardcode": desc = "Deterministic hardcoded; zero LLM calls"; break;
                case "smart":    desc = "LLM only for REGEX/SEMANTIC; static for the rest (default)"; break;
                default:
                    logger.warn("Unknown negative.input.generation.mode '{}' — falling back to 'smart'", negativeInputMode);
                    modeLc = "smart";
                    System.setProperty("negative.input.generation.mode", modeLc);
                    desc = "LLM only for REGEX/SEMANTIC; static for the rest (default)";
            }
            logger.info("MST negative input generation mode: {} ({})", modeLc, desc);
        } else {
            // Default to SMART mode: the universal fault categories (NULL, EMPTY, OVERFLOW,
            // SPECIAL_CHARACTERS, TYPE_MISMATCH, BOUNDARY_VIOLATION) are filled with static/
            // schema-derived payloads and only REGEX_MISMATCH + SEMANTIC_MISMATCH hit the LLM.
            System.setProperty("negative.input.generation.mode", "smart");
            logger.info("MST negative input generation mode: smart (default — static payloads + LLM only for REGEX/SEMANTIC)");
        }

        // 7. Instantiate the generator
        gen = new MultiServiceTestCaseGenerator(
                spec,                   // primarySpec
                dummyPrimaryConf,
                serviceSpecs,
                serviceConfigs,
                scenarios,
                /* use LLM for params  */ true,
                /* use LLM for flows   */ true
        );

        gen.setCheckTestCases(inputs.checkTestCases);

        return gen;
    }

    /**
     * Lifted MST branch from {@code TestGenerationAndExecution.createWriter()}
     * (L597-L609).
     */
    private IWriter createMstWriter() {
        // Get base URL from properties or default
        String baseUrl = readParameterValue("base.url");
        if (baseUrl == null) {
            // Fallback to spec if no base.url property is set
            baseUrl = spec.getSpecification().getServers().get(0).getUrl();
        }

        // Multi‑service mode: hand off to the specialized writer
        MultiServiceRESTAssuredWriter msWriter = new MultiServiceRESTAssuredWriter(
                inputs.oasPath, inputs.confPath, inputs.targetDirJava, inputs.testClassName,
                inputs.packageName, baseUrl, inputs.logToFile
        );
        // exactly mirror single‑service feature‑toggles:
        msWriter.setLogging(true);
        msWriter.setAllureReport(Boolean.TRUE.equals(inputs.allureReports));
        msWriter.setEnableStats(Boolean.TRUE.equals(inputs.enableCSVStats));
        msWriter.setEnableOutputCoverage(Boolean.TRUE.equals(inputs.enableOutputCoverage));
        msWriter.setAPIName(inputs.experimentName);
        msWriter.setProxy(inputs.proxy);
        return msWriter;
    }

    /**
     * Lifted MST branches from
     * {@code TestGenerationAndExecution.createAllureReportManager()}
     * (L633-L668).
     */
    private AllureReportManager createMstAllureReportManager() {
        AllureReportManager arm = null;
        if (Boolean.TRUE.equals(inputs.executeTestCases)) {
            String allureResultsDir;
            String allureReportDir;

            // For MST mode, use base directories directly since Maven generates results in target/allure-results
            allureResultsDir = readParameterValue("allure.results.dir");
            allureReportDir = readParameterValue("allure.report.dir");

            // Conventional defaults when the .properties file omits these (matches
            // the values shipped in config.properties). Without the fallback,
            // FileManager.deleteDir(null) NPE's before variant generation can even
            // start, hiding the underlying "you forgot to configure allure paths"
            // signal behind a useless stack trace.
            if (allureResultsDir == null || allureResultsDir.isEmpty()) {
                allureResultsDir = "target/allure-results";
                logger.info("allure.results.dir not set; defaulting to {}", allureResultsDir);
            }
            if (allureReportDir == null || allureReportDir.isEmpty()) {
                allureReportDir = "target/allure-reports";
                logger.info("allure.report.dir not set; defaulting to {}", allureReportDir);
            }

            if (Boolean.TRUE.equals(inputs.deletePreviousResults)) {
                deleteDir(allureResultsDir);
                deleteDir(allureReportDir);
            }

            //Find auth property names (if any)
            List<String> authProperties = Collections.emptyList();
            // For MST mode, we'll use default empty auth properties
            logger.info("Using default auth properties for MST mode");
            arm = new AllureReportManager(allureResultsDir, allureReportDir, authProperties);
            arm.setEnvironmentProperties(inputs.propertiesFilePath);
            arm.setHistoryTrend(true);
        }
        return arm;
    }

    /**
     * Lifted MST branches from
     * {@code TestGenerationAndExecution.createStatsReportManager()}
     * (L671-L697).
     */
    private StatsReportManager createMstStatsReportManager() {
        String testDataDir = readParameterValue("data.tests.dir") + "/" + inputs.experimentName;
        String coverageDataDir = readParameterValue("data.coverage.dir") + "/" + inputs.experimentName;

        // Delete previous results (if any)
        if (Boolean.TRUE.equals(inputs.deletePreviousResults)) {
            deleteDir(testDataDir);
            deleteDir(coverageDataDir);

            // Recreate directories
            createDir(testDataDir);
            createDir(coverageDataDir);
        }

        CoverageMeter coverageMeter = null;

        // For MST mode, we skip coverage measurement due to multi-service configuration structure
        if (Boolean.TRUE.equals(inputs.enableInputCoverage) || Boolean.TRUE.equals(inputs.enableOutputCoverage)) {
            logger.info("Coverage measurement disabled for MST mode due to multi-service configuration structure");
        }

        return new StatsReportManager(testDataDir, coverageDataDir,
                Boolean.TRUE.equals(inputs.enableCSVStats),
                Boolean.TRUE.equals(inputs.enableInputCoverage),
                Boolean.TRUE.equals(inputs.enableOutputCoverage),
                coverageMeter);
    }

    /**
     * Bridge a handful of MIST-core values that downstream generators read
     * via System.getProperty (oas.path, base.url) and log a summary of the
     * MST configuration that was already pushed to System properties.
     *
     * The bulk MST-key propagation lives in
     * {@code MstConfig#applyToSystemProperties()} — this method does not
     * re-list every MST key.
     */
    private void passSmartInputFetchingProperties() {
        logger.info("🔧 Bridging MIST-core values for MST and logging MST configuration summary...");

        // MIST-core values that the smart fetcher / generators read via
        // System.getProperty — these live in the core file, not the MST file,
        // so MstConfig.applyToSystemProperties() does not cover them.
        for (String coreKey : new String[] { "oas.path", "base.url" }) {
            String value = readParameterValue(coreKey);
            if (value != null && System.getProperty(coreKey) == null) {
                System.setProperty(coreKey, value);
            }
        }

        es.us.isa.restest.configuration.MstConfig mstCfg = es.us.isa.restest.configuration.MstConfig.instance();
        es.us.isa.restest.configuration.MstConfig.SmartFetch sfCfg = mstCfg.smartFetch();
        boolean enabled = sfCfg.enabled();
        double percentage = sfCfg.percentage();
        String registryPath = sfCfg.registryPath();

        String llmEnabled = System.getProperty("llm.enabled", "false");
        String llmModelType = System.getProperty("llm.model.type", "openai_compatible");
        String geminiApiKey = System.getProperty("llm.gemini.api.key", "not set");
        String ollamaEnabled = System.getProperty("llm.ollama.enabled", "false");
        String ollamaModel = System.getProperty("llm.ollama.model", "not set");

        logger.info("📊 Smart Fetching Settings:");
        logger.info("   - Enabled: {}", enabled);
        logger.info("   - Percentage: {}% smart fetching", percentage * 100);
        logger.info("   - Registry: {}", registryPath);

        logger.info("🤖 LLM Settings:");
        logger.info("   - Enabled: {}", llmEnabled);
        logger.info("   - Model Type: {}", llmModelType);
        logger.info("   - Gemini API Key: {}", geminiApiKey.equals("not set") ? "not set" : "configured");
        logger.info("   - Ollama Enabled: {}", ollamaEnabled);
        logger.info("   - Ollama Model: {}", ollamaModel);

        es.us.isa.restest.configuration.MstConfig.Llm llmCfg = mstCfg.llm();
        boolean llmValidationEnabled = llmCfg.responseValidationEnabled();
        boolean llmValidationOnly2xx = llmCfg.responseValidationOnly2xx();
        boolean llmValidationRca = llmCfg.responseValidationIncludeRca();

        logger.info("🔍 LLM Response Validation (Soft Error Detection):");
        logger.info("   - Enabled: {}", llmValidationEnabled);
        logger.info("   - Only 2XX responses: {}", llmValidationOnly2xx);
        logger.info("   - Include RCA in reports: {}", llmValidationRca);

        if (enabled) {
            logger.info("🎯 Smart Input Fetching is ENABLED - you should see 'Smart Fetch' logs during test generation!");
        } else {
            logger.warn("❌ Smart Input Fetching is DISABLED - enable it by setting smart.input.fetch.enabled=true in the MST configuration file");
        }
    }

    /**
     * Bootstrap the {@link TraceShapeOracle} for this MistRunner invocation.
     *
     * <p>On cold start (no {@code .mist/trace-shape-invariants.json} file yet)
     * walks the labelled seed corpus and trains every invariant family — the
     * resulting store is then flushed to disk. On subsequent invocations the
     * pre-existing JSON is simply re-loaded; no training fires.
     *
     * <p>Logger contract:
     * <ul>
     *   <li>INFO when the cache file is created for the first time</li>
     *   <li>DEBUG when it is reused on a subsequent invocation</li>
     * </ul>
     * This method is additive — failures (missing seed labels, unreadable
     * corpus, etc.) downgrade to WARN; the oracle is still instantiated with
     * an empty store so {@code evaluate(...)} returns a clean pass.
     */
    private TraceShapeOracle bootstrapTraceShapeOracle() {
        String overridePath = System.getProperty("mist.tso.store.path");
        traceShapeStorePath = (overridePath != null && !overridePath.isEmpty())
                ? Paths.get(overridePath)
                : Paths.get(".mist", "trace-shape-invariants.json");

        boolean coldStart = !Files.exists(traceShapeStorePath);

        ShapeInvariantStore store = new ShapeInvariantStore(traceShapeStorePath);

        if (coldStart) {
            logger.info("🧬 Trace Shape Oracle: cold start — no {} on disk, training from seed corpus",
                    traceShapeStorePath);
            try {
                Path seedLabels = resolveSeedLabelsPath();
                Path seedCorpusDir = resolveSeedCorpusDir(seedLabels);
                if (seedLabels != null && Files.isRegularFile(seedLabels)
                        && seedCorpusDir != null && Files.isDirectory(seedCorpusDir)) {
                    TraceShapeLearner.LearnResult lr =
                            TraceShapeLearner.learn(seedCorpusDir, seedLabels, store);
                    logger.info("🧬 Trace Shape Oracle: learned invariants for {} root API(s) from {} (corpus={})",
                            lr.rootApisLearned.size(), seedLabels.getFileName(), seedCorpusDir);
                } else {
                    logger.warn("🧬 Trace Shape Oracle: skipping seed-corpus training (labels={}, corpus={}) — empty store written",
                            seedLabels, seedCorpusDir);
                    store.flush();
                }
            } catch (Exception ex) {
                logger.warn("🧬 Trace Shape Oracle: seed training failed — empty store written: {}",
                        ex.getMessage());
                try { store.flush(); } catch (RuntimeException ignored) { }
            }
        } else {
            logger.debug("🧬 Trace Shape Oracle: loaded {} from disk (warm cache)", traceShapeStorePath);
        }

        return new TraceShapeOracle(store);
    }

    /**
     * Resolve where the labelled seed corpus index lives. Override via
     * {@code -Dmist.tso.seed.labels=...}; otherwise default to the bundled
     * artefact at {@code mist-core/src/main/resources/mist/seed-trace-labels.json}
     * (relative to the workdir).
     */
    private Path resolveSeedLabelsPath() {
        String overridePath = System.getProperty("mist.tso.seed.labels");
        if (overridePath != null && !overridePath.isEmpty()) {
            return Paths.get(overridePath);
        }
        // Look in the workdir first; this is a versioned artefact in the repo.
        Path bundled = workdir.resolve("mist-core/src/main/resources/mist/seed-trace-labels.json");
        if (Files.exists(bundled)) return bundled;
        // Fallback for callers that run from inside mist-restest-adapter/.
        Path relative = workdir.resolve("../mist-core/src/main/resources/mist/seed-trace-labels.json").normalize();
        if (Files.exists(relative)) return relative;
        return bundled;
    }

    /**
     * Resolve the seed corpus directory. The labels JSON optionally carries a
     * {@code _corpus_root_default} pointer to declare the canonical directory;
     * fall back to a sibling directory if absent.
     */
    private Path resolveSeedCorpusDir(Path seedLabels) {
        String overridePath = System.getProperty("mist.tso.seed.corpus");
        if (overridePath != null && !overridePath.isEmpty()) {
            return Paths.get(overridePath);
        }
        if (seedLabels != null && Files.isRegularFile(seedLabels)) {
            try {
                String content = new String(Files.readAllBytes(seedLabels), java.nio.charset.StandardCharsets.UTF_8);
                org.json.JSONObject obj = new org.json.JSONObject(content);
                String declared = obj.optString("_corpus_root_default", "");
                if (!declared.isEmpty()) {
                    Path resolved = workdir.resolve(declared);
                    if (Files.exists(resolved)) return resolved;
                    Path relative = workdir.resolve("../" + declared).normalize();
                    if (Files.exists(relative)) return relative;
                    return resolved;
                }
            } catch (Exception ignored) { }
        }
        // Conventional fallback.
        return workdir.resolve("mist-restest-adapter/src/main/resources/My-Example/trainticket/test-trace");
    }

    /**
     * Execute generated test classes using direct JUnit execution optimized for IntelliJ
     * Programmatically adds AspectJ weaving support for Allure step capture
     * ENHANCED: Better isolation to ensure only newly generated tests are executed
     */
    private void executeGeneratedTestsWithJUnit(String fullPackageName, String className) {
        logger.info("Executing generated tests for package: {} and class: {} (IntelliJ mode)", fullPackageName, className);

        try {
            // ENHANCED: Clean old compiled test classes to avoid conflicts
            cleanOldCompiledTestClasses(fullPackageName);

            // Set up Allure environment for IntelliJ
            setupAllureForIntelliJ();

            // Find the test class directory
            String packagePath = fullPackageName.replace('.', '/');
            File testClassDir = resolveTestTargetRoot().resolve(packagePath).toFile();

            List<String> testClassNames = new ArrayList<>();

            if (testClassDir.exists() && testClassDir.isDirectory()) {
                logger.info("Searching for test classes in: {}", testClassDir.getAbsolutePath());
                logger.info("ISOLATION: Only looking in the specific timestamped directory to avoid old tests");

                // Look for all .java files directly in the package directory
                File[] javaFiles = testClassDir.listFiles((dir, name) -> name.endsWith(".java"));
                if (javaFiles != null && javaFiles.length > 0) {
                    for (File javaFile : javaFiles) {
                        String testClassName = fullPackageName + "." + javaFile.getName().replace(".java", "");
                        testClassNames.add(testClassName);
                        logger.info("Found test class: {}", testClassName);
                    }
                } else {
                    logger.info("No .java files found directly in package directory, checking subdirectories...");

                    // If no direct files, check subdirectories (previous behavior)
                    File[] classDirs = testClassDir.listFiles(File::isDirectory);
                    if (classDirs != null) {
                        for (File classDir : classDirs) {
                            logger.info("Checking subdirectory: {}", classDir.getName());
                            // Find Java files in this class directory
                            File[] subJavaFiles = classDir.listFiles((dir, name) -> name.endsWith(".java"));
                            if (subJavaFiles != null) {
                                for (File javaFile : subJavaFiles) {
                                    String testClassName = fullPackageName + "." + classDir.getName() + "." +
                                            javaFile.getName().replace(".java", "");
                                    testClassNames.add(testClassName);
                                    logger.info("Found test class in subdirectory: {}", testClassName);
                                }
                            }
                        }
                    }
                }
            } else {
                logger.error("Test class directory does not exist: {}", testClassDir.getAbsolutePath());
                return;
            }

            if (testClassNames.isEmpty()) {
                logger.warn("No test classes found in package: {}", fullPackageName);
                logger.warn("Expected directory: {}", testClassDir.getAbsolutePath());

                // Debug: List what's actually in the directory
                if (testClassDir.exists()) {
                    File[] allFiles = testClassDir.listFiles();
                    if (allFiles != null) {
                        logger.info("Directory contents:");
                        for (File file : allFiles) {
                            logger.info("  {} ({})", file.getName(), file.isDirectory() ? "directory" : "file");
                        }
                    }
                }
                return;
            }

            logger.info("Found {} test classes to execute (NEWLY GENERATED ONLY)", testClassNames.size());

            // *** CRITICAL FIX: Ensure Java 11 environment for IntelliJ ***
            logger.info("Setting up Java 11 environment for IntelliJ execution...");
            setupJava11Environment();

            // *** CRITICAL FIX: Compile test classes before loading them ***
            logger.info("Compiling generated test classes...");
            boolean compilationSuccess = compileTestClasses();
            if (!compilationSuccess) {
                logger.error("Test compilation failed. Cannot proceed with execution.");
                return;
            }
            logger.info("Test compilation completed successfully");

            // *** CRITICAL FIX: Add test-classes to classpath for IntelliJ ***
            logger.info("Adding test classes to classpath...");
            boolean classpathSuccess = addTestClassesToClasspath();
            if (!classpathSuccess) {
                logger.warn("Could not add test classes to classpath. Class loading may fail.");
            } else {
                logger.info("Test classes added to classpath successfully");
            }

            // Load test classes
            List<Class<?>> testClasses = new ArrayList<>();
            for (String testClassName : testClassNames) {
                try {
                    // Use the updated context class loader that includes test-classes
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    Class<?> testClass = Class.forName(testClassName, true, classLoader);
                    testClasses.add(testClass);
                    logger.info("Loaded test class: {}", testClassName);
                } catch (ClassNotFoundException e) {
                    logger.error("Could not load test class: {} - {}", testClassName, e.getMessage());
                    logger.debug("ClassNotFoundException details:", e);

                    // Debug: Show current classpath information
                    logger.debug("Current classpath: {}", System.getProperty("java.class.path"));
                    logger.debug("Context class loader: {}", Thread.currentThread().getContextClassLoader().getClass().getName());
                }
            }

            if (testClasses.isEmpty()) {
                logger.error("No test classes could be loaded");
                return;
            }

            // Execute tests using JUnit with proper Allure integration for IntelliJ
            logger.info("Executing {} test classes with IntelliJ-compatible Allure integration...", testClasses.size());
            logger.info("ISOLATION: Executing only newly generated tests from timestamp: {}", className);

            // Create JUnit runner with Allure listener (no custom lifecycle to avoid conflicts)
            JUnitCore junit = new JUnitCore();

            // Add standard Allure listener (works better in IDE environment)
            AllureJunit4 allureListener = new AllureJunit4();
            junit.addListener(allureListener);

            // Add console progress listener
            junit.addListener(new RunListener() {
                @Override
                public void testStarted(Description description) {
                    logger.info("Starting test: {}", description.getDisplayName());
                }

                @Override
                public void testFinished(Description description) {
                    logger.info("Finished test: {}", description.getDisplayName());
                }

                @Override
                public void testFailure(Failure failure) {
                    logger.error("Test failed: {} - {}", failure.getDescription().getDisplayName(), failure.getMessage());
                }
            });

            Timer.startCounting(Timer.TestStep.TEST_SUITE_EXECUTION);

            // Optional parallel execution. Default 1 = sequential, preserving prior
            // behavior byte-for-byte. mst.test.parallelism=N runs N test classes
            // concurrently — each on its own JUnitCore with its own AllureJunit4
            // listener (Allure's lifecycle is thread-local by design). Audit #21
            // confirmed the 6 shared resources (MstAuth/SmartInputFetcher/
            // LLMConfig/LLMCallCache/ParameterErrorAnalyzer/generated-test statics)
            // are safe; #22 added a JVM-wide lock around the writer-emitted
            // InputFetchRegistry load/mutate/save block; #26 added W3C traceparent
            // injection so Jaeger trace correlation stays deterministic 1:1 across
            // concurrent test threads.
            //
            // Resolution priority: -D system property > .properties value > "auto".
            // "auto" caps at 8 (or 4 when LLM response validation is enabled — LLM
            // rate-limit safety) bounded by the available CPU count. This is the
            // default in config.properties so a fresh checkout runs parallel out
            // of the box on multi-core machines without any user configuration.
            boolean __llmValidationOn = parseBooleanProperty(
                    System.getProperty("llm.response.validation.enabled"),
                    readParameterValue("llm.response.validation.enabled"),
                    false);
            int __parallelism = resolveTestParallelism(
                    Runtime.getRuntime().availableProcessors(),
                    System.getProperty("mst.test.parallelism"),
                    readParameterValue("mst.test.parallelism"),
                    __llmValidationOn);
            logger.info("Test execution parallelism: {} ({}; CPUs={}, LLM validation={})",
                    __parallelism,
                    __parallelism <= 1 ? "sequential" : "parallel",
                    Runtime.getRuntime().availableProcessors(),
                    __llmValidationOn ? "on" : "off");

            int __aggRun = 0, __aggFailure = 0, __aggIgnore = 0;
            long __aggRunTime = 0L;
            java.util.List<Failure> __aggFailures = new ArrayList<>();

            if (__parallelism <= 1) {
                // Sequential path — single JUnitCore, prior-equivalent behavior.
                Result result = junit.run(testClasses.toArray(new Class[0]));
                __aggRun = result.getRunCount();
                __aggFailure = result.getFailureCount();
                __aggIgnore = result.getIgnoreCount();
                __aggRunTime = result.getRunTime();
                __aggFailures.addAll(result.getFailures());
            } else {
                // Parallel path. Each task runs ONE test class on a fresh JUnitCore.
                // Allure listener is per-task; Allure's AllureLifecycle uses
                // ThreadLocal context, so per-thread listener instances do not
                // collide. The original `junit` instance (with the console
                // RunListener) is not reused here — under parallel execution the
                // per-test progress logs would interleave to noise; the aggregate
                // result is logged once at the end.
                ExecutorService __pool = Executors.newFixedThreadPool(__parallelism);
                java.util.List<Callable<Result>> __tasks = new ArrayList<>();
                for (Class<?> __cls : testClasses) {
                    final Class<?> __c = __cls;
                    __tasks.add(() -> {
                        JUnitCore __local = new JUnitCore();
                        __local.addListener(new AllureJunit4());
                        return __local.run(__c);
                    });
                }
                long __wallStart = System.currentTimeMillis();
                try {
                    java.util.List<Future<Result>> __futures = __pool.invokeAll(__tasks);
                    __pool.shutdown();
                    if (!__pool.awaitTermination(24, TimeUnit.HOURS)) {
                        logger.warn("Parallel execution did not complete within 24h cap; forcing shutdown");
                        __pool.shutdownNow();
                    }
                    for (Future<Result> __fut : __futures) {
                        try {
                            Result __r = __fut.get();
                            __aggRun += __r.getRunCount();
                            __aggFailure += __r.getFailureCount();
                            __aggIgnore += __r.getIgnoreCount();
                            __aggFailures.addAll(__r.getFailures());
                        } catch (Exception __taskEx) {
                            logger.error("Parallel test-class execution threw: {}", __taskEx.toString());
                            __aggFailure++;
                        }
                    }
                } catch (InterruptedException __ie) {
                    Thread.currentThread().interrupt();
                    __pool.shutdownNow();
                    logger.error("Parallel execution interrupted; partial results follow");
                }
                __aggRunTime = System.currentTimeMillis() - __wallStart;
            }

            Timer.stopCounting(Timer.TestStep.TEST_SUITE_EXECUTION);

            // Log results
            logger.info("=== TEST EXECUTION RESULTS (NEWLY GENERATED TESTS ONLY) ===");
            logger.info("Tests run: {}", __aggRun);
            logger.info("Failures: {}", __aggFailure);
            logger.info("Ignored: {}", __aggIgnore);
            logger.info("Run time: {} ms", __aggRunTime);

            if (__aggFailure > 0) {
                logger.error("=== FAILURES ===");
                for (Failure failure : __aggFailures) {
                    logger.error("Failed: {} - {}", failure.getDescription().getDisplayName(), failure.getMessage());
                }
            }

            if (__aggFailure == 0) {
                logger.info("✅ All newly generated tests executed successfully!");
            } else {
                logger.warn("❌ Some newly generated tests failed. Check the logs above for details.");
            }

        } catch (Exception e) {
            logger.error("Error executing test classes", e);
        }
    }

    /**
     * Execute tests with enhancement loop.
     *
     * Flow:
     * 1. Execute initial tests (Round 0)
     * 2. Collect failed tests
     * 3. Send to LLM for enhancement suggestions
     * 4. Regenerate test files with enhanced values
     * 5. Re-execute enhanced tests
     * 6. Repeat for configured number of rounds
     */
    private void executeWithEnhancement(String fullPackageName, String className,
                                               int enhancerRounds, boolean skip5xx, String testId) {
        logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
        logger.info("║              TEST CASE ENHANCER - MULTI-ROUND EXECUTION                     ║");
        logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
        logger.info("║  Rounds: {}                                                                  ║", enhancerRounds);
        logger.info("║  Skip 5xx: {}                                                               ║", skip5xx);
        logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");

        String enhancerOutputDir = "target/enhancer/" + testId;

        // Check if status code exploration is enabled
        es.us.isa.restest.configuration.MstConfig.StatusCodeExploration sceCfg =
                es.us.isa.restest.configuration.MstConfig.instance().statusCodeExploration();
        boolean statusCodeExplorationEnabled = sceCfg.enabled();
        int maxExplorationPerTest = sceCfg.maxPerTest();
        int maxExplorationPerRound = sceCfg.maxPerRound();

        if (statusCodeExplorationEnabled) {
            logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
            logger.info("║              STATUS CODE EXPLORATION ENABLED                                ║");
            logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
            logger.info("║  Max per test: {}                                                            ║", maxExplorationPerTest);
            logger.info("║  Max per round: {}                                                           ║", maxExplorationPerRound);
            logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");
        }

        try {
            // Initialize LLM service for enhancement
            Map<String, String> llmProperties = new HashMap<>();
            for (String key : System.getProperties().stringPropertyNames()) {
                if (key.startsWith("llm.") || key.startsWith("smart.")) {
                    llmProperties.put(key, System.getProperty(key));
                }
            }
            LLMService llmService = LLMService.getInstance(llmProperties);
            TestCaseEnhancer enhancer = new TestCaseEnhancer(llmService);
            TestFileRegenerator regenerator = new TestFileRegenerator();

            // Initialize StatusCodeExplorationEnhancer if enabled
            StatusCodeExplorationEnhancer statusCodeEnhancer = null;
            if (statusCodeExplorationEnabled) {
                statusCodeEnhancer = new StatusCodeExplorationEnhancer(llmService);
                statusCodeEnhancer.setMaxExplorationTestsPerOriginal(maxExplorationPerTest);
                statusCodeEnhancer.setMaxExplorationTestsPerRound(maxExplorationPerRound);
                logger.info("✅ StatusCodeExplorationEnhancer initialized");
            }

            // Track overall statistics
            int totalEnhanced = 0;
            int totalImproved = 0;  // Tests that passed after enhancement

            // Compute total outer slots so the bar advances after EACH major sub-phase,
            // not just at the end of each round. Without this the bar would sit at the
            // same fraction for the entire round (which contains tests + optional status
            // code exploration + enhancement + regeneration), since each inner phase only
            // credits up to 1/outerTotal of the outer fraction and the monotonic clamp
            // keeps the visual fixed across sequential inner phases.
            int totalSlots = 0;
            for (int r = 0; r <= enhancerRounds; r++) {
                boolean rIsFinal = (r == enhancerRounds);
                totalSlots += 1; // tests
                if (r == 0 && statusCodeExplorationEnabled) totalSlots += 1; // exploration
                if (!rIsFinal) totalSlots += 2; // enhance + regen
            }
            ConsoleProgressBar.begin("Enhance Rounds", totalSlots);
            for (int round = 0; round <= enhancerRounds; round++) {
                boolean isFinalRound = (round == enhancerRounds);

                logger.info("═══════════════════════════════════════════════════════════════════════════");
                logger.info("🔄 EXECUTION ROUND {} of {} {}", round, enhancerRounds,
                        isFinalRound ? "(FINAL - Results saved to Allure)" : "(Enhancement round)");
                logger.info("═══════════════════════════════════════════════════════════════════════════");

                // Create collector for this round
                FailedTestCollector collector = new FailedTestCollector(round, skip5xx, enhancerOutputDir);

                // Execute tests with collector
                Result result = executeTestsWithCollector(fullPackageName, className, collector, isFinalRound);

                if (result == null) {
                    logger.error("Test execution failed in round {}", round);
                    break;
                }
                ConsoleProgressBar.update("Round " + round + " tests done");

                // Status Code Exploration: Run after round 0 to discover and create exploration tests
                // NOTE: Moved BEFORE round results logging so collector reflects exploration execution
                if (round == 0 && statusCodeExplorationEnabled && statusCodeEnhancer != null) {
                    logger.info("═══════════════════════════════════════════════════════════════════════════");
                    logger.info("🔍 RUNNING STATUS CODE EXPLORATION PHASE");
                    logger.info("═══════════════════════════════════════════════════════════════════════════");

                    // Build execution results from captured test data
                    Map<String, StatusCodeExplorationEnhancer.TestExecutionResult> explorationResults =
                        buildExplorationResults(collector, result);

                    if (!explorationResults.isEmpty()) {
                        logger.info("📊 Built {} execution results for status code exploration",
                            explorationResults.size());

                        // Log status code coverage summary
                        logStatusCodeCoverage(explorationResults);

                        // Check if we have stored test cases and writer for full exploration
                        if (!generatedMSTTestCases.isEmpty() && mstWriter != null) {
                            logger.info("═══════════════════════════════════════════════════════════════════════════");
                            logger.info("🔬 STATUS CODE EXPLORATION (Efficient Mode)");
                            logger.info("═══════════════════════════════════════════════════════════════════════════");
                            logger.info("Processing {} original test cases for exploration", generatedMSTTestCases.size());

                            // STEP 1: Get ALL exploration suggestions from LLM (ONE call per test case)
                            StatusCodeExplorationEnhancer.ExplorationResult explorationOutcome =
                                statusCodeEnhancer.exploreEfficient(generatedMSTTestCases, explorationResults);

                            List<MultiServiceTestCase> explorationTests = explorationOutcome.getExplorationTests();

                            if (!explorationTests.isEmpty()) {
                                logger.info("🎯 Created {} exploration tests", explorationTests.size());

                                // STEP 2: Write ALL tests (original + exploration) and execute exploration tests
                                try {
                                    // Add exploration tests to our collection
                                    int originalCount = generatedMSTTestCases.size();
                                    generatedMSTTestCases.addAll(explorationTests);

                                    // Write ALL test cases (original + exploration)
                                    List<TestCase> allTestCases = new ArrayList<>(generatedMSTTestCases);
                                    mstWriter.write(allTestCases);
                                    logger.info("📝 Wrote {} total tests ({} original + {} exploration)",
                                        allTestCases.size(), originalCount, explorationTests.size());

                                    // Compile
                                    boolean compiled = compileTestClasses();
                                    if (!compiled) {
                                        logger.warn("⚠️ Compilation failed for exploration tests");
                                    } else {
                                        logger.info("✅ Compilation successful");

                                        // STEP 3: Execute ALL tests (only exploration tests are new)
                                        logger.info("🚀 Executing tests (exploration tests will run)...");

                                        TestResultCapture.enableCapture();
                                        FailedTestCollector explorationCollector = new FailedTestCollector(
                                            round, skip5xx, enhancerOutputDir);

                                        // Execute with skipAllureClean=true to preserve results
                                        Result execResult = executeTestsWithCollector(
                                            fullPackageName, className, explorationCollector, true, true);

                                        if (execResult != null) {
                                            logger.info("✅ Execution complete: {} tests run, {} failures",
                                                execResult.getRunCount(), execResult.getFailureCount());

                                            // CRITICAL: Replace the collector reference so enhancement uses exploration results
                                            collector = explorationCollector;
                                            logger.info("📊 Updated collector for enhancement: {} enhanceable failures from exploration execution",
                                                collector.getFailedTestCount());

                                            // STEP 4: Record exploration results to update round-robin
                                            Map<String, Integer> explorationResultsMap = new HashMap<>();
                                            Map<String, FailedTestResult> capturedResults = TestResultCapture.getResultsSnapshot();

                                            for (MultiServiceTestCase exploreTest : explorationTests) {
                                                String exploreTestId = exploreTest.getOperationId();
                                                String apiKey = getApiKeyFromTest(exploreTest);
                                                int targetStatus = exploreTest.getTargetStatusCode();
                                                int actualStatus = -1;

                                                // Find the actual status from captured results
                                                for (Map.Entry<String, FailedTestResult> entry : capturedResults.entrySet()) {
                                                    if (entry.getKey().contains(exploreTestId) ||
                                                        exploreTestId.contains(entry.getKey().replaceAll(".*\\.", ""))) {
                                                        actualStatus = entry.getValue().getActualStatusCode();
                                                        break;
                                                    }
                                                }

                                                // Record result
                                                if (actualStatus > 0 && apiKey != null) {
                                                    if (actualStatus == targetStatus) {
                                                        // SUCCESS: Remove from round-robin
                                                        statusCodeEnhancer.getTracker().markTriggered(apiKey, targetStatus);
                                                        logger.info("   ✅ {} triggered target {} - REMOVED from round-robin",
                                                            exploreTestId, targetStatus);
                                                    } else {
                                                        // FAILED: Move to end of round-robin
                                                        statusCodeEnhancer.getTracker().moveToEndOfRoundRobin(apiKey, targetStatus);
                                                        logger.info("   ❌ {} got {} instead of {} - MOVED to end",
                                                            exploreTestId, actualStatus, targetStatus);
                                                    }

                                                    // Also record actual status (might discover new codes)
                                                    if (actualStatus != targetStatus) {
                                                        statusCodeEnhancer.getTracker().markTriggered(apiKey, actualStatus);
                                                    }
                                                }
                                            }

                                            logger.info("📊 All tests are now in Allure report");
                                        }
                                    }
                                } catch (Exception e) {
                                    logger.error("Error in exploration: {}", e.getMessage(), e);
                                }

                                // Log coverage summary
                                logger.info("📊 Status Code Coverage: {}", explorationOutcome.getCoverageSummary());
                            } else {
                                logger.info("✅ No exploration tests created - all status codes covered or no candidates");
                            }
                        } else {
                            logger.info("⚠️ Skipping exploration: MST test cases={}, writer={}",
                                generatedMSTTestCases.size(), mstWriter != null ? "available" : "null");
                        }

                        // Start new round for the exploration enhancer
                        statusCodeEnhancer.startNewRound();

                        // Clear captured results now that exploration is done
                        TestResultCapture.clearResults();
                    } else {
                        logger.info("⚠️ No execution results available for status code exploration");
                    }
                    ConsoleProgressBar.update("Round " + round + " exploration done");
                }

                // Log round results (after exploration so collector reflects final state)
                logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
                logger.info("║  ROUND {} RESULTS                                                            ║", round);
                logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
                logger.info("║  Tests Run: {}                                                               ║", result.getRunCount());
                logger.info("║  Failures: {}                                                                ║", result.getFailureCount());
                logger.info("║  Enhanceable Failures: {}                                                    ║", collector.getFailedTestCount());
                logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");

                // If this is the final round or no failures to enhance, we're done
                if (isFinalRound) {
                    logger.info("✅ Final round complete. Results saved to Allure.");
                    break;
                }

                if (collector.getFailedTestCount() == 0) {
                    logger.info("🎉 No enhanceable failures found! Skipping remaining enhancement rounds.");
                    break;
                }

                // Enhance failed tests
                logger.info("🔧 Enhancing {} failed tests with LLM...", collector.getFailedTestCount());
                List<FailedTestResult> failedTests = collector.getFailedTests();
                List<TestCaseEnhancer.EnhancementResult> enhancementResults = enhancer.enhanceBatch(failedTests);
                ConsoleProgressBar.update("Round " + round + " enhance done");

                // Save enhancement results
                enhancer.saveEnhancementResults(enhancementResults, enhancerOutputDir, round);

                // Regenerate test files with enhanced values
                int regenerated = 0;
                ConsoleProgressBar.begin("Regenerating", enhancementResults.size());
                for (int i = 0; i < enhancementResults.size(); i++) {
                    TestCaseEnhancer.EnhancementResult enhancement = enhancementResults.get(i);
                    if (!enhancement.isSuccess()) {
                        ConsoleProgressBar.update("skip");
                        continue;
                    }

                    FailedTestResult originalFailure = failedTests.get(i);
                    String testFilePath = findTestFilePath(fullPackageName, className,
                            originalFailure.getTestClassName(), originalFailure.getTestMethodName());

                    if (testFilePath != null) {
                        boolean success = regenerator.regenerateTestFile(
                                testFilePath,
                                originalFailure.getTestMethodName(),
                                enhancement.getEnhancedParameters(),
                                originalFailure
                        );
                        if (success) {
                            regenerated++;
                            totalEnhanced++;
                        }
                    }
                    ConsoleProgressBar.update(originalFailure.getTestMethodName());
                }
                ConsoleProgressBar.complete();

                logger.info("📝 Regenerated {} test files with enhanced values", regenerated);

                // Recompile for next round
                if (regenerated > 0) {
                    logger.info("🔨 Recompiling test classes for next round...");
                    boolean compiled = compileTestClasses();
                    if (!compiled) {
                        logger.error("Compilation failed after enhancement. Stopping.");
                        break;
                    }
                }
                ConsoleProgressBar.update("Round " + round + " regen done");
            }
            ConsoleProgressBar.complete();

            // Final summary
            logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
            logger.info("║              TEST CASE ENHANCER - FINAL SUMMARY                             ║");
            logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
            logger.info("║  Total Tests Enhanced: {}                                                    ║", totalEnhanced);
            logger.info("║  Enhanced Tests Info: See target/enhancer/{}                                 ║", testId);
            logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            logger.error("Error during enhancement loop: {}", e.getMessage(), e);
            // Fall back to standard execution
            logger.info("Falling back to standard execution without enhancement...");
            executeGeneratedTestsWithJUnit(fullPackageName, className);
        }
    }

    /**
     * Execute tests with a FailedTestCollector to gather failure information.
     * @param isFinalRound if true, skip Allure setup/cleaning (for exploration tests that should add to existing results)
     */
    private Result executeTestsWithCollector(String fullPackageName, String className,
                                                    FailedTestCollector collector, boolean isFinalRound) {
        return executeTestsWithCollector(fullPackageName, className, collector, isFinalRound, false);
    }

    /**
     * Execute tests with a FailedTestCollector to gather failure information.
     * @param skipAllureClean if true, skip Allure setup/cleaning (for exploration tests that should add to existing results)
     */
    private Result executeTestsWithCollector(String fullPackageName, String className,
                                                    FailedTestCollector collector, boolean isFinalRound,
                                                    boolean skipAllureClean) {
        try {
            // Clean and setup
            cleanOldCompiledTestClasses(fullPackageName);

            // Find test class directory
            String packagePath = fullPackageName.replace('.', '/');
            File testClassDir = resolveTestTargetRoot().resolve(packagePath).toFile();

            List<String> testClassNames = findTestClassNames(testClassDir, fullPackageName);

            if (testClassNames.isEmpty()) {
                logger.error("No test classes found in package: {}", fullPackageName);
                return null;
            }

            // Compile tests
            if (!compileTestClasses()) {
                logger.error("Test compilation failed");
                return null;
            }

            // 🔧 FIX: Only setup and clean Allure AFTER successful compilation.
            // This prevents nuking previous results if the current round fails to compile.
            if (!skipAllureClean) {
                setupAllureForIntelliJ();
            }

            // For non-final rounds, clear Allure results to avoid accumulating intermediate results
            if (!isFinalRound && !skipAllureClean) {
                clearAllureResults();
            }

            // Add to classpath
            addTestClassesToClasspath();

            // 🔧 CRITICAL FIX: Create a fresh ClassLoader for each round
            // The JVM caches loaded classes, so we need a new ClassLoader to pick up
            // changes from recompiled test files during enhancement rounds
            File testClassesDir = new File(System.getProperty("user.dir"), "target/test-classes");
            URL[] urls = new URL[] { testClassesDir.toURI().toURL() };

            // Create isolated ClassLoader that loads from test-classes first
            URLClassLoader freshClassLoader = new URLClassLoader(
                urls,
                MistRunner.class.getClassLoader()
            ) {
                // Override loadClass to force loading from our URLs first for test classes
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    // For test classes in our package, try to load fresh from URL first
                    if (name.startsWith(fullPackageName)) {
                        try {
                            // First check if class file exists in our test-classes
                            String classFile = name.replace('.', '/') + ".class";
                            URL resource = findResource(classFile);
                            if (resource != null) {
                                // Load fresh by reading the bytes directly
                                try (InputStream is = resource.openStream()) {
                                    byte[] bytes = is.readAllBytes();
                                    return defineClass(name, bytes, 0, bytes.length);
                                }
                            }
                        } catch (Exception e) {
                            // Fall through to parent
                        }
                    }
                    return super.loadClass(name);
                }
            };

            logger.debug("Created fresh ClassLoader for test execution to pick up recompiled classes");

            // Load test classes with the fresh ClassLoader
            List<Class<?>> testClasses = new ArrayList<>();
            for (String testClassName : testClassNames) {
                try {
                    Class<?> testClass = freshClassLoader.loadClass(testClassName);
                    testClasses.add(testClass);
                    logger.debug("Loaded test class: {} (fresh)", testClassName);
                } catch (ClassNotFoundException e) {
                    logger.error("Could not load test class: {} - {}", testClassName, e.getMessage());
                }
            }

            if (testClasses.isEmpty()) {
                logger.error("No test classes could be loaded");
                return null;
            }

            // Create JUnit runner
            JUnitCore junit = new JUnitCore();

            // ALWAYS add AllureJunit4 listener - required for Allure lifecycle management
            // The generated tests use Allure.step(), Allure.parameter(), etc. which need
            // an active Allure test context to avoid "no test case running" errors
            AllureJunit4 allureListener = new AllureJunit4();
            junit.addListener(allureListener);

            // Add our collector
            junit.addListener(collector);

            // Add console listener with progress tracking
            junit.addListener(new RunListener() {
                @Override
                public void testRunStarted(Description description) {
                    ConsoleProgressBar.begin("tests", description.testCount());
                }

                @Override
                public void testStarted(Description description) {
                    ConsoleProgressBar.update(description.getMethodName());
                    logger.debug("Starting: {}", description.getMethodName());
                }

                @Override
                public void testFailure(Failure failure) {
                    logger.debug("Failed: {} - {}", failure.getDescription().getMethodName(),
                            failure.getMessage() != null ? failure.getMessage().substring(0, Math.min(100, failure.getMessage().length())) : "");
                }

                @Override
                public void testRunFinished(Result result) {
                    ConsoleProgressBar.complete();
                }
            });

            // Execute
            Timer.startCounting(Timer.TestStep.TEST_SUITE_EXECUTION);
            Result result = junit.run(testClasses.toArray(new Class[0]));
            Timer.stopCounting(Timer.TestStep.TEST_SUITE_EXECUTION);

            return result;

        } catch (Exception e) {
            logger.error("Error executing tests with collector: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Find all test class names in a directory.
     */
    private List<String> findTestClassNames(File testClassDir, String packageName) {
        List<String> testClassNames = new ArrayList<>();

        if (!testClassDir.exists() || !testClassDir.isDirectory()) {
            return testClassNames;
        }

        // Look for .java files directly in the package directory
        File[] javaFiles = testClassDir.listFiles((dir, name) -> name.endsWith(".java"));
        if (javaFiles != null && javaFiles.length > 0) {
            for (File javaFile : javaFiles) {
                testClassNames.add(packageName + "." + javaFile.getName().replace(".java", ""));
            }
        }

        // Check subdirectories
        File[] classDirs = testClassDir.listFiles(File::isDirectory);
        if (classDirs != null) {
            for (File classDir : classDirs) {
                File[] subJavaFiles = classDir.listFiles((dir, name) -> name.endsWith(".java"));
                if (subJavaFiles != null) {
                    for (File javaFile : subJavaFiles) {
                        testClassNames.add(packageName + "." + classDir.getName() + "." +
                                javaFile.getName().replace(".java", ""));
                    }
                }
            }
        }

        return testClassNames;
    }

    /**
     * Find the path to a test file given class and method names.
     */
    private String findTestFilePath(String packageName, String className,
                                           String testClassName, String testMethodName) {
        String baseDir = System.getProperty("user.dir");
        String packagePath = packageName.replace('.', '/');

        // The test class might be directly in the package or in a subdirectory
        // Try direct path first
        java.nio.file.Path testTargetRoot = resolveTestTargetRoot();
        String directPath = testTargetRoot.resolve(packagePath).resolve(
                testClassName.substring(testClassName.lastIndexOf('.') + 1) + ".java").toString();
        File directFile = new File(directPath);
        if (directFile.exists()) {
            return directPath;
        }

        // Try with full class path
        String fullClassPath = testClassName.replace('.', '/');
        String fullPath = testTargetRoot.resolve(fullClassPath + ".java").toString();
        File fullFile = new File(fullPath);
        if (fullFile.exists()) {
            return fullPath;
        }

        logger.warn("Could not find test file for class: {}", testClassName);
        return null;
    }

    /**
     * ENHANCED: Clean old compiled test classes that might interfere with execution
     * This ensures only newly compiled classes are available for loading
     */
    private void cleanOldCompiledTestClasses(String fullPackageName) {
        try {
            String baseDir = System.getProperty("user.dir");
            File testClassesDir = new File(baseDir, "target/test-classes");

            if (!testClassesDir.exists()) {
                return; // Nothing to clean
            }

            // Clean all .class files except those from the current package
            String packagePath = fullPackageName.replace('.', '/');
            File currentPackageDir = new File(testClassesDir, packagePath);

            // Only clean if we're dealing with a timestamped package (contains underscore and numbers)
            if (fullPackageName.matches(".*_\\d+")) {
                logger.info("Cleaning old compiled test classes to ensure isolation...");

                // Get the base package (e.g., "trainticket_twostage_test")
                String basePackage = fullPackageName.substring(0, fullPackageName.lastIndexOf('.'));
                File basePackageDir = new File(testClassesDir, basePackage.replace('.', '/'));

                if (basePackageDir.exists()) {
                    File[] oldPackageDirs = basePackageDir.listFiles(File::isDirectory);
                    if (oldPackageDirs != null) {
                        int cleanedCount = 0;
                        for (File oldDir : oldPackageDirs) {
                            // Only clean directories that look like old timestamped packages
                            if (!oldDir.equals(currentPackageDir) && oldDir.getName().matches(".*_\\d+")) {
                                if (deleteDirectory(oldDir)) {
                                    cleanedCount++;
                                    logger.debug("Cleaned old package directory: {}", oldDir.getName());
                                }
                            }
                        }
                        if (cleanedCount > 0) {
                            logger.info("Cleaned {} old compiled test package directories", cleanedCount);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("Could not clean old compiled test classes: {}", e.getMessage());
        }
    }

    /**
     * Recursively delete a directory and all its contents
     */
    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        return dir.delete();
    }

    /**
     * Fast compilation using built-in Java compiler API
     * This is much faster than Maven and ensures Java 11 compatibility
     * ENHANCED: Only compiles newly generated test classes to avoid including old tests
     */
    private boolean compileTestClasses() {
        try {
            String baseDir = System.getProperty("user.dir");
            File testSourceDir = resolveTestTargetRoot().toFile();
            File testClassesDir = new File(baseDir, "target/test-classes");
            File mainClassesDir = new File(baseDir, "target/classes");

            // Ensure target directories exist
            if (!testClassesDir.exists()) {
                testClassesDir.mkdirs();
            }

            // 🔧 FIX: Check if main classes are compiled - required for test compilation
            // If target/classes doesn't exist or is empty, fall back to Maven which will compile both
            if (!mainClassesDir.exists() || mainClassesDir.list() == null || mainClassesDir.list().length == 0) {
                logger.info("Main classes not found in target/classes. Using Maven to compile both main and test classes...");
                return fallbackMavenCompilation();
            }

            // Verify essential main classes exist (spot check)
            File testCaseClass = new File(mainClassesDir, "es/us/isa/restest/testcases/MultiServiceTestCase.class");
            if (!testCaseClass.exists()) {
                logger.info("Essential main classes not compiled. Using Maven to compile both main and test classes...");
                return fallbackMavenCompilation();
            }

            // ENHANCED: Only find Java files in the newly generated test directories
            List<File> javaFiles = findNewlyGeneratedJavaFiles(testSourceDir);
            if (javaFiles.isEmpty()) {
                logger.info("No newly generated Java files found to compile in: {}", testSourceDir);
                return true; // Nothing to compile is not an error
            }

            long startTime = System.currentTimeMillis();
            logger.info("Fast-compiling {} newly generated test classes using built-in Java compiler...", javaFiles.size());

            // Verify we're using Java 11
            String javaVersion = System.getProperty("java.version");
            logger.info("Using Java version: {} for compilation", javaVersion);

            // Get Java compiler (this uses the JDK that's running, which should be Java 11 in IntelliJ)
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                logger.error("Java compiler not available. Make sure you're running with JDK (not JRE)");
                logger.error("Current Java home: {}", System.getProperty("java.home"));
                return fallbackMavenCompilation();
            }

            // Build classpath including all dependencies
            String classpath = buildCompilationClasspath(mainClassesDir);
            logger.debug("Compilation classpath: {}", classpath);

            // Prepare compiler options
            List<String> options = Arrays.asList(
                "-cp", classpath,
                "-d", testClassesDir.getAbsolutePath(),
                "-source", "11",
                "-target", "11",
                "-Xlint:none", // Suppress warnings for faster compilation
                "-g:none"      // Skip debug info for faster compilation
            );

            // Convert File list to String list for compiler
            List<String> fileNames = javaFiles.stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

            // Combine options and file names
            List<String> compilerArgs = new ArrayList<>();
            compilerArgs.addAll(options);
            compilerArgs.addAll(fileNames);

            // 🔧 FIX: Capture compiler output to detect compilation errors
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PrintStream errorPrintStream = new PrintStream(errorStream);

            // Run compilation with error capture
            int result = compiler.run(null, errorPrintStream, errorPrintStream, compilerArgs.toArray(new String[0]));

            // Check for any error output even if result is 0
            String errorOutput = errorStream.toString();
            if (!errorOutput.isEmpty()) {
                logger.warn("Compilation output:\n{}", errorOutput);
            }

            // Verify all class files were actually created
            int compiledCount = 0;
            int failedCount = 0;
            for (File javaFile : javaFiles) {
                String classNameLocal = javaFile.getName().replace(".java", ".class");
                String relativePath = javaFile.getAbsolutePath()
                    .replace(testSourceDir.getAbsolutePath(), "")
                    .replace(".java", ".class");
                File classFile = new File(testClassesDir.getAbsolutePath() + relativePath);
                if (classFile.exists()) {
                    compiledCount++;
                } else {
                    failedCount++;
                    logger.error("❌ Failed to compile: {} (no .class file found at {})",
                        javaFile.getName(), classFile.getAbsolutePath());
                }
            }

            logger.info("Compilation summary: {} compiled, {} failed out of {} total",
                compiledCount, failedCount, javaFiles.size());

            if (result == 0 && failedCount == 0) {
                long duration = System.currentTimeMillis() - startTime;
                logger.info("✅ Fast compilation of newly generated tests completed successfully in {} ms", duration);
                return true;
            } else if (failedCount > 0 && compiledCount > 0) {
                // 🔧 FIX: Partial compilation success - proceed with the tests that compiled.
                // The class loader already handles missing classes gracefully (catches ClassNotFoundException).
                // Previously this fell back to Maven which also failed, causing ZERO tests to run.
                long duration = System.currentTimeMillis() - startTime;
                logger.warn("⚠️ Partial compilation: {} compiled, {} failed out of {} total in {} ms",
                    compiledCount, failedCount, javaFiles.size(), duration);
                logger.warn("⚠️ Proceeding with {} successfully compiled test classes. " +
                    "Failed classes will be skipped during class loading.", compiledCount);
                return true;
            } else if (failedCount > 0 && compiledCount == 0) {
                logger.error("❌ All files failed to compile ({} failures). Falling back to Maven...", failedCount);
                return fallbackMavenCompilation();
            } else {
                logger.error("❌ Fast compilation failed with exit code: {}", result);
                logger.info("Falling back to Maven compilation...");
                return fallbackMavenCompilation();
            }

        } catch (Exception e) {
            logger.warn("Fast compilation failed: {} - falling back to Maven", e.getMessage());
            return fallbackMavenCompilation();
        }
    }

    /**
     * ENHANCED: Find only newly generated Java files based on generation start time
     * This prevents old test files from being included in compilation
     *
     * 🔥 FIX: Uses testGenerationStartTime instead of hardcoded 5 minutes
     * This ensures ALL files generated in the current run are included,
     * even if test generation takes hours!
     */
    private List<File> findNewlyGeneratedJavaFiles(File dir) {
        List<File> javaFiles = new ArrayList<>();
        if (!dir.exists() || !dir.isDirectory()) {
            return javaFiles;
        }

        // 🔥 FIX: Use the stored generation start time, or fallback to 2 hours ago if not set
        // This ensures we include all files generated during this run, regardless of how long it took
        long cutoffTime;
        if (testGenerationStartTime > 0) {
            // Use the actual start time (minus 1 second buffer for file system timing)
            cutoffTime = testGenerationStartTime - 1000;
            logger.debug("Using test generation start time as cutoff: {} ({}ms ago)",
                cutoffTime, System.currentTimeMillis() - cutoffTime);
        } else {
            // Fallback: 2 hours ago (much safer than 5 minutes)
            cutoffTime = System.currentTimeMillis() - (2 * 60 * 60 * 1000);
            logger.warn("testGenerationStartTime not set, using 2-hour fallback cutoff");
        }

        findRecentJavaFiles(dir, javaFiles, cutoffTime);

        // Log what files we found
        if (!javaFiles.isEmpty()) {
            logger.info("Found {} recently generated Java files:", javaFiles.size());
            for (File file : javaFiles) {
                logger.info("  - {}", file.getAbsolutePath());
            }
        } else {
            logger.warn("No recently generated Java files found! Cutoff time: {} ({}ms ago)",
                cutoffTime, System.currentTimeMillis() - cutoffTime);
        }

        return javaFiles;
    }

    /**
     * Recursively find Java files modified after the cutoff time
     */
    private void findRecentJavaFiles(File dir, List<File> javaFiles, long cutoffTime) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findRecentJavaFiles(file, javaFiles, cutoffTime);
                } else if (file.getName().endsWith(".java") && file.lastModified() > cutoffTime) {
                    javaFiles.add(file);
                }
            }
        }
    }

    /**
     * Recursively find all Java files in a directory
     */
    private List<File> findJavaFiles(File dir) {
        List<File> javaFiles = new ArrayList<>();
        if (!dir.exists() || !dir.isDirectory()) {
            return javaFiles;
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    javaFiles.addAll(findJavaFiles(file));
                } else if (file.getName().endsWith(".java")) {
                    javaFiles.add(file);
                }
            }
        }
        return javaFiles;
    }

    /**
     * Build comprehensive classpath for compilation
     */
    private String buildCompilationClasspath(File mainClassesDir) {
        Set<String> classpathEntries = new LinkedHashSet<>();

        // Add main classes
        if (mainClassesDir.exists()) {
            classpathEntries.add(mainClassesDir.getAbsolutePath());
        }

        // Add current classpath (includes all Maven dependencies)
        String currentClasspath = System.getProperty("java.class.path");
        if (currentClasspath != null) {
            classpathEntries.addAll(Arrays.asList(currentClasspath.split(File.pathSeparator)));
        }

        // Add Maven dependencies from target/dependency (if exists)
        String baseDir = System.getProperty("user.dir");
        File dependencyDir = new File(baseDir, "target/dependency");
        if (dependencyDir.exists()) {
            File[] jars = dependencyDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jars != null) {
                for (File jar : jars) {
                    classpathEntries.add(jar.getAbsolutePath());
                }
            }
        }

        return String.join(File.pathSeparator, classpathEntries);
    }

    /**
     * Fallback to Maven compilation if direct compilation fails
     * 🔧 FIX: Compile BOTH main and test classes to ensure all dependencies are available
     */
    private boolean fallbackMavenCompilation() {
        try {
            long startTime = System.currentTimeMillis();
            String baseDir = System.getProperty("user.dir");

            // Use Maven with optimized settings for speed
            // IMPORTANT: Use "compile test-compile" to ensure main classes are compiled first
            ProcessBuilder pb = new ProcessBuilder();

            // Handle Windows command formatting
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // 🔧 FIX: Removed "-Dmaven.test.skip=true" which was skipping test COMPILATION
                // Keep "-DskipTests=true" to skip test EXECUTION but still compile tests
                pb.command("cmd.exe", "/c", "mvn", "compile", "test-compile",
                    "-q",                    // Quiet mode
                    "-T", "1C",             // Use 1 thread per CPU core
                    "-Djacoco.skip=true",   // Skip JaCoCo
                    "-Dmaven.javadoc.skip=true", // Skip JavaDoc
                    "-Dcheckstyle.skip=true",    // Skip CheckStyle
                    "-DskipTests=true"      // Skip test execution (but still compile)
                );
            } else {
                pb.command("mvn", "compile", "test-compile",
                    "-q", "-T", "1C", "-Djacoco.skip=true",
                    "-Dmaven.javadoc.skip=true",
                    "-Dcheckstyle.skip=true", "-DskipTests=true");
            }

            pb.directory(new File(baseDir));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // 🔧 FIX: Consume process output to prevent OS buffer deadlock
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug("Maven: {}", line);
                }
            }

            int exitCode = process.waitFor();

            long duration = System.currentTimeMillis() - startTime;

            if (exitCode == 0) {
                logger.info("✅ Maven compilation completed successfully in {} ms", duration);
                return true;
            } else {
                logger.error("❌ Maven compilation failed with exit code: {} after {} ms", exitCode, duration);
                return false;
            }

        } catch (Exception e) {
            logger.error("Maven compilation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Setup Java 11 environment for IntelliJ execution
     */
    private void setupJava11Environment() {
        try {
            String javaVersion = System.getProperty("java.version");
            String javaHome = System.getProperty("java.home");

            logger.info("Current Java environment:");
            logger.info("  Java version: {}", javaVersion);
            logger.info("  Java home: {}", javaHome);
            logger.info("  JVM name: {}", System.getProperty("java.vm.name"));
            logger.info("  JVM vendor: {}", System.getProperty("java.vm.vendor"));

            // Check if we're running Java 11
            if (!javaVersion.startsWith("11.")) {
                logger.warn("⚠️  Not running Java 11! Current version: {}", javaVersion);
                logger.warn("   This may cause compilation issues. Please ensure IntelliJ is configured to use Java 11.");
            } else {
                logger.info("✅ Java 11 detected - optimal for compilation");
            }

            // Ensure compiler is available
            if (ToolProvider.getSystemJavaCompiler() == null) {
                logger.error("❌ Java compiler not available!");
                logger.error("   Make sure you're running with JDK 11, not JRE");
                logger.error("   In IntelliJ: File → Project Structure → Project → Project SDK should be JDK 11");
            } else {
                logger.info("✅ Java compiler available for fast compilation");
            }

        } catch (Exception e) {
            logger.warn("Could not fully setup Java 11 environment: {}", e.getMessage());
        }
    }

    /**
     * Alternative compilation method for environments where Maven command is not available
     */
    private boolean compileTestClassesAlternative() throws Exception {
        logger.info("Attempting alternative test compilation method...");

        // This method is now redundant since we have fast direct compilation
        // Just call the main compilation method
        return compileTestClasses();
    }

    /**
     * Add test-classes directory to classpath programmatically
     * This is necessary for IntelliJ execution where test-classes may not be on the classpath
     */
    private boolean addTestClassesToClasspath() {
        try {
            String baseDir = System.getProperty("user.dir");
            File testClassesDir = new File(baseDir, "target/test-classes");

            if (!testClassesDir.exists()) {
                logger.warn("Test classes directory does not exist: {}", testClassesDir.getAbsolutePath());
                return false;
            }

            // Get the current classpath
            String currentClasspath = System.getProperty("java.class.path");
            String testClassesPath = testClassesDir.getAbsolutePath();

            // Check if test-classes is already in classpath
            if (currentClasspath.contains(testClassesPath)) {
                logger.info("Test classes directory already in classpath");
                return true;
            }

            // Add test-classes to classpath using URLClassLoader approach
            URL testClassesURL = testClassesDir.toURI().toURL();

            // Get current thread's context class loader
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

            // Create new URLClassLoader with test-classes added
            URLClassLoader newClassLoader = new URLClassLoader(
                new URL[]{testClassesURL},
                currentClassLoader
            );

            // Set the new class loader as context class loader
            Thread.currentThread().setContextClassLoader(newClassLoader);

            logger.info("Added test classes directory to classpath: {}", testClassesPath);
            return true;

        } catch (Exception e) {
            logger.error("Failed to add test classes to classpath: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Clear Allure results between enhancement rounds.
     * This ensures only the final round's results are persisted.
     */
    private void clearAllureResults() {
        try {
            String baseDir = System.getProperty("user.dir");
            File allureResultsDir = new File(baseDir, "target/allure-results");
            if (allureResultsDir.exists()) {
                File[] files = allureResultsDir.listFiles();
                if (files != null) {
                    int deleted = 0;
                    for (File file : files) {
                        if (file.isFile() && file.delete()) {
                            deleted++;
                        }
                    }
                    logger.debug("Cleared {} Allure result files (intermediate round)", deleted);
                }
            }
        } catch (Exception e) {
            logger.warn("Could not clear Allure results: {}", e.getMessage());
        }
    }

    /**
     * Set up Allure environment for IntelliJ execution
     * Ensures proper directories and system properties are configured
     * ENHANCED: More thorough cleanup to ensure only current run results are included
     */
    private void setupAllureForIntelliJ() {
        try {
            // Ensure Allure results directory exists
            String baseDir = System.getProperty("user.dir");
            File allureResultsDir = new File(baseDir, "target/allure-results");
            if (!allureResultsDir.exists()) {
                allureResultsDir.mkdirs();
                logger.info("Created Allure results directory: {}", allureResultsDir.getAbsolutePath());
            }

            // Set Allure system properties for IntelliJ
            System.setProperty("allure.results.directory", allureResultsDir.getAbsolutePath());
            System.setProperty("allure.link.issue.pattern", "");
            System.setProperty("allure.link.tms.pattern", "");

            // ENHANCED: Complete cleanup of previous results to ensure only current run is included
            logger.info("Cleaning previous Allure results to ensure fresh report generation...");
            File[] existingFiles = allureResultsDir.listFiles();
            if (existingFiles != null) {
                int deletedCount = 0;
                for (File file : existingFiles) {
                    if (file.isFile()) {
                        // Delete all Allure result files (not just -result.json)
                        if (file.getName().endsWith("-result.json") ||
                            file.getName().endsWith("-container.json") ||
                            file.getName().endsWith("-attachment.txt") ||
                            file.getName().endsWith("-attachment.json") ||
                            file.getName().startsWith("environment.") ||
                            file.getName().equals("categories.json") ||
                            file.getName().equals("executor.json")) {
                            if (file.delete()) {
                                deletedCount++;
                    }
                }
                    }
                }
                logger.info("Cleaned {} previous Allure result files", deletedCount);
            }

            logger.info("Allure environment configured for IntelliJ at: {}", allureResultsDir.getAbsolutePath());

        } catch (Exception e) {
            logger.warn("Could not fully set up Allure environment: {}", e.getMessage());
        }
    }

    /**
     * Build exploration results from test execution data.
     * Extracts status codes and response information from captured test results.
     */
    private Map<String, StatusCodeExplorationEnhancer.TestExecutionResult> buildExplorationResults(
            FailedTestCollector collector, Result junitResult) {

        Map<String, StatusCodeExplorationEnhancer.TestExecutionResult> results = new HashMap<>();

        try {
            // Get all captured results from TestResultCapture using snapshot (preserves results)
            Map<String, FailedTestResult> capturedMap = TestResultCapture.getResultsSnapshot();

            if (capturedMap != null && !capturedMap.isEmpty()) {
                logger.info("📊 Processing {} captured test results for status code exploration",
                    capturedMap.size());

                for (Map.Entry<String, FailedTestResult> entry : capturedMap.entrySet()) {
                    String testKey = entry.getKey();
                    FailedTestResult captured = entry.getValue();
                    String testName = captured.getTestMethodName();

                    StatusCodeExplorationEnhancer.TestExecutionResult explorationResult =
                        new StatusCodeExplorationEnhancer.TestExecutionResult();
                    explorationResult.setTestName(testName);
                    explorationResult.setActualStatusCode(captured.getActualStatusCode());
                    explorationResult.setResponseBody(captured.getResponseBody());
                    explorationResult.setPassed(captured.isEnhanceable()); // 5xx = not enhanceable = server error
                    explorationResult.setErrorMessage(captured.getErrorMessage());

                    // Extract API key from captured data
                    String apiKey = captured.getHttpMethod() + " " + captured.getEndpoint();
                    explorationResult.setApiKey(apiKey);

                    results.put(testName, explorationResult);
                }
            } else {
                logger.info("No TestResultCapture results. Using FailedTestCollector data only.");
            }

            // Also include failed tests from collector (may have different/additional info)
            for (FailedTestResult failed : collector.getFailedTests()) {
                String testName = failed.getTestMethodName();
                if (!results.containsKey(testName)) {
                    StatusCodeExplorationEnhancer.TestExecutionResult explorationResult =
                        new StatusCodeExplorationEnhancer.TestExecutionResult();
                    explorationResult.setTestName(testName);
                    explorationResult.setActualStatusCode(failed.getActualStatusCode());
                    explorationResult.setResponseBody(failed.getResponseBody());
                    explorationResult.setPassed(false);
                    explorationResult.setErrorMessage(failed.getErrorMessage());

                    String apiKey = failed.getHttpMethod() + " " + failed.getEndpoint();
                    explorationResult.setApiKey(apiKey);

                    results.put(testName, explorationResult);
                }
            }

        } catch (Exception e) {
            logger.error("Error building exploration results: {}", e.getMessage(), e);
        }

        return results;
    }

    /**
     * Extract API key (HTTP method + path) from a MultiServiceTestCase.
     */
    private String getApiKeyFromTest(MultiServiceTestCase test) {
        if (test == null || test.getSteps().isEmpty()) return null;
        MultiServiceTestCase.StepCall step = test.getSteps().get(0);
        return step.getMethod().toString() + " " + step.getPath();
    }

    /**
     * Extract API key (HTTP method + path) from test name.
     * Test names typically follow pattern: test_METHOD_operationId_variant
     */
    private String extractApiKeyFromTestName(String testName) {
        if (testName == null) return "UNKNOWN";

        // Try to extract method from test name patterns like test_POST_1_1, test_negative_GET_1_2
        String upper = testName.toUpperCase();
        for (String method : new String[]{"GET", "POST", "PUT", "DELETE", "PATCH"}) {
            if (upper.contains("_" + method + "_")) {
                return method + " /api/unknown"; // We don't have path info from just the test name
            }
        }

        return "UNKNOWN " + testName;
    }

    /**
     * Log status code coverage summary from execution results.
     */
    private void logStatusCodeCoverage(
            Map<String, StatusCodeExplorationEnhancer.TestExecutionResult> results) {

        // Group by status code to get coverage overview
        Map<Integer, Integer> statusCodeCounts = new HashMap<>();
        Map<String, Set<Integer>> apiStatusCodes = new HashMap<>();

        for (StatusCodeExplorationEnhancer.TestExecutionResult result : results.values()) {
            int statusCode = result.getActualStatusCode();
            statusCodeCounts.merge(statusCode, 1, Integer::sum);

            String apiKey = result.getApiKey();
            if (apiKey != null) {
                apiStatusCodes.computeIfAbsent(apiKey, k -> new HashSet<>()).add(statusCode);
            }
        }

        logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
        logger.info("║              STATUS CODE COVERAGE SUMMARY                                   ║");
        logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
        logger.info("║  Total Tests Analyzed: {}                                                    ║", results.size());
        logger.info("║  Unique Status Codes: {}                                                     ║", statusCodeCounts.size());
        logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");

        // Log status code distribution
        statusCodeCounts.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String category = getStatusCodeCategory(entry.getKey());
                logger.info("║  {} ({}): {} occurrences                                               ║",
                    entry.getKey(), category, entry.getValue());
            });

        logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
        logger.info("║  APIs with Multiple Status Codes:                                           ║");

        apiStatusCodes.entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .forEach(entry -> {
                logger.info("║  - {}: {}                                                  ║",
                    truncateApiKey(entry.getKey(), 30), entry.getValue());
            });

        logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Get human-readable category for a status code.
     */
    private String getStatusCodeCategory(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) return "Success";
        if (statusCode >= 300 && statusCode < 400) return "Redirect";
        if (statusCode >= 400 && statusCode < 500) return "Client Error";
        if (statusCode >= 500) return "Server Error";
        return "Unknown";
    }

    /**
     * Truncate API key for display.
     */
    private String truncateApiKey(String apiKey, int maxLen) {
        if (apiKey == null) return "null";
        if (apiKey.length() <= maxLen) return apiKey;
        return apiKey.substring(0, maxLen - 3) + "...";
    }

    /**
     * Read a parameter value, falling through:
     *  1) {@code inputs.propertiesFilePath} via PropertyManager;
     *  2) {@code inputs.mstPropertiesFilePath} via PropertyManager (if set);
     *  3) the global PropertyManager.
     *
     * MistRunner is invoked AFTER args parsing in
     * {@link TestGenerationAndExecution#main(String[])}, so the args check
     * from the original implementation is dropped here.
     */
    private String readParameterValue(String propertyName) {
        String value = null;

        if (inputs.propertiesFilePath != null
                && PropertyManager.readProperty(inputs.propertiesFilePath, propertyName) != null) {
            value = PropertyManager.readProperty(inputs.propertiesFilePath, propertyName);
        } else if (inputs.mstPropertiesFilePath != null
                && PropertyManager.readProperty(inputs.mstPropertiesFilePath, propertyName) != null) {
            value = PropertyManager.readProperty(inputs.mstPropertiesFilePath, propertyName);
        } else if (PropertyManager.readProperty(propertyName) != null) {
            value = PropertyManager.readProperty(propertyName);
        }

        return value;
    }

    /**
     * Resolve the test-execution parallelism from (in priority order) the
     * {@code -D} system property, the .properties file, and an "auto"
     * environment-aware fallback. Extracted as a pure static so the resolution
     * rules — auto-detect cap, LLM-aware ceiling, explicit-N pass-through —
     * are unit-testable without standing up a full {@link MistRunner}.
     *
     * @param availableProcessors  result of {@code Runtime.availableProcessors()};
     *                             passed in for testability.
     * @param systemPropValue      value of {@code -Dmst.test.parallelism=…} or null.
     * @param configValue          value read from the user / mst .properties file
     *                             or {@code config.properties}, or null when absent.
     * @param llmValidationEnabled whether {@code llm.response.validation.enabled}
     *                             is on; when true the auto-cap is lowered to
     *                             4 to keep LLM rate-limits under control.
     * @return resolved thread count, always &ge; 1.
     */
    static int resolveTestParallelism(int availableProcessors,
                                      String systemPropValue,
                                      String configValue,
                                      boolean llmValidationEnabled) {
        String raw = pickFirstNonBlank(systemPropValue, configValue, "auto");
        if ("auto".equalsIgnoreCase(raw.trim())) {
            int cap = llmValidationEnabled ? 4 : 8;
            return Math.max(1, Math.min(availableProcessors, cap));
        }
        try {
            return Math.max(1, Integer.parseInt(raw.trim()));
        } catch (NumberFormatException nfe) {
            int cap = llmValidationEnabled ? 4 : 8;
            return Math.max(1, Math.min(availableProcessors, cap));
        }
    }

    /**
     * Three-way boolean parse for {@code llm.response.validation.enabled}
     * style flags: {@code -D} wins over {@code .properties}; explicit "false"
     * stays "false" even when defaultValue is true.
     */
    static boolean parseBooleanProperty(String systemPropValue,
                                        String configValue,
                                        boolean defaultValue) {
        String raw = pickFirstNonBlank(systemPropValue, configValue, null);
        if (raw == null) return defaultValue;
        return Boolean.parseBoolean(raw.trim());
    }

    private static String pickFirstNonBlank(String a, String b, String fallback) {
        if (a != null && !a.trim().isEmpty()) return a;
        if (b != null && !b.trim().isEmpty()) return b;
        return fallback;
    }

    /**
     * Returns the absolute directory holding generated test sources for this
     * MIST run. Resolves {@code inputs.targetDirJava} against the JVM's CWD
     * when it is relative, mirroring the writer's emission and so the JUnit
     * compile/execute machinery finds the same files. Falls back to
     * {@code <CWD>/src/test/java} when no value was supplied.
     */
    private java.nio.file.Path resolveTestTargetRoot() {
        String raw = inputs.targetDirJava;
        if (raw == null || raw.trim().isEmpty()) {
            raw = "src/test/java";
        }
        java.nio.file.Path p = java.nio.file.Paths.get(raw);
        if (!p.isAbsolute()) {
            p = java.nio.file.Paths.get(System.getProperty("user.dir")).resolve(raw);
        }
        return p.normalize();
    }

    /**
     * Resolve a relative path value against {@code inputs.propertiesFilePath}'s
     * directory. Null, empty, or already-absolute values pass through unchanged.
     * Used for INPUT-path readParameterValue calls inside MistRunner so they
     * succeed regardless of the launcher's working directory.
     */
    private String resolveInputPath(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.isEmpty()) return value;
        java.nio.file.Path p = java.nio.file.Paths.get(trimmed);
        if (p.isAbsolute() || inputs.propertiesFilePath == null) return value;
        java.nio.file.Path base = java.nio.file.Paths.get(inputs.propertiesFilePath)
                .toAbsolutePath().normalize().getParent();
        if (base == null) return value;
        return base.resolve(trimmed).normalize().toString();
    }
}
