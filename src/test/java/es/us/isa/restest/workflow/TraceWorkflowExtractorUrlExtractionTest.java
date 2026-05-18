package es.us.isa.restest.workflow;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Regression + extension tests for URL path-parameter extraction in
 * {@link TraceWorkflowExtractor#extractFieldsFromUrl(String, Map)}.
 *
 * <p>Driven via reflection because the target method is package-private
 * {@code static} and intentionally not exposed on the public API.
 *
 * <p>Covers:
 * <ul>
 *   <li>The legacy TrainTicket regression case (single ID after a known noun).</li>
 *   <li>Hyphenated noun → camelCase fallback key.</li>
 *   <li>Nested URLs producing multiple semantic fields (no early-exit drop).</li>
 *   <li>Unknown-noun fallback derivation.</li>
 * </ul>
 */
public class TraceWorkflowExtractorUrlExtractionTest {

    private static Map<String, String> extract(String url) throws Exception {
        Method m = TraceWorkflowExtractor.class
                .getDeclaredMethod("extractFieldsFromUrl", String.class, Map.class);
        m.setAccessible(true);
        Map<String, String> out = new HashMap<>();
        m.invoke(null, url, out);
        return out;
    }

    @Test
    public void trainticketRegressionStillWorks() throws Exception {
        // Long-numeric IDs (5+ digits) are recognised — UUID not required.
        Map<String, String> fields = extract("/api/v1/orderservice/order/123456");
        assertEquals("123456", fields.get("orderId"));
    }

    @Test
    public void hyphenatedNounProducesCamelCaseKey() throws Exception {
        // "order-items" is not in the default map → must fall back to derived key.
        Map<String, String> fields = extract(
                "/api/v1/order-items/4d2a46c7-71cb-4f29-91a7-2b8c1f7e7e51");
        assertEquals("4d2a46c7-71cb-4f29-91a7-2b8c1f7e7e51", fields.get("orderItemId"));
    }

    @Test
    public void nestedUrlProducesMultipleFields() throws Exception {
        // Both customers/{AAA} and orders/{BBB} must be captured, not just the first.
        String aaa = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";
        String bbb = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";
        Map<String, String> fields = extract("/api/v1/customers/" + aaa + "/orders/" + bbb);
        assertEquals(aaa, fields.get("customerId"));
        assertEquals(bbb, fields.get("orderId"));
    }

    @Test
    public void unknownNounFallback() throws Exception {
        // "widgets" is not in the bundled TrainTicket default → derived key.
        Map<String, String> fields = extract(
                "/api/v1/widgets/4d2a46c7-71cb-4f29-91a7-2b8c1f7e7e51");
        assertEquals("4d2a46c7-71cb-4f29-91a7-2b8c1f7e7e51", fields.get("widgetId"));
    }
}
