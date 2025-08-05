package es.us.isa.restest.inputs.smart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Authentication manager for Smart Input Fetching
 * Handles login and JWT token management for TrainTicket system
 */
public class SmartFetchAuthManager {
    
    private static final Logger log = LogManager.getLogger(SmartFetchAuthManager.class);
    
    private final String baseUrl;
    private final String adminUsername;
    private final String adminPassword;
    
    // JWT token management
    private String jwtToken;
    private String jwtType = "Bearer";
    private LocalDateTime tokenExpiry;
    private static final int TOKEN_VALIDITY_MINUTES = 30; // Conservative estimate
    
    public SmartFetchAuthManager(String baseUrl, String adminUsername, String adminPassword) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        
        log.info("SmartFetchAuthManager initialized for baseUrl: {}, username: {}", baseUrl, adminUsername);
    }
    
    /**
     * Get valid JWT token, performing login if necessary
     */
    public String getValidToken() {
        if (isTokenValid()) {
            log.debug("Using existing valid JWT token");
            return jwtToken;
        }
        
        log.info("🔐 JWT token expired or missing, performing admin login...");
        return performLogin();
    }
    
    /**
     * Get authorization header value for API calls
     */
    public String getAuthorizationHeader() {
        String token = getValidToken();
        if (token != null) {
            return jwtType + " " + token;
        }
        return null;
    }
    
    /**
     * Check if current token is valid and not expired
     */
    private boolean isTokenValid() {
        if (jwtToken == null || tokenExpiry == null) {
            return false;
        }
        
        // Check if token expires within next 5 minutes (buffer for safety)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryWithBuffer = tokenExpiry.minus(5, ChronoUnit.MINUTES);
        
        return now.isBefore(expiryWithBuffer);
    }
    
    /**
     * Perform admin login and get JWT token
     */
    private String performLogin() {
        try {
            String loginUrl = baseUrl + "/api/v1/users/login";
            log.info("🔐 Attempting admin login to: {}", loginUrl);
            
            // Create login request
            URL url = new URL(loginUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000); // 10 seconds
            conn.setReadTimeout(10000);
            
            // Build login payload
            JSONObject loginPayload = new JSONObject();
            loginPayload.put("username", adminUsername);
            loginPayload.put("password", adminPassword);
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = loginPayload.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            String responseBody = readResponse(conn);
            
            if (responseCode == 200) {
                // Parse JWT token from response
                JSONObject response = new JSONObject(responseBody);
                
                if (response.has("data") && response.getJSONObject("data").has("token")) {
                    jwtToken = response.getJSONObject("data").getString("token");
                    tokenExpiry = LocalDateTime.now().plus(TOKEN_VALIDITY_MINUTES, ChronoUnit.MINUTES);
                    
                    log.info("✅ Admin login successful, JWT token obtained (expires: {})", tokenExpiry);
                    log.debug("JWT token: {}...", jwtToken != null ? jwtToken.substring(0, Math.min(20, jwtToken.length())) : "null");
                    
                    return jwtToken;
                } else {
                    log.error("❌ Login response missing token field: {}", responseBody);
                }
            } else {
                log.error("❌ Admin login failed with HTTP {}: {}", responseCode, responseBody);
            }
            
        } catch (Exception e) {
            log.error("❌ Admin login failed with exception: {}", e.getMessage(), e);
        }
        
        // Clear invalid token
        jwtToken = null;
        tokenExpiry = null;
        return null;
    }
    
    /**
     * Add authentication headers to an HTTP connection
     */
    public void addAuthHeaders(HttpURLConnection conn) {
        String authHeader = getAuthorizationHeader();
        if (authHeader != null) {
            conn.setRequestProperty("Authorization", authHeader);
            log.debug("Added Authorization header to request");
        } else {
            log.warn("⚠️ No valid authentication token available for API request");
        }
    }
    
    /**
     * Check if authentication is properly configured
     */
    public boolean isConfigured() {
        return adminUsername != null && !adminUsername.trim().isEmpty() &&
               adminPassword != null && !adminPassword.trim().isEmpty() &&
               baseUrl != null && !baseUrl.trim().isEmpty();
    }
    
    /**
     * Force token refresh on next request
     */
    public void invalidateToken() {
        log.info("🔄 Invalidating JWT token, will re-login on next request");
        jwtToken = null;
        tokenExpiry = null;
    }
    
    /**
     * Read response from HTTP connection
     */
    private String readResponse(HttpURLConnection conn) throws IOException {
        BufferedReader reader;
        
        // Try to read from input stream first, fall back to error stream
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            // If input stream fails, try error stream
            if (conn.getErrorStream() != null) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            } else {
                throw e;
            }
        }
        
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        return response.toString();
    }
    
    /**
     * Get token expiry for debugging
     */
    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }
    
    /**
     * Check if we have a valid token without triggering login
     */
    public boolean hasValidToken() {
        return isTokenValid();
    }
    
    @Override
    public String toString() {
        return String.format("SmartFetchAuthManager{baseUrl='%s', username='%s', hasToken=%s, tokenExpiry=%s}",
                baseUrl, adminUsername, jwtToken != null, tokenExpiry);
    }
}
