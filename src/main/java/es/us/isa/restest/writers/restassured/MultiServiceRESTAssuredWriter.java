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
import java.util.Collection;
import java.util.Map;



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
        try {
            writeTestSuite(testCases);
        } catch (RESTestException e) {
            // wrap in an unchecked exception so we don't break the interface
            throw new RuntimeException("Error writing multi‑service test suite", e);
        }
    }

    public void writeTestSuite(Collection<TestCase> testCases) throws RESTestException {

        if (testCases == null || testCases.isEmpty()) return;

        String className = this.testClassName;

        try {
            File dir = new File(outputDir);
            if (!dir.exists()) dir.mkdirs();

            File javaFile = new File(outputDir, className + ".java");

            try (PrintWriter pw = new PrintWriter(new FileWriter(javaFile))) {

                /* ---------- package & imports ------------------------------------ */
                if (packageName != null && !packageName.isEmpty()) {
                    pw.println("package " + packageName + ";");
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
                if (allureReport) pw.println("        RestAssured.filters(new AllureRestAssured());");
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
                                + verb.toUpperCase() + " " + step.getPath();

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

                        /* --------- open an explicit Allure step with enhanced metadata ------------------ */
                        if (allureReport) {
                            pw.println("            String __uuid = java.util.UUID.randomUUID().toString();");
                            pw.println("            io.qameta.allure.model.StepResult __sr = "
                                    + "new io.qameta.allure.model.StepResult()"
                                    + ".setName(\"" + escape(stepTitle) + "\")");
                            pw.println("                    .setDescription(\"Service: " + escape(step.getServiceName()) + " | Method: " + verb.toUpperCase() 
                                    + " | Path: " + escape(step.getPath()) + " | Expected Status: " + step.getExpectedStatus() + "\");");
                            pw.println("            Allure.getLifecycle().startStep(__uuid, __sr);");
                            
                            // Add step metadata as parameters
                            pw.println("            Allure.parameter(\"Service\", \"" + escape(step.getServiceName()) + "\");");
                            pw.println("            Allure.parameter(\"HTTP Method\", \"" + verb.toUpperCase() + "\");");
                            pw.println("            Allure.parameter(\"Endpoint\", \"" + escape(step.getPath()) + "\");");
                            pw.println("            Allure.parameter(\"Expected Status\", " + step.getExpectedStatus() + ");");
                        }

                        pw.println("            try {");
                        pw.println("                RequestSpecification req = RestAssured.given();");
                        pw.println("                if (loginSucceeded.get()) {");
                        pw.println("                    req = req.header(\"Authorization\", jwtType + \" \" + jwt);");
                        pw.println("                }");
                        
                        // Add dependency resolution for parameters
                        for (Map.Entry<String, MultiServiceTestCase.Dependency> dep : step.getParamDependencies().entrySet()) {
                            String paramName = dep.getKey();
                            int sourceStepIdx = dep.getValue().sourceStepIndex;
                            String sourceKey = dep.getValue().sourceOutputKey;
                            pw.println("                String " + paramName + "Value = capturedOutputs.get(" + sourceStepIdx + ");");
                            pw.println("                if (" + paramName + "Value != null) {");
                            pw.println("                    // Use captured value from step " + sourceStepIdx);
                            pw.println("                }");
                        }
                        
                        // Add path parameters
                        for (Map.Entry<String, String> pathParam : step.getPathParams().entrySet()) {
                            pw.println("                req = req.pathParam(\"" + escape(pathParam.getKey()) + "\", \"" + escape(pathParam.getValue()) + "\");");
                        }
                        
                        // Add query parameters
                        for (Map.Entry<String, String> queryParam : step.getQueryParams().entrySet()) {
                            pw.println("                req = req.queryParam(\"" + escape(queryParam.getKey()) + "\", \"" + escape(queryParam.getValue()) + "\");");
                        }
                        
                        // Add headers
                        for (Map.Entry<String, String> header : step.getHeaders().entrySet()) {
                            pw.println("                req = req.header(\"" + escape(header.getKey()) + "\", \"" + escape(header.getValue()) + "\");");
                        }
                        
                        // Add body for POST/PUT/PATCH requests
                        if (step.getBody() != null && !step.getBody().trim().isEmpty() && 
                            (verb.equals("post") || verb.equals("put") || verb.equals("patch"))) {
                            pw.println("                req = req.contentType(\"application/json\");");
                            pw.println("                req = req.body(\"" + escape(step.getBody()) + "\");");
                            if (allureReport) {
                                pw.println("                Allure.addAttachment(\"Request Body\", \"application/json\", \"" + escape(step.getBody()) + "\");");
                            }
                        }
                        
                        // → keep console logging only when we are **not** using the Allure filter
                        if (loggingEnabled && !allureReport)
                            pw.println("                req = req.log().all();");

                        pw.println("                Response stepResponse" + stepIdx + " = req.when()." + verb + "(\"" + escape(step.getPath()) + "\")");
                        pw.println("                   .then().log().ifValidationFails()");
                        pw.println("                   .statusCode(" + step.getExpectedStatus() + ")");
                        pw.println("                   .extract().response();");
                        
                        // Capture outputs for future steps
                        if (!step.getCaptureOutputKeys().isEmpty()) {
                            pw.println("                // Capture outputs for future steps");
                            for (String outputKey : step.getCaptureOutputKeys()) {
                                pw.println("                try {");
                                pw.println("                    String captured" + outputKey + " = stepResponse" + stepIdx + ".jsonPath().getString(\"" + outputKey + "\");");
                                pw.println("                    if (captured" + outputKey + " != null) {");
                                pw.println("                        capturedOutputs.put(" + stepIdx + ", captured" + outputKey + ");");
                                pw.println("                    }");
                                pw.println("                } catch (Exception e) {");
                                pw.println("                    // Output key '" + outputKey + "' not found in response");
                                pw.println("                }");
                            }
                        }
                        
                        pw.println("                stepResults.put(" + stepIdx + ", true);");
                        pw.println("                System.out.println(\"✓ " + escape(stepTitle) + " - SUCCESS\");");

                        /* --------- mark step PASS with response details -------------------------------- */
                        if (allureReport) {
                            pw.println("                // Capture response details for successful steps");
                            pw.println("                try {");
                            pw.println("                    String responseBody = stepResponse" + stepIdx + ".getBody().asString();");
                            pw.println("                    int actualStatus = stepResponse" + stepIdx + ".getStatusCode();");
                            pw.println("                    Allure.addAttachment(\"Response Body (Status: \" + actualStatus + \")\", \"application/json\", responseBody);");
                            pw.println("                    Allure.parameter(\"Actual Status Code\", actualStatus);");
                            pw.println("                    ");
                            pw.println("                    // Add timing information if available");
                            pw.println("                    long responseTime = stepResponse" + stepIdx + ".getTime();");
                            pw.println("                    Allure.parameter(\"Response Time (ms)\", responseTime);");
                            pw.println("                } catch (Exception e) {");
                            pw.println("                    Allure.addAttachment(\"Response Capture Error\", e.getMessage());");
                            pw.println("                }");
                            pw.println("                Allure.getLifecycle()"
                                    + ".updateStep(s -> s.setStatus(Status.PASSED));");
                        }
                        pw.println("            } catch (Throwable t) {");
                        pw.println("                stepResults.put(" + stepIdx + ", false);");
                        pw.println("                System.out.println(\"✗ " + escape(stepTitle) + " - FAILED: \" + t.getMessage());");
                        if (allureReport) {
                            pw.println("                // Enhanced error reporting with detailed debugging information");
                            pw.println("                StringBuilder errorDetails = new StringBuilder();");
                            pw.println("                errorDetails.append(\"ERROR DETAILS:\\n\");");
                            pw.println("                errorDetails.append(\"Exception Type: \").append(t.getClass().getSimpleName()).append(\"\\n\");");
                            pw.println("                errorDetails.append(\"Error Message: \").append(t.getMessage()).append(\"\\n\\n\");");
                            pw.println("                errorDetails.append(\"STEP DETAILS:\\n\");");
                            pw.println("                errorDetails.append(\"Service: " + escape(step.getServiceName()) + "\\n\");");
                            pw.println("                errorDetails.append(\"Method: " + verb.toUpperCase() + "\\n\");");
                            pw.println("                errorDetails.append(\"Path: " + escape(step.getPath()) + "\\n\");");
                            pw.println("                errorDetails.append(\"Expected Status: " + step.getExpectedStatus() + "\\n\");");
                            pw.println("                ");
                            pw.println("                // Try to capture response information even on failure");
                            pw.println("                try {");
                            pw.println("                    if (t instanceof AssertionError && t.getMessage().contains(\"Expected status code\")) {");
                            pw.println("                        // This is likely a status code mismatch - try to get actual response");
                            pw.println("                        errorDetails.append(\"\\nThis appears to be a status code mismatch.\\n\");");
                            pw.println("                        errorDetails.append(\"Check if the service is running and the endpoint is correct.\\n\");");
                            pw.println("                    }");
                            pw.println("                } catch (Exception responseError) {");
                            pw.println("                    errorDetails.append(\"\\nCould not capture response details: \").append(responseError.getMessage());");
                            pw.println("                }");
                            pw.println("                ");
                            pw.println("                errorDetails.append(\"\\n\\nFull Stack Trace:\\n\").append(t.toString());");
                            pw.println("                ");
                            pw.println("                Allure.addAttachment(\"Detailed Error Report • " + escape(stepTitle) + "\", \"text/plain\", errorDetails.toString());");
                            pw.println("                ");
                            pw.println("                // Add failure categorization");
                            pw.println("                if (t instanceof java.net.ConnectException) {");
                            pw.println("                    Allure.parameter(\"Error Category\", \"Connection Failed - Service Unreachable\");");
                            pw.println("                } else if (t instanceof AssertionError) {");
                            pw.println("                    Allure.parameter(\"Error Category\", \"Assertion Failed - Unexpected Response\");");
                            pw.println("                } else if (t instanceof java.net.SocketTimeoutException) {");
                            pw.println("                    Allure.parameter(\"Error Category\", \"Timeout - Service Too Slow\");");
                            pw.println("                } else {");
                            pw.println("                    Allure.parameter(\"Error Category\", \"Unknown - \" + t.getClass().getSimpleName());");
                            pw.println("                }");
                            // use FAILED so the icon turns red (cross) instead of green
                            pw.println("                Allure.getLifecycle()"
                                    + ".updateStep(s -> s.setStatus(Status.FAILED));");
                        }
                        pw.println("            }");

                        if (allureReport) {
                            pw.println("            Allure.getLifecycle().stopStep();");
                        }
                        pw.println("        } else {   // step skipped due to dependency failure");
                        pw.println("            stepResults.put(" + stepIdx + ", false);");
                        pw.println("            System.out.println(\"⚠ " + escape(stepTitle) + " - SKIPPED (dependency failed)\");");
                        if (allureReport) {
                            pw.println("            Allure.getLifecycle().startStep(");
                            pw.println("                java.util.UUID.randomUUID().toString(),");
                            pw.println("                new io.qameta.allure.model.StepResult()");
                            pw.println("                     .setName(\"" + escape(stepTitle) + "\")");
                            pw.println("                     .setStatus(Status.SKIPPED));");
                            pw.println("            Allure.getLifecycle().stopStep();");
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
                    pw.println("            System.out.println(\"✓ Scenario PASSED: All \" + totalSteps + \" steps completed successfully\");");
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
