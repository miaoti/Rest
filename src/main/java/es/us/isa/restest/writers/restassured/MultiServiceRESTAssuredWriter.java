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
            // wrap in an unchecked exception so we don’t break the interface
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

                    pw.println("    @Test");
                    pw.println("    public void testScenario" + scenarioIdx + "() throws Exception {");

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
                    pw.println("                    .body(\"{\\\"username\\\":\\\"YOUR_USERNAME\\\"," +
                            "\\\"password\\\":\\\"YOUR_PASSWORD\\\"}\")");
                    pw.println("                .when().post(\"/login\")");
                    pw.println("                    .then().log().ifValidationFails()");
                    pw.println("                    .statusCode(200)");
                    pw.println("                    .extract().response();");
                    pw.println("                _jwt[0]     = loginRes.jsonPath().getString(\"id\");");
                    pw.println("                _jwtType[0] = loginRes.jsonPath().getString(\"type\");");
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
                    int stepIdx = 1;
                    for (MultiServiceTestCase.StepCall step : scenario.getSteps()) {

                        String verb = step.getMethod() == null || step.getMethod().getMethod() == null
                                ? "get"
                                : step.getMethod().getMethod().toLowerCase();
                        String stepTitle = "Step " + stepIdx + ": "
                                + step.getServiceName() + " "
                                + verb.toUpperCase() + " " + step.getPath();

                        pw.println("        // " + escape(stepTitle));
                        pw.println("        if (!scenarioFailed.get()) {");

                        /* --------- open an explicit Allure step ------------------ */
                        if (allureReport) {
                            pw.println("            String __uuid = java.util.UUID.randomUUID().toString();");
                            pw.println("            io.qameta.allure.model.StepResult __sr = "
                                    + "new io.qameta.allure.model.StepResult()"
                                    + ".setName(\"" + escape(stepTitle) + "\");");
                            pw.println("            Allure.getLifecycle().startStep(__uuid, __sr);");
                        }

                        pw.println("            try {");
                        pw.println("                RequestSpecification req = RestAssured.given();");
                        pw.println("                if (loginSucceeded.get()) {");
                        pw.println("                    req = req.header(\"Authorization\", jwtType + \" \" + jwt);");
                        pw.println("                }");
                        // → keep console logging only when we are **not** using the Allure filter
                        if (loggingEnabled && !allureReport)
                            pw.println("                req = req.log().all();");

                        pw.println("                req.when()." + verb + "(\"" + escape(step.getPath()) + "\")");
                        pw.println("                   .then().log().ifValidationFails()");
                        pw.println("                   .statusCode(" + step.getExpectedStatus() + ");");

                        /* --------- mark step PASS -------------------------------- */
                        if (allureReport) {
                            pw.println("                Allure.getLifecycle()"
                                    + ".updateStep(s -> s.setStatus(Status.PASSED));");
                        }
                        pw.println("            } catch (Throwable t) {");
                        pw.println("                scenarioFailed.set(true);");
                        if (allureReport) {
                            pw.println("                Allure.addAttachment(\"Error • " + escape(stepTitle) + "\", t.toString());");
                            // use FAILED so the icon turns red (cross) instead of green
                            pw.println("                Allure.getLifecycle()"
                                    + ".updateStep(s -> s.setStatus(Status.FAILED));");
                        }
                        pw.println("            }");

                        if (allureReport) {
                            pw.println("            Allure.getLifecycle().stopStep();");
                        }
                        pw.println("        } else {   // scenario already broken – mark as SKIPPED");
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

                    pw.println("        if (scenarioFailed.get()) {");
                    pw.println("            fail(\"Scenario finished with one or more failed steps\");");
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
