package es.us.isa.restest.workflow;

import java.util.*;
import java.util.regex.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Utility methods for workflow scenarios. */
public class WorkflowScenarioUtils {
    private static final Logger log = LogManager.getLogger(WorkflowScenarioUtils.class);

    private static final Pattern HTTP_PATTERN =
        Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    /**
     * Deduplicate scenarios based on the ROOT API ONLY (first business API call).
     * Different workflow patterns (success vs failure) with the same root API
     * are considered duplicates for test generation, but all are registered
     * to the Root API Registry for learning purposes.
     */
    public static List<WorkflowScenario> deduplicateBySteps(List<WorkflowScenario> scenarios) {
        if (scenarios == null || scenarios.isEmpty()) {
            log.info("No scenarios to deduplicate");
            return new ArrayList<>();
        }

        log.info("=== SCENARIO DEDUPLICATION ANALYSIS ===");
        log.info("Starting with {} scenarios from traces", scenarios.size());
        log.info("Deduplication strategy: Root API only (ignoring downstream workflow differences)");
        
        Map<String, ScenarioGroup> signatureGroups = new LinkedHashMap<>();
        
        // Group scenarios by their ROOT API signature only
        for (int i = 0; i < scenarios.size(); i++) {
            WorkflowScenario scenario = scenarios.get(i);
            String rootSignature = buildRootApiSignature(scenario);
            String fullSignature = buildSignature(scenario);
            
            log.info("Scenario {} (from traces: {}): root API = {}", 
                     i + 1, scenario.getTraceIds(), rootSignature);
            log.debug("  Full workflow: {}", fullSignature);
            
            ScenarioGroup group = signatureGroups.computeIfAbsent(rootSignature, 
                k -> new ScenarioGroup(rootSignature));
            group.addScenario(scenario, i + 1);
        }
        
        // Analyze duplicates and select representatives
        List<WorkflowScenario> uniqueScenarios = new ArrayList<>();
        int totalDuplicatesEliminated = 0;
        
        log.info("\n=== DEDUPLICATION RESULTS ===");
        for (ScenarioGroup group : signatureGroups.values()) {
            WorkflowScenario representative = group.getRepresentative();
            uniqueScenarios.add(representative);
            
            if (group.size() == 1) {
                log.info("✓ UNIQUE: Scenario {} will generate tests (no duplicates for root API: {})", 
                         group.getRepresentativeIndex(), group.getSignature());
            } else {
                int duplicatesCount = group.size() - 1;
                totalDuplicatesEliminated += duplicatesCount;
                
                log.info("✓ KEPT: Scenario {} as representative for {} scenarios with same root API", 
                         group.getRepresentativeIndex(), group.size());
                log.info("  → Root API: {}", group.getSignature());
                log.info("  → Representative workflow: {}", formatStepsForLogging(representative));
                log.info("  → Combined traces: {}", representative.getTraceIds());
                
                StringBuilder duplicateInfo = new StringBuilder();
                duplicateInfo.append("✗ ELIMINATED: Scenarios ");
                group.getDuplicateIndices().stream()
                     .forEach(idx -> duplicateInfo.append(idx).append(" "));
                duplicateInfo.append("(same root API, different downstream workflows)");
                log.info(duplicateInfo.toString());
                
                // Show trace details for eliminated scenarios
                for (Integer duplicateIdx : group.getDuplicateIndices()) {
                    WorkflowScenario duplicate = group.getScenarioByIndex(duplicateIdx);
                    log.info("    Scenario {} traces: {} - workflow: {}", 
                             duplicateIdx, duplicate.getTraceIds(), formatStepsForLogging(duplicate));
                }
            }
        }
        
        log.info("\n=== FINAL SUMMARY ===");
        log.info("Original scenarios: {}", scenarios.size());
        log.info("Unique scenarios for test generation: {}", uniqueScenarios.size());
        log.info("Duplicate scenarios eliminated: {}", totalDuplicatesEliminated);
        log.info("Resource savings: {:.1f}% fewer test cases to generate", 
                 (double) totalDuplicatesEliminated / scenarios.size() * 100);
        
        return uniqueScenarios;
    }

    // Helper class to track scenario groups with detailed information
    private static class ScenarioGroup {
        private final String signature;
        private final List<WorkflowScenario> scenarios = new ArrayList<>();
        private final List<Integer> originalIndices = new ArrayList<>();
        
        public ScenarioGroup(String signature) {
            this.signature = signature;
        }
        
        public void addScenario(WorkflowScenario scenario, int originalIndex) {
            scenarios.add(scenario);
            originalIndices.add(originalIndex);
        }
        
        public WorkflowScenario getRepresentative() {
            return scenarios.get(0); // First scenario in the group
        }
        
        public int getRepresentativeIndex() {
            return originalIndices.get(0);
        }
        
        public List<Integer> getDuplicateIndices() {
            return originalIndices.subList(1, originalIndices.size());
        }
        
        public WorkflowScenario getScenarioByIndex(int originalIndex) {
            int position = originalIndices.indexOf(originalIndex);
            return position >= 0 ? scenarios.get(position) : null;
        }
        
        public int size() {
            return scenarios.size();
        }
        
        public String getSignature() {
            return signature;
        }
    }

    // Build a signature from ROOT API only (first step)
    private static String buildRootApiSignature(WorkflowScenario sc) {
        List<WorkflowStep> steps = flatten(sc);
        if (steps.isEmpty()) {
            return "EMPTY_SCENARIO";
        }
        
        // Get the first step (root API)
        WorkflowStep rootStep = steps.get(0);
        String verb = httpMethod(rootStep);
        String path = httpPath(rootStep);
        
        return verb + " " + path;
    }
    
    // Build a unique signature string from ordered steps (full workflow)
    private static String buildSignature(WorkflowScenario sc) {
        StringBuilder sb = new StringBuilder();
        List<WorkflowStep> steps = flatten(sc);
        
        for (int i = 0; i < steps.size(); i++) {
            WorkflowStep step = steps.get(i);
            String verb = httpMethod(step);
            String path = httpPath(step);
            
            if (i > 0) sb.append(" → ");
            sb.append(verb).append(" ").append(path);
        }
        
        return sb.toString();
    }

    // Format steps for readable logging
    private static String formatStepsForLogging(WorkflowScenario sc) {
        StringBuilder sb = new StringBuilder();
        List<WorkflowStep> steps = flatten(sc);
        
        for (int i = 0; i < steps.size(); i++) {
            WorkflowStep step = steps.get(i);
            String verb = httpMethod(step);
            String path = httpPath(step);
            String service = step.getServiceName();
            
            if (i > 0) sb.append(" → ");
            sb.append(String.format("%s[%s %s]", service, verb, path));
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
