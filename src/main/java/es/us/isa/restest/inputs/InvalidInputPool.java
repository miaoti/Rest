package es.us.isa.restest.inputs;

import java.util.*;

/**
 * Manages invalid input values for a parameter, tracking which values have been used
 * in round-robin mode to ensure variety in negative testing.
 */
public class InvalidInputPool {
    
    private final String parameterName;
    private final String parameterType;
    
    // Map of InvalidInputType -> List of values for that type
    private final Map<InvalidInputType, List<Object>> valuesByType;
    
    // Track which values have been used (for round-robin mode)
    private final Map<InvalidInputType, Set<Integer>> usedIndicesByType;
    
    // Current index for round-robin selection across all types
    private int currentTypeIndex = 0;
    private final List<InvalidInputType> typeRotation;
    
    public InvalidInputPool(String parameterName, String parameterType) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
        this.valuesByType = new EnumMap<>(InvalidInputType.class);
        this.usedIndicesByType = new EnumMap<>(InvalidInputType.class);
        
        // Initialize type rotation order
        this.typeRotation = new ArrayList<>(Arrays.asList(InvalidInputType.values()));
        
        // Initialize maps for each type
        for (InvalidInputType type : InvalidInputType.values()) {
            valuesByType.put(type, new ArrayList<>());
            usedIndicesByType.put(type, new HashSet<>());
        }
    }
    
    /**
     * Add invalid value for a specific type
     */
    public void addValue(InvalidInputType type, Object value) {
        valuesByType.get(type).add(value);
    }
    
    // Track which type the last value came from (for logging)
    private InvalidInputType lastSelectedType = null;
    
    /**
     * Get the next unused invalid input in round-robin fashion
     * Rotates through types, then through values within each type
     * Returns null when all values have been used
     */
    public Object getNextRoundRobin() {
        int typesChecked = 0;
        
        // Try each type in rotation
        while (typesChecked < typeRotation.size()) {
            InvalidInputType currentType = typeRotation.get(currentTypeIndex);
            List<Object> values = valuesByType.get(currentType);
            Set<Integer> usedIndices = usedIndicesByType.get(currentType);
            
            // Find an unused value in this type
            for (int i = 0; i < values.size(); i++) {
                if (!usedIndices.contains(i)) {
                    usedIndices.add(i);
                    Object value = values.get(i);
                    
                    // Track which type this value came from
                    lastSelectedType = currentType;
                    
                    // Move to next type for next call
                    currentTypeIndex = (currentTypeIndex + 1) % typeRotation.size();
                    
                    return value;
                }
            }
            
            // All values in this type used, try next type
            currentTypeIndex = (currentTypeIndex + 1) % typeRotation.size();
            typesChecked++;
        }
        
        // All values exhausted
        lastSelectedType = null;
        return null;
    }
    
    /**
     * Get the invalid type of the last selected value (for logging)
     */
    public InvalidInputType getLastSelectedType() {
        return lastSelectedType;
    }
    
    /**
     * Get a random invalid input (can repeat)
     */
    public Object getRandomValue(Random random) {
        // Select random type that has values
        List<InvalidInputType> availableTypes = new ArrayList<>();
        for (InvalidInputType type : InvalidInputType.values()) {
            if (!valuesByType.get(type).isEmpty()) {
                availableTypes.add(type);
            }
        }
        
        if (availableTypes.isEmpty()) {
            return null;
        }
        
        // Pick random type
        InvalidInputType randomType = availableTypes.get(random.nextInt(availableTypes.size()));
        List<Object> values = valuesByType.get(randomType);
        
        // Pick random value from that type
        return values.get(random.nextInt(values.size()));
    }
    
    /**
     * Reset usage tracking (for new test run)
     */
    public void resetUsage() {
        for (Set<Integer> indices : usedIndicesByType.values()) {
            indices.clear();
        }
        currentTypeIndex = 0;
    }
    
    /**
     * Get total number of invalid values across all types
     */
    public int getTotalCount() {
        int count = 0;
        for (List<Object> values : valuesByType.values()) {
            count += values.size();
        }
        return count;
    }
    
    /**
     * Get count of values for a specific type
     */
    public int getCountForType(InvalidInputType type) {
        return valuesByType.get(type).size();
    }
    
    /**
     * Check if all values have been used (round-robin mode)
     */
    public boolean allValuesUsed() {
        for (InvalidInputType type : InvalidInputType.values()) {
            List<Object> values = valuesByType.get(type);
            Set<Integer> usedIndices = usedIndicesByType.get(type);
            
            if (values.size() > usedIndices.size()) {
                return false; // This type still has unused values
            }
        }
        return true; // All values used
    }
    
    /**
     * Get details about the pool for logging
     */
    public String getPoolSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid Input Pool for '").append(parameterName).append("' (").append(parameterType).append("):\n");
        
        for (InvalidInputType type : InvalidInputType.values()) {
            int count = valuesByType.get(type).size();
            if (count > 0) {
                sb.append("  - ").append(type.getDisplayName()).append(": ").append(count).append(" values\n");
            }
        }
        
        sb.append("Total: ").append(getTotalCount()).append(" invalid values");
        return sb.toString();
    }
    
    public String getParameterName() {
        return parameterName;
    }
    
    public String getParameterType() {
        return parameterType;
    }
}

