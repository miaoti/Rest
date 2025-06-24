package es.us.isa.restest.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.us.isa.restest.workflow.TraceWorkflowExtractor;
import es.us.isa.restest.workflow.WorkflowScenario;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TraceMain {
    public static void main(String[] args) throws Exception {
        String traceFilePath = "src/main/resources/My-Example/user_order_get.json";
        List<WorkflowScenario> scenarios = TraceWorkflowExtractor.extractScenarios(traceFilePath);
        for (WorkflowScenario scenario : scenarios) {
            System.out.println(scenario);
        }
    }
}
