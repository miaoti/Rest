package es.us.isa.restest.registry;

import org.json.JSONArray;
import org.json.JSONObject;
import es.us.isa.restest.workflow.WorkflowStep;

/**
 * Represents a specific tree structure (execution pattern) for a root API.
 * Each tree captures the hierarchical microservice interaction pattern
 * observed from a particular trace execution.
 * 
 * The tree structure shows:
 * - Which services are called
 * - In what hierarchical order
 * - The API operations invoked
 * 
 * This class focuses on the structural pattern rather than specific data values,
 * making it useful for understanding microservice interaction patterns.
 */
public class ApiTree {
    private final String treeId;
    private final String sourceTrace;
    private final TreeNode root;
    private final String structureHash;
    
    /**
     * Creates a new ApiTree from a WorkflowStep tree.
     */
    public ApiTree(String treeId, String sourceTrace, WorkflowStep rootStep, String structureHash) {
        this.treeId = treeId;
        this.sourceTrace = sourceTrace;
        this.structureHash = structureHash;
        this.root = convertWorkflowStepToTreeNode(rootStep);
    }
    
    /**
     * Private constructor for deserialization.
     */
    private ApiTree(String treeId, String sourceTrace, TreeNode root, String structureHash) {
        this.treeId = treeId;
        this.sourceTrace = sourceTrace;
        this.root = root;
        this.structureHash = structureHash;
    }
    
    /**
     * Converts a WorkflowStep tree to a simplified TreeNode structure.
     * This focuses on the API call patterns rather than specific execution data.
     */
    private TreeNode convertWorkflowStepToTreeNode(WorkflowStep step) {
        TreeNode node = new TreeNode(
            step.getServiceName(),
            step.getOperationName(),
            extractHttpMethod(step),
            extractApiPath(step)
        );
        
        // Convert children
        for (WorkflowStep child : step.getChildren()) {
            node.addChild(convertWorkflowStepToTreeNode(child));
        }
        
        return node;
    }
    
    /**
     * Extracts HTTP method from a workflow step.
     */
    private String extractHttpMethod(WorkflowStep step) {
        return step.getInputFields().getOrDefault("http.method", "UNKNOWN");
    }
    
    /**
     * Extracts API path from a workflow step.
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
                // Fallback to operation name
            }
        }
        
        return step.getOperationName();
    }
    
    /**
     * Converts this tree to JSON format.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("tree_id", treeId);
        json.put("source_trace", sourceTrace);
        json.put("structure_hash", structureHash);
        json.put("root", root.toJson());
        return json;
    }
    
    /**
     * Creates an ApiTree from JSON data.
     */
    public static ApiTree fromJson(JSONObject json) {
        String treeId = json.optString("tree_id", "");
        String sourceTrace = json.optString("source_trace", "");
        String structureHash = json.optString("structure_hash", "");
        TreeNode root = TreeNode.fromJson(json.optJSONObject("root"));
        
        return new ApiTree(treeId, sourceTrace, root, structureHash);
    }
    
    // Getters
    public String getTreeId() { return treeId; }
    public String getSourceTrace() { return sourceTrace; }
    public TreeNode getRoot() { return root; }
    public String getStructureHash() { return structureHash; }
    
    @Override
    public String toString() {
        return String.format("ApiTree{id='%s', source='%s', root=%s}", 
                           treeId, sourceTrace, root.getServiceName() + ":" + root.getOperation());
    }
    
    /**
     * Simple tree node representing a microservice API call in the interaction tree.
     */
    public static class TreeNode {
        private final String serviceName;
        private final String operation;
        private final String httpMethod;
        private final String apiPath;
        private final java.util.List<TreeNode> children = new java.util.ArrayList<>();
        
        public TreeNode(String serviceName, String operation, String httpMethod, String apiPath) {
            this.serviceName = serviceName;
            this.operation = operation;
            this.httpMethod = httpMethod;
            this.apiPath = apiPath;
        }
        
        public void addChild(TreeNode child) {
            children.add(child);
        }
        
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("service", serviceName);
            json.put("operation", operation);
            json.put("http_method", httpMethod);
            json.put("api_path", apiPath);
            
            if (!children.isEmpty()) {
                JSONArray childrenArray = new JSONArray();
                for (TreeNode child : children) {
                    childrenArray.put(child.toJson());
                }
                json.put("children", childrenArray);
            }
            
            return json;
        }
        
        public static TreeNode fromJson(JSONObject json) {
            if (json == null) return null;
            
            String serviceName = json.optString("service", "unknown");
            String operation = json.optString("operation", "unknown");
            String httpMethod = json.optString("http_method", "UNKNOWN");
            String apiPath = json.optString("api_path", "");
            
            TreeNode node = new TreeNode(serviceName, operation, httpMethod, apiPath);
            
            if (json.has("children")) {
                JSONArray childrenArray = json.getJSONArray("children");
                for (int i = 0; i < childrenArray.length(); i++) {
                    TreeNode child = fromJson(childrenArray.getJSONObject(i));
                    if (child != null) {
                        node.addChild(child);
                    }
                }
            }
            
            return node;
        }
        
        // Getters
        public String getServiceName() { return serviceName; }
        public String getOperation() { return operation; }
        public String getHttpMethod() { return httpMethod; }
        public String getApiPath() { return apiPath; }
        public java.util.List<TreeNode> getChildren() { 
            return java.util.Collections.unmodifiableList(children); 
        }
        
        @Override
        public String toString() {
            return String.format("TreeNode{service='%s', operation='%s', children=%d}", 
                               serviceName, operation, children.size());
        }
    }
}
