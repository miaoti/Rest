package es.us.isa.restest.main;

import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationGenerator;
import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationIO;
import es.us.isa.restest.configuration.multiservice.MultiServiceTestConfiguration;
import es.us.isa.restest.configuration.pojos.Auth;
import es.us.isa.restest.configuration.pojos.TestConfiguration;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.coverage.CoverageGatherer;
import es.us.isa.restest.coverage.CoverageMeter;

import java.util.*;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import es.us.isa.restest.generators.*;
import es.us.isa.restest.reporting.AllureReportManager;
import es.us.isa.restest.reporting.StatsReportManager;
import es.us.isa.restest.runners.RESTestWorkflow;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.Timer;
import es.us.isa.restest.workflow.TraceWorkflowExtractor;
import es.us.isa.restest.registry.RootApiRegistry;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowScenarioUtils;
import es.us.isa.restest.writers.IWriter;
import es.us.isa.restest.writers.restassured.MultiServiceRESTAssuredWriter;
import es.us.isa.restest.writers.restassured.RESTAssuredWriter;
import es.us.isa.restest.util.*;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.junit4.AllureJunit4;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static es.us.isa.restest.configuration.TestConfigurationIO.loadConfiguration;
import static es.us.isa.restest.util.FileManager.createDir;
import static es.us.isa.restest.util.FileManager.deleteDir;
import static es.us.isa.restest.util.Timer.TestStep.ALL;
import static io.restassured.RestAssured.baseURI;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

// Test Case Enhancer imports
import es.us.isa.restest.enhancer.FailedTestCollector;
import es.us.isa.restest.enhancer.FailedTestResult;
import es.us.isa.restest.enhancer.TestCaseEnhancer;
import es.us.isa.restest.enhancer.TestFileRegenerator;
import es.us.isa.restest.enhancer.TestResultCapture;
import es.us.isa.restest.enhancer.StatusCodeExplorationEnhancer;
import es.us.isa.restest.llm.LLMService;
import es.us.isa.restest.testcases.MultiServiceTestCase;

/*
 * This class show the basic workflow of test case generation -> test case execution -> test reporting
 */
public class TestGenerationAndExecution {

	// Properties file with configuration settings
	private static String propertiesFilePath = "src/main/resources/My-Example/trainticket-demo.properties";
	private static String TraceFile = "src\\main\\resources\\My-Example\\trainticket\\traces\\";

	private static List<String> argsList;								// List containing args

	private static Integer numTestCases;
	
	// Store generated MST test cases for status code exploration (available during enhancement)
	private static List<MultiServiceTestCase> generatedMSTTestCases = new ArrayList<>();
	
	// Store the writer for generating exploration tests during enhancement
	private static MultiServiceRESTAssuredWriter mstWriter = null;
	// Number of test cases per operation
	private static String OAISpecPath; 									// Path to OAS specification file
	private static OpenAPISpecification spec; 							// OAS specification
	private static String confPath; 									// Path to test configuration file
	private static String targetDirJava; 								// Directory where tests will be generated.
	private static String packageName; 									// Package name.
	private static String experimentName; 								// Used as identifier for folders, etc.
	private static String testClassName; 								// Name prefix of the class to be generated
	private static Boolean enableInputCoverage; 						// Set to 'true' if you want the input coverage report.
	private static Boolean enableOutputCoverage; 						// Set to 'true' if you want the input coverage report.
	private static Boolean enableCSVStats; 								// Set to 'true' if you want statistics in a CSV file.
	private static Boolean deletePreviousResults; 						// Set to 'true' if you want previous CSVs and Allure reports.
	private static Float faultyRatio; 									// Percentage of faulty test cases to generate. Defaults to 0.1
	private static Integer totalNumTestCases; 							// Total number of test cases to be generated (-1 for infinite loop)
	private static Integer timeDelay; 									// Delay between requests in seconds (-1 for no delay)
	private static String generator; 									// Generator (RT: Random testing, CBT:Constraint-based testing)
	private static Boolean logToFile;									// If 'true', log messages will be printed to external files
	private static long testGenerationStartTime = 0;					// Timestamp when test generation started (used for file filtering)
	private static boolean executeTestCases;							// If 'false', test cases will be generated but not executed
	private static boolean allureReports;								// If 'true', Allure reports will be generated
	private static boolean checkTestCases;								// If 'true', test cases will be checked with OASValidator before executing them
	private static String proxy;										// Proxy to use for all requests in format host:port

	// For Constraint-based testing and AR Testing:
	private static Float faultyDependencyRatio; 						// Percentage of faulty test cases due to dependencies to generate.
	private static Integer reloadInputDataEvery; 						// Number of requests using the same randomly generated input data
	private static Integer inputDataMaxValues; 							// Number of values used for each parameter when reloading input data

	// For AR Testing only:
	private static String similarityMetric;								// The algorithm to measure the similarity between test cases
	private static Integer numberCandidates;							// Number of candidate test cases per AR iteration

	private static Logger logger = LogManager.getLogger(TestGenerationAndExecution.class.getName());

	public static void main(String[] args) throws RESTestException, IOException {

		Timer.startCounting(ALL);


		// Read .properties file path. This file contains the configuration parameters for the generation
		if (args.length > 0)
			propertiesFilePath = args[0];

		// Populate configuration parameters, either from arguments or from .properties file
		argsList = Arrays.asList(args);
		readParameterValues();

		// Set proxy globally, if specified
		if (proxy != null) {
			System.setProperty("http.proxyHost", proxy.split(":")[0]);
			System.setProperty("http.proxyPort", proxy.split(":")[1]);
			System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
			System.setProperty("https.proxyHost", proxy.split(":")[0]);
			System.setProperty("https.proxyPort", proxy.split(":")[1]);
			System.setProperty("https.nonProxyHosts", "localhost|127.0.0.1");
		}

		// Create target directory if it does not exists
		createDir(targetDirJava);

		// RESTest runner
		AbstractTestCaseGenerator generator = createGenerator(); // Test case generator
		IWriter writer = createWriter(); // Test case writer
		StatsReportManager statsReportManager = createStatsReportManager(); // Stats reporter
		AllureReportManager reportManager = createAllureReportManager(); // Allure test case reporter

		RESTestWorkflow runner = null;
		
		// For MST mode, we handle the workflow differently since it generates multiple files
		if (!"MST".equals(TestGenerationAndExecution.generator)) {
			runner = new RESTestWorkflow(testClassName, targetDirJava, packageName, spec, confPath, generator, writer,
				reportManager, statsReportManager);
		runner.setExecuteTestCases(executeTestCases);
		runner.setAllureReport(allureReports);
		}



		// Main loop
		int iteration = 1;
		int maxIterations = -1;
		String maxIterProperty = readParameterValue("max.iterations");
		if (maxIterProperty != null) {
			maxIterations = Integer.parseInt(maxIterProperty);
			logger.info("Maximum iterations set to: {}", maxIterations);
		}
		
		if ("MST".equals(TestGenerationAndExecution.generator)) {
			// MST mode: generate once and write multiple files
			logger.info("Running MST mode - generating multi-service test files");
			logger.info("ISOLATION MODE: Each run will generate and execute only newly generated tests");
			
			// 🔥 FIX: Store the start timestamp BEFORE test generation begins
			// This is used to filter which test files to compile/execute (instead of hardcoded 5 minutes)
			testGenerationStartTime = System.currentTimeMillis();
			logger.info("Test generation start timestamp: {} (will be used for file filtering)", testGenerationStartTime);
			
			// Generate unique test class name
			String id = IDGenerator.generateTimeId();
			String className = testClassName + "_" + id;
			logger.info("Generated unique test identifier: {}", className);
			
			// 🔍 FAULT DETECTION: Initialize tracker with injected faults
			logger.info("🔍 Initializing Fault Detection Tracker...");
			String faultsJsonPath = readParameterValue("fault.detection.injected.faults.path");
			if (faultsJsonPath == null || faultsJsonPath.isEmpty()) {
				// Default path if not configured
				faultsJsonPath = "src/main/resources/My-Example/trainticket/injectedFaults/injected-faults.json";
			}
			es.us.isa.restest.analysis.FaultDetectionTracker.getInstance().reset();
			es.us.isa.restest.analysis.FaultDetectionTracker.getInstance().loadInjectedFaults(faultsJsonPath);
			logger.info("🔍 Fault Detection Tracker initialized from: {}", faultsJsonPath);
			
			// Set up writer
			if (writer instanceof MultiServiceRESTAssuredWriter) {
				((MultiServiceRESTAssuredWriter) writer).setClassName(className);
				((MultiServiceRESTAssuredWriter) writer).setTestId(id);
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
			logger.info("TARGET: All test files will be in timestamped package: {}.{}", packageName, className);
			writer.write(testCases);
			
			// Execute tests if enabled
			if (executeTestCases) {
				logger.info("Executing generated test cases");
				logger.info("ISOLATION: Only executing tests from current run (timestamp: {})", id);
				
				// For MST mode, find the actual generated test classes and execute them individually
				String actualPackageName = packageName + "." + className;
				
				// Check if Test Case Enhancer is enabled
				boolean enhancerEnabled = Boolean.parseBoolean(System.getProperty("test.enhancer.enabled", "false"));
				int enhancerRounds = Integer.parseInt(System.getProperty("test.enhancer.rounds", "1"));
				boolean skip5xx = Boolean.parseBoolean(System.getProperty("test.enhancer.skip.5xx", "true"));
				
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
				if (allureReports && reportManager != null) {
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
				es.us.isa.restest.analysis.FaultDetectionTracker.getInstance()
					.generateReport(faultReportDir, experimentName + "_" + id);
				
				// Log detection statistics
				java.util.Map<String, Object> stats = es.us.isa.restest.analysis.FaultDetectionTracker.getInstance().getStatistics();
				logger.info("🔍 Fault Detection Summary:");
				logger.info("   - Total Injected Faults: {}", stats.get("totalInjectedFaults"));
				logger.info("   - Detected Faults: {}", stats.get("detectedFaults"));
				logger.info("   - Detection Rate: {:.1f}%", stats.get("detectionRate"));
				logger.info("   - Report saved to: {}", faultReportDir);
			}
			
			// Generate reports
			if (statsReportManager != null) {
				logger.info("Generating CSV data");
				statsReportManager.generateReport(id, executeTestCases);
			}
			
			logger.info("Iteration 1. {} test cases generated.", testCases.size());
			logger.info("SUMMARY: Generated and executed only newly created tests from timestamp: {}", id);
			logger.info("Stopped after 1 iterations (max.iterations limit reached)");
			
		} else {
			// Classic mode: use RESTestWorkflow
		while ((totalNumTestCases == -1 || runner.getNumTestCases() < totalNumTestCases) && 
		       (maxIterations == -1 || iteration <= maxIterations)) {

			// Introduce optional delay
			if (iteration != 1 && timeDelay != -1)
				delay(timeDelay);

			// Generate unique test class name to avoid the same class being loaded everytime
			String id = IDGenerator.generateTimeId();
			String className = testClassName + "_" + id;
			((RESTAssuredWriter) writer).setClassName(className);
			((RESTAssuredWriter) writer).setTestId(id);
			runner.setTestClassName(className);
			runner.setTestId(id);

			// Test case generation + execution + test report generation
			runner.run();

			logger.info("Iteration {}. {} test cases generated.", iteration, runner.getNumTestCases());
			iteration++;
		}
		
		if (maxIterations != -1 && iteration > maxIterations) {
			logger.info("Stopped after {} iterations (max.iterations limit reached)", maxIterations);
			}
		}

		Timer.stopCounting(ALL);

		generateTimeReport(iteration-1);
		
		logger.info("Test generation and execution completed successfully. Exiting.");
		
		// Force exit to prevent hanging on background threads
		System.exit(0);
	}

	// Create a test case generator
	private static AbstractTestCaseGenerator createGenerator() throws RESTestException, IOException {
		// Load specification
		spec = new OpenAPISpecification(OAISpecPath);


		// Load configuration
		TestConfigurationObject conf = null;

		if (!"MST".equals(generator)) {               // <- only for classic modes
			conf = loadConfiguration(confPath, spec);
		}

		if(generator.equals("FT") && confPath == null) {
			logger.info("No testConf specified. Generating one");
			String[] args = {OAISpecPath};
			CreateTestConf.main(args);

			String specDir = OAISpecPath.substring(0, OAISpecPath.lastIndexOf('/'));
			confPath = specDir + "/testConf.yaml";
			logger.info("Created testConf in '{}'", confPath);
		}

//		conf = loadConfiguration(confPath, spec);


		// Create generator
		AbstractTestCaseGenerator gen = null;

		switch (generator) {
		case "FT":
			gen = new FuzzingTestCaseGenerator(spec, conf, numTestCases);
			break;
		case "RT":
			gen = new RandomTestCaseGenerator(spec, conf, numTestCases);
			((RandomTestCaseGenerator) gen).setFaultyRatio(faultyRatio);
			break;
		case "CBT":
			gen = new ConstraintBasedTestCaseGenerator(spec, conf, numTestCases);
			((ConstraintBasedTestCaseGenerator) gen).setFaultyDependencyRatio(faultyDependencyRatio);
			((ConstraintBasedTestCaseGenerator) gen).setInputDataMaxValues(inputDataMaxValues);
			((ConstraintBasedTestCaseGenerator) gen).setReloadInputDataEvery(reloadInputDataEvery);
			gen.setFaultyRatio(faultyRatio);
			break;
		case "ART":
			gen = new ARTestCaseGenerator(spec, conf, numTestCases);
			((ARTestCaseGenerator) gen).setFaultyDependencyRatio(faultyDependencyRatio);
			((ARTestCaseGenerator) gen).setInputDataMaxValues(inputDataMaxValues);
			((ARTestCaseGenerator) gen).setReloadInputDataEvery(reloadInputDataEvery);
			((ARTestCaseGenerator) gen).setDiversity(similarityMetric);
			((ARTestCaseGenerator) gen).setNumberOfCandidates(numberCandidates);
			gen.setFaultyRatio(faultyRatio);
			break;
		case "LLM":
			gen = new LLMOnlyTestCaseGenerator(spec, conf, numTestCases);
			// gen.setFaultyRatio(faultyRatio);
			break;
		case "Word2Vec":
			gen = new Word2VecTestCaseGenerator(spec, conf, numTestCases);
			break;
		case "SAT":
			AbstractTestCaseGenerator base =
					new SmartSemanticTestCaseGenerator(spec, conf, numTestCases);
			gen = new SchemaGuidedDependencyMutator(base, spec, conf, numTestCases, 0.15f);
			break;

			case "MST": {                                             // multi‑service
				// 1. OpenAPI spec (single file or already merged)
				spec = new OpenAPISpecification(OAISpecPath);

				TestConfiguration emptyTc = new TestConfiguration();
				emptyTc.setOperations(new ArrayList<>());      // ← avoid NPE in preconditions

				Auth emptyAuth = new Auth();                   // no API‑keys, just present
				TestConfigurationObject dummyPrimaryConf = new TestConfigurationObject();
				dummyPrimaryConf.setTestConfiguration(emptyTc);
				dummyPrimaryConf.setAuth(emptyAuth);

				// 3. Load the **multi‑service** YAML
				FileInputStream in = new FileInputStream(confPath);
				Map<String, TestConfigurationObject> serviceConfigs =
						MicroserviceTestConfigurationIO.loadMultiServiceConfiguration(in);
				in.close();

				if (serviceConfigs.isEmpty()) {
					throw new RESTestException(
							"No services found in configuration file " + confPath);
				}

				// 4. Build a map service‑name → spec (single‑spec variant)
				Map<String, OpenAPISpecification> serviceSpecs = new LinkedHashMap<>();
				for (String svc : serviceConfigs.keySet()) {
					serviceSpecs.put(svc, spec);
				}

				// 5. Get the recorded workflows from the trace file
                                List<WorkflowScenario> scenarios =
                                                TraceWorkflowExtractor.extractScenarios(TraceFile);

				// 5.5. Register root APIs with their tree structures in the registry
				// ⚠️ IMPORTANT: Register BEFORE deduplication to capture ALL trace patterns
				String registryPath = readParameterValue("root.api.registry.path");
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
				if (numTestCases != null) {
					System.setProperty("testsperoperation", numTestCases.toString());
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
				if (faultyRatio != null) {
					System.setProperty("faulty.ratio", faultyRatio.toString());
					logger.info("MST faulty ratio: {}", faultyRatio);
				}
				
				// Pass faulty.round-robin strategy to MST generator
				String faultyRoundRobin = readParameterValue("faulty.round-robin");
				if (faultyRoundRobin != null) {
					System.setProperty("faulty.round-robin", faultyRoundRobin);
					logger.info("MST faulty round-robin mode: {}", faultyRoundRobin);
				}
				
				// Pass negative input generation mode (llm/hardcode) to MST generator
				String negativeInputMode = readParameterValue("negative.input.generation.mode");
				if (negativeInputMode != null) {
					System.setProperty("negative.input.generation.mode", negativeInputMode);
					logger.info("MST negative input generation mode: {} ({})", negativeInputMode,
							"llm".equalsIgnoreCase(negativeInputMode) ? 
									"LLM-generated context-aware inputs" : "Deterministic hardcoded inputs");
				} else {
					// Default to hardcode mode for faster execution
					System.setProperty("negative.input.generation.mode", "hardcode");
					logger.info("MST negative input generation mode: hardcode (default - faster, deterministic)");
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
				break;
			}



			default:
			throw new RESTestException("Property 'generator' must be one of 'FT', 'RT', 'CBT' or 'ART'");
		}

		gen.setCheckTestCases(checkTestCases);

		return gen;
	}

	// Create a writer for RESTAssured
	private static IWriter createWriter() {
		// Get base URL from properties or default
		String baseUrl = readParameterValue("base.url");
		if (baseUrl == null) {
			// Fallback to spec if no base.url property is set
			baseUrl = spec.getSpecification().getServers().get(0).getUrl();
		}

		if ("MST".equals(generator)) {
				// Multi‑service mode: hand off to the specialized writer
						MultiServiceRESTAssuredWriter msWriter = new MultiServiceRESTAssuredWriter(
								OAISpecPath, confPath, targetDirJava, testClassName, packageName, baseUrl, logToFile
						);
				// exactly mirror single‑service feature‑toggles:
						msWriter.setLogging(true);
				msWriter.setAllureReport(allureReports);
				msWriter.setEnableStats(enableCSVStats);
				msWriter.setEnableOutputCoverage(enableOutputCoverage);
				msWriter.setAPIName(experimentName);
				msWriter.setProxy(proxy);
				return msWriter;
			} else {
			// Classic single‑service mode
			RESTAssuredWriter writer = new RESTAssuredWriter(
					OAISpecPath,
					confPath,
					targetDirJava,
					testClassName,
					packageName,
					baseUrl,
					logToFile
			);
			writer.setLogging(true);
			writer.setAllureReport(true);
			writer.setEnableStats(enableCSVStats);
			writer.setEnableOutputCoverage(enableOutputCoverage);
			writer.setAPIName(experimentName);
			writer.setProxy(proxy);
			return writer;
		}
	}


	// Create an Allure report manager
	private static AllureReportManager createAllureReportManager() {
		AllureReportManager arm = null;
		if(executeTestCases) {
			String allureResultsDir;
			String allureReportDir;
			
			if ("MST".equals(generator)) {
				// For MST mode, use base directories directly since Maven generates results in target/allure-results
				allureResultsDir = readParameterValue("allure.results.dir");
				allureReportDir = readParameterValue("allure.report.dir");
			} else {
				// For classic modes, use subdirectories by experiment name
				allureResultsDir = readParameterValue("allure.results.dir") + "/" + experimentName;
				allureReportDir = readParameterValue("allure.report.dir") + "/" + experimentName;
			}

			if (deletePreviousResults) {
				deleteDir(allureResultsDir);
				deleteDir(allureReportDir);
			}

			//Find auth property names (if any)
			List<String> authProperties = Collections.emptyList();
			if (!"MST".equals(generator) && confPath != null) {
				// Only load auth properties for non-MST modes
				authProperties = AllureAuthManager.findAuthProperties(spec, confPath);
			} else if ("MST".equals(generator)) {
				// For MST mode, we'll use default empty auth properties
				logger.info("Using default auth properties for MST mode");
			}
			arm = new AllureReportManager(allureResultsDir, allureReportDir, authProperties);
			arm.setEnvironmentProperties(propertiesFilePath);
			arm.setHistoryTrend(true);
		}
		return arm;
	}

	// Create an statistics report manager
	private static StatsReportManager createStatsReportManager() {
		String testDataDir = readParameterValue("data.tests.dir") + "/" + experimentName;
		String coverageDataDir = readParameterValue("data.coverage.dir") + "/" + experimentName;

		// Delete previous results (if any)
		if (deletePreviousResults) {
			deleteDir(testDataDir);
			deleteDir(coverageDataDir);

			// Recreate directories
			createDir(testDataDir);
			createDir(coverageDataDir);
		}

		CoverageMeter coverageMeter = null;
		
		// Only create CoverageMeter for non-MST modes since MST has different config structure
		if ((enableInputCoverage || enableOutputCoverage) && !"MST".equals(generator)) {
			coverageMeter = new CoverageMeter(new CoverageGatherer(spec));
		} else if ("MST".equals(generator)) {
			// For MST mode, we skip coverage measurement due to multi-service configuration structure
			logger.info("Coverage measurement disabled for MST mode due to multi-service configuration structure");
		}

		return new StatsReportManager(testDataDir, coverageDataDir, enableCSVStats, enableInputCoverage,
					enableOutputCoverage, coverageMeter);
	}

	private static void generateTimeReport(Integer iterations) {
		String timePath = readParameterValue("data.tests.dir") + "/" + experimentName + "/" + readParameterValue("data.tests.time");
		try {
			Timer.exportToCSV(timePath, iterations);
		} catch (RuntimeException e) {
			logger.error("The time report cannot be generated. Stack trace:");
			logger.error(e.getMessage());
		}
		logger.info("Time report generated.");
	}

	/*
	 * Stop the execution n seconds
	 */
	private static void delay(Integer time) {
		try {
			logger.info("Introducing delay of {} seconds", time);
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			logger.error("Error introducing delay", e);
			logger.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	// Read the parameter values from the .properties file. If the value is not found, the system looks for it in the global .properties file (config.properties)
	private static void readParameterValues() {

		logToFile = Boolean.parseBoolean(readParameterValue("logToFile"));
		if(logToFile) {
			setUpLogger();
		}

		logger.info("Loading configuration parameter values");
		
		generator = readParameterValue("generator");
		logger.info("Generator: {}", generator);
		
		OAISpecPath = readParameterValue("oas.path");
		logger.info("OAS path: {}", OAISpecPath);
		
		confPath = readParameterValue("conf.path");
		logger.info("Test configuration path: {}", confPath);
		
		targetDirJava = readParameterValue("test.target.dir");
		logger.info("Target dir for test classes: {}", targetDirJava);
		
		experimentName = readParameterValue("experiment.name");
		logger.info("Experiment name: {}", experimentName);
		packageName = experimentName;

		if (readParameterValue("experiment.execute") != null) {
			executeTestCases = Boolean.parseBoolean(readParameterValue("experiment.execute"));
		}
		logger.info("Experiment execution: {}", executeTestCases);

		if (readParameterValue("allure.report") != null) {
			allureReports = Boolean.parseBoolean(readParameterValue("allure.report"));
		}
		logger.info("Allure reports: {}", allureReports);

		if (readParameterValue("proxy") != null) {
			proxy = readParameterValue("proxy");
			if ("null".equals(proxy) || proxy.split(":").length != 2)
				proxy = null;
		}
		logger.info("Proxy: {}", proxy);

		if (readParameterValue("testcases.check") != null)
			checkTestCases = Boolean.parseBoolean(readParameterValue("testcases.check"));
		logger.info("Check test cases: {}", checkTestCases);
		
		testClassName = readParameterValue("testclass.name");
		logger.info("Test class name: {}", testClassName);

		if (readParameterValue("testsperoperation") != null)
			numTestCases = Integer.parseInt(readParameterValue("testsperoperation"));
		logger.info("Number of test cases per operation: {}", numTestCases);

		if (readParameterValue("numtotaltestcases") != null)
			totalNumTestCases = Integer.parseInt(readParameterValue("numtotaltestcases"));
		logger.info("Max number of test cases: {}", totalNumTestCases);

		if (readParameterValue("delay") != null)
			timeDelay = Integer.parseInt(readParameterValue("delay"));
		logger.info("Time delay: {}", timeDelay);

		if (readParameterValue("reloadinputdataevery") != null)
			reloadInputDataEvery = Integer.parseInt(readParameterValue("reloadinputdataevery"));
		logger.info("Input data reloading  (CBT): {}", reloadInputDataEvery);

		if (readParameterValue("inputdatamaxvalues") != null)
			inputDataMaxValues = Integer.parseInt(readParameterValue("inputdatamaxvalues"));
		logger.info("Max input test data (CBT): {}", inputDataMaxValues);

		if (readParameterValue("coverage.input") != null)
			enableInputCoverage = Boolean.parseBoolean(readParameterValue("coverage.input"));
		logger.info("Input coverage: {}", enableInputCoverage);

		if (readParameterValue("coverage.output") != null)
			enableOutputCoverage = Boolean.parseBoolean(readParameterValue("coverage.output"));
		logger.info("Output coverage: {}", enableOutputCoverage);

		if (readParameterValue("stats.csv") != null)
			enableCSVStats = Boolean.parseBoolean(readParameterValue("stats.csv"));
		logger.info("CSV statistics: {}", enableCSVStats);

		if (readParameterValue("deletepreviousresults") != null)
			deletePreviousResults = Boolean.parseBoolean(readParameterValue("deletepreviousresults"));
		logger.info("Delete previous results: {}", deletePreviousResults);

		if (readParameterValue("similarity.metric") != null)
			similarityMetric = readParameterValue("similarity.metric");
		logger.info("Similarity metric: {}", similarityMetric);

		if (readParameterValue("art.number.candidates") != null)
			numberCandidates = Integer.parseInt(readParameterValue("art.number.candidates"));
		logger.info("Number of candidates: {}", numberCandidates);

		if (readParameterValue("faulty.ratio") != null)
			faultyRatio = Float.parseFloat(readParameterValue("faulty.ratio"));
		logger.info("Faulty ratio: {}", faultyRatio);

		if (readParameterValue("faulty.dependency.ratio") != null)
			faultyDependencyRatio = Float.parseFloat(readParameterValue("faulty.dependency.ratio"));
		logger.info("Faulty dependency ratio: {}", faultyDependencyRatio);


	}

	// Read the parameter value from: 1) CLI; 2) the local .properties file; 3) the global .properties file (config.properties)
	private static String readParameterValue(String propertyName) {

		String value = null;

		if (argsList.contains(propertyName))
			value = argsList.get(argsList.indexOf(propertyName) + 1);
		else if (argsList.stream().anyMatch(arg -> arg.matches("^" + propertyName + "=.*")))
			value = argsList.stream().filter(arg -> arg.matches("^" + propertyName + "=.*")).findFirst().get().split("=")[1];
		else if (PropertyManager.readProperty(propertiesFilePath, propertyName) != null) // Read value from local .properties file
			value = PropertyManager.readProperty(propertiesFilePath, propertyName);
		else if (PropertyManager.readProperty(propertyName) != null) // Read value from global .properties file
			value = PropertyManager.readProperty(propertyName);

		return value;
	}

	/**
	 * Pass Smart Input Fetching and LLM configuration properties to system properties
	 * This ensures MST generator can access the smart fetching and LLM configuration
	 */
	private static void passSmartInputFetchingProperties() {
		logger.info("🔧 Configuring Smart Input Fetching and LLM for MST mode...");

		// List of all smart input fetching and LLM properties
		String[] smartProperties = {
			"smart.input.fetch.enabled",
			"smart.input.fetch.percentage",
			"smart.input.fetch.registry.path",
			"smart.input.fetch.openapi.spec.path",
			"smart.input.fetch.llm.discovery.enabled",
			"smart.input.fetch.max.candidates",
			"smart.input.fetch.dependency.resolution.enabled",
			"smart.input.fetch.discovery.timeout.ms",
			"smart.input.fetch.cache.enabled",
			"smart.input.fetch.cache.ttl.seconds",
			"oas.path",
			"base.url",
			// LLM configuration properties
			"llm.enabled",
			"llm.model.type",
			"llm.local.enabled",
			"llm.local.url",
			"llm.local.model",
			"llm.gemini.enabled",
			"llm.gemini.api.key",
			"llm.gemini.model",
			"llm.gemini.api.url",
			"llm.ollama.enabled",
			"llm.ollama.url",
			"llm.ollama.model",
			"llm.rate.limit.retry.enabled",
			"llm.rate.limit.max.retries",
			// Authentication properties for smart fetch
			"auth.admin.username",
			"auth.admin.password",
			"auth.user.username",
			"auth.user.password",
			// Jaeger trace fetching properties
			"jaeger.enabled",
			"jaeger.base.url",
			"jaeger.lookback",
			// LLM response validation properties (soft error detection)
			"llm.response.validation.enabled",
			"llm.response.validation.only.2xx",
			"llm.response.validation.include.rca",
			// LLM communication logging properties
			"llm.communication.logging.enabled",
			"llm.communication.logging.dir",
			"llm.communication.logging.file.prefix",
			"llm.communication.logging.include.response.time",
			"llm.communication.logging.include.content",
			"llm.communication.logging.include.metadata",
			"llm.communication.logging.level",
			"llm.communication.logging.max.content.length",
			// LLM resource monitoring properties
			"llm.resource.monitoring.enabled",
			"llm.resource.monitoring.interval.ms",
			// Test Case Enhancer properties
			"test.enhancer.enabled",
			"test.enhancer.rounds",
			"test.enhancer.skip.5xx",
			// Status Code Exploration properties
			"status.code.exploration.enabled",
			"status.code.exploration.max.per.test",
			"status.code.exploration.max.per.round"
		};
		
		int configuredCount = 0;
		for (String property : smartProperties) {
			String value = readParameterValue(property);
			if (value != null) {
				System.setProperty(property, value);
				logger.info("✅ Set system property: {} = {}", property, value);
				configuredCount++;
			} else {
				logger.debug("⚠️  Property not found: {}", property);
			}
		}
		
		if (configuredCount > 0) {
			logger.info("🚀 Smart Input Fetching and LLM configured with {} properties for MST mode", configuredCount);

			// Log the key settings
			String enabled = System.getProperty("smart.input.fetch.enabled", "false");
			String percentage = System.getProperty("smart.input.fetch.percentage", "0.0");
			String registryPath = System.getProperty("smart.input.fetch.registry.path", "not set");

			// Log LLM settings
			String llmEnabled = System.getProperty("llm.enabled", "false");
			String llmModelType = System.getProperty("llm.model.type", "local");
			String geminiApiKey = System.getProperty("llm.gemini.api.key", "not set");
			String ollamaEnabled = System.getProperty("llm.ollama.enabled", "false");
			String ollamaModel = System.getProperty("llm.ollama.model", "not set");

			logger.info("📊 Smart Fetching Settings:");
			logger.info("   - Enabled: {}", enabled);
			logger.info("   - Percentage: {}% smart fetching",
				Float.parseFloat(percentage) * 100);
			logger.info("   - Registry: {}", registryPath);

			logger.info("🤖 LLM Settings:");
			logger.info("   - Enabled: {}", llmEnabled);
			logger.info("   - Model Type: {}", llmModelType);
			logger.info("   - Gemini API Key: {}", geminiApiKey.equals("not set") ? "not set" : "configured");
			logger.info("   - Ollama Enabled: {}", ollamaEnabled);
			logger.info("   - Ollama Model: {}", ollamaModel);
			
			// Log LLM response validation settings (soft error detection)
			String llmValidationEnabled = System.getProperty("llm.response.validation.enabled", "false");
			String llmValidationOnly2xx = System.getProperty("llm.response.validation.only.2xx", "true");
			String llmValidationRca = System.getProperty("llm.response.validation.include.rca", "true");
			
			logger.info("🔍 LLM Response Validation (Soft Error Detection):");
			logger.info("   - Enabled: {}", llmValidationEnabled);
			logger.info("   - Only 2XX responses: {}", llmValidationOnly2xx);
			logger.info("   - Include RCA in reports: {}", llmValidationRca);
			
			if ("true".equals(enabled)) {
				logger.info("🎯 Smart Input Fetching is ENABLED - you should see 'Smart Fetch' logs during test generation!");
			} else {
				logger.warn("❌ Smart Input Fetching is DISABLED - enable it by setting smart.input.fetch.enabled=true");
			}
		} else {
			logger.warn("⚠️  No Smart Input Fetching properties found - make sure they're in your properties file");
		}
	}


	public static TestConfigurationObject getTestConfigurationObject(){
		return loadConfiguration(confPath, spec);
	}

	public static String getExperimentName(){ return experimentName; }

	/**
	 * Execute generated test classes using direct JUnit execution optimized for IntelliJ
	 * Programmatically adds AspectJ weaving support for Allure step capture
	 * ENHANCED: Better isolation to ensure only newly generated tests are executed
	 */
	private static void executeGeneratedTestsWithJUnit(String fullPackageName, String className) {
		logger.info("Executing generated tests for package: {} and class: {} (IntelliJ mode)", fullPackageName, className);
		
		try {
			// ENHANCED: Clean old compiled test classes to avoid conflicts
			cleanOldCompiledTestClasses(fullPackageName);
			
			// Set up Allure environment for IntelliJ
			setupAllureForIntelliJ();
			
			// Find the test class directory
			String baseDir = System.getProperty("user.dir");
			String packagePath = fullPackageName.replace('.', '/');
			File testClassDir = new File(baseDir + "/src/test/java/" + packagePath);
			
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
					java.lang.ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
			
			// Execute all test classes
			Result result = junit.run(testClasses.toArray(new Class[0]));
			
			Timer.stopCounting(Timer.TestStep.TEST_SUITE_EXECUTION);
			
			// Log results
			logger.info("=== TEST EXECUTION RESULTS (NEWLY GENERATED TESTS ONLY) ===");
			logger.info("Tests run: {}", result.getRunCount());
			logger.info("Failures: {}", result.getFailureCount());
			logger.info("Ignored: {}", result.getIgnoreCount());
			logger.info("Run time: {} ms", result.getRunTime());
			
			if (result.getFailureCount() > 0) {
				logger.error("=== FAILURES ===");
				for (Failure failure : result.getFailures()) {
					logger.error("Failed: {} - {}", failure.getDescription().getDisplayName(), failure.getMessage());
				}
			}
			
			if (result.wasSuccessful()) {
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
	private static void executeWithEnhancement(String fullPackageName, String className, 
											   int enhancerRounds, boolean skip5xx, String testId) {
		logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
		logger.info("║              TEST CASE ENHANCER - MULTI-ROUND EXECUTION                     ║");
		logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
		logger.info("║  Rounds: {}                                                                  ║", enhancerRounds);
		logger.info("║  Skip 5xx: {}                                                               ║", skip5xx);
		logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");
		
		String enhancerOutputDir = "target/enhancer/" + testId;
		
		// Check if status code exploration is enabled
		boolean statusCodeExplorationEnabled = Boolean.parseBoolean(
			System.getProperty("status.code.exploration.enabled", "false"));
		int maxExplorationPerTest = Integer.parseInt(
			System.getProperty("status.code.exploration.max.per.test", "3"));
		int maxExplorationPerRound = Integer.parseInt(
			System.getProperty("status.code.exploration.max.per.round", "20"));
		
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
				
				// Log round results
				logger.info("╔══════════════════════════════════════════════════════════════════════════════╗");
				logger.info("║  ROUND {} RESULTS                                                            ║", round);
				logger.info("╠══════════════════════════════════════════════════════════════════════════════╣");
				logger.info("║  Tests Run: {}                                                               ║", result.getRunCount());
				logger.info("║  Failures: {}                                                                ║", result.getFailureCount());
				logger.info("║  Enhanceable Failures: {}                                                    ║", collector.getFailedTestCount());
				logger.info("╚══════════════════════════════════════════════════════════════════════════════╝");
				
				// Status Code Exploration: Run after round 0 to discover and create exploration tests
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
				}
				
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
				
				// Save enhancement results
				enhancer.saveEnhancementResults(enhancementResults, enhancerOutputDir, round);
				
				// Regenerate test files with enhanced values
				int regenerated = 0;
				for (int i = 0; i < enhancementResults.size(); i++) {
					TestCaseEnhancer.EnhancementResult enhancement = enhancementResults.get(i);
					if (!enhancement.isSuccess()) {
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
				}
				
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
			}
			
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
	 * @param skipAllureClean if true, skip Allure setup/cleaning (for exploration tests that should add to existing results)
	 */
	private static Result executeTestsWithCollector(String fullPackageName, String className,
													FailedTestCollector collector, boolean isFinalRound) {
		return executeTestsWithCollector(fullPackageName, className, collector, isFinalRound, false);
	}
	
	/**
	 * Execute tests with a FailedTestCollector to gather failure information.
	 * @param skipAllureClean if true, skip Allure setup/cleaning (for exploration tests that should add to existing results)
	 */
	private static Result executeTestsWithCollector(String fullPackageName, String className,
													FailedTestCollector collector, boolean isFinalRound,
													boolean skipAllureClean) {
		try {
			// Clean and setup
			cleanOldCompiledTestClasses(fullPackageName);
			
			// Only setup Allure (which cleans) if not skipping
			if (!skipAllureClean) {
				setupAllureForIntelliJ();
			}
			
			// For non-final rounds, clear Allure results to avoid accumulating intermediate results
			if (!isFinalRound && !skipAllureClean) {
				clearAllureResults();
			}
			
			// Find test class directory
			String baseDir = System.getProperty("user.dir");
			String packagePath = fullPackageName.replace('.', '/');
			File testClassDir = new File(baseDir + "/src/test/java/" + packagePath);
			
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
			
			// Add to classpath
			addTestClassesToClasspath();
			
			// 🔧 CRITICAL FIX: Create a fresh ClassLoader for each round
			// The JVM caches loaded classes, so we need a new ClassLoader to pick up
			// changes from recompiled test files during enhancement rounds
			File testClassesDir = new File(baseDir, "target/test-classes");
			java.net.URL[] urls = new java.net.URL[] { testClassesDir.toURI().toURL() };
			
			// Create isolated ClassLoader that loads from test-classes first
			java.net.URLClassLoader freshClassLoader = new java.net.URLClassLoader(
				urls, 
				TestGenerationAndExecution.class.getClassLoader()
			) {
				// Override loadClass to force loading from our URLs first for test classes
				@Override
				public Class<?> loadClass(String name) throws ClassNotFoundException {
					// For test classes in our package, try to load fresh from URL first
					if (name.startsWith(fullPackageName)) {
						try {
							// First check if class file exists in our test-classes
							String classFile = name.replace('.', '/') + ".class";
							java.net.URL resource = findResource(classFile);
							if (resource != null) {
								// Load fresh by reading the bytes directly
								try (java.io.InputStream is = resource.openStream()) {
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
			
			// Add console listener
			junit.addListener(new RunListener() {
				@Override
				public void testStarted(Description description) {
					logger.debug("Starting: {}", description.getMethodName());
				}
				
				@Override
				public void testFailure(Failure failure) {
					logger.debug("Failed: {} - {}", failure.getDescription().getMethodName(), 
							failure.getMessage() != null ? failure.getMessage().substring(0, Math.min(100, failure.getMessage().length())) : "");
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
	private static List<String> findTestClassNames(File testClassDir, String packageName) {
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
	private static String findTestFilePath(String packageName, String className, 
										   String testClassName, String testMethodName) {
		String baseDir = System.getProperty("user.dir");
		String packagePath = packageName.replace('.', '/');
		
		// The test class might be directly in the package or in a subdirectory
		// Try direct path first
		String directPath = baseDir + "/src/test/java/" + packagePath + "/" + 
				testClassName.substring(testClassName.lastIndexOf('.') + 1) + ".java";
		File directFile = new File(directPath);
		if (directFile.exists()) {
			return directPath;
		}
		
		// Try with full class path
		String fullClassPath = testClassName.replace('.', '/');
		String fullPath = baseDir + "/src/test/java/" + fullClassPath + ".java";
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
	private static void cleanOldCompiledTestClasses(String fullPackageName) {
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
	private static boolean deleteDirectory(File dir) {
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
	private static boolean compileTestClasses() {
		try {
			String baseDir = System.getProperty("user.dir");
			File testSourceDir = new File(baseDir, "src/test/java");
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
			javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
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
				.collect(java.util.stream.Collectors.toList());
			
			// Combine options and file names
			List<String> compilerArgs = new ArrayList<>();
			compilerArgs.addAll(options);
			compilerArgs.addAll(fileNames);
			
			// 🔧 FIX: Capture compiler output to detect compilation errors
			java.io.ByteArrayOutputStream errorStream = new java.io.ByteArrayOutputStream();
			java.io.PrintStream errorPrintStream = new java.io.PrintStream(errorStream);
			
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
				String className = javaFile.getName().replace(".java", ".class");
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
			} else if (failedCount > 0) {
				logger.error("❌ Some files failed to compile ({} failures). Falling back to Maven...", failedCount);
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
	private static List<File> findNewlyGeneratedJavaFiles(File dir) {
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
	private static void findRecentJavaFiles(File dir, List<File> javaFiles, long cutoffTime) {
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
	private static List<File> findJavaFiles(File dir) {
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
	private static String buildCompilationClasspath(File mainClassesDir) {
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
	private static boolean fallbackMavenCompilation() {
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
	private static void setupJava11Environment() {
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
			if (javax.tools.ToolProvider.getSystemJavaCompiler() == null) {
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
	private static boolean compileTestClassesAlternative() throws Exception {
		logger.info("Attempting alternative test compilation method...");
		
		// This method is now redundant since we have fast direct compilation
		// Just call the main compilation method
		return compileTestClasses();
	}
	
	/**
	 * Add test-classes directory to classpath programmatically
	 * This is necessary for IntelliJ execution where test-classes may not be on the classpath
	 */
	private static boolean addTestClassesToClasspath() {
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
			java.net.URL testClassesURL = testClassesDir.toURI().toURL();
			
			// Get current thread's context class loader
			java.lang.ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
			
			// Create new URLClassLoader with test-classes added
			java.net.URLClassLoader newClassLoader = new java.net.URLClassLoader(
				new java.net.URL[]{testClassesURL}, 
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
	private static void clearAllureResults() {
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
	private static void setupAllureForIntelliJ() {
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


	private static void setUpLogger() {
		// Recreate log directory if necessary
		if (Boolean.parseBoolean(readParameterValue("deletepreviousresults"))) {
			String logDataDir = readParameterValue("data.log.dir") + "/" + readParameterValue("experiment.name");
			deleteDir(logDataDir);
			createDir(logDataDir);
		}

		// Attach stdout and stderr to logger
		System.setOut(new PrintStream(new LoggerStream(LogManager.getLogger("stdout"), Level.INFO, System.out)));
		System.setErr(new PrintStream(new LoggerStream(LogManager.getLogger("stderr"), Level.ERROR, System.err)));

		// Configure regular logger
		String logPath = readParameterValue("data.log.dir") + "/" + readParameterValue("experiment.name") + "/" + readParameterValue("data.log.file");

		System.setProperty("logFilename", logPath);
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		File file = new File("src/main/resources/log4j2-logToFile.properties");
		ctx.setConfigLocation(file.toURI());
		ctx.reconfigure();
	}
	
	/**
	 * Build exploration results from test execution data.
	 * Extracts status codes and response information from captured test results.
	 */
	private static Map<String, StatusCodeExplorationEnhancer.TestExecutionResult> buildExplorationResults(
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
	private static String getApiKeyFromTest(MultiServiceTestCase test) {
		if (test == null || test.getSteps().isEmpty()) return null;
		MultiServiceTestCase.StepCall step = test.getSteps().get(0);
		return step.getMethod().toString() + " " + step.getPath();
	}
	
	/**
	 * Extract API key (HTTP method + path) from test name.
	 * Test names typically follow pattern: test_METHOD_operationId_variant
	 */
	private static String extractApiKeyFromTestName(String testName) {
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
	private static void logStatusCodeCoverage(
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
	private static String getStatusCodeCategory(int statusCode) {
		if (statusCode >= 200 && statusCode < 300) return "Success";
		if (statusCode >= 300 && statusCode < 400) return "Redirect";
		if (statusCode >= 400 && statusCode < 500) return "Client Error";
		if (statusCode >= 500) return "Server Error";
		return "Unknown";
	}
	
	/**
	 * Truncate API key for display.
	 */
	private static String truncateApiKey(String apiKey, int maxLen) {
		if (apiKey == null) return "null";
		if (apiKey.length() <= maxLen) return apiKey;
		return apiKey.substring(0, maxLen - 3) + "...";
	}
	
}
