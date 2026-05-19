package io.mist.core.fault;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verifies the gating semantics of {@link FaultMiner}. When the system
 * property {@code mist.fault.mining.enabled} is unset/false the miner returns
 * an empty list so the registry-driven Sniper baseline is byte-for-byte
 * identical with the legacy enum baseline. When enabled the stub returns
 * a TrainTicket-specific category so the test can assert the plumbing.
 */
public class FaultMinerTest {

    private String previous;

    @Before
    public void saveProperty() {
        previous = System.getProperty(FaultMiner.ENABLED_PROPERTY);
        System.clearProperty(FaultMiner.ENABLED_PROPERTY);
    }

    @After
    public void restoreProperty() {
        if (previous == null) {
            System.clearProperty(FaultMiner.ENABLED_PROPERTY);
        } else {
            System.setProperty(FaultMiner.ENABLED_PROPERTY, previous);
        }
    }

    @Test
    public void disabledByDefaultReturnsEmptyList() {
        FaultMiner miner = new FaultMiner();
        Map<String, String> descriptions = new LinkedHashMap<>();
        descriptions.put("stationName", "Name of the train station");
        SpecRef spec = new SpecRef("TrainTicket", descriptions);
        List<FaultType> mined = miner.mine(spec, Collections.<ObservedResponse>emptyList());
        assertEquals(0, mined.size());
    }

    @Test
    public void enabledReturnsStubTrainTicketTypes() {
        System.setProperty(FaultMiner.ENABLED_PROPERTY, "true");
        FaultMiner miner = new FaultMiner();
        Map<String, String> descriptions = new LinkedHashMap<>();
        descriptions.put("stationName", "Name of the train station");
        SpecRef spec = new SpecRef("TrainTicket", descriptions);
        List<FaultType> mined = miner.mine(spec, Collections.<ObservedResponse>emptyList());
        assertEquals(1, mined.size());
        FaultType ft = mined.get(0);
        assertEquals("INVALID_STATION_NAME", ft.id());
        assertEquals(FaultType.FaultSource.MINED, ft.source());
        assertTrue(ft.applicableTo().contains("string"));
    }

    @Test
    public void enabledForNonTrainTicketReturnsEmpty() {
        System.setProperty(FaultMiner.ENABLED_PROPERTY, "true");
        FaultMiner miner = new FaultMiner();
        SpecRef spec = new SpecRef("Spotify", Collections.<String, String>emptyMap());
        List<FaultType> mined = miner.mine(spec, Collections.<ObservedResponse>emptyList());
        assertEquals(0, mined.size());
    }
}
