package es.us.isa.restest.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.specification.OpenAPISpecification;
import io.swagger.v3.oas.models.PathItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Utility class to load and save test configuration files
 * 
 * @author Sergio Segura
 *
 */
public class TestConfigurationIO {

	private static Logger logger = LogManager.getLogger(TestConfigurationIO.class);

	private TestConfigurationIO() {
	}

	/**
	 * Load test configuration file in YAML format
	 * @param path The path where the test configuration file is located
	 * @param spec The OpenAPI specification related to the test configuration file
	 * @return the test configuration as an object
	 */
	public static TestConfigurationObject loadConfiguration(String path, OpenAPISpecification spec) {
		YAMLMapper mapper = new YAMLMapper();
		TestConfigurationObject conf = null;
		try {
			conf = mapper.readValue(new File(path), TestConfigurationObject.class);

			// MST configs populate `services:` (Map<service, List<Op>>), classic configs
			// populate `operations:` (flat List<Op>). Only the classic shape carries
			// per-operation OpenAPI hookups here; for MST shape, getOperations() is null
			// and the per-service operations are wired separately by the MST generator.
			List<Operation> operations = conf.getTestConfiguration() != null
					? conf.getTestConfiguration().getOperations()
					: null;
			if (operations == null || operations.isEmpty()) {
				logger.debug("Test configuration at '{}' uses multi-service ('services:') format; "
						+ "skipping flat-operations OpenAPI wiring.", path);
				return conf;
			}

			Map<String, PathItem> pathsByName = spec.getSpecification() != null
					&& spec.getSpecification().getPaths() != null
					? spec.getSpecification().getPaths()
					: java.util.Collections.emptyMap();

			operations.forEach(x -> {
				if (x == null || x.getTestPath() == null || x.getMethod() == null) {
					logger.warn("Skipping operation entry with missing testPath or method: {}", x);
					return;
				}
				PathItem pathItem = pathsByName.get(x.getTestPath());
				if (pathItem == null) {
					logger.warn("Test path '{}' not found in OpenAPI spec; "
							+ "leaving openApiOperation unset.", x.getTestPath());
					return;
				}
				switch (x.getMethod().toLowerCase()) {
				case "get":
					x.setOpenApiOperation(pathItem.getGet());
					break;
				case "post":
					x.setOpenApiOperation(pathItem.getPost());
					break;
				case "put":
					x.setOpenApiOperation(pathItem.getPut());
					break;
				case "patch":
					x.setOpenApiOperation(pathItem.getPatch());
					break;
				case "delete":
					x.setOpenApiOperation(pathItem.getDelete());
					break;
				default:
					throw new IllegalArgumentException("Method type not supported: " + x.getMethod());
				}
			});

		} catch (Exception e) {
			logger.error("Error parsing configuration file: {}", e.getMessage());
		}

		return conf;
	}

	public static String toString(TestConfigurationObject conf) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonConf = null;
		try {
			jsonConf = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf);
		} catch (JsonProcessingException e) {
			logger.error("Error converting configuration object to a string: {}", e.getMessage());
		}
		return jsonConf;
	}

	/**
	 *  Save a test configuration object in a YAML file
	 * @param conf The test configuration object
	 * @param path The path where the test configuration file will be generated
	 *
	 */
	public static void toFile(TestConfigurationObject conf, String path) {
		ObjectMapper mapper = new ObjectMapper();
		try (FileWriter confFile = new FileWriter(path)) {
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf);
			JsonNode jsonNode = mapper.readTree(json);

			// Convert JSON to YAML and write file
			YAMLMapper yamlMapper = new YAMLMapper();
			yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
			String yaml = yamlMapper.writeValueAsString(jsonNode);
			confFile.write(yaml);
			confFile.flush();
		} catch (IOException e) {
			logger.error("Error converting configuration object to a file: {}", e.getMessage());
		}
	}
}
