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
        String traceFilePath = "C:\\Users\\Tingshuo_Miao2\\Rest\\src\\main\\resources\\My-Example\\trainticket\\traces\\admin_price_create.json";
        List<WorkflowScenario> scenarios = TraceWorkflowExtractor.extractScenarios(traceFilePath);
        for (WorkflowScenario scenario : scenarios) {
            System.out.println(scenario);



        }
    }
}
