package com.ecommerce;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
	protected WebDriver driver;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		
		// Set Chrome options for headless mode
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new"); // Use 'new' for Chrome 109+
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(ConfigReader.getBaseURL());
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
