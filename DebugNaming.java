import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Locale;

public class DebugNaming {
    public static void main(String[] args) {
        System.out.println("Debugging the improved test naming logic...");
        
        // Pattern to match HTTP operations in operation names
        Pattern HTTP_OPERATION_PATTERN = 
            Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        
        // Pattern to match service-prefixed operations
        Pattern SERVICE_PATTERN = 
            Pattern.compile(".*?\\s+(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        
        // Test different operation name formats that might be in the trace data
        String[] testOperations = {
            "POST /api/v1/adminrouteservice/adminroute",
            "GET /api/v1/stationservice/stations",
            "POST /api/v1/stationservice/stations/idlist",
            "login",  // This should be skipped
            "auth",   // This should be skipped
            "POST",   // Just method without path
            "GET",    // Just method without path
            "ts-admin-route-service POST /api/v1/adminrouteservice/adminroute",  // Service prefix
            "ts-station-service GET /api/v1/stationservice/stations"  // Service prefix
        };
        
        for (String opName : testOperations) {
            System.out.println("\nTesting operation: '" + opName + "'");
            
            // Check if it's an HTTP operation pattern
            Matcher httpMatcher = HTTP_OPERATION_PATTERN.matcher(opName);
            if (httpMatcher.matches()) {
                String verb = httpMatcher.group(1).toLowerCase(Locale.ROOT);
                String route = httpMatcher.group(2);
                System.out.println("  ✓ Matches HTTP pattern: " + verb + " " + route);
                
                // Check if it's login/auth related
                String opLower = opName.toLowerCase();
                if (opLower.contains("login") || opLower.contains("auth") || 
                    opLower.contains("signin") || opLower.contains("token")) {
                    System.out.println("  ✗ Skipping login/auth operation");
                } else {
                    String descriptiveName = verb.toUpperCase() + "_" + route.replaceAll("[^a-zA-Z0-9_]", "_");
                    System.out.println("  ✓ Business API operation: " + descriptiveName);
                }
            } else {
                // Try service-prefixed pattern
                Matcher serviceMatcher = SERVICE_PATTERN.matcher(opName);
                if (serviceMatcher.matches()) {
                    String verb = serviceMatcher.group(1).toLowerCase(Locale.ROOT);
                    String route = serviceMatcher.group(2);
                    System.out.println("  ✓ Matches service pattern: " + verb + " " + route);
                    
                    String descriptiveName = verb.toUpperCase() + "_" + route.replaceAll("[^a-zA-Z0-9_]", "_");
                    System.out.println("  ✓ Business API operation: " + descriptiveName);
                } else {
                    System.out.println("  ✗ Does not match any pattern");
                    
                    // Check if it's just a method name
                    if (opName.matches("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)$")) {
                        System.out.println("  ✓ Just HTTP method: " + opName);
                        // Use service name as route for method-only operations
                        String serviceName = "ts-service"; // Example service name
                        String descriptiveName = opName.toUpperCase() + "_" + serviceName.replaceAll("[^a-zA-Z0-9_]", "_");
                        System.out.println("  ✓ Constructed name: " + descriptiveName);
                    } else if (opName.toLowerCase().contains("login") || opName.toLowerCase().contains("auth")) {
                        System.out.println("  ✗ Login/auth operation - should be skipped");
                    } else {
                        System.out.println("  ? Unknown operation format");
                    }
                }
            }
        }
        
        System.out.println("\n=== Test completed ===");
    }
} 