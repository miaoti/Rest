package es.us.isa.restest.inputs.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import es.us.isa.restest.generators.AiDrivenLLMGenerator;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import es.us.isa.restest.inputs.stateful.ParameterGenerator;
import es.us.isa.restest.specification.OpenAPIParameter;
import es.us.isa.restest.specification.OpenAPISpecificationVisitor;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * Example LLM-based generator that extends ParameterGenerator
 * to fetch parameter metadata from your OAS and pass it to ZeroShotLLMGenerator.
 *
 * This version:
 *   1) Finds the OAS Operation from the path + method
 *   2) Calls 'findParameterFeatures(operation, paramName, paramIn)'
 *   3) Copies the example to pinfo.setSchemaExample(...)
 */
public class LLMParameterGenerator extends ParameterGenerator {

    private static final Logger logger = LogManager.getLogger(LLMParameterGenerator.class);

    private final AiDrivenLLMGenerator aiDriven;
    private final Random rand;

    public LLMParameterGenerator() {
        super();
        this.aiDriven = new AiDrivenLLMGenerator();
        this.rand = new Random();
    }

    @Override
    public JsonNode nextValue() {
        // 1) The parent's fallback chain (stateful_data.json, defaultValue, fuzz)
        JsonNode parentFallbackNode = super.nextValue();

        // 2) Build a ParameterInfo from the data we have
        ParameterInfo pinfo = buildParameterInfo();

        // 3) Ask the LLM for possible values
        List<String> llmValues = aiDriven.generateParameterValues(pinfo);

        // 4) Decide which value to return
        if (llmValues != null && !llmValues.isEmpty()) {
//            if (rand.nextFloat() < 0.8f)
                // e.g. 80% chance we pick an LLM-based value
                String chosen = llmValues.get(rand.nextInt(llmValues.size()));
                logger.debug("LLMParameterGenerator used LLM value for '{}' => {}", pinfo.getName(), chosen);
                return TextNode.valueOf(chosen);
//             else {
//                logger.debug("LLMParameterGenerator fallback from parent => {}", parentFallbackNode.asText());
//                return parentFallbackNode;
//            }
        } else {
            logger.debug("LLMParameterGenerator had no LLM output, fallback => {}", parentFallbackNode.asText());
            return parentFallbackNode;
        }
    }

    @Override
    public String nextValueAsString() {
        JsonNode node = nextValue();
        return (node != null) ? node.asText() : "";
    }

    /**
     * Build a ParameterInfo object with all the fields we can gather from your
     * ParameterGenerator plus the OAS parameter details (including 'example').
     */
    private ParameterInfo buildParameterInfo() {
        ParameterInfo pinfo = new ParameterInfo();

        // Use altParameterName if set, else the normal param name
        String finalParamName = (getAltParameterName() != null) ? getAltParameterName() : getParameterName();
        pinfo.setName(finalParamName);

        // "type" from the parent
        pinfo.setType(getParameterType());

        // We guess the paramIn. If your testConf.yaml has "in" somewhere,
        // you may store it in the parent. For now let's just guess "query".
        String paramIn = "query";  // or "path", "header", etc.
        // If your code knows the real paramIn, use that instead.

        // 1) Find the Operation from the OAS
        Operation openApiOp = findOperation(
                getSpec().getSpecification(), // The 'OpenAPI' object
                getOperationPath(),
                getOperationMethod().toLowerCase()
        );

        // 2) If we found the Operation, find the parameter features
        if (openApiOp != null && finalParamName != null) {
            // Adjust this call to match your actual method signature:
            // e.g. findParameterFeatures(Operation operation, String paramName, String paramIn)
            OpenAPIParameter paramObj = OpenAPISpecificationVisitor.findParameterFeatures(openApiOp, finalParamName, paramIn);

            if (paramObj != null) {
                pinfo.setInLocation(paramObj.getIn());
                pinfo.setFormat(paramObj.getFormat());
                pinfo.setRegex(paramObj.getPattern());
                pinfo.setDescription(paramObj.getDescription());

                // Because your parameter type is already known,
                // but let's store paramObj.getType() in schemaType
                pinfo.setSchemaType(paramObj.getType());

                // Here is the example we do NOT skip
                if (paramObj.getExample() != null) {
                    pinfo.setSchemaExample(paramObj.getExample().toString());
                }

                logger.debug("Found param in OAS => in: {}, type: {}, format: {}, pattern: {}, example: {}, description: {}",
                        paramObj.getIn(),
                        paramObj.getType(),
                        paramObj.getFormat(),
                        paramObj.getPattern(),
                        paramObj.getExample(),
                        paramObj.getDescription());
            } else {
                logger.warn("Could NOT find param '{}' with in='{}' in operation {} {}",
                        finalParamName, paramIn, getOperationMethod(), getOperationPath());
            }
        } else {
            logger.warn("No Operation found for path='{}', method='{}', or paramName null", getOperationPath(), getOperationMethod());
        }

        return pinfo;
    }

    /**
     * Helper method: find the correct OAS Operation from your OpenAPI spec
     * given the path + method. This avoids the "Required type: Operation" error,
     * because we produce an Operation object for 'findParameterFeatures'.
     */
    private Operation findOperation(io.swagger.v3.oas.models.OpenAPI openApi, String path, String method) {
        if (openApi == null || openApi.getPaths() == null || path == null) {
            return null;
        }
        io.swagger.v3.oas.models.PathItem pi = openApi.getPaths().get(path);
        if (pi == null) return null;

        switch (method.toLowerCase()) {
            case "get":    return pi.getGet();
            case "post":   return pi.getPost();
            case "put":    return pi.getPut();
            case "delete": return pi.getDelete();
            case "patch":  return pi.getPatch();
            default:       return null;
        }
    }
}
