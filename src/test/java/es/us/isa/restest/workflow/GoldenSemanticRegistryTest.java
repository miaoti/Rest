package es.us.isa.restest.workflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationIO;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.specification.OpenAPISpecification;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Regression guard for the SemanticDependencyRegistry's dependency dictionary.
 *
 * <p>Rebuilds the registry from the trainticket sample inputs and compares the
 * resulting dump against the checked-in golden copy. Any divergence either
 * indicates a genuine quality change (update the golden file intentionally) or
 * an unintended regression (fix the code). Either outcome is surfaced as a
 * reviewable diff in the failing test message.
 *
 * <p>The golden file is produced by running
 * {@code SemanticRegistryDumper} on the same inputs and copying the output to
 * {@code src/test/resources/golden/semantic-registry.trainticket.golden.json}.
 */
public class GoldenSemanticRegistryTest {

    private static final String OAS_PATH   = "src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml";
    private static final String CONF_PATH  = "src/main/resources/My-Example/trainticket/real-system-conf.yaml";
    private static final String TRACE_PATH = "src/main/resources/My-Example/trainticket/test-trace";
    private static final String GOLDEN     = "src/test/resources/golden/semantic-registry.trainticket.golden.json";

    @Test
    public void registryMatchesGoldenForTrainticket() throws Exception {
        OpenAPISpecification spec = new OpenAPISpecification(OAS_PATH);
        Map<String, TestConfigurationObject> configs;
        try (FileInputStream in = new FileInputStream(CONF_PATH)) {
            configs = MicroserviceTestConfigurationIO.loadMultiServiceConfiguration(in);
        }
        Map<String, OpenAPISpecification> specs = new LinkedHashMap<>();
        for (String svc : configs.keySet()) specs.put(svc, spec);

        List<WorkflowScenario> scenarios;
        try {
            scenarios = TraceWorkflowExtractor.extractScenarios(TRACE_PATH);
        } catch (Exception e) {
            scenarios = null;
        }

        SemanticDependencyRegistry reg = SemanticDependencyRegistry.build(configs, specs, scenarios);
        String actual = canonicalize(reg);
        String expected = canonicalize(Files.readString(Paths.get(GOLDEN)));

        if (!actual.equals(expected)) {
            fail("Registry diverged from golden file.\n" +
                 "If this change is intentional, regenerate the golden file:\n" +
                 "  mvn -q exec:java -Dexec.mainClass=es.us.isa.restest.main.SemanticRegistryDumper\n" +
                 "  cp target/semantic-registry-dump.json " + GOLDEN + "\n" +
                 firstDiff(expected, actual));
        }
    }

    /**
     * Canonicalize a registry to JSON with sorted keys and stable field order
     * so byte-level diffing is reproducible. Also strips the {@code _stats}
     * block whose counts may legitimately shift when the dictionary expands.
     */
    private static String canonicalize(SemanticDependencyRegistry reg) throws IOException {
        File tmp = File.createTempFile("registry", ".json");
        tmp.deleteOnExit();
        reg.dumpRegistryToFile(tmp.getAbsolutePath());
        return canonicalize(Files.readString(tmp.toPath()));
    }

    private static String canonicalize(String json) throws IOException {
        ObjectMapper m = new ObjectMapper();
        m.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        JsonNode node = m.readTree(json);
        if (node.isObject()) ((com.fasterxml.jackson.databind.node.ObjectNode) node).remove("_stats");
        return m.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    /** Produce a short "first difference" hint for the failure message. */
    private static String firstDiff(String expected, String actual) {
        String[] e = expected.split("\n"), a = actual.split("\n");
        int n = Math.min(e.length, a.length);
        for (int i = 0; i < n; i++) {
            if (!e[i].equals(a[i])) {
                StringBuilder sb = new StringBuilder("\nFirst divergence at line ").append(i + 1).append(":\n");
                sb.append("  expected: ").append(e[i]).append('\n');
                sb.append("  actual:   ").append(a[i]).append('\n');
                return sb.toString();
            }
        }
        if (e.length != a.length) {
            return "\nFiles differ in length: expected=" + e.length + " actual=" + a.length + " lines.\n";
        }
        return "";
    }
}
