package es.us.isa.restest.util;

public class ApiResponse {

    private final int    statusCode;
    private final String body;
    private final String traceId; // Trace‑propagation header if present

    public ApiResponse(int statusCode, String body, String traceId) {
        this.statusCode = statusCode;
        this.body      = body;
        this.traceId   = traceId;
    }

    public int    getStatusCode() { return statusCode; }
    public String getBody()       { return body;       }
    public String getTraceId()    { return traceId;    }
}
