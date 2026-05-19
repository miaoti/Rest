package es.us.isa.restest.test;

import es.us.isa.restest.workflow.TraceWorkflowExtractor;
import es.us.isa.restest.workflow.WorkflowScenario;
import es.us.isa.restest.workflow.WorkflowScenarioUtils;

import java.util.List;

/**
 * Demonstration test for the enhanced scenario deduplication system.
 * This test shows how duplicate scenarios are detected and eliminated
 * to save computational resources during test generation.
 */
public class TestScenarioDeduplication {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== SCENARIO DEDUPLICATION DEMONSTRATION ===\n");
            
            // Path to trace files directory
            String traceDirectory = "src/main/resources/My-Example/trainticket/traces/";
            
            System.out.println("Reading trace files from: " + traceDirectory);
            System.out.println("This will extract scenarios from all JSON trace files...\n");
            
            // Extract scenarios from all trace files
            List<WorkflowScenario> allScenarios = TraceWorkflowExtractor.extractScenarios(traceDirectory);
            
            System.out.println("BEFORE DEDUPLICATION:");
            System.out.println("Total scenarios extracted: " + allScenarios.size());
            
            if (allScenarios.isEmpty()) {
                System.out.println("No scenarios found. Make sure trace files exist in the directory.");
                return;
            }
            
            // Show scenario details before deduplication
            for (int i = 0; i < allScenarios.size(); i++) {
                WorkflowScenario scenario = allScenarios.get(i);
                System.out.println("  Scenario " + (i + 1) + ": " + scenario.getTraceIds() + 
                                   " (Root steps: " + scenario.getRootSteps().size() + ")");
            }
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("STARTING DEDUPLICATION PROCESS...");
            System.out.println("=".repeat(80) + "\n");
            
            // Apply deduplication with detailed logging
            List<WorkflowScenario> uniqueScenarios = WorkflowScenarioUtils.deduplicateBySteps(allScenarios);
            
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEDUPLICATION COMPLETE");
            System.out.println("=".repeat(80) + "\n");
            
            System.out.println("AFTER DEDUPLICATION:");
            System.out.println("Unique scenarios for test generation: " + uniqueScenarios.size());
            
            // Calculate savings
            int duplicatesRemoved = allScenarios.size() - uniqueScenarios.size();
            double savingsPercent = (double) duplicatesRemoved / allScenarios.size() * 100;
            
            System.out.println("\nRESOURCE SAVINGS SUMMARY:");
            System.out.println("  Original scenarios: " + allScenarios.size());
            System.out.println("  Duplicate scenarios eliminated: " + duplicatesRemoved);
            System.out.println("  Unique scenarios kept: " + uniqueScenarios.size());
            System.out.println("  Computational savings: " + String.format("%.1f%%", savingsPercent));
            
            if (duplicatesRemoved > 0) {
                System.out.println("\n✅ SUCCESS: Deduplication eliminated redundant test generation!");
                System.out.println("   The system will generate tests only for unique workflow patterns,");
                System.out.println("   saving time and computational resources.");
            } else {
                System.out.println("\n✅ RESULT: All scenarios are unique - no duplicates found.");
                System.out.println("   Each scenario represents a distinct workflow pattern.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: Failed to test scenario deduplication");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 