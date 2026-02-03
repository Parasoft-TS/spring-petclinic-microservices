package org.springframework.samples.petclinic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;
import java.net.URL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.samples.petclinic.ParasoftWatcher;

@ExtendWith(ParasoftWatcher.class)
public class PetTest {

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

    @Test
    public void testRename() throws Exception{
        // Test logic
        
        // Navigate to the desired web page
        driver.get(baseUrl);

        Thread.sleep(1000);

        System.out.println("Test Rename Pet");
        // Thread.sleep(1000);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//a[@class=\"dropdown-toggle\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//a[@ui-sref=\"owners\"]")).click();
		Thread.sleep(1000);

        System.out.println("Navigate Add Pet");
		driver.navigate().refresh();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//owner-list/table/tbody/tr[1]/td[1]/a")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//dd/a")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Lena");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		Thread.sleep(1000);
        System.out.println("Navigate Rename Pet");
		driver.navigate().refresh();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//a[@class=\"dropdown-toggle\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//a[@ui-sref=\"owners\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//owner-list/table/tbody/tr[1]/td[1]/a")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//dd/a")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Leo");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//a[@title=\"home page\"]")).click();
    }

    @AfterAll
    static void tearDown() {
        // Close the WebDriver session
        if (driver != null) {
            driver.quit();
        }
    }
}
