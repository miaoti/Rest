package es.us.isa.restest.generators;
import es.us.isa.restest.inputs.llm.ParameterInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The new AI-driven generator that leverages the ZeroShotLLMGenerator.
 * Replaces your old 'AiDrivenInputGenerator'.
 */
public class AiDrivenLLMGenerator {

    private static final Logger log = LogManager.getLogger(AiDrivenLLMGenerator.class);
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
    
    /**
     * Generate faulty parameter values using LLM - DEPRECATED
     * Use generateInvalidInputPool instead
     */
    @Deprecated
    public List<String> generateFaultyParameterValues(ParameterInfo param, int howMany) {
        log.info("DEPRECATED: Use generateInvalidInputPool instead");
        return zeroShotLLM.generateFaultyParameterValues(param, howMany);
    }
    
    /**
     * Generate comprehensive invalid input pool with 8 fault types
     * Delegates to ZeroShotLLMGenerator for actual generation
     */
    public es.us.isa.restest.inputs.InvalidInputPool generateInvalidInputPool(ParameterInfo param) {
        log.info("Delegating invalid input pool generation to ZeroShotLLMGenerator for parameter '{}'", param.getName());
        return zeroShotLLM.generateInvalidInputPool(param);
    }
}
