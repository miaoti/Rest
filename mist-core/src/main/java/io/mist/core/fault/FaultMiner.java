package io.mist.core.fault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LLM-assisted miner that proposes SUT-specific {@link FaultType} categories.
 *
 * <p>Gated by the system property {@code mist.fault.mining.enabled} (default
 * {@code false}). When disabled, {@link #mine(SpecRef, List)} returns an empty
 * list so the registry-driven Sniper baseline stays byte-for-byte identical
 * with the legacy enum behaviour.
 *
 * <p>This session ships a stub: when enabled, the miner returns a hardcoded
 * example list (an {@code INVALID_STATION_NAME} type for TrainTicket) so the
 * unit test can assert the plumbing. The real LLM call lands in a follow-up.
 */
public final class FaultMiner {

    public static final String ENABLED_PROPERTY = "mist.fault.mining.enabled";

    private static final Logger log = LogManager.getLogger(FaultMiner.class);

    public List<FaultType> mine(SpecRef spec, List<ObservedResponse> observedResponses) {
        if (!isEnabled()) {
            log.debug("FaultMiner: mining disabled ({}=false); returning empty list", ENABLED_PROPERTY);
            return Collections.emptyList();
        }
        // STUB(Phase 3.B): real miner builds an LLM prompt from
        // spec.parameterDescriptions() + observedResponses, runs it through
        // LLMCallCache, and parses the response.
        List<FaultType> mined = new ArrayList<>();
        if (spec != null && "TrainTicket".equalsIgnoreCase(spec.apiKey())) {
            mined.add(new FaultType(
                    "INVALID_STATION_NAME",
                    "Invalid station name",
                    Set.of("string"),
                    Set.of("path", "query", "body"),
                    FaultType.FaultSource.MINED));
        }
        log.info("FaultMiner: mined {} SUT-specific fault types (stub)", mined.size());
        return mined;
    }

    private static boolean isEnabled() {
        return Boolean.parseBoolean(System.getProperty(ENABLED_PROPERTY, "false"));
    }
}
