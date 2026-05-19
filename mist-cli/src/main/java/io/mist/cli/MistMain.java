package io.mist.cli;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.main.MistRunResult;
import es.us.isa.restest.main.MistRunner;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Stand-alone MIST entry point. Lives in the {@code mist-cli} module
 * (Stage 1.C of {@code PATH_B_REBUILD_PLAN.md}). The legacy
 * {@code TestGenerationAndExecution} still dispatches MST work to
 * {@link MistRunner}; this class lets {@code java -jar mist.jar} reach
 * the same runner without crossing the RESTest main class.
 *
 * <p>Behaviour mirror.  Both launch paths
 * ({@code java -jar restest.jar} and {@code java -jar mist.jar})
 * end at {@link MistRunner#run()} with the same {@link MstConfig}, so
 * under {@code -Drandom.seed=42} the two jars produce byte-identical
 * scenario output (gate § Stage 1.B of the plan).
 */
public final class MistMain {

    private MistMain() {}

    public static void main(String[] args) throws Exception {
        Path propsFile = Paths.get(args.length > 0
                ? args[0]
                : "src/main/resources/My-Example/trainticket-demo.properties");

        // Load the .properties file and push every key into System
        // properties so MstConfig.fromSystemProperties() and the legacy
        // readers in generators / writers / generated test code see the
        // values, matching TestGenerationAndExecution's behaviour.
        Properties coreProps = new Properties();
        try (InputStream in = Files.newInputStream(propsFile)) {
            coreProps.load(in);
        } catch (NoSuchFileException nsfe) {
            System.err.println("MIST: properties file not found: " + propsFile.toAbsolutePath());
            System.exit(2);
            return;
        }
        coreProps.forEach((k, v) -> System.setProperty(String.valueOf(k), String.valueOf(v)));

        // MST mode loads its dedicated properties file via mst.config.path.
        String mstConfigPath = System.getProperty("mst.config.path");
        if (mstConfigPath != null && !mstConfigPath.trim().isEmpty()) {
            Properties mstProps = new Properties();
            try (InputStream in = Files.newInputStream(Paths.get(mstConfigPath))) {
                mstProps.load(in);
            }
            mstProps.forEach((k, v) -> {
                if (System.getProperty(String.valueOf(k)) == null) {
                    System.setProperty(String.valueOf(k), String.valueOf(v));
                }
            });
        }

        MstConfig config = MstConfig.fromSystemProperties();
        Path workdir = Paths.get(System.getProperty("user.dir"));

        MistRunner.Inputs inputs = MistRunner.Inputs.builder()
                .testClassName(System.getProperty("testclass.name"))
                .targetDirJava(System.getProperty("test.target.dir"))
                .packageName(System.getProperty("experiment.name"))
                .experimentName(System.getProperty("experiment.name"))
                .oasPath(System.getProperty("oas.path"))
                .confPath(System.getProperty("conf.path"))
                .propertiesFilePath(propsFile.toString())
                .mstPropertiesFilePath(mstConfigPath)
                .traceFilePath("src/main/resources/My-Example/trainticket/test-trace")
                .numTestCases(parseIntOrNull(System.getProperty("testsperoperation")))
                .faultyRatio(parseFloatOrNull(System.getProperty("faulty.ratio")))
                .executeTestCases(parseBoolOrNull(System.getProperty("experiment.execute")))
                .allureReports(parseBoolOrNull(System.getProperty("allure.report")))
                .enableCSVStats(parseBoolOrNull(System.getProperty("stats.csv")))
                .enableInputCoverage(parseBoolOrNull(System.getProperty("coverage.input")))
                .enableOutputCoverage(parseBoolOrNull(System.getProperty("coverage.output")))
                .deletePreviousResults(parseBoolOrNull(System.getProperty("deletepreviousresults")))
                .logToFile(parseBoolOrNull(System.getProperty("logToFile")))
                .checkTestCases(Boolean.parseBoolean(System.getProperty("testcases.check", "false")))
                .proxy(normaliseProxy(System.getProperty("proxy")))
                .build();

        MistRunResult result = new MistRunner(config, workdir, inputs).run();
        result.summarise(System.out);
        System.exit(result.exitCode());
    }

    private static Integer parseIntOrNull(String raw) {
        if (raw == null || raw.trim().isEmpty()) return null;
        try { return Integer.parseInt(raw.trim()); } catch (NumberFormatException e) { return null; }
    }

    private static Float parseFloatOrNull(String raw) {
        if (raw == null || raw.trim().isEmpty()) return null;
        try { return Float.parseFloat(raw.trim()); } catch (NumberFormatException e) { return null; }
    }

    private static Boolean parseBoolOrNull(String raw) {
        if (raw == null || raw.trim().isEmpty()) return null;
        return Boolean.parseBoolean(raw.trim());
    }

    private static String normaliseProxy(String raw) {
        if (raw == null || "null".equals(raw) || raw.split(":").length != 2) return null;
        return raw;
    }
}
