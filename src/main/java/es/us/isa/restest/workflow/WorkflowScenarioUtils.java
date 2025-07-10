package es.us.isa.restest.workflow;

import java.util.*;
import java.util.regex.*;

/** Utility methods for workflow scenarios. */
public class WorkflowScenarioUtils {

    private static final Pattern HTTP_PATTERN =
        Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    /**
     * Deduplicate scenarios based on the sequence of HTTP method and path
     * observed in each scenario.
     */
    public static List<WorkflowScenario> deduplicateBySteps(List<WorkflowScenario> scenarios) {
        Map<String, WorkflowScenario> unique = new LinkedHashMap<>();
        for (WorkflowScenario sc : scenarios) {
            String signature = buildSignature(sc);
            unique.putIfAbsent(signature, sc);
        }
        return new ArrayList<>(unique.values());
    }

    // Build a unique signature string from ordered steps
    private static String buildSignature(WorkflowScenario sc) {
        StringBuilder sb = new StringBuilder();
        for (WorkflowStep step : flatten(sc)) {
            String verb = httpMethod(step);
            String path = httpPath(step);
            sb.append(verb).append(' ').append(path).append("->");
        }
        return sb.toString();
    }

    // Depth-first pre-order traversal
    private static List<WorkflowStep> flatten(WorkflowScenario sc) {
        List<WorkflowStep> list = new ArrayList<>();
        for (WorkflowStep root : sc.getRootSteps()) dfs(root, list);
        return list;
    }
    private static void dfs(WorkflowStep s, List<WorkflowStep> out) {
        out.add(s);
        for (WorkflowStep c : s.getChildren()) dfs(c, out);
    }

    private static String httpMethod(WorkflowStep step) {
        String verb = step.getOutputFields().get("http.method");
        if (verb == null) {
            Matcher m = HTTP_PATTERN.matcher(step.getOperationName());
            if (m.matches()) verb = m.group(1);
        }
        return verb != null ? verb.toUpperCase(Locale.ROOT) : step.getOperationName();
    }

    private static String httpPath(WorkflowStep step) {
        String path = step.getOutputFields().get("http.target");
        if (path == null) path = step.getOutputFields().get("http.url");
        if (path != null) {
            int q = path.indexOf('?');
            if (q >= 0) path = path.substring(0, q);
        }
        if (path == null) {
            Matcher m = HTTP_PATTERN.matcher(step.getOperationName());
            if (m.matches()) path = m.group(2);
        }
        return path != null ? path : step.getOperationName();
    }
}
