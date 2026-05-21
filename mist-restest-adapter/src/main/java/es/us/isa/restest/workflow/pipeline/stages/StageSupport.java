package es.us.isa.restest.workflow.pipeline.stages;

import es.us.isa.restest.configuration.pojos.Operation;
import es.us.isa.restest.configuration.pojos.TestConfigurationObject;
import es.us.isa.restest.configuration.pojos.TestParameter;
import io.mist.core.llm.ParameterInfo;
import io.mist.core.workflow.WorkflowScenario;
import io.mist.core.workflow.WorkflowStep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Package-private collection of pure helpers shared across the pipeline stages.
 *
 * <p>The methods were previously {@code private} instance methods on
 * {@code MultiServiceTestCaseGenerator}; lifting them here as {@code static}
 * lets the stage classes call them without holding a generator reference and
 * keeps the helpers reusable across stages without duplication.
 *
 * <p>All methods are pure functions of their parameters — no field state is
 * threaded through this class.
 */
public final class StageSupport {
    private static final Logger log = LogManager.getLogger(StageSupport.class);

    /** Matches operation names of the form {@code "VERB /path"}. */
    static final Pattern HTTP_OPERATION_PATTERN =
            Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$",
                    Pattern.CASE_INSENSITIVE);

    private StageSupport() {}

    /**
     * Extract root API key (method_path) from a scenario's first business operation.
     */
    static String getRootApiKey(WorkflowScenario scenario) {
        for (WorkflowStep rootStep : scenario.getRootSteps()) {
            String apiKey = extractRootApiFromStep(rootStep);
            if (apiKey != null) {
                return apiKey;
            }
        }
        return null;
    }

    /**
     * Recursively find the first business API operation and return method_path key.
     */
    static String extractRootApiFromStep(WorkflowStep step) {
        String opName = step.getOperationName();
        String serviceName = step.getServiceName();

        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();

            boolean isLoginAuth = opLower.contains("login") || opLower.contains("auth") ||
                                  serviceLower.contains("login") || serviceLower.contains("auth") ||
                                  opLower.contains("signin") || opLower.contains("token");

            boolean isGateway = serviceLower.contains("gateway") ||
                               opName.equals("POST /*") || opName.equals("GET /*") ||
                               opName.equals("PUT /*") || opName.equals("DELETE /*");

            if (!isLoginAuth && !isGateway) {
                String verb = null, route = null;

                Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
                if (httpMatcher.matches()) {
                    verb = httpMatcher.group(1).toLowerCase();
                    route = httpMatcher.group(2);
                } else {
                    Pattern servicePattern = Pattern.compile(".*?\\s+(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
                    Matcher serviceMatcher = servicePattern.matcher(opName);
                    if (serviceMatcher.matches()) {
                        verb = serviceMatcher.group(1).toLowerCase();
                        route = serviceMatcher.group(2);
                    } else {
                        Map<String, String> outputs = step.getOutputFields();
                        String httpMethod = outputs.get("http.method");
                        String httpTarget = outputs.get("http.target");
                        String httpUrl = outputs.get("http.url");

                        if (httpMethod != null && (httpTarget != null || httpUrl != null)) {
                            verb = httpMethod.toLowerCase();
                            route = httpTarget != null ? httpTarget : extractPathFromUrl(httpUrl);
                        }
                    }
                }

                if (verb != null && route != null) {
                    String normalizedRoute = route.replaceAll("[^a-zA-Z0-9_]", "_");
                    return verb.toUpperCase() + "_" + normalizedRoute;
                }
            }
        }

        for (WorkflowStep child : step.getChildren()) {
            String apiKey = extractRootApiFromStep(child);
            if (apiKey != null) {
                return apiKey;
            }
        }

        return null;
    }

    /** Extract the URL path component for the {@code http.url} fallback. */
    static String extractPathFromUrl(String url) {
        if (url == null) return null;
        try {
            java.net.URL parsed = new java.net.URL(url);
            return parsed.getPath();
        } catch (Exception e) {
            int pathStart = url.indexOf("://");
            if (pathStart >= 0) {
                int pathBegin = url.indexOf("/", pathStart + 3);
                if (pathBegin >= 0) {
                    int queryStart = url.indexOf("?", pathBegin);
                    return queryStart >= 0 ? url.substring(pathBegin, queryStart) : url.substring(pathBegin);
                }
            }
            return url;
        }
    }

    /** Find the first business step (non-login) in a scenario. */
    static WorkflowStep findFirstBusinessStep(WorkflowScenario scenario) {
        for (WorkflowStep rootStep : scenario.getRootSteps()) {
            WorkflowStep businessStep = findFirstBusinessStepRecursive(rootStep);
            if (businessStep != null) {
                return businessStep;
            }
        }
        return null;
    }

    /** Recursively find the first business step. */
    static WorkflowStep findFirstBusinessStepRecursive(WorkflowStep step) {
        String opName = step.getOperationName();
        String serviceName = step.getServiceName();

        if (opName != null && serviceName != null) {
            String opLower = opName.toLowerCase();
            String serviceLower = serviceName.toLowerCase();

            boolean isLoginAuth = opLower.contains("login") || opLower.contains("auth") ||
                                  serviceLower.contains("login") || serviceLower.contains("auth") ||
                                  opLower.contains("signin") || opLower.contains("token");

            boolean isGateway = serviceLower.contains("gateway") ||
                               opName.equals("POST /*") || opName.equals("GET /*") ||
                               opName.equals("PUT /*") || opName.equals("DELETE /*");

            if (!isLoginAuth && !isGateway) {
                return step;
            }
        }

        for (WorkflowStep child : step.getChildren()) {
            WorkflowStep businessStep = findFirstBusinessStepRecursive(child);
            if (businessStep != null) {
                return businessStep;
            }
        }

        return null;
    }

    /** Locate the corresponding Operation object by method + path. */
    static Operation findOperation(TestConfigurationObject cfg, String verb, String path) {
        if (cfg.getTestConfiguration() == null ||
                cfg.getTestConfiguration().getOperations() == null) {
            return null;
        }

        Operation exact = cfg.getTestConfiguration().getOperations().stream()
                .filter(o -> verb.equalsIgnoreCase(o.getMethod()) &&
                        path.equals(o.getTestPath()))
                .findFirst().orElse(null);
        if (exact != null) return exact;

        // Path-template tolerant fallback: a trace step may carry a literal
        // path like /admintravel/G1235 while the OpenAPI testPath is the
        // templated /admintravel/{tripId}. Match each {param} segment against
        // [^/]+ so pool generation succeeds for path-parameterised endpoints.
        return cfg.getTestConfiguration().getOperations().stream()
                .filter(o -> verb.equalsIgnoreCase(o.getMethod()) &&
                        pathMatchesTemplate(path, o.getTestPath()))
                .findFirst().orElse(null);
    }

    private static boolean pathMatchesTemplate(String literal, String template) {
        if (template == null || literal == null) return false;
        if (!template.contains("{")) return false;
        String regex = template.replaceAll("\\{[^/]+\\}", "[^/]+");
        return literal.matches(regex);
    }

    /** Build a {@link ParameterInfo} with extended context for richer LLM prompts. */
    static ParameterInfo createParameterInfoWithContext(TestParameter p, String apiName,
                                                       String serviceName, List<String> allParamNames) {
        ParameterInfo info = createParameterInfo(p);
        info.setApiName(apiName);
        info.setServiceName(serviceName);
        info.setAllParameterNames(allParamNames);
        return info;
    }

    private static ParameterInfo createParameterInfo(TestParameter p) {
        ParameterInfo info = new ParameterInfo();
        info.setName(p.getName());
        info.setDescription(p.getDescription());
        info.setInLocation(p.getIn());
        info.setType(p.getType());
        info.setFormat(p.getFormat());
        info.setSchemaType(p.getType());
        info.setSchemaExample(p.getExample() != null ? p.getExample().toString() : "");
        info.setRegex(p.getPattern());
        info.setRequired(p.getRequired());
        info.setEnumValues(p.getEnumValues());
        info.setMinimum(p.getMinimum());
        info.setMaximum(p.getMaximum());
        info.setMinLength(p.getMinLength());
        info.setMaxLength(p.getMaxLength());
        return info;
    }

    /** Read variant count from properties file with fallback to defaults. */
    static int getVariantCountFromProperties() {
        try {
            String testsProp = System.getProperty("testsperoperation");
            if (testsProp != null) {
                int count = Integer.parseInt(testsProp);
                log.info("Using testsperoperation from properties: {}", count);
                return count;
            }

            String variantsProp = System.getProperty("test.variants.per.scenario");
            if (variantsProp != null) {
                int count = Integer.parseInt(variantsProp);
                log.info("Using test.variants.per.scenario from properties: {}", count);
                return count;
            }

            int defaultCount = 1;
            log.warn("No variant count found in properties, using default: {}", defaultCount);
            return defaultCount;
        } catch (NumberFormatException e) {
            int defaultCount = 1;
            log.warn("Invalid variant count in properties, using default: {} (error: {})", defaultCount, e.getMessage());
            return defaultCount;
        }
    }

    /** Pool sizing heuristic — scales with parameter cardinality. */
    static int computeTargetPoolSize(int numParams, int variantCount) {
        if (numParams <= 1) {
            return variantCount + 10;
        } else if (numParams == 2) {
            return Math.max(25, (int) Math.sqrt(variantCount) * 2);
        } else if (numParams <= 5) {
            return Math.max(20, (int) Math.ceil(Math.pow(variantCount, 1.0 / numParams)) + 5);
        } else {
            return 15;
        }
    }

    /** Type-aware fallback padding value, parseable as the parameter's declared type. */
    static String typeAwareFallbackValue(TestParameter p, int idx) {
        String name = p != null && p.getName() != null ? p.getName() : "param";
        String type = p != null && p.getType() != null
                ? p.getType().toLowerCase(java.util.Locale.ROOT) : "string";
        switch (type) {
            case "integer":
            case "int":
            case "int32":
            case "int64":
            case "long":
                return Integer.toString(idx);
            case "number":
            case "double":
            case "float":
                return idx + ".0";
            case "boolean":
            case "bool":
                return (idx % 2 == 0) ? "false" : "true";
            case "array":
                return "[]";
            case "object":
                return "{}";
            case "string":
            default:
                return "FALLBACK_" + name + "_" + idx;
        }
    }

    /** Normalise an OpenAPI parameter location to one of path|query|header|cookie|body|other. */
    public static String normaliseParamLocation(String in) {
        if (in == null || in.trim().isEmpty()) return "body";
        String lower = in.trim().toLowerCase(Locale.ROOT);
        switch (lower) {
            case "path":
            case "query":
            case "header":
            case "cookie":
            case "body":
                return lower;
            case "formdata":
                return "body";
            default:
                return "other";
        }
    }
}
