package es.us.isa.restest.generators;
import es.us.isa.restest.inputs.llm.ParameterInfo;

import java.util.List;

/**
 * The new AI-driven generator that leverages the ZeroShotLLMGenerator.
 * Replaces your old 'AiDrivenInputGenerator'.
 */
public class AiDrivenLLMGenerator {

    private final ZeroShotLLMGenerator zeroShotLLM;

    public AiDrivenLLMGenerator() {
        this.zeroShotLLM = new ZeroShotLLMGenerator();
    }

    /**
     * Produce candidate values for the parameter using the zero-shot approach.
     */
    public List<String> generateParameterValues(ParameterInfo param) {
        // e.g. we want 5 examples
        return zeroShotLLM.generateParameterValues(param, 5);
    }
}
