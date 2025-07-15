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
                pw.println("import static org.junit.Assert.*;");

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
                        pw.println("        try {");
                        pw.println("            Allure.step(\"Login\", () -> {");
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
                        pw.println("            });");                     // close Allure.step
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
                        
                        // Check if this step should be executed based on dependencies
                        pw.println("        boolean shouldExecuteStep" + stepIdx + " = true;");
                        
                        // Check dependencies based on trace analysis
                        if (!step.getParamDependencies().isEmpty()) {
                            pw.println("        // Check dependencies for " + stepTitle);
                            for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                                int sourceStepIdx = dep.getValue().sourceStepIndex;
                                pw.println("        if (!stepResults.getOrDefault(" + sourceStepIdx + ", false)) {");
                                pw.println("            shouldExecuteStep" + stepIdx + " = false;");
                                pw.println("            // Dependency step " + sourceStepIdx + " failed");
                                pw.println("        }");
                            }
                        }
                        
                        pw.println("        if (shouldExecuteStep" + stepIdx + ") {");

                        /* --------- Use high-level Allure.step() for prominent step visualization ------------------ */
                        if (allureReport) {
                            pw.println("            // Use Allure.step() for proper step hierarchy and visualization");
                            pw.println("            Allure.step(\"" + escape(stepTitle) + "\", () -> {");
                            
                            // Add step metadata as parameters that will be prominently displayed
                            pw.println("                Allure.parameter(\"🏢 Service\", \"" + escape(step.getServiceName()) + "\");");
                            pw.println("                Allure.parameter(\"📡 HTTP Method\", \"" + verb.toUpperCase() + "\");");
                            pw.println("                Allure.parameter(\"🔗 Endpoint\", \"" + escape(step.getPath()) + "\");");
                            pw.println("                Allure.parameter(\"✅ Expected Status\", " + step.getExpectedStatus() + ");");
                            
                            // Add a description that will be visible in the report
                            pw.println("                Allure.description(\"🎯 **Testing**: " + escape(step.getServiceName()) + "\\n\" +");
                            pw.println("                                 \"📡 **Method**: " + verb.toUpperCase() + "\\n\" +");
                            pw.println("                                 \"🔗 **Path**: " + escape(step.getPath()) + "\\n\" +");
                            pw.println("                                 \"✅ **Expected**: " + step.getExpectedStatus() + "\");");
                        }

                        String indent = allureReport ? "                " : "            ";
                        pw.println(indent + "try {");
                        pw.println(indent + "    RequestSpecification req = RestAssured.given();");
                        pw.println(indent + "    if (loginSucceeded.get()) {");
                        pw.println(indent + "        req = req.header(\"Authorization\", jwtType + \" \" + jwt);");
                        pw.println(indent + "    }");
                        
                        // Add dependency resolution for parameters
                        for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                            String paramName = dep.getKey();
                            int sourceStepIdx = dep.getValue().sourceStepIndex;
                            String sourceKey = dep.getValue().sourceOutputKey;
                            pw.println(indent + "    String " + paramName + "Value = capturedOutputs.get(" + sourceStepIdx + ");");
                            pw.println(indent + "    if (" + paramName + "Value != null) {");
                            pw.println(indent + "        // Use captured value from step " + sourceStepIdx);
                            pw.println(indent + "    }");
                        }
                        
                        // Add path parameters
                        for (Map.Entry<String, String> pathParam : step.getPathParams().entrySet()) {
                            pw.println(indent + "    req = req.pathParam(\"" + escape(pathParam.getKey()) + "\", \"" + escape(pathParam.getValue()) + "\");");
                        }
                        
                        // Add query parameters
                        for (Map.Entry<String, String> queryParam : step.getQueryParams().entrySet()) {
                            pw.println(indent + "    req = req.queryParam(\"" + escape(queryParam.getKey()) + "\", \"" + escape(queryParam.getValue()) + "\");");
                        }
                        
                        // Add headers
                        for (Map.Entry<String, String> header : step.getHeaders().entrySet()) {
                            pw.println(indent + "    req = req.header(\"" + escape(header.getKey()) + "\", \"" + escape(header.getValue()) + "\");");
                        }
                        
                        // Add body for POST/PUT/PATCH requests
                        if (step.getBody() != null && !step.getBody().trim().isEmpty() && 
                            (verb.equals("post") || verb.equals("put") || verb.equals("patch"))) {
                            pw.println(indent + "    req = req.contentType(\"application/json\");");
                            pw.println(indent + "    req = req.body(\"" + escape(step.getBody()) + "\");");
                            if (allureReport) {
                                pw.println(indent + "    Allure.addAttachment(\"📋 Request Body\", \"application/json\", \"" + escape(step.getBody()) + "\");");
                            }
                        }
                        
                        // → keep console logging only when we are **not** using the Allure filter
                        if (loggingEnabled && !allureReport)
                            pw.println(indent + "    req = req.log().all();");

                        pw.println(indent + "    Response stepResponse" + stepIdx + " = req.when()." + verb + "(\"" + escape(step.getPath()) + "\")");
                        pw.println(indent + "       .then().log().ifValidationFails()");
                        pw.println(indent + "       .statusCode(" + step.getExpectedStatus() + ")");
                        pw.println(indent + "       .extract().response();");
                        
                        // Capture outputs for future steps
                        if (!step.getCaptureOutputKeys().isEmpty()) {
                            pw.println(indent + "    // Capture outputs for future steps");
                            for (String outputKey : step.getCaptureOutputKeys()) {
                                pw.println(indent + "    try {");
                                pw.println(indent + "        String captured" + outputKey + " = stepResponse" + stepIdx + ".jsonPath().getString(\"" + outputKey + "\");");
                                pw.println(indent + "        if (captured" + outputKey + " != null) {");
                                pw.println(indent + "            capturedOutputs.put(" + stepIdx + ", captured" + outputKey + ");");
                                pw.println(indent + "        }");
                                pw.println(indent + "    } catch (Exception e) {");
                                pw.println(indent + "        // Output key '" + outputKey + "' not found in response");
                                pw.println(indent + "    }");
                            }
                        }
                        
                        pw.println(indent + "    stepResults.put(" + stepIdx + ", true);");
                        pw.println(indent + "    System.out.println(\"✅ " + escape(stepTitle) + " - SUCCESS\");");

                        /* --------- mark step PASS with response details -------------------------------- */
                        if (allureReport) {
                            pw.println(indent + "    // ✅ Step completed successfully - capture response details");
                            pw.println(indent + "    try {");
                            pw.println(indent + "        String responseBody = stepResponse" + stepIdx + ".getBody().asString();");
                            pw.println(indent + "        int actualStatus = stepResponse" + stepIdx + ".getStatusCode();");
                            pw.println(indent + "        long responseTime = stepResponse" + stepIdx + ".getTime();");
                            pw.println(indent + "        ");
                            pw.println(indent + "        // Add response as prominently visible attachment");
                            pw.println(indent + "        Allure.addAttachment(\"📄 Response (Status: \" + actualStatus + \")\", \"application/json\", responseBody);");
                            pw.println(indent + "        ");
                            pw.println(indent + "        // Add key metrics as visible parameters");
                            pw.println(indent + "        Allure.parameter(\"✅ Actual Status\", actualStatus + \" (SUCCESS)\");");
                            pw.println(indent + "        Allure.parameter(\"⏱️ Response Time\", responseTime + \" ms\");");
                            pw.println(indent + "        Allure.parameter(\"📊 Response Size\", responseBody.length() + \" chars\");");
                            pw.println(indent + "    } catch (Exception e) {");
                            pw.println(indent + "        Allure.addAttachment(\"⚠️ Response Capture Error\", \"text/plain\", e.getMessage());");
                            pw.println(indent + "    }");
                        }
                        pw.println(indent + "} catch (Throwable t) {");
                        pw.println(indent + "    stepResults.put(" + stepIdx + ", false);");
                        pw.println(indent + "    System.out.println(\"❌ " + escape(stepTitle) + " - FAILED: \" + t.getMessage());");
                        if (allureReport) {
                            pw.println(indent + "    // ❌ Step failed - capture detailed error information");
                            pw.println(indent + "    String errorCategory = \"Unknown\";");
                            pw.println(indent + "    if (t instanceof java.net.ConnectException) {");
                            pw.println(indent + "        errorCategory = \"🔌 Connection Failed - Service Unreachable\";");
                            pw.println(indent + "    } else if (t instanceof AssertionError) {");
                            pw.println(indent + "        errorCategory = \"❗ Assertion Failed - Unexpected Response\";");
                            pw.println(indent + "    } else if (t instanceof java.net.SocketTimeoutException) {");
                            pw.println(indent + "        errorCategory = \"⏰ Timeout - Service Too Slow\";");
                            pw.println(indent + "    } else {");
                            pw.println(indent + "        errorCategory = \"❓ \" + t.getClass().getSimpleName();");
                            pw.println(indent + "    }");
                            pw.println(indent + "    ");
                            pw.println(indent + "    // Add error details as visible parameters");
                            pw.println(indent + "    Allure.parameter(\"❌ Error Category\", errorCategory);");
                            pw.println(indent + "    Allure.parameter(\"💥 Error Message\", t.getMessage());");
                            pw.println(indent + "    Allure.parameter(\"🔍 Exception Type\", t.getClass().getSimpleName());");
                            pw.println(indent + "    ");
                            pw.println(indent + "    // Create detailed error report");
                            pw.println(indent + "    StringBuilder errorDetails = new StringBuilder();");
                            pw.println(indent + "    errorDetails.append(\"🚨 STEP FAILURE REPORT\\n\\n\");");
                            pw.println(indent + "    errorDetails.append(\"📋 STEP INFO:\\n\");");
                            pw.println(indent + "    errorDetails.append(\"Service: " + escape(step.getServiceName()) + "\\n\");");
                            pw.println(indent + "    errorDetails.append(\"Method: " + verb.toUpperCase() + "\\n\");");
                            pw.println(indent + "    errorDetails.append(\"Path: " + escape(step.getPath()) + "\\n\");");
                            pw.println(indent + "    errorDetails.append(\"Expected Status: " + step.getExpectedStatus() + "\\n\\n\");");
                            pw.println(indent + "    errorDetails.append(\"💥 ERROR INFO:\\n\");");
                            pw.println(indent + "    errorDetails.append(\"Type: \").append(t.getClass().getSimpleName()).append(\"\\n\");");
                            pw.println(indent + "    errorDetails.append(\"Message: \").append(t.getMessage()).append(\"\\n\\n\");");
                            pw.println(indent + "    errorDetails.append(\"📚 FULL STACK TRACE:\\n\").append(t.toString());");
                            pw.println(indent + "    ");
                            pw.println(indent + "    Allure.addAttachment(\"🚨 Step Failure Details\", \"text/plain\", errorDetails.toString());");
                            pw.println(indent + "    ");
                            pw.println(indent + "    // Throw to mark step as failed in Allure");
                            pw.println(indent + "    throw new RuntimeException(\"Step failed: \" + t.getMessage(), t);");
                        }
                        pw.println(indent + "}");
                        
                        // Close the Allure.step() lambda
                        if (allureReport) {
                            pw.println("            }); // End of Allure.step()");
                        }
                        pw.println("        } else {   // step skipped due to dependency failure");
                        pw.println("            stepResults.put(" + stepIdx + ", false);");
                        pw.println("            System.out.println(\"⏭️ " + escape(stepTitle) + " - SKIPPED (dependency failed)\");");
                        if (allureReport) {
                            pw.println("            // Create skipped step with visible reason");
                            pw.println("            Allure.step(\"⏭️ " + escape(stepTitle) + " (SKIPPED)\", () -> {");
                            pw.println("                Allure.parameter(\"🏢 Service\", \"" + escape(step.getServiceName()) + "\");");
                            pw.println("                Allure.parameter(\"📡 HTTP Method\", \"" + verb.toUpperCase() + "\");");
                            pw.println("                Allure.parameter(\"🔗 Endpoint\", \"" + escape(step.getPath()) + "\");");
                            pw.println("                Allure.parameter(\"⏭️ Skip Reason\", \"Dependency failed - previous step(s) failed\");");
                            pw.println("                throw new org.junit.AssumptionViolatedException(\"Step skipped due to dependency failure\");");
                            pw.println("            });");
                        }
                        pw.println("        }");
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
