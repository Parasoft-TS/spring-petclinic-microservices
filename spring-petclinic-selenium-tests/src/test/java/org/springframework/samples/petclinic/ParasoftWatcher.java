package org.springframework.samples.petclinic;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class ParasoftWatcher implements BeforeEachCallback, TestWatcher  {

	private static int ENV_ID;
	private static String sessionId;
	//private static String baselineId;
	// private static String ctpUri;

	static {
		ENV_ID = Integer.parseInt(System.getProperty("ENV_ID", "32"));
		// Get the base URL from a system property or environment variable
		String ctpUrl = System.getProperty("ctpUrl", "http://ctp:8080");

		// Split the ctpUrl into URI and port components
		String[] uriComponents = ctpUrl.split(":");
		String uri = uriComponents[0] + ":" + uriComponents[1]; // Extract the URI with protocol (e.g., http://35.90.203.18)
		int port = Integer.parseInt(uriComponents[2]); // Extract the port (e.g., 8080)
		
	    // CTP connection
		RestAssured.baseURI = uri;
	    RestAssured.port = port;
	    RestAssured.authentication = RestAssured.basic("demo", "d3mo-user");
		sessionId = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).post("em/api/v3/environments/" + ENV_ID + "/agents/session/start").body().jsonPath().getString("session");
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		System.out.println("Session Id is: " + sessionId);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		String testId = getTestId(context);
		System.out.println("[ParasoftWatcher] Starting test setup for: " + testId);
		
		// Retrieve the WebDriver from the test instance and initialize header injection
		org.openqa.selenium.remote.RemoteWebDriver driver = null;
		Object testInstance = context.getTestInstance().orElse(null);
		if (testInstance != null) {
			System.out.println("[ParasoftWatcher] Test instance found: " + testInstance.getClass().getSimpleName());
			try {
				// Use reflection to get the 'driver' field from test class
				var driverField = testInstance.getClass().getDeclaredField("driver");
				driverField.setAccessible(true);
				driver = (org.openqa.selenium.remote.RemoteWebDriver) driverField.get(testInstance);
				System.out.println("[ParasoftWatcher] WebDriver instance retrieved successfully");
			} catch (NoSuchFieldException e) {
				System.out.println("[ParasoftWatcher] Warning: Could not retrieve driver field for node assignment - " + e.getMessage());
			}
		} else {
			System.out.println("[ParasoftWatcher] Warning: No test instance available");
		}
		
		// Initialize header injection if driver is available
		if (driver != null) {
			System.out.println("[ParasoftWatcher] Driver is available, attempting to retrieve node assignment");
			try {
				String gridUrl = System.getProperty("gridUrl", "http://localhost:4444/wd/hub");
				System.out.println("[ParasoftWatcher] Grid URL from system property: " + gridUrl);
				String nodeId = GridNodeHelper.getAssignedNodeName(driver, gridUrl);
				GridNodeHelper.initializeHeaderInjection(driver, nodeId);
				System.out.println("[ParasoftWatcher] Successfully assigned to node: " + nodeId);
			} catch (Exception e) {
				System.out.println("[ParasoftWatcher] ERROR: Could not determine node ID: " + e.getMessage());
				e.printStackTrace();
				// Continue gracefully if node ID retrieval fails
			}
		} else {
			System.out.println("[ParasoftWatcher] Warning: Driver is null, skipping header injection");
		}
		
		// Existing CTP test tracking code
		System.out.println("[ParasoftWatcher] Sending test start request to CTP for: " + testId);
		Response response = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body("{\"test\":\"" + testId + "\"}").post("em/api/v3/environments/" + ENV_ID + "/agents/test/start");
		System.out.println("[ParasoftWatcher] CTP Response Status Code: " + response.getStatusCode());
        System.out.println("[ParasoftWatcher] CTP Response Payload: " + response.getBody().asString());
    
	}

	@Override
	public void testSuccessful(ExtensionContext context) {
		String testId = getTestId(context);
		System.out.println("[ParasoftWatcher] Test passed: " + testId);
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append('{');
		bodyBuilder.append("\"test\":\"" + testId + "\"");
		bodyBuilder.append(',');
		bodyBuilder.append("\"result\":\"PASS\"");
		bodyBuilder.append('}');
		System.out.println("[ParasoftWatcher] Sending test stop request (PASS) to CTP");
		Response response = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(bodyBuilder.toString()).post("em/api/v3/environments/" + ENV_ID + "/agents/test/stop");
		System.out.println("[ParasoftWatcher] CTP Response Status Code: " + response.getStatusCode());
        System.out.println("[ParasoftWatcher] CTP Response Payload: " + response.getBody().asString());
    }
	

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		String testId = getTestId(context);
		System.out.println("[ParasoftWatcher] Test failed: " + testId);
		System.out.println("[ParasoftWatcher] Failure cause: " + cause.getMessage());
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append('{');
		bodyBuilder.append("\"test\":\"" + testId + "\"");
		bodyBuilder.append(',');
		bodyBuilder.append("\"result\":\"FAIL\"");
		bodyBuilder.append(',');
		bodyBuilder.append("\"message\":\"" + cause.getMessage() + "\"");
		bodyBuilder.append('}');
		System.out.println("[ParasoftWatcher] Sending test stop request (FAIL) to CTP");
		Response response = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(bodyBuilder.toString()).post("em/api/v3/environments/" + ENV_ID + "/agents/test/stop");
		System.out.println("[ParasoftWatcher] CTP Response Status Code: " + response.getStatusCode());
        System.out.println("[ParasoftWatcher] CTP Response Payload: " + response.getBody().asString());
    
	}

	private static String getTestId(ExtensionContext context) {
		return context.getTestClass().get().getName() + '#' + context.getTestMethod().get().getName();
	}

	static class ShutdownHook extends Thread {
		@Override
		public void run() {
			//baselineId = System.getProperty("baselineId", "latestBaseline");
			RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).post("em/api/v3/environments/" + ENV_ID + "/agents/session/stop");
			StringBuilder bodyBuilder = new StringBuilder();
			bodyBuilder.append('{');
			bodyBuilder.append("\"sessionTag\":\"jenkins-build\"");
			bodyBuilder.append(',');
			bodyBuilder.append("\"analysisType\":\"FUNCTIONAL_TEST\"");
			bodyBuilder.append('}');
			String publish = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body(bodyBuilder.toString()).post("em/api/v3/environments/" + ENV_ID + "/coverage/" + sessionId).body().asString();
			System.out.println(publish);
			//String baseline = RestAssured.given().relaxedHTTPSValidation().contentType(ContentType.JSON).body("string").post("em/api/v3/environments/" + ENV_ID + "/coverage/baselines/" + baselineId).body().asString();
			//System.out.println(baseline);
		}
	}
}
