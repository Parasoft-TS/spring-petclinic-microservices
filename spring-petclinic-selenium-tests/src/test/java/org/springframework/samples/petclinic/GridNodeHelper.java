package org.springframework.samples.petclinic;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.bidi.network.NetworkInterceptor;
import org.openqa.selenium.bidi.network.Route;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class GridNodeHelper {
    
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static NetworkInterceptor interceptor;
    
    /**
     * Fetches the assigned node ID from the Selenium Hub via GraphQL
     */
    public static String getAssignedNodeName(RemoteWebDriver driver, String gridUrl) throws Exception {
        String sessionId = driver.getSessionId().toString();
        String hubUrl = gridUrl.replace("/wd/hub", "/graphql");
        
        String query = "{ \"query\": \"{ session (id: \\\"" + sessionId + "\\\") { slot { stereotype } } }\" }";
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(hubUrl))
            .POST(HttpRequest.BodyPublishers.ofString(query))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Parse JSON response to extract node-id from stereotype
        JsonObject jsonObj = JsonParser.parseString(response.body()).getAsJsonObject();
        String nodeId = jsonObj.getAsJsonObject("data")
                               .getAsJsonObject("session")
                               .getAsJsonObject("slot")
                               .getAsJsonObject("stereotype")
                               .get("node-id")
                               .getAsString();
        return nodeId;
    }
    
    /**
     * Initializes BiDi and injects the baggage header with node ID into all requests
     */
    public static void initializeHeaderInjection(RemoteWebDriver driver, String nodeId) {
        // Start intercepting requests and inject baggage header
        interceptor = new NetworkInterceptor(
            driver,
            Route.matching(req -> true)
                 .sending(req -> req.addHeader("baggage", "node-id=" + nodeId))
        );
    }
    
    /**
     * Closes the network interceptor when done
     */
    public static void closeInterceptor() {
        if (interceptor != null) {
            interceptor.close();
        }
    }
}
