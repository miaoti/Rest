package io.mist.cli;

import es.us.isa.restest.configuration.MstConfig;
import es.us.isa.restest.main.MistPathResolver;
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
 * end at {@link MistRunner#run()} with the same {@link MstConfig}.
 * To keep the resulting System-properties state byte-identical to
 * {@code TestGenerationAndExecution.main}, this entry point pushes
 * only the MST-file keys into System (via the legacy
 * {@code es.us.isa.restest.configuration.multiservice.MstConfig}
 * loader's {@code applyToSystemProperties()}); core-file keys stay in
 * the local {@code Properties} bag and feed the {@link MistRunner.Inputs}
 * builder directly. Under {@code -Drandom.seed=42} this gives byte-
 * identical scenario output between the two launch paths (Stage 1.D
 * gate of the plan).
 */
public final class MistMain {

    private MistMain() {}

    public static void main(String[] args) throws Exception {
        Path propsFile = Paths.get(args.length > 0
                ? args[0]
                : "mist-restest-adapter/src/main/resources/My-Example/trainticket-demo.properties")
                .toAbsolutePath().normalize();

        Properties coreProps = new Properties();
        try (InputStream in = Files.newInputStream(propsFile)) {
            coreProps.load(in);
        } catch (NoSuchFileException nsfe) {
            System.err.println("MIST: properties file not found: " + propsFile);
            System.exit(2);
            return;
        }

        // Resolve relative INPUT paths against the .properties file's own
        // directory so the demo works the same from repo root, from any
        // module directory, or via IntelliJ play-button (the JVM CWD no
        // longer matters for input lookup).
        MistPathResolver.resolveInputPaths(coreProps, propsFile.toFile());

        // MST mode loads its dedicated properties file via mst.config.path and
        // pushes those keys (only) to System properties — same code path the
        // legacy TestGenerationAndExecution.loadMstConfig() takes. Keeping the
        // System state identical between the two launch paths is what makes
        // their generation byte-identical under -Drandom.seed.
        String mstConfigPath = coreProps.getProperty("mst.config.path");
        if (mstConfigPath != null && !mstConfigPath.trim().isEmpty()) {
            es.us.isa.restest.configuration.multiservice.MstConfig
                    .load(mstConfigPath)
                    .applyToSystemProperties();
            // The MST file's own input paths are now in System; resolve them
            // against the MST file's own directory (which can differ from the
            // core file's directory when the user splits the two).
            Properties mstView = new Properties();
            for (String key : new String[]{
                    "input.fetch.registry.path",
                    "smart.input.fetch.registry.path",
                    "root.api.registry.path",
                    "noun.map.path",
                    "fault.types.path",
                    "mist.fault.types.path",
                    "seed.trace.labels.path",
                    "mist.tso.store.path",
                    "fault.detection.injected.faults.path"}) {
                String v = System.getProperty(key);
                if (v != null) mstView.setProperty(key, v);
            }
            MistPathResolver.resolveInputPaths(
                    mstView, java.nio.file.Paths.get(mstConfigPath).toFile());
            mstView.forEach((k, v) -> System.setProperty(String.valueOf(k), String.valueOf(v)));
        }

        MstConfig config = MstConfig.fromSystemProperties();
        Path workdir = Paths.get(System.getProperty("user.dir"));

        MistRunner.Inputs inputs = MistRunner.Inputs.builder()
                .testClassName(coreProps.getProperty("testclass.name"))
                .targetDirJava(coreProps.getProperty("test.target.dir"))
                .packageName(coreProps.getProperty("experiment.name"))
                .experimentName(coreProps.getProperty("experiment.name"))
                .oasPath(coreProps.getProperty("oas.path"))
                .confPath(coreProps.getProperty("conf.path"))
                .propertiesFilePath(propsFile.toString())
                .mstPropertiesFilePath(mstConfigPath)
                .traceFilePath(coreProps.getProperty(
                        "trace.file.path",
                        "src/main/resources/My-Example/trainticket/test-trace"))
                .numTestCases(parseIntOrNull(coreProps.getProperty("testsperoperation")))
                .faultyRatio(parseFloatOrNull(coreProps.getProperty("faulty.ratio")))
                .executeTestCases(parseBoolOrNull(coreProps.getProperty("experiment.execute")))
                .allureReports(parseBoolOrNull(coreProps.getProperty("allure.report")))
                .enableCSVStats(parseBoolOrNull(coreProps.getProperty("stats.csv")))
                .enableInputCoverage(parseBoolOrNull(coreProps.getProperty("coverage.input")))
                .enableOutputCoverage(parseBoolOrNull(coreProps.getProperty("coverage.output")))
                .deletePreviousResults(parseBoolOrNull(coreProps.getProperty("deletepreviousresults")))
                .logToFile(parseBoolOrNull(coreProps.getProperty("logToFile")))
                .checkTestCases(Boolean.parseBoolean(coreProps.getProperty("testcases.check", "false")))
                .proxy(normaliseProxy(coreProps.getProperty("proxy")))
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
