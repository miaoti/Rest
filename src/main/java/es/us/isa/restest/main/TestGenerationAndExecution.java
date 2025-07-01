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

import es.us.isa.restest.generators.*;
import es.us.isa.restest.reporting.AllureReportManager;
import es.us.isa.restest.reporting.StatsReportManager;
import es.us.isa.restest.runners.RESTestWorkflow;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.util.Timer;
import es.us.isa.restest.workflow.TraceWorkflowExtractor;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.writers.IWriter;
import es.us.isa.restest.writers.restassured.MultiServiceRESTAssuredWriter;
import es.us.isa.restest.writers.restassured.RESTAssuredWriter;
import es.us.isa.restest.util.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
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

/*
 * This class show the basic workflow of test case generation -> test case execution -> test reporting
 */
public class TestGenerationAndExecution {

	// Properties file with configuration settings
	private static String propertiesFilePath = "src/main/resources/My-Example/trainticket-demo.properties";
	private static String TraceFile = "src\\main\\resources\\My-Example\\trainticket\\traces\\admin_price_create.json";

	private static List<String> argsList;								// List containing args

	private static Integer numTestCases; 								// Number of test cases per operation
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
		while (totalNumTestCases == -1 || runner.getNumTestCases() < totalNumTestCases) {

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

		Timer.stopCounting(ALL);

		generateTimeReport(iteration-1);
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
			String allureResultsDir = readParameterValue("allure.results.dir") + "/" + experimentName;
			String allureReportDir = readParameterValue("allure.report.dir") + "/" + experimentName;


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
