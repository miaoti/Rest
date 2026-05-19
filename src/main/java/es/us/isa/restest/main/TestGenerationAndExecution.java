package es.us.isa.restest.main;

import es.us.isa.restest.configuration.multiservice.MstConfig;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.coverage.CoverageGatherer;
import es.us.isa.restest.coverage.CoverageMeter;

import java.io.*;
import java.util.*;

import es.us.isa.restest.generators.*;
import es.us.isa.restest.reporting.AllureReportManager;
import es.us.isa.restest.reporting.StatsReportManager;
import es.us.isa.restest.runners.RESTestWorkflow;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.util.Timer;
import es.us.isa.restest.writers.IWriter;
import es.us.isa.restest.writers.restassured.RESTAssuredWriter;
import es.us.isa.restest.util.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.concurrent.TimeUnit;

import static es.us.isa.restest.configuration.TestConfigurationIO.loadConfiguration;
import static es.us.isa.restest.util.FileManager.createDir;
import static es.us.isa.restest.util.FileManager.deleteDir;
import static es.us.isa.restest.util.Timer.TestStep.ALL;

import java.io.IOException;

/*
 * This class show the basic workflow of test case generation -> test case execution -> test reporting
 */
public class TestGenerationAndExecution {

	// Properties file with configuration settings
	private static String propertiesFilePath = "src/main/resources/My-Example/trainticket-demo.properties";
	/** MST-only properties file (loaded only when generator=MST; resolved from the
	 *  {@code mst.config.path} key in {@link #propertiesFilePath}). Held in a static
	 *  field so {@link #readParameterValue(String)} can fall through to it for any
	 *  MST-only key without forcing every call site to know about MstConfig. */
	private static String mstPropertiesFilePath;
	private static MstConfig mstConfig;

	private static List<String> argsList;								// List containing args

	private static Integer numTestCases;

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

		// MST mode loads its dedicated properties file (LLM, smart fetch, jaeger,
		// fault detection, soft-error cache, enhancer, status-code exploration,
		// root-API registry, trace merging, ...) and pushes every key to System
		// properties so generators / writers / generated test code that read via
		// System.getProperty keep working unchanged. Classic generators never
		// reach this branch and never see MST configuration. The MST run path
		// is delegated to MistRunner (Stage 1.A of PATH_B_REBUILD_PLAN.md).
		if ("MST".equals(generator)) {
			loadMstConfig();

			es.us.isa.restest.configuration.MstConfig cfg =
					es.us.isa.restest.configuration.MstConfig.fromSystemProperties();
			MistRunner.Inputs inputs = MistRunner.Inputs.builder()
					.testClassName(testClassName)
					.targetDirJava(targetDirJava)
					.packageName(packageName)
					.experimentName(experimentName)
					.oasPath(OAISpecPath)
					.confPath(confPath)
					.propertiesFilePath(propertiesFilePath)
					.mstPropertiesFilePath(mstPropertiesFilePath)
					.traceFilePath("src/main/resources/My-Example/trainticket/test-trace")
					.numTestCases(numTestCases)
					.faultyRatio(faultyRatio)
					.executeTestCases(executeTestCases)
					.allureReports(allureReports)
					.enableCSVStats(enableCSVStats)
					.enableInputCoverage(enableInputCoverage)
					.enableOutputCoverage(enableOutputCoverage)
					.deletePreviousResults(deletePreviousResults)
					.logToFile(logToFile)
					.checkTestCases(checkTestCases)
					.proxy(proxy)
					.build();
			java.nio.file.Path workdir = java.nio.file.Paths.get(System.getProperty("user.dir"));
			try {
				MistRunResult result = new MistRunner(cfg, workdir, inputs).run();
				result.summarise(System.out);
				System.exit(result.exitCode());
			} catch (Exception e) {
				logger.error("MistRunner failed: {}", e.getMessage(), e);
				System.exit(1);
			}
			return;
		}

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

		RESTestWorkflow runner = new RESTestWorkflow(testClassName, targetDirJava, packageName, spec, confPath, generator, writer,
				reportManager, statsReportManager);
		runner.setExecuteTestCases(executeTestCases);
		runner.setAllureReport(allureReports);

		// Main loop
		int iteration = 1;
		int maxIterations = -1;
		String maxIterProperty = readParameterValue("max.iterations");
		if (maxIterProperty != null) {
			maxIterations = Integer.parseInt(maxIterProperty);
			logger.info("Maximum iterations set to: {}", maxIterations);
		}

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
		TestConfigurationObject conf = loadConfiguration(confPath, spec);

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


	// Create an Allure report manager
	private static AllureReportManager createAllureReportManager() {
		AllureReportManager arm = null;
		if(executeTestCases) {
			// For classic modes, use subdirectories by experiment name
			String allureResultsDir = readParameterValue("allure.results.dir") + "/" + experimentName;
			String allureReportDir = readParameterValue("allure.report.dir") + "/" + experimentName;

			if (deletePreviousResults) {
				deleteDir(allureResultsDir);
				deleteDir(allureReportDir);
			}

			//Find auth property names (if any)
			List<String> authProperties = Collections.emptyList();
			if (confPath != null) {
				authProperties = AllureAuthManager.findAuthProperties(spec, confPath);
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

		if (enableInputCoverage || enableOutputCoverage) {
			coverageMeter = new CoverageMeter(new CoverageGatherer(spec));
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

	// Read the parameter value from: 1) CLI; 2) the local .properties file;
	// 3) the MST-only .properties file (when MST mode is active);
	// 4) the global .properties file (config.properties)
	private static String readParameterValue(String propertyName) {

		String value = null;

		if (argsList.contains(propertyName))
			value = argsList.get(argsList.indexOf(propertyName) + 1);
		else if (argsList.stream().anyMatch(arg -> arg.matches("^" + propertyName + "=.*")))
			value = argsList.stream().filter(arg -> arg.matches("^" + propertyName + "=.*")).findFirst().get().split("=")[1];
		else if (PropertyManager.readProperty(propertiesFilePath, propertyName) != null) // Read value from local .properties file
			value = PropertyManager.readProperty(propertiesFilePath, propertyName);
		else if (mstPropertiesFilePath != null && PropertyManager.readProperty(mstPropertiesFilePath, propertyName) != null) // MST-only file
			value = PropertyManager.readProperty(mstPropertiesFilePath, propertyName);
		else if (PropertyManager.readProperty(propertyName) != null) // Read value from global .properties file
			value = PropertyManager.readProperty(propertyName);

		return value;
	}

	/**
	 * Resolve the MST-only properties file via the {@code mst.config.path} key,
	 * load it, and push every entry to System properties so that downstream
	 * readers (writers, generators, smart fetcher, generated test code) that
	 * use {@code System.getProperty} pick the values up unchanged.
	 *
	 * Called once from {@link #main(String[])} after the RESTest-core file has
	 * been read and only when {@code generator=MST}.
	 */
	private static void loadMstConfig() {
		String mstPath = readParameterValue("mst.config.path");
		if (mstPath == null || mstPath.trim().isEmpty()) {
			logger.warn("MST mode is active but 'mst.config.path' is not set. " +
					"MST-only keys (LLM, smart fetch, jaeger, ...) will fall back to defaults. " +
					"Set 'mst.config.path' in {} to load them from a dedicated file.", propertiesFilePath);
			return;
		}
		try {
			mstConfig = MstConfig.load(mstPath);
			mstPropertiesFilePath = mstConfig.getFilePath();
			mstConfig.applyToSystemProperties();
		} catch (IOException e) {
			logger.error("Failed to load MST configuration from '{}': {}", mstPath, e.getMessage());
			throw new RuntimeException("Cannot load MST configuration file: " + mstPath, e);
		}
	}

	public static TestConfigurationObject getTestConfigurationObject(){
		return loadConfiguration(confPath, spec);
	}

	public static String getExperimentName(){ return experimentName; }

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
}
