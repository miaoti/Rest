package es.us.isa.restest.registry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import es.us.isa.restest.workflow.WorkflowStep;

/**
 * Represents a single Root API endpoint entry in the registry.
 * Each entry contains the API metadata (method, path, service) and multiple
 * tree structures representing different execution patterns observed from traces.
 * 
 * This class is thread-safe and supports multiple tree patterns for the same root API,
 * which is common in microservice systems where the same endpoint can trigger
 * different service interaction patterns based on input parameters or system state.
 */
public class RootApiEntry {
    private final String method;
    private final String path;
    private final String serviceName;
    private final String operationName;
    private final Map<String, ApiTree> trees = new ConcurrentHashMap<>();
    
    /**
     * Creates a new RootApiEntry from a WorkflowStep representing the root API.
     */
    public RootApiEntry(WorkflowStep rootStep) {
        this.method = rootStep.getInputFields().getOrDefault("http.method", "UNKNOWN");
        this.path = extractApiPath(rootStep);
        this.serviceName = rootStep.getServiceName();
        this.operationName = rootStep.getOperationName();
    }
    
    /**
     * Private constructor for deserialization from JSON.
     */
    private RootApiEntry(String method, String path, String serviceName, String operationName) {
        this.method = method;
        this.path = path;
        this.serviceName = serviceName;
        this.operationName = operationName;
    }
    
    /**
     * Adds a new tree structure for this root API if it doesn't already exist.
     * @param treeId unique identifier for this tree pattern
     * @param sourceTrace name of the trace file this tree came from
     * @param rootStep the root step with its complete tree structure
     * @return true if a new tree was added, false if equivalent tree already exists
     */
    public boolean addTree(String treeId, String sourceTrace, WorkflowStep rootStep) {
        // Create tree structure hash to detect duplicates
        String treeStructureHash = generateTreeStructureHash(rootStep);
        
        // Check if we already have a tree with the same structure
        for (ApiTree existingTree : trees.values()) {
            if (existingTree.getStructureHash().equals(treeStructureHash)) {
                return false; // Duplicate tree structure found
            }
        }
        
        // Add new tree
        ApiTree newTree = new ApiTree(treeId, sourceTrace, rootStep, treeStructureHash);
        trees.put(treeId, newTree);
        return true;
    }
    
    /**
     * Generates a hash representing the tree structure to detect duplicates.
     * This hash is based on the service calls and their hierarchical relationships,
     * not on specific data values which may vary between executions.
     */
    private String generateTreeStructureHash(WorkflowStep step) {
        StringBuilder hash = new StringBuilder();
        appendStepToHash(step, hash, 0);
        return Integer.toString(hash.toString().hashCode());
    }
    
    /**
     * Recursively appends step information to the structure hash.
     */
    private void appendStepToHash(WorkflowStep step, StringBuilder hash, int depth) {
        // Add depth prefix for hierarchy
        hash.append("D").append(depth).append(":");
        
        // Add service and operation (normalized)
        hash.append(step.getServiceName()).append(":");
        hash.append(normalizeOperation(step.getOperationName())).append(";");
        
        // Recursively add children in a consistent order
        List<WorkflowStep> sortedChildren = new ArrayList<>(step.getChildren());
        sortedChildren.sort((a, b) -> {
            int serviceCompare = a.getServiceName().compareTo(b.getServiceName());
            if (serviceCompare != 0) return serviceCompare;
            return normalizeOperation(a.getOperationName()).compareTo(normalizeOperation(b.getOperationName()));
        });
        
        for (WorkflowStep child : sortedChildren) {
            appendStepToHash(child, hash, depth + 1);
        }
    }
    
    /**
     * Normalizes operation names to focus on the API pattern rather than specific values.
     */
    private String normalizeOperation(String operation) {
        if (operation == null) return "unknown";
        
        // Remove specific IDs and parameters to focus on the API pattern
        String normalized = operation.replaceAll("/\\d+", "/{id}")  // Replace numeric IDs
                                    .replaceAll("\\?.*", "");         // Remove query parameters
        
        return normalized;
    }
    
    /**
     * Extracts the API path from the root step.
     */
    private String extractApiPath(WorkflowStep step) {
        String target = step.getInputFields().get("http.target");
        if (target != null && !target.isEmpty()) {
            int queryIndex = target.indexOf('?');
            return queryIndex > 0 ? target.substring(0, queryIndex) : target;
        }
        
        String url = step.getInputFields().get("http.url");
        if (url != null && !url.isEmpty()) {
            try {
                java.net.URI parsedUri = java.net.URI.create(url);
                String path = parsedUri.getPath();
                return path != null && !path.isEmpty() ? path : "/";
            } catch (Exception e) {
                // Fallback to operation name if URL parsing fails
            }
        }
        
        return step.getOperationName();
    }
    
    /**
     * Converts this entry to JSON format for storage.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("method", method);
        json.put("path", path);
        json.put("service", serviceName);
        json.put("operation", operationName);
        
        JSONArray treesArray = new JSONArray();
        for (ApiTree tree : trees.values()) {
            treesArray.put(tree.toJson());
        }
        json.put("trees", treesArray);
        
        return json;
    }
    
    /**
     * Creates a RootApiEntry from JSON data.
     */
    public static RootApiEntry fromJson(JSONObject json) {
        String method = json.optString("method", "UNKNOWN");
        String path = json.optString("path", "");
        String serviceName = json.optString("service", "unknown");
        String operationName = json.optString("operation", "unknown");
        
        RootApiEntry entry = new RootApiEntry(method, path, serviceName, operationName);
        
        if (json.has("trees")) {
            JSONArray treesArray = json.getJSONArray("trees");
            for (int i = 0; i < treesArray.length(); i++) {
                JSONObject treeJson = treesArray.getJSONObject(i);
                ApiTree tree = ApiTree.fromJson(treeJson);
                entry.trees.put(tree.getTreeId(), tree);
            }
        }
        
        return entry;
    }
    
    // Getters
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getServiceName() { return serviceName; }
    public String getOperationName() { return operationName; }
    public Collection<ApiTree> getTrees() { return Collections.unmodifiableCollection(trees.values()); }
    public ApiTree getTree(String treeId) { return trees.get(treeId); }
    
    @Override
    public String toString() {
        return String.format("RootApiEntry{method='%s', path='%s', service='%s', trees=%d}", 
                           method, path, serviceName, trees.size());
    }
}
