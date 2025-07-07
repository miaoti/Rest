import es.us.isa.restest.generators.ZeroShotLLMGenerator;
import es.us.isa.restest.inputs.llm.ParameterInfo;

class PromptDemo {
    public static void main(String[] args) {
        // Create endStation parameter like in your example
        ParameterInfo param = new ParameterInfo();
        param.setName("endStation");
        param.setDescription("");
        param.setInLocation("formData");
        param.setType("string");
        param.setFormat("");
        param.setSchemaType("string");
        param.setSchemaExample("");
        param.setRegex("");
        
        ZeroShotLLMGenerator generator = new ZeroShotLLMGenerator();
        
        try {
            java.lang.reflect.Method buildPromptMethod = 
                ZeroShotLLMGenerator.class.getDeclaredMethod("buildPrompt", ParameterInfo.class, int.class);
            buildPromptMethod.setAccessible(true);
            
            String prompt = (String) buildPromptMethod.invoke(generator, param, 5);
            
            System.out.println("=".repeat(80));
            System.out.println("NEW IMPROVED PROMPT FOR 'endStation':");
            System.out.println("=".repeat(80));
            System.out.println(prompt);
            System.out.println("=".repeat(80));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 