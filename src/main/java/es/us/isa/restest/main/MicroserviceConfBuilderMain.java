package es.us.isa.restest.main;

import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationGenerator;
import es.us.isa.restest.configuration.multiservice.MultiServiceTestConfiguration;
import es.us.isa.restest.configuration.multiservice.MicroserviceTestConfigurationIO;
import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.specification.OpenAPISpecification;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class MicroserviceConfBuilderMain {

    public static void main(String[] args) throws Exception {

        // Path to your real system OpenAPI specification
        String openApiPath = "src/main/resources/My-Example/ts-wait-order-service.yaml";

        // Path where the generated configuration will be saved
        String outputConfPath = "src/main/resources/My-Example/real-system-conf.yaml";

        // Load OpenAPI Specification
        OpenAPISpecification spec = new OpenAPISpecification(openApiPath);

        // Generate the multi-service configuration
        MicroserviceTestConfigurationGenerator mgen = new MicroserviceTestConfigurationGenerator(spec);
        MultiServiceTestConfiguration multiConfig = mgen.generateTestConfiguration(outputConfPath);

        System.out.println("Generated Multi-Service Test Configuration at: " + outputConfPath);

        // Load the generated configuration back to verify
        InputStream in = new FileInputStream(outputConfPath);
        Map<String, TestConfigurationObject> serviceConfigs = MicroserviceTestConfigurationIO.loadMultiServiceConfiguration(in);

        System.out.println("Loaded services:");
        for (String service : serviceConfigs.keySet()) {
            System.out.println("  - " + service);
            System.out.println("    #Operations: " + serviceConfigs.get(service).getTestConfiguration().getOperations().size());
        }
    }
}