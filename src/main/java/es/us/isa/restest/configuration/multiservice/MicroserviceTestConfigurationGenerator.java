package es.us.isa.restest.configuration.multiservice;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.us.isa.restest.specification.OpenAPIOperation;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.specification.OpenAPISpecificationVisitor;
import es.us.isa.restest.specification.OpenAPIParameter;
import es.us.isa.restest.util.SchemaManager;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MicroserviceTestConfigurationGenerator {
    private final OpenAPISpecification openApiSpec;
    private final boolean includeInvalidTests = false; // could be a config option to include invalid-value test parameters

    public MicroserviceTestConfigurationGenerator(OpenAPISpecification openApiSpec) {
        this.openApiSpec = openApiSpec;
    }

    /**
     * Generates a multi-service test configuration from the OpenAPI spec.
     * @param outputYamlPath File path to write the configuration YAML.
     * @return the MultiServiceTestConfiguration object representing the config.
     * @throws IOException if writing to file fails.
     */
    public MultiServiceTestConfiguration generateTestConfiguration(String outputYamlPath) throws IOException {
        MultiServiceTestConfiguration multiConfig = new MultiServiceTestConfiguration();
        Map<String, List<OperationConfig>> opsByService = new LinkedHashMap<>();

        for (OpenAPIOperation apiOp : openApiSpec.getAllOperations()) {
            String serviceName = determineServiceName(apiOp);
            opsByService.computeIfAbsent(serviceName, k -> new ArrayList<>());

            OperationConfig opConfig = new OperationConfig();
            opConfig.setTestPath(apiOp.getPath());
            opConfig.setMethod(apiOp.getMethod().toLowerCase());
            String opId = apiOp.getOperationId();
            if (opId == null || opId.isEmpty()) {
                opId = (apiOp.getMethod() + " " + apiOp.getPath()).trim();
            }
            opConfig.setOperationId(opId);

            List<TestParameter> paramConfigs = new ArrayList<>();

            // 1) existing path/query/header parameters
            for (OpenAPIParameter apiParam : apiOp.getParameters()) {
                paramConfigs.add(toTestParam(apiParam));
            }

            // 2) ▼ Enhanced: inspect requestBody → application/json schema with $ref resolution
            RequestBody rb = apiOp.getRequestBody();
            if (rb != null) {
                // Resolve requestBody if it has $ref
                RequestBody resolvedRb = rb;
                if (rb.get$ref() != null) {
                    String refName = rb.get$ref().replace("#/components/requestBodies/", "");
                    
                    // Handle service-specific prefix mapping
                    // e.g., api_PriceInfo -> ts-admin-basic-info-service_PriceInfo
                    String actualRefName = resolveServiceSpecificReference(refName, serviceName, 
                            openApiSpec.getSpecification().getComponents().getRequestBodies().keySet());
                    
                    if (openApiSpec.getSpecification().getComponents() != null && 
                        openApiSpec.getSpecification().getComponents().getRequestBodies() != null &&
                        openApiSpec.getSpecification().getComponents().getRequestBodies().containsKey(actualRefName)) {
                        resolvedRb = openApiSpec.getSpecification().getComponents().getRequestBodies().get(actualRefName);
                    }
                }
                
                if (resolvedRb.getContent() != null && resolvedRb.getContent().containsKey("application/json")) {
                    MediaType mt = resolvedRb.getContent().get("application/json");
                    Schema<?> schema = mt.getSchema();
                    
                    // Resolve the schema if it has $ref with service-specific mapping
                    if (schema != null) {
                        Schema<?> resolvedSchema = resolveSchemaWithServiceMapping(schema, serviceName, openApiSpec.getSpecification());
                        
                        if (resolvedSchema != null && resolvedSchema.getProperties() != null) {
                            List<String> requiredProps = resolvedSchema.getRequired() != null
                                    ? resolvedSchema.getRequired() : Collections.emptyList();

                            @SuppressWarnings("unchecked")
                            Map<String, Schema<?>> props =
                                    (Map<String, Schema<?>>)(Map) resolvedSchema.getProperties();

                            for (Map.Entry<String, Schema<?>> pe : props.entrySet()) {
                                String propName = pe.getKey();
                                Schema<?> propSchema = pe.getValue();
                                // wrap into your OpenAPIParameter body‐param constructor
                                OpenAPIParameter bodyParam = new OpenAPIParameter(propName, propSchema,
                                        requiredProps.contains(propName));
                                paramConfigs.add(toTestParam(bodyParam));
                            }
                        }
                    }
                }
            }

            opConfig.setTestParameters(paramConfigs.isEmpty() ? null : paramConfigs);
            opConfig.setExpectedResponse(deriveExpectedResponse(apiOp));
            opsByService.get(serviceName).add(opConfig);
        }

        // sort each service's operations by path
        for (List<OperationConfig> list : opsByService.values()) {
            list.sort(Comparator.comparing(OperationConfig::getTestPath));
        }

        // assemble and write YAML
        for (Map.Entry<String, List<OperationConfig>> e : opsByService.entrySet()) {
            multiConfig.addServiceConfig(e.getKey(), e.getValue());
        }
        writeYamlFile(multiConfig, outputYamlPath);
        return multiConfig;
    }

    /**
     * Helper to convert any OpenAPIParameter into our TestParameter,
     * carrying over description, type, format, pattern, enum, min/max, example, etc.
     */
    private TestParameter toTestParam(OpenAPIParameter apiParam) {
        TestParameter tp = new TestParameter();
        tp.setName(apiParam.getName());
        tp.setIn(apiParam.getIn());
        tp.setWeight(null);
        tp.setValid(true);
        tp.setDescription(apiParam.getDescription());
        tp.setType(apiParam.getType());
        tp.setFormat(apiParam.getFormat());
        tp.setPattern(apiParam.getPattern());
        tp.setEnumValues(apiParam.getEnumValues());
        tp.setMin(apiParam.getMin());
        tp.setMax(apiParam.getMax());
        tp.setMinLength(apiParam.getMinLength());
        tp.setMaxLength(apiParam.getMaxLength());
        tp.setExample(apiParam.getExample());

        List<ValueGenerator> gens = new ArrayList<>();
        gens.add(createDefaultGenerator(apiParam));
        tp.setGenerators(gens);
        return tp;
    }


    /**
     * Determine the service name for a given operation using OpenAPI metadata (tags, servers, etc.).
     */
    private String determineServiceName(OpenAPIOperation apiOp) {
        // 1. Check for a custom extension (e.g., x-service-name)
        Object serviceExt = apiOp.getExtension("x-service-name");
        if (serviceExt instanceof String && !((String) serviceExt).isEmpty()) {
            return ((String) serviceExt).trim();
        }
        
        // 2. If the operation has tags, look for one ending with "service"
        List<String> tags = apiOp.getTags();
        if (tags != null && !tags.isEmpty()) {
            // First, try to find a tag that ends with "service"
            for (String tag : tags) {
                if (tag != null && tag.toLowerCase().endsWith("service")) {
                    return tag.trim();
                }
            }
            // Fallback: use the first tag if no service tag found
            return tags.get(0).trim();
        }
        
        // 3. If the operation has a specific server URL, derive service name from it
        if (apiOp.getServers() != null && !apiOp.getServers().isEmpty()) {
            String url = apiOp.getServers().get(0).getUrl();
            // Try to extract a service name from the URL (e.g., subdomain or first path segment)
            try {
                URL serverUrl = new URL(url);
                String host = serverUrl.getHost();
                String path = serverUrl.getPath();
                if (path != null && path.length() > 1) {
                    // e.g., "http://example.com/serviceA/..." -> serviceA
                    String[] segments = path.split("/");
                    if (segments.length > 1 && !segments[1].isEmpty()) {
                        return segments[1];
                    }
                }
                // If path is just "/" or empty, use subdomain or host
                if (host != null && !host.isEmpty()) {
                    // Take the first part of the host (before the first dot) as service name
                    String[] hostParts = host.split("\\.");
                    if (hostParts.length > 0 && hostParts[0].length() > 0) {
                        return hostParts[0];
                    }
                }
            } catch (Exception e) {
                // URL parsing failed, fall through to default
            }
        }
        // 4. Fallback: if none of the above, use a default name
        return "DefaultService";
    }

    /**
     * Create a default value generator for a parameter based on its schema (type/format).
     */
    private ValueGenerator createDefaultGenerator(OpenAPIParameter apiParam) {
        String type = apiParam.getType();      // e.g., "integer", "string", "boolean"
        String format = apiParam.getFormat();  // e.g., "int32", "uuid", etc.
        ValueGenerator generator = new ValueGenerator();
        List<GenParam> genParams = new ArrayList<>();

        if (type == null) {
            // If type is undefined, treat as string
            type = "string";
        }
        switch (type.toLowerCase()) {
            case "integer":
            case "number":
                generator.setType("LLMParameterGenerator");
                // For numeric types, default range 1-100 (could refine using schema if available)
                GenParam typeParam = new GenParam();
                typeParam.setName("type");
                typeParam.setValues(Collections.singletonList("integer"));
                GenParam minParam = new GenParam();
                minParam.setName("min");
                minParam.setValues(Collections.singletonList("1"));
                GenParam maxParam = new GenParam();
                maxParam.setName("max");
                maxParam.setValues(Collections.singletonList("100"));
                genParams.add(typeParam);
                genParams.add(minParam);
                genParams.add(maxParam);
                break;
            case "boolean":
                generator.setType("LLMParameterGenerator");
                // For boolean, provide true/false as possible values (choose valid ones only by default)
                GenParam boolValues = new GenParam();
                boolValues.setName("values");
                boolValues.setValues(Arrays.asList("true", "false"));
                genParams.add(boolValues);
                break;
//            case "string":
//                generator.setType("number");
            default:
                generator.setType("LLMParameterGenerator");//type.toLowerCase());
                // For strings, we can specify a length or pattern if known. We'll use a simple length=10 default.
                GenParam lengthParam = new GenParam();
                lengthParam.setName("length");
                lengthParam.setValues(Collections.singletonList("10"));
                genParams.add(lengthParam);
                // If format suggests an email/uuid etc., we could adjust generator type accordingly (not shown for brevity).
                generator.setDescription(apiParam.getDescription());
                break;
        }
        generator.setGenParameters(genParams);
        return generator;
    }

    /**
     * Derive a default expected response code for an operation (e.g., 200 or 201).
     */
    private Integer deriveExpectedResponse(OpenAPIOperation apiOp) {
        // If the OpenAPI spec provides responses, choose the first 2xx code if available
        List<String> responseCodes = new ArrayList<>(apiOp.getResponseCodes());
        Collections.sort(responseCodes); // sort lexicographically (e.g., "200" comes before "404")
        for (String code : responseCodes) {
            if (code.startsWith("2")) {
                try {
                    return Integer.parseInt(code);
                } catch (NumberFormatException ignore) {}
            }
        }
        // Default to 200 if no specific success code found
        return 200;
    }

    /**
     * Write the MultiServiceTestConfiguration to a YAML file.
     */
    private void writeYamlFile(MultiServiceTestConfiguration config, String outputPath) throws IOException {
        // 1) Configure block style
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        Yaml yaml = new Yaml(options);

        // 2) Build a plain Map<String,Object> structure
        Map<String,Object> top = new LinkedHashMap<>();
        Map<String,Object> servicesMap = new LinkedHashMap<>();

        for (Map.Entry<String,List<OperationConfig>> svcEntry : config.getServices().entrySet()) {
            String svcName = svcEntry.getKey();
            List<Map<String,Object>> opsList = new ArrayList<>();

            for (OperationConfig oc : svcEntry.getValue()) {
                Map<String,Object> opMap = new LinkedHashMap<>();
                opMap.put("expectedResponse", oc.getExpectedResponse());
                opMap.put("method",           oc.getMethod());
                opMap.put("operationId",      oc.getOperationId());
                opMap.put("testPath",         oc.getTestPath());

                if (oc.getTestParameters() != null) {
                    List<Map<String,Object>> params = new ArrayList<>();
                    for (TestParameter tp : oc.getTestParameters()) {
                        Map<String,Object> p = new LinkedHashMap<>();
                        p.put("name",        tp.getName());
                        p.put("in",          tp.getIn());
                        p.put("weight",      tp.getWeight());
                        p.put("description", tp.getDescription());
                        if (tp.getType()       != null) p.put("type",       tp.getType());
                        if (tp.getFormat()     != null) p.put("format",     tp.getFormat());
                        if (tp.getPattern()    != null) p.put("pattern",    tp.getPattern());
                        if (tp.getEnumValues() != null) p.put("enumValues", tp.getEnumValues());
                        if (tp.getMin()        != null) p.put("minimum",    tp.getMin());
                        if (tp.getMax()        != null) p.put("maximum",    tp.getMax());
                        if (tp.getMinLength()  != null) p.put("minLength",  tp.getMinLength());
                        if (tp.getMaxLength()  != null) p.put("maxLength",  tp.getMaxLength());

                        // ▼ Example conversion: turn any Jackson ArrayNode/ObjectNode into plain Java
                        Object example = tp.getExample();
                        if (example instanceof com.fasterxml.jackson.databind.JsonNode) {
                            example = convertJsonNode((com.fasterxml.jackson.databind.JsonNode) example);
                        }
                        if (example != null) {
                            p.put("example", example);
                        }

                        // generators
                        List<Map<String,Object>> gens = new ArrayList<>();
                        for (ValueGenerator vg : tp.getGenerators()) {
                            Map<String,Object> gm = new LinkedHashMap<>();
                            gm.put("type", vg.getType());
                            List<Map<String,Object>> gpList = new ArrayList<>();
                            for (GenParam gp : vg.getGenParameters()) {
                                Map<String,Object> single = new LinkedHashMap<>();
                                single.put("name",   gp.getName());
                                single.put("values", gp.getValues());
                                gpList.add(single);
                            }
                            gm.put("genParameters", gpList);
                            gens.add(gm);
                        }
                        p.put("generators", gens);

                        params.add(p);
                    }
                    opMap.put("testParameters", params);
                } else {
                    opMap.put("testParameters", null);
                }

                opsList.add(opMap);
            }

            servicesMap.put(svcName, opsList);
        }

        top.put("testConfiguration", Collections.singletonMap("services", servicesMap));

        // 3) Dump!
        try (FileWriter writer = new FileWriter(outputPath)) {
            yaml.dump(top, writer);
        }
    }

    /** Recursively converts a Jackson JsonNode into plain Java types. */
    private Object convertJsonNode(com.fasterxml.jackson.databind.JsonNode node) {
        if (node.isObject()) {
            Map<String,Object> map = new LinkedHashMap<>();
            node.fieldNames().forEachRemaining(field ->
                    map.put(field, convertJsonNode(node.get(field)))
            );
            return map;
        }
        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            node.forEach(elem -> list.add(convertJsonNode(elem)));
            return list;
        }
        if (node.isTextual())    return node.asText();
        if (node.isNumber())     return node.numberValue();
        if (node.isBoolean())    return node.asBoolean();
        // fallback: serialise to String
        return node.toString();
    }

    /**
     * Resolves service-specific reference names.
     * Maps generic references like "api_PriceInfo" to actual service-specific names like "ts-admin-basic-info-service_PriceInfo"
     */
    private String resolveServiceSpecificReference(String genericRefName, String serviceName, Set<String> availableRefs) {
        // First try direct match
        if (availableRefs.contains(genericRefName)) {
            return genericRefName;
        }
        
        // Try with service prefix
        String serviceSpecificRef = serviceName + "_" + genericRefName.replace("api_", "");
        if (availableRefs.contains(serviceSpecificRef)) {
            return serviceSpecificRef;
        }
        
        // Try to find any reference that ends with the generic name (without api_ prefix)
        String withoutApiPrefix = genericRefName.replace("api_", "");
        for (String ref : availableRefs) {
            if (ref.endsWith("_" + withoutApiPrefix)) {
                return ref;
            }
        }
        
        // Fallback to original name
        return genericRefName;
    }
    
    /**
     * Resolves schema references with service-specific mapping.
     */
    private Schema<?> resolveSchemaWithServiceMapping(Schema<?> schema, String serviceName, OpenAPI spec) {
        if (schema.get$ref() == null) {
            return SchemaManager.resolveSchema(schema, spec);
        }
        
        String refName = schema.get$ref().replace("#/components/schemas/", "");
        String actualRefName = resolveServiceSpecificReference(refName, serviceName, 
                spec.getComponents().getSchemas().keySet());
        
        // Create a temporary schema with the corrected reference
        Schema<?> correctedSchema = new Schema<>();
        correctedSchema.set$ref("#/components/schemas/" + actualRefName);
        
        return SchemaManager.resolveSchema(correctedSchema, spec);
    }

}
