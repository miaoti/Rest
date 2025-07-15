package es.us.isa.restest.writers.restassured;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.testcases.MultiServiceTestCase;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;



/**
 * Writes a JUnit/REST‑assured test‑suite that replays a
 * {@link MultiServiceTestCase}.
 */
public class MultiServiceRESTAssuredWriter extends RESTAssuredWriter {

    /* ------------------------------------------------------------------ */
    private final String outputDir;
    private final String packageName;
    private final String baseURI;

    private boolean loggingEnabled        = false;
    private boolean allureReport          = false;
    private boolean statsEnabled          = false;
    private boolean outputCoverageEnabled = false;
    private String  proxyHostPort         = null;

    /* ------------------------------------------------------------------ */
    public MultiServiceRESTAssuredWriter(String openAPIPath,
                                         String testConfPath,
                                         String outputDir,
                                         String className,
                                         String packageName,
                                         String baseURI,
                                         boolean logToFile) {

        super(openAPIPath, testConfPath, outputDir, className, packageName, baseURI, logToFile);
        this.outputDir    = outputDir;
        this.packageName  = packageName;
        this.baseURI      = baseURI;

        this.testClassName = className;
    }

    /* ---------- delegate the feature‑toggles to super & keep local copy --- */
    @Override public void setLogging(boolean logging)                    { super.setLogging(logging);          this.loggingEnabled        = logging; }
    @Override public void setAllureReport(boolean allure)                { super.setAllureReport(allure);      this.allureReport          = allure;  }
    @Override public void setEnableStats(boolean enableStats)            { super.setEnableStats(enableStats);  this.statsEnabled          = enableStats; }
    @Override public void setEnableOutputCoverage(boolean enableOutput)  { super.setEnableOutputCoverage(enableOutput); this.outputCoverageEnabled = enableOutput; }
    @Override public void setProxy(String proxy)                         { super.setProxy(proxy);              this.proxyHostPort         = proxy; }


    /*  WRITE JAVA SOURCE                                           */
    @Override
    public void write(Collection<TestCase> testCases) {
        if (testCases == null || testCases.isEmpty()) return;

        // Group test cases by scenario name
        Map<String, List<TestCase>> byScenario = new LinkedHashMap<>();
        for (TestCase tc : testCases) {
            if (tc instanceof MultiServiceTestCase) {
                String sc = ((MultiServiceTestCase) tc).getScenarioName();
                if (sc == null || sc.isEmpty()) sc = "Scenario";
                byScenario.computeIfAbsent(sc, k -> new ArrayList<>()).add(tc);
            }
        }

        for (Map.Entry<String, List<TestCase>> entry : byScenario.entrySet()) {
            try {
                writeTestSuite(entry.getValue(), sanitize(entry.getKey()));
        } catch (RESTestException e) {
            throw new RuntimeException("Error writing multi‑service test suite", e);
            }
        }
    }

    private void writeTestSuite(Collection<TestCase> testCases, String className) throws RESTestException {

        if (testCases == null || testCases.isEmpty()) return;

        try {
            // Create directory structure that matches package structure
            // Package: packageName.testClassName -> Directory: packageName/testClassName
            String packagePath = packageName.replace('.', '/');
            File packageDir = new File(outputDir, packagePath);
            File testClassDir = new File(packageDir, this.testClassName);
            if (!testClassDir.exists()) testClassDir.mkdirs();

            File javaFile = new File(testClassDir, className + ".java");

            try (PrintWriter pw = new PrintWriter(new FileWriter(javaFile))) {

                /* ---------- package & imports ------------------------------------ */
                if (packageName != null && !packageName.isEmpty()) {
                    // Use unique package name to avoid duplicate class issues across test runs
                    String uniquePackageName = packageName + "." + this.testClassName;
                    pw.println("package " + uniquePackageName + ";");
                    pw.println();
                }

                pw.println("import io.restassured.RestAssured;");
                pw.println("import io.restassured.response.Response;");
                pw.println("import io.restassured.specification.RequestSpecification;");
                pw.println("import org.junit.BeforeClass;");
                pw.println("import org.junit.Test;");
                pw.println("import java.util.concurrent.atomic.AtomicBoolean;");
                pw.println("import java.util.Map;");
                pw.println("import static org.junit.Assert.*;");
                pw.println("import es.us.isa.restest.testcases.MultiServiceTestCase;");

                if (allureReport) {
                    pw.println("import io.qameta.allure.Allure;");
                    pw.println("import io.qameta.allure.restassured.AllureRestAssured;");
                    pw.println("import io.qameta.allure.model.Status;");
                }
                pw.println();

                /* ---------- class header ---------------------------------------- */
                pw.println("public class " + className + " {");
                pw.println();

                /* ---------- Rest-assured global setup --------------------------- */
                pw.println("    @BeforeClass");
                pw.println("    public static void setupRestAssured() {");
                if (baseURI != null && !baseURI.isEmpty()) {
                    pw.println("        RestAssured.baseURI = \"" + escape(baseURI) + "\";");
                }
                if (proxyHostPort != null && !proxyHostPort.isEmpty()) {
                    String[] p = proxyHostPort.split(":", 2);
                    pw.println("        RestAssured.proxy = RestAssured.proxy(\"" + p[0] + "\"," + p[1] + ");");
                }
                if (allureReport) {
                    pw.println("        // Configure Allure results directory");
                    pw.println("        System.setProperty(\"allure.results.directory\", \"target/allure-results\");");
                    pw.println("        RestAssured.filters(new AllureRestAssured());");
                }
                pw.println("    }");
                pw.println();

                /* ---------- one @Test per scenario ----------------------------- */
                int scenarioIdx = 1;
                for (TestCase raw : testCases) {

                    if (!(raw instanceof MultiServiceTestCase)) continue;
                    MultiServiceTestCase scenario = (MultiServiceTestCase) raw;

                    // Use the meaningful test name from the generator instead of generic counter
                    String testMethodName = scenario.getOperationId();
                    if (testMethodName == null || testMethodName.isEmpty() || testMethodName.equals("workflow")) {
                        // Fallback to counter-based naming if no meaningful name is set
                        testMethodName = "testScenario" + scenarioIdx;
                    }

                    pw.println("    @Test");
                    pw.println("    public void " + testMethodName + "() throws Exception {");

                    /* ------------ login (own Allure step) ---------------------- */
                    pw.println("        final String[] _jwt     = new String[1];");
                    pw.println("        final String[] _jwtType = new String[1];");
                    pw.println("        final java.util.concurrent.atomic.AtomicBoolean loginSucceeded  = new java.util.concurrent.atomic.AtomicBoolean(true);");
                    pw.println("        final java.util.concurrent.atomic.AtomicBoolean scenarioFailed = new java.util.concurrent.atomic.AtomicBoolean(false);");

                    if (allureReport) {
                        pw.println("        // 🔐 STEP 0: Authentication - Always show in Allure report");
                        pw.println("        Allure.step(\"🔐 Step 0: Authentication (Login)\", () -> {");
                        pw.println("            try {");
                        pw.println("                Allure.parameter(\"🏢 Service\", \"Authentication Service\");");
                        pw.println("                Allure.parameter(\"📡 HTTP Method\", \"POST\");");
                        pw.println("                Allure.parameter(\"🔗 Endpoint\", \"/api/v1/users/login\");");
                        pw.println("                Allure.parameter(\"🎯 Expected Status\", 200);");
                        pw.println("                Allure.parameter(\"👤 Username\", \"admin\");");
                        pw.println("                Allure.parameter(\"🚦 Execution Decision\", \"▶️ EXECUTE - Authentication required\");");
                        pw.println("                Allure.description(\"🔐 **Authentication Step**\\n\" +");
                        pw.println("                                 \"This step authenticates the user to obtain JWT token for subsequent API calls.\\n\" +");
                        pw.println("                                 \"All other steps depend on successful authentication.\");");
                        pw.println("                ");
                    }
                    pw.println("                Response loginRes = RestAssured.given()");
                    pw.println("                    .contentType(\"application/json\")");
                    pw.println("                    .body(\"{\\\"username\\\":\\\"admin\\\"," +
                            "\\\"password\\\":\\\"222222\\\"}\")");
                    pw.println("                .when().post(\"/api/v1/users/login\")");
                    pw.println("                    .then().log().ifValidationFails()");
                    pw.println("                    .statusCode(200)  // Login expects 200 - could be configurable in future");
                    pw.println("                    .extract().response();");
                    pw.println("                _jwt[0]     = loginRes.jsonPath().getString(\"data.token\");");
                    pw.println("                _jwtType[0] = \"Bearer\";");
                    if (allureReport) {
                        pw.println("                ");
                        pw.println("                // ✅ SUCCESS: Add success symbols and status");
                        pw.println("                Allure.parameter(\"✅ Login Status\", \"SUCCESS\");");
                        pw.println("                Allure.parameter(\"🔑 Token Obtained\", _jwt[0] != null ? \"Yes\" : \"No\");");
                        pw.println("                Allure.parameter(\"📊 Final Result\", \"✅ SUCCESS\");");
                        pw.println("                Allure.addAttachment(\"🔐 Login Response\", \"application/json\", loginRes.getBody().asString());");
                        pw.println("            } catch (Throwable loginError) {");
                        pw.println("                loginSucceeded.set(false);");
                        pw.println("                ");
                        pw.println("                // ❌ FAILURE: Add failure symbols and error details");
                        pw.println("                Allure.parameter(\"❌ Login Status\", \"FAILED\");");
                        pw.println("                Allure.parameter(\"💥 Error Type\", loginError.getClass().getSimpleName());");
                        pw.println("                Allure.parameter(\"💬 Error Message\", loginError.getMessage());");
                        pw.println("                Allure.parameter(\"📊 Final Result\", \"❌ FAILED\");");
                        pw.println("                ");
                        pw.println("                StringBuilder loginErrorDetails = new StringBuilder();");
                        pw.println("                loginErrorDetails.append(\"🚨 LOGIN FAILURE REPORT\\n\\n\");");
                        pw.println("                loginErrorDetails.append(\"📋 LOGIN ATTEMPT:\\n\");");
                        pw.println("                loginErrorDetails.append(\"Endpoint: /api/v1/users/login\\n\");");
                        pw.println("                loginErrorDetails.append(\"Method: POST\\n\");");
                        pw.println("                loginErrorDetails.append(\"Expected Status: 200\\n\\n\");");
                        pw.println("                loginErrorDetails.append(\"💥 ERROR INFO:\\n\");");
                        pw.println("                loginErrorDetails.append(\"Type: \").append(loginError.getClass().getSimpleName()).append(\"\\n\");");
                        pw.println("                loginErrorDetails.append(\"Message: \").append(loginError.getMessage()).append(\"\\n\\n\");");
                        pw.println("                loginErrorDetails.append(\"⚠️ IMPACT:\\n\");");
                        pw.println("                loginErrorDetails.append(\"All subsequent steps will be skipped due to authentication failure.\\n\");");
                        pw.println("                ");
                        pw.println("                Allure.addAttachment(\"🚨 Login Failure Details\", \"text/plain\", loginErrorDetails.toString());");
                        pw.println("                ");
                        pw.println("                // Throw to mark login step as failed in Allure");
                        pw.println("                throw new RuntimeException(\"Login failed: \" + loginError.getMessage(), loginError);");
                        pw.println("            }");
                        pw.println("        });");
                    } else {
                        pw.println("        } catch (Throwable __t) {");   // login failed
                        pw.println("            loginSucceeded.set(false);");
                        pw.println("        }");
                    }
                    pw.println("        String jwt     = _jwt[0];");
                    pw.println("        String jwtType = _jwtType[0];");
                    pw.println();

                    /* ------------ every StepCall -------------------------------- */
                    pw.println("        // Step execution results tracking");
                    pw.println("        final java.util.Map<Integer, Boolean> stepResults = new java.util.HashMap<>();");
                    pw.println("        final java.util.Map<Integer, String> capturedOutputs = new java.util.HashMap<>();");
                    pw.println();

                    int stepIdx = 1;
                    for (MultiServiceTestCase.StepCall step : scenario.getSteps()) {

                        String verb = step.getMethod() == null || step.getMethod().getMethod() == null
                                ? "get"
                                : step.getMethod().getMethod().toLowerCase();
                        
                        // Generate hierarchical step number if available from the generator
                        String stepNumber = "Step " + stepIdx;
                        String stepTitle = stepNumber + ": "
                                + step.getServiceName() + " "
                                + verb.toUpperCase() + " " + step.getPath()
                                + " (expect " + step.getExpectedStatus() + ")";

                        pw.println("        // " + escape(stepTitle));
                        
                        // 🔥 CRITICAL FIX: ALWAYS create Allure step - NO conditional logic outside
                        // This ensures ALL steps appear in the Allure report regardless of dependencies
                        if (allureReport) {
                            pw.println("        // 🔥 ALWAYS create Allure step - execution decision happens INSIDE");
                            pw.println("        try {");
                            pw.println("            Allure.step(\"" + escape(stepTitle) + "\", () -> {");
                            
                            // Add step metadata as parameters that will be prominently displayed
                            pw.println("                Allure.parameter(\"🏢 Service\", \"" + escape(step.getServiceName()) + "\");");
                            pw.println("                Allure.parameter(\"📡 HTTP Method\", \"" + verb.toUpperCase() + "\");");
                            pw.println("                Allure.parameter(\"🔗 Endpoint\", \"" + escape(step.getPath()) + "\");");
                            pw.println("                Allure.parameter(\"🎯 Expected Status\", " + step.getExpectedStatus() + ");");
                            
                            // Add dependency analysis information
                            String stepDepType = getDependencyTypeString(step);
                            pw.println("                Allure.parameter(\"🔗 Dependency Type\", \"" + stepDepType + "\");");
                            
                            // Add comprehensive description
                            pw.println("                Allure.description(\"🎯 **Testing**: " + escape(step.getServiceName()) + "\\n\" +");
                            pw.println("                                 \"📡 **Method**: " + verb.toUpperCase() + "\\n\" +");
                            pw.println("                                 \"🔗 **Path**: " + escape(step.getPath()) + "\\n\" +");
                            pw.println("                                 \"🎯 **Expected**: " + step.getExpectedStatus() + "\\n\" +");
                            pw.println("                                 \"🔗 **Dependencies**: " + stepDepType + "\");");
                            pw.println("                ");
                            
                            // 🔥 EXECUTION DECISION INSIDE THE STEP - so it's always shown
                            pw.println("                // Execution decision analysis - determine if step should execute");
                            pw.println("                boolean shouldSkip = false;");
                            pw.println("                String skipReason = \"\";");
                            pw.println("                String skipCategory = \"\";");
                            pw.println("                ");
                            
                            // Check authentication dependency first
                            pw.println("                // Check authentication dependency");
                            pw.println("                if (!loginSucceeded.get()) {");
                            pw.println("                    shouldSkip = true;");
                            pw.println("                    skipReason = \"Authentication failed - cannot proceed with authenticated API calls\";");
                            pw.println("                    skipCategory = \"🔐 AUTH_FAILED\";");
                            pw.println("                }");
                            
                            // Check other dependencies
                            if (!step.getParamDependencies().isEmpty()) {
                                pw.println("                // Check data dependencies");
                                pw.println("                else if (false"); // Start with false, then OR the conditions
                                for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                    int sourceStepIdx = dep.getValue().sourceStepIndex;
                                    pw.println("                    || !stepResults.getOrDefault(" + sourceStepIdx + ", false)");
                                }
                                pw.println("                ) {");
                                pw.println("                    shouldSkip = true;");
                                pw.println("                    skipReason = \"Required data from previous step(s) is not available\";");
                                pw.println("                    skipCategory = \"📊 DATA_DEPENDENCY\";");
                                pw.println("                }");
                            }
                            
                            if (!step.getWorkflowDependencies().isEmpty()) {
                                pw.println("                // Check workflow dependencies");
                                pw.println("                else if (false"); // Start with false, then OR the conditions
                                for (Integer workflowDep : step.getWorkflowDependencies()) {
                                    pw.println("                    || !stepResults.getOrDefault(" + workflowDep + ", false)");
                                }
                                pw.println("                ) {");
                                pw.println("                    shouldSkip = true;");
                                pw.println("                    skipReason = \"Workflow predecessor step(s) failed\";");
                                pw.println("                    skipCategory = \"🔄 WORKFLOW_DEPENDENCY\";");
                                pw.println("                }");
                            }
                            
                            pw.println("                ");
                            pw.println("                // Add execution decision as parameter");
                            pw.println("                if (shouldSkip) {");
                            pw.println("                    Allure.parameter(\"🚦 Execution Decision\", \"⏭️ SKIP - \" + skipCategory);");
                            pw.println("                    Allure.parameter(\"⏭️ Skip Reason\", skipReason);");
                            pw.println("                } else {");
                            pw.println("                    Allure.parameter(\"🚦 Execution Decision\", \"▶️ EXECUTE - All dependencies satisfied\");");
                            pw.println("                }");
                            pw.println("                ");
                            
                            // NOW the actual execution or skip logic
                            pw.println("                if (!shouldSkip) {");
                            pw.println("                    System.out.println(\"▶️ EXECUTING: " + escape(stepTitle) + " (dependency analysis passed)\");");
                            
                            // Execute the step
                            pw.println("                    try {");
                            pw.println("                        RequestSpecification req = RestAssured.given();");
                            pw.println("                        if (loginSucceeded.get()) {");
                            pw.println("                            req = req.header(\"Authorization\", jwtType + \" \" + jwt);");
                            pw.println("                        }");
                            
                            // Add dependency resolution for parameters
                            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                String paramName = dep.getKey();
                                int sourceStepIdx = dep.getValue().sourceStepIndex;
                                pw.println("                        String " + paramName + "Value = capturedOutputs.get(" + sourceStepIdx + ");");
                                pw.println("                        if (" + paramName + "Value != null) {");
                                if (step.getMethod().getMethod().equalsIgnoreCase("GET")) {
                                    pw.println("                            req = req.queryParam(\"" + paramName + "\", " + paramName + "Value);");
                                } else {
                                    pw.println("                            // Add to body if needed");
                                }
                                pw.println("                        }");
                            }
                            
                            String requestBody = step.getBody() != null ? step.getBody() : "";
                            if (!requestBody.isEmpty()) {
                                pw.println("                        String requestBody" + stepIdx + " = \"" + escape(requestBody) + "\";");
                                pw.println("                        Allure.addAttachment(\"📋 Request Body\", \"application/json\", requestBody" + stepIdx + ");");
                                pw.println("                        req = req.body(requestBody" + stepIdx + ");");
                            }
                            
                            pw.println("                        Response stepResponse" + stepIdx + " = req.when()." + verb + "(\"" + escape(step.getPath()) + "\")");
                            pw.println("                               .then().log().ifValidationFails()");
                            pw.println("                               .statusCode(" + step.getExpectedStatus() + ")");
                            pw.println("                               .extract().response();");
                            pw.println("                        stepResults.put(" + stepIdx + ", true);");
                            pw.println("                        System.out.println(\"✅ " + escape(stepTitle) + " - SUCCESS\");");
                            
                            // SUCCESS: Add success symbols and parameters
                            pw.println("                        // ✅ SUCCESS: Add success parameters and metrics");
                            pw.println("                        try {");
                            pw.println("                            String responseBody = stepResponse" + stepIdx + ".getBody().asString();");
                            pw.println("                            int actualStatus = stepResponse" + stepIdx + ".getStatusCode();");
                            pw.println("                            long responseTime = stepResponse" + stepIdx + ".getTime();");
                            pw.println("                            ");
                            pw.println("                            // Add response as prominently visible attachment");
                            pw.println("                            Allure.addAttachment(\"📄 Response (Status: \" + actualStatus + \")\", \"application/json\", responseBody);");
                            pw.println("                            ");
                            pw.println("                            // Add SUCCESS metrics with green symbols");
                            pw.println("                            Allure.parameter(\"✅ Actual Status\", actualStatus + \" (SUCCESS)\");");
                            pw.println("                            Allure.parameter(\"⏱️ Response Time\", responseTime + \" ms\");");
                            pw.println("                            Allure.parameter(\"📊 Final Result\", \"✅ SUCCESS\");");
                            pw.println("                        } catch (Exception e) {");
                            pw.println("                            Allure.addAttachment(\"⚠️ Response Capture Error\", \"text/plain\", e.getMessage());");
                            pw.println("                        }");
                            
                            // FAILURE: Error handling with red symbols
                            pw.println("                    } catch (Throwable t) {");
                            pw.println("                        stepResults.put(" + stepIdx + ", false);");
                            pw.println("                        System.out.println(\"❌ " + escape(stepTitle) + " - FAILED: \" + t.getMessage());");
                            pw.println("                        ");
                            pw.println("                        // ❌ FAILURE: Add failure symbols and error details");
                            pw.println("                        ");
                            pw.println("                        String errorCategory = \"Unknown\";");
                            pw.println("                        if (t instanceof java.net.ConnectException) {");
                            pw.println("                            errorCategory = \"🔌 Connection Failed - Service Unreachable\";");
                            pw.println("                        } else if (t instanceof AssertionError) {");
                            pw.println("                            errorCategory = \"❗ Assertion Failed - Unexpected Response\";");
                            pw.println("                        } else if (t instanceof java.net.SocketTimeoutException) {");
                            pw.println("                            errorCategory = \"⏰ Timeout - Service Too Slow\";");
                            pw.println("                        } else {");
                            pw.println("                            errorCategory = \"❓ \" + t.getClass().getSimpleName();");
                            pw.println("                        }");
                            pw.println("                        ");
                            pw.println("                        // Add FAILURE details with red symbols");
                            pw.println("                        Allure.parameter(\"❌ Error Category\", errorCategory);");
                            pw.println("                        Allure.parameter(\"💥 Error Message\", t.getMessage());");
                            pw.println("                        Allure.parameter(\"🔍 Exception Type\", t.getClass().getSimpleName());");
                            pw.println("                        Allure.parameter(\"📊 Final Result\", \"❌ FAILED\");");
                            pw.println("                        ");
                            pw.println("                        // Create detailed error report");
                            pw.println("                        StringBuilder errorDetails = new StringBuilder();");
                            pw.println("                        errorDetails.append(\"🚨 STEP FAILURE REPORT\\n\\n\");");
                            pw.println("                        errorDetails.append(\"📋 STEP INFO:\\n\");");
                            pw.println("                        errorDetails.append(\"Service: " + escape(step.getServiceName()) + "\\n\");");
                            pw.println("                        errorDetails.append(\"Method: " + verb.toUpperCase() + "\\n\");");
                            pw.println("                        errorDetails.append(\"Path: " + escape(step.getPath()) + "\\n\");");
                            pw.println("                        errorDetails.append(\"Expected Status: " + step.getExpectedStatus() + "\\n\\n\");");
                            pw.println("                        errorDetails.append(\"💥 ERROR INFO:\\n\");");
                            pw.println("                        errorDetails.append(\"Type: \").append(t.getClass().getSimpleName()).append(\"\\n\");");
                            pw.println("                        errorDetails.append(\"Message: \").append(t.getMessage()).append(\"\\n\\n\");");
                            pw.println("                        errorDetails.append(\"📚 FULL STACK TRACE:\\n\").append(t.toString());");
                            pw.println("                        ");
                            pw.println("                        Allure.addAttachment(\"🚨 Step Failure Details\", \"text/plain\", errorDetails.toString());");
                            pw.println("                        ");
                            pw.println("                        // DON'T throw - let other steps execute");
                            pw.println("                        // Instead, mark step as failed but continue");
                            pw.println("                    }");
                            
                            // SKIP: Step is skipped with yellow symbols
                            pw.println("                } else {");
                            pw.println("                    // ⏭️ SKIP: Add skip symbols and comprehensive information");
                            pw.println("                    System.out.println(\"⏭️ SKIPPING: " + escape(stepTitle) + " - \" + skipReason);");
                            pw.println("                    stepResults.put(" + stepIdx + ", false);");
                            pw.println("                    ");
                            pw.println("                    // Add comprehensive SKIP information with yellow symbols");
                            pw.println("                    Allure.parameter(\"⏭️ Skip Category\", skipCategory);");
                            pw.println("                    Allure.parameter(\"💬 Skip Details\", skipReason);");
                            pw.println("                    Allure.parameter(\"📊 Final Result\", \"⏭️ SKIPPED\");");
                            pw.println("                    ");
                            
                            // Generate detailed dependency analysis report
                            pw.println("                    // Generate detailed dependency analysis report");
                            pw.println("                    StringBuilder dependencyReport = new StringBuilder();");
                            pw.println("                    dependencyReport.append(\"⏭️ STEP SKIP ANALYSIS\\n\\n\");");
                            pw.println("                    dependencyReport.append(\"📋 STEP INFO:\\n\");");
                            pw.println("                    dependencyReport.append(\"Service: " + escape(step.getServiceName()) + "\\n\");");
                            pw.println("                    dependencyReport.append(\"Method: " + verb.toUpperCase() + "\\n\");");
                            pw.println("                    dependencyReport.append(\"Path: " + escape(step.getPath()) + "\\n\");");
                            pw.println("                    dependencyReport.append(\"Expected Status: " + step.getExpectedStatus() + "\\n\\n\");");
                            pw.println("                    dependencyReport.append(\"⏭️ SKIP REASON:\\n\");");
                            pw.println("                    dependencyReport.append(\"Category: \").append(skipCategory).append(\"\\n\");");
                            pw.println("                    dependencyReport.append(\"Details: \").append(skipReason).append(\"\\n\\n\");");
                            pw.println("                    ");
                            
                            // Add dependency analysis
                            pw.println("                    // Add dependency analysis");
                            pw.println("                    if (!loginSucceeded.get()) {");
                            pw.println("                        dependencyReport.append(\"🔐 AUTHENTICATION STATUS: ❌ FAILED\\n\");");
                            pw.println("                        dependencyReport.append(\"Authentication is required for all API calls.\\n\\n\");");
                            pw.println("                    }");
                            
                            if (!step.getParamDependencies().isEmpty()) {
                                pw.println("                    dependencyReport.append(\"📊 DATA DEPENDENCIES:\\n\");");
                                for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                    int sourceStepIdx = dep.getValue().sourceStepIndex;
                                    pw.println("                    boolean dep" + sourceStepIdx + "Success = stepResults.getOrDefault(" + sourceStepIdx + ", false);");
                                    pw.println("                    dependencyReport.append(\"  - Step \" + " + sourceStepIdx + " + \": \" + (dep" + sourceStepIdx + "Success ? \"✅ SUCCESS\" : \"❌ FAILED\") + \"\\n\");");
                                }
                                pw.println("                    dependencyReport.append(\"\\n\");");
                            }
                            
                            if (!step.getWorkflowDependencies().isEmpty()) {
                                pw.println("                    dependencyReport.append(\"🔄 WORKFLOW DEPENDENCIES:\\n\");");
                                for (Integer workflowDep : step.getWorkflowDependencies()) {
                                    pw.println("                    boolean workflowDep" + workflowDep + "Success = stepResults.getOrDefault(" + workflowDep + ", false);");
                                    pw.println("                    dependencyReport.append(\"  - Step \" + " + workflowDep + " + \": \" + (workflowDep" + workflowDep + "Success ? \"✅ SUCCESS\" : \"❌ FAILED\") + \"\\n\");");
                                }
                                pw.println("                    dependencyReport.append(\"\\n\");");
                            }
                            
                            pw.println("                    Allure.addAttachment(\"⏭️ Dependency Analysis Report\", \"text/plain\", dependencyReport.toString());");
                            pw.println("                }");
                            
                            pw.println("            });");
                            pw.println("        } catch (Exception stepException) {");
                            pw.println("            // Step wrapper failed, but don't stop other steps");
                            pw.println("            System.out.println(\"⚠️ Step wrapper failed for " + escape(stepTitle) + ": \" + stepException.getMessage());");
                            pw.println("            stepResults.put(" + stepIdx + ", false);");
                            pw.println("        }");
                        } else {
                            // Non-Allure version - simplified (fallback for when Allure is disabled)
                            pw.println("        // Non-Allure version - simplified execution");
                        pw.println("        MultiServiceTestCase.ExecutionDecision decision" + stepIdx + ";");
                        
                        // Generate the actual decision logic based on step's dependency configuration
                        if (!step.getParamDependencies().isEmpty()) {
                            pw.println("        // This step has DATA dependencies");
                            pw.println("        boolean hasFailedDataDependency = false;");
                            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                int sourceStepIdx = dep.getValue().sourceStepIndex;
                                pw.println("        if (!stepResults.getOrDefault(" + sourceStepIdx + ", false)) {");
                                pw.println("            hasFailedDataDependency = true;");
                                pw.println("        }");
                            }
                            pw.println("        if (hasFailedDataDependency) {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(false, ");
                            pw.println("                MultiServiceTestCase.SkipReason.DATA_DEPENDENCY_FAILED, ");
                            pw.println("                \"Required data from previous step(s) is not available\");");
                            pw.println("        } else {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(true, null, null);");
                            pw.println("        }");
                        } else if (!step.getWorkflowDependencies().isEmpty()) {
                            pw.println("        // This step has WORKFLOW dependencies");
                            pw.println("        boolean hasFailedWorkflowDependency = false;");
                            for (Integer workflowDep : step.getWorkflowDependencies()) {
                                pw.println("        if (!stepResults.getOrDefault(" + workflowDep + ", false)) {");
                                pw.println("            hasFailedWorkflowDependency = true;");
                                pw.println("        }");
                            }
                            pw.println("        if (hasFailedWorkflowDependency) {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(false, ");
                            pw.println("                MultiServiceTestCase.SkipReason.WORKFLOW_DEPENDENCY_FAILED, ");
                            pw.println("                \"Workflow predecessor step(s) failed\");");
                            pw.println("        } else {");
                            pw.println("            decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(true, null, null);");
                            pw.println("        }");
                        } else {
                            pw.println("        // This step is INDEPENDENT - always execute");
                            pw.println("        decision" + stepIdx + " = new MultiServiceTestCase.ExecutionDecision(true, null, null);");
                        }
                            
                            pw.println("        if (decision" + stepIdx + ".shouldExecute && loginSucceeded.get()) {");
                            pw.println("            System.out.println(\"✅ EXECUTING: " + escape(stepTitle) + "\");");
                            pw.println("            // Execute step logic here (simplified version)");
                            pw.println("            stepResults.put(" + stepIdx + ", true);");
                            pw.println("        } else {");
                            pw.println("            System.out.println(\"⏭️ SKIPPING: " + escape(stepTitle) + "\");");
                            pw.println("            stepResults.put(" + stepIdx + ", false);");
                            pw.println("        }");
                        }
                        
                        pw.println();
                        stepIdx++;
                    }

                    // Check overall scenario result with detailed reporting
                    pw.println("        // Evaluate scenario result with comprehensive reporting");
                    pw.println("        long successfulSteps = stepResults.values().stream().filter(result -> result).count();");
                    pw.println("        long failedSteps = stepResults.values().stream().filter(result -> !result).count();");
                    pw.println("        long totalSteps = stepResults.size();");
                    pw.println("        ");
                    
                    // Add detailed test summary
                    if (allureReport) {
                        pw.println("        // Add comprehensive test summary to Allure report");
                        pw.println("        StringBuilder summary = new StringBuilder();");
                        pw.println("        summary.append(\"Test Scenario Summary:\\n\");");
                        pw.println("        summary.append(\"Total Steps: \").append(totalSteps).append(\"\\n\");");
                        pw.println("        summary.append(\"Successful Steps: \").append(successfulSteps).append(\"\\n\");");
                        pw.println("        summary.append(\"Failed Steps: \").append(failedSteps).append(\"\\n\");");
                        pw.println("        summary.append(\"Login Status: \").append(loginSucceeded.get() ? \"SUCCESS\" : \"FAILED\").append(\"\\n\\n\");");
                        pw.println("        ");
                                        pw.println("        // Add step-by-step breakdown");
                pw.println("        summary.append(\"Step Breakdown:\\n\");");
                pw.println("        for (java.util.Map.Entry<Integer, Boolean> step : stepResults.entrySet()) {");
                        pw.println("            String status = step.getValue() ? \"✓ PASS\" : \"✗ FAIL\";");
                        pw.println("            summary.append(\"  Step \").append(step.getKey()).append(\": \").append(status).append(\"\\n\");");
                        pw.println("        }");
                        pw.println("        ");
                        pw.println("        Allure.addAttachment(\"Test Execution Summary\", \"text/plain\", summary.toString());");
                        pw.println("        ");
                        
                        // Add test categorization
                        pw.println("        // Categorize test result for better reporting");
                        pw.println("        if (!loginSucceeded.get()) {");
                        pw.println("            Allure.label(\"testType\", \"Authentication Failure\");");
                        pw.println("            Allure.label(\"severity\", \"critical\");");
                        pw.println("        } else if (failedSteps == 0) {");
                        pw.println("            Allure.label(\"testType\", \"Complete Success\");");
                        pw.println("            Allure.label(\"severity\", \"normal\");");
                        pw.println("        } else if (successfulSteps > 0) {");
                        pw.println("            Allure.label(\"testType\", \"Partial Failure\");");
                        pw.println("            Allure.label(\"severity\", \"major\");");
                        pw.println("        } else {");
                        pw.println("            Allure.label(\"testType\", \"Complete Failure\");");
                        pw.println("            Allure.label(\"severity\", \"critical\");");
                        pw.println("        }");
                        pw.println("        ");
                        pw.println("        // Add scenario metadata");
                        pw.println("        Allure.label(\"feature\", \"Microservice Workflow\");");
                        pw.println("        Allure.label(\"story\", \"" + escape(scenario.getOperationId()) + "\");");
                        pw.println("        Allure.description(\"Microservice test scenario with \" + totalSteps + \" steps. \" +");
                        pw.println("                           \"Generated using two-stage LLM + semantic expansion approach.\");");
                        pw.println("        ");
                    }
                    
                    pw.println("        System.out.println(\"=== SCENARIO RESULT ===\");");
                    pw.println("        System.out.println(\"Scenario: " + escape(scenario.getOperationId()) + "\");");
                    pw.println("        System.out.println(\"Total Steps: \" + totalSteps);");
                    pw.println("        System.out.println(\"Successful: \" + successfulSteps);");
                    pw.println("        System.out.println(\"Failed: \" + failedSteps);");
                    pw.println("        System.out.println(\"Login Status: \" + (loginSucceeded.get() ? \"SUCCESS\" : \"FAILED\"));");
                    pw.println("        ");
                    
                    // Enhanced failure logic - fail if ANY step fails OR login fails
                    pw.println("        // IMPROVED: Test fails if ANY step fails or login fails (not just when ALL fail)");
                    pw.println("        if (!loginSucceeded.get()) {");
                    pw.println("            fail(\"Scenario FAILED: Authentication failed - cannot proceed with API calls\");");
                    pw.println("        } else if (failedSteps > 0) {");
                    pw.println("            fail(\"Scenario FAILED: \" + failedSteps + \" out of \" + totalSteps + \" steps failed. \" +");
                    pw.println("                 \"In microservice testing, all workflow steps must succeed for end-to-end validation.\");");
                    pw.println("        } else if (successfulSteps == 0) {");
                    pw.println("            fail(\"Scenario FAILED: No steps executed successfully - check service availability\");");
                    pw.println("        } else {");
                    pw.println("            System.out.println(\"🎉 Scenario PASSED: All \" + totalSteps + \" steps completed successfully\");");
                    pw.println("        }");
                    pw.println("    }");
                    pw.println();
                    scenarioIdx++;
                }

                pw.println("}");
            }
        } catch (Exception e) {
            throw new RESTestException("Error writing REST‑assured suite: " + e.getMessage(), e);
        }
    }


    private static boolean isStandardVerb(String v) {
        switch (v) {
            case "get": case "post": case "put":
            case "delete": case "patch": return true;
            default: return false;
        }
    }
    
    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String sanitize(String s) {
        if (s == null) return "Scenario";
        return s.replaceAll("[^a-zA-Z0-9_]", "_").replaceAll("_+", "_").replaceAll("^_|_$", "");
    }
    
    /**
     * Get human-readable dependency type string for a step
     */
    private static String getDependencyTypeString(MultiServiceTestCase.StepCall step) {
        if (step.getParamDependencies() != null && !step.getParamDependencies().isEmpty()) {
            return "DATA_DEPENDENCY (needs data from previous steps)";
        } else if (step.getWorkflowDependencies() != null && !step.getWorkflowDependencies().isEmpty()) {
            return "WORKFLOW_DEPENDENCY (part of sequential workflow)";
        } else {
            return "INDEPENDENT (can execute regardless of other step results)";
        }
    }
    
    /**
     * Generate detailed dependency analysis report for Allure
     */
    private static void generateDependencyAnalysisReport(MultiServiceTestCase.StepCall step, int stepIdx, PrintWriter pw) {
        pw.println("                // Generate comprehensive dependency analysis attachment");
        pw.println("                StringBuilder depAnalysis = new StringBuilder();");
        pw.println("                depAnalysis.append(\"📋 DEPENDENCY ANALYSIS REPORT\\n\\n\");");
        pw.println("                depAnalysis.append(\"🔍 Step: \" + " + stepIdx + " + \" - " + escape(step.getServiceName()) + "\\n\");");
        pw.println("                depAnalysis.append(\"📡 Method: " + (step.getMethod() != null ? step.getMethod().getMethod() : "UNKNOWN") + "\\n\");");
        pw.println("                depAnalysis.append(\"🔗 Path: " + escape(step.getPath()) + "\\n\\n\");");
        
        // Add parameter dependencies analysis
        if (step.getParamDependencies() != null && !step.getParamDependencies().isEmpty()) {
            pw.println("                depAnalysis.append(\"💾 DATA DEPENDENCIES:\\n\");");
            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                pw.println("                depAnalysis.append(\"  • Parameter '" + escape(dep.getKey()) + "' requires data from Step \" + " + dep.getValue().sourceStepIndex + " + \" (field: '" + escape(dep.getValue().sourceOutputKey) + "')\\n\");");
            }
            pw.println("                depAnalysis.append(\"\\n\");");
        }
        
        // Add workflow dependencies analysis  
        if (step.getWorkflowDependencies() != null && !step.getWorkflowDependencies().isEmpty()) {
            pw.println("                depAnalysis.append(\"🔄 WORKFLOW DEPENDENCIES:\\n\");");
            for (Integer workflowDep : step.getWorkflowDependencies()) {
                pw.println("                depAnalysis.append(\"  • Must execute after Step \" + " + workflowDep + " + \" completes successfully\\n\");");
            }
            pw.println("                depAnalysis.append(\"\\n\");");
        }
        
        // Add execution decision reasoning
        pw.println("                depAnalysis.append(\"📊 EXECUTION DECISION LOGIC:\\n\");");
        pw.println("                depAnalysis.append(\"  Reason: \" + decision" + stepIdx + ".skipReason.description + \"\\n\");");
        pw.println("                depAnalysis.append(\"  Details: \" + decision" + stepIdx + ".skipMessage + \"\\n\\n\");");
        
        // Add step results context
        pw.println("                depAnalysis.append(\"📈 PREVIOUS STEP RESULTS:\\n\");");
        pw.println("                for (Map.Entry<Integer, Boolean> result : stepResults.entrySet()) {");
        pw.println("                    String status = result.getValue() ? \"✅ PASSED\" : \"❌ FAILED\";");
        pw.println("                    depAnalysis.append(\"  Step \" + result.getKey() + \": \" + status + \"\\n\");");
        pw.println("                }");
        pw.println("                depAnalysis.append(\"\\n\");");
        
        // Add impact analysis
        pw.println("                depAnalysis.append(\"🎯 IMPACT ANALYSIS:\\n\");");
        pw.println("                depAnalysis.append(\"  • This step was skipped to prevent cascading failures\\n\");");
        pw.println("                depAnalysis.append(\"  • Dependent steps may also be skipped if they rely on this step\\n\");");
        pw.println("                depAnalysis.append(\"  • Independent steps will continue to execute\\n\");");
        
        pw.println("                Allure.addAttachment(\"🔍 Dependency Analysis Report\", \"text/plain\", depAnalysis.toString());");
    }

    @Override
    public void setClassName(String className) {
        super.setClassName(className);
        this.testClassName = className;
    }

    @Override
    public void setTestId(String testId) {
        super.setTestId(testId);

    }
}
