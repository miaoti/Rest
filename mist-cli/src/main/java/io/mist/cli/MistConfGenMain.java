package io.mist.cli;

import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationGenerator;
import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationIO;
import es.us.isa.restest.configuration.multiservice.MultiServiceTestConfiguration;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.specification.OpenAPISpecification;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * MIST MST configuration generator. Reads an OpenAPI specification and
 * emits a per-service test configuration YAML — the file that the
 * bundled demo's {@code conf.path} property points at and that
 * {@link MistMain} hands to the {@code MultiServiceTestCaseGenerator}.
 *
 * <p>This is a one-shot tool: run it once per OpenAPI spec change, save
 * the output where your demo {@code .properties} expects it, and from
 * then on launch MIST normally via {@link MistMain}.
 *
 * <p>Usage:
 * <pre>
 *   java -cp mist-cli/target/mist.jar io.mist.cli.MistConfGenMain \
 *        &lt;path/to/your-openapi.yaml&gt; &lt;path/to/output-conf.yaml&gt;
 * </pre>
 *
 * <p>Replaces the legacy
 * {@code es.us.isa.restest.main.MicroserviceConfBuilderMain} which
 * required editing source code to change the input / output paths.
 */
public final class MistConfGenMain {

    private MistConfGenMain() {}

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage();
            System.exit(2);
            return;
        }

        Path oasPath = Paths.get(args[0]).toAbsolutePath().normalize();
        Path outPath = Paths.get(args[1]).toAbsolutePath().normalize();

        if (!Files.exists(oasPath)) {
            System.err.println("ERROR: OpenAPI spec not found: " + oasPath);
            System.err.println();
            printUsage();
            System.exit(2);
            return;
        }

        Path outDir = outPath.getParent();
        if (outDir != null && !Files.exists(outDir)) {
            Files.createDirectories(outDir);
        }

        System.out.println("MIST conf-gen: reading  " + oasPath);
        System.out.println("MIST conf-gen: writing  " + outPath);

        OpenAPISpecification spec = new OpenAPISpecification(oasPath.toString());
        MicroserviceTestConfigurationGenerator gen =
                new MicroserviceTestConfigurationGenerator(spec);
        MultiServiceTestConfiguration multiConfig =
                gen.generateTestConfiguration(outPath.toString());

        try (InputStream in = new FileInputStream(outPath.toString())) {
            Map<String, TestConfigurationObject> serviceConfigs =
                    MicroserviceTestConfigurationIO.loadMultiServiceConfiguration(in);
            int total = 0;
            for (TestConfigurationObject co : serviceConfigs.values()) {
                total += co.getTestConfiguration().getOperations().size();
            }
            System.out.println("MIST conf-gen: " + serviceConfigs.size()
                    + " service(s), " + total + " operation(s) written.");
            for (Map.Entry<String, TestConfigurationObject> e : serviceConfigs.entrySet()) {
                System.out.println("  - " + e.getKey()
                        + "  (" + e.getValue().getTestConfiguration().getOperations().size()
                        + " operations)");
            }
        } catch (NoSuchFileException nsfe) {
            System.err.println("WARN: configuration was written but could not be read back: "
                    + nsfe.getMessage());
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  java -cp mist-cli/target/mist.jar io.mist.cli.MistConfGenMain \\");
        System.err.println("       <input-openapi.yaml> <output-conf.yaml>");
        System.err.println();
        System.err.println("Bundled TrainTicket demo example (run from repo root):");
        System.err.println("  java -cp mist-cli/target/mist.jar io.mist.cli.MistConfGenMain \\");
        System.err.println("       'mist-restest-adapter/src/main/resources/My-Example/trainticket/merged_openapi_spec 1.yaml' \\");
        System.err.println("       mist-restest-adapter/src/main/resources/My-Example/trainticket/real-system-conf.yaml");
        System.err.println();
        System.err.println("In IntelliJ, use the pre-shipped run configuration");
        System.err.println("'MIST: Generate MST Conf From OAS' (Run → Edit Configurations).");
    }
}
