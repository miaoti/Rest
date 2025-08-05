import java.util.regex.Pattern;

/**
 * Simple test to verify pattern matching for smart fetch discovery
 */
public class TestPatternMatching {
    
    public static void main(String[] args) {
        System.out.println("=== Pattern Matching Test ===");
        
        // Test parameters from the actual test cases
        String[] parameters = {
            "body", "distanceList", "distances", "endStation", "id", 
            "loginId", "startStation", "stationList", "stations"
        };
        
        // Patterns from the registry
        String[] patterns = {
            ".*[Ss]tation.*",
            ".*[Rr]oute.*|.*[Ii]d.*", 
            ".*[Oo]rder.*",
            ".*[Dd]istance.*",
            ".*[Pp]rice.*|.*[Cc]ost.*"
        };
        
        String[] patternNames = {
            "Station Pattern",
            "Route/ID Pattern", 
            "Order Pattern",
            "Distance Pattern",
            "Price/Cost Pattern"
        };
        
        for (String param : parameters) {
            System.out.println("\nParameter: " + param);
            boolean matched = false;
            
            for (int i = 0; i < patterns.length; i++) {
                if (Pattern.matches(patterns[i], param)) {
                    System.out.println("  ✅ Matches: " + patternNames[i] + " (" + patterns[i] + ")");
                    matched = true;
                } else {
                    System.out.println("  ❌ No match: " + patternNames[i] + " (" + patterns[i] + ")");
                }
            }
            
            if (!matched) {
                System.out.println("  ⚠️  NO PATTERNS MATCHED!");
            }
        }
        
        System.out.println("\n=== Expected Mappings ===");
        System.out.println("distanceList → Distance Pattern → route service");
        System.out.println("endStation → Station Pattern → station service");
        System.out.println("startStation → Station Pattern → station service");
        System.out.println("stations → Station Pattern → station service");
        System.out.println("stationList → Station Pattern → station service");
        System.out.println("id → Route/ID Pattern → route service");
        System.out.println("loginId → Route/ID Pattern → route service (or user service)");
    }
}
