/**
 * 
 */
package org.springframework.samples.petclinic;


import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import page.IPAddress_18_237_133_64_Page;
import page.PetClinicaSpringFrameworkdemonstrationPage;

import java.net.URL;
import org.openqa.selenium.remote.RemoteWebDriver;

@ExtendWith(ParasoftWatcher.class)
public class VisitTest {

    private static WebDriver driver;
    private static String baseUrl;
    private static String gridUrl;

    @BeforeAll
    static void setUp() throws Exception {
        
        // base URL
        baseUrl = System.getProperty("baseUrl", "http://35.90.147.16:8099");

        // URL of the Selenium Grid Hub
        gridUrl = System.getProperty("gridUrl","http://35.90.147.16:4444/wd/hub");
        
        // Set ChromeOptions with desired arguments
        ChromeOptions options = new ChromeOptions();
        
        // Add any additional Chrome arguments or preferences if needed
        // options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        options.setCapability("webSocketUrl", true);

        // Create a RemoteWebDriver instance with ChromeOptions and grid URL
        URL url = new URL(gridUrl);
        driver = new RemoteWebDriver(url, options, false);

    }

    
    @AfterAll
    static void tearDown() {
        // Close the WebDriver session
        if (driver != null) {
            driver.quit();
        }
    }

	/**
	 * Name: VisitTest
	 * Recording file: VisitTest.json
	 *
	 * Parasoft recorded Selenium test on Thu Nov 09 2023 10:00:07 GMT-0800 (Pacific Standard Time)
	 */
	@Test
	public void testVisitTest() throws Throwable {
		driver.get(baseUrl);

		PetClinicaSpringFrameworkdemonstrationPage petClinicaSpringFrameworkdemonstrationPage = new PetClinicaSpringFrameworkdemonstrationPage(
				driver);
		Thread.sleep(1000);
		petClinicaSpringFrameworkdemonstrationPage.clickWebElement();
		Thread.sleep(1000);
		petClinicaSpringFrameworkdemonstrationPage.clickWebElement22();
		Thread.sleep(1000);
		petClinicaSpringFrameworkdemonstrationPage.clickWebElement3();
		Thread.sleep(1000);
		
		IPAddress_18_237_133_64_Page iPAddress_18_237_133_64_Page = new IPAddress_18_237_133_64_Page(driver);
		Thread.sleep(2000);
		iPAddress_18_237_133_64_Page.clickWebElement();
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.clickAddVisitLink();
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.setVisitDate("11152023");
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.setDescriptionField("yearly checkup");
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.clickAddNewVisitButton();
		Thread.sleep(2000);
		//iPAddress_18_237_133_64_Page.clickWebElement();
		//Thread.sleep(1000);
	}

}