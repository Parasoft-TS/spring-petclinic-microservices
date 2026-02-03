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
public class RegisterOwnerAndPetTest {

	
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
	 * Name: RegisterOwnerAndPet
	 * Recording file: RegisterOwnerAndPet.json
	 *
	 * Parasoft recorded Selenium test on Thu Nov 09 2023 10:44:18 GMT-0800 (Pacific Standard Time)
	 */
	@Test
	public void testRegisterOwnerAndPetTest() throws Throwable {
		driver.get(baseUrl);

		PetClinicaSpringFrameworkdemonstrationPage petClinicaSpringFrameworkdemonstrationPage = new PetClinicaSpringFrameworkdemonstrationPage(
				driver);
		Thread.sleep(1000);
		petClinicaSpringFrameworkdemonstrationPage.clickHOMELink();
		petClinicaSpringFrameworkdemonstrationPage.clickOWNERSLink();
		petClinicaSpringFrameworkdemonstrationPage.clickREGISTERLink();
		Thread.sleep(1000);
		
		IPAddress_18_237_133_64_Page iPAddress_18_237_133_64_Page = new IPAddress_18_237_133_64_Page(driver);
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.setFirstNameField("Mark");
		iPAddress_18_237_133_64_Page.setLastNameField("Verdugo");
		iPAddress_18_237_133_64_Page.setAddressField("101 E. Huntington Dr.");
		iPAddress_18_237_133_64_Page.setCityField("Monrovia");
		iPAddress_18_237_133_64_Page.setTelephoneField("016267391734");
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.clickSubmitButton();
		Thread.sleep(2000);
		iPAddress_18_237_133_64_Page.clickMarkVerdugoLink();
		Thread.sleep(2000);
		iPAddress_18_237_133_64_Page.clickAddNewPetLink();
		Thread.sleep(2000);
		iPAddress_18_237_133_64_Page.setNameField("Arty");
		iPAddress_18_237_133_64_Page.setBirthDate("02022010");
		iPAddress_18_237_133_64_Page.selectTypeDropdown("dog");
		Thread.sleep(1000);
		iPAddress_18_237_133_64_Page.clickSubmitButton();
		Thread.sleep(1000);
	}

}