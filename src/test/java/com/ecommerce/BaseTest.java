package com.ecommerce;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.time.Duration;

public class BaseTest {
	protected WebDriver driver;

	@BeforeClass
	public void setUp() throws java.io.IOException {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();

		// Common options
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.addArguments("--disable-extensions");
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		options.setExperimentalOption("useAutomationExtension", false);

		// Detect CI environments (Jenkins and GitHub Actions)
		var env = System.getenv();
		boolean isCI = env.containsKey("JENKINS_URL") || "true".equalsIgnoreCase(env.get("GITHUB_ACTIONS"))
				|| "true".equalsIgnoreCase(env.get("CI")); // keep your original guard too

		if (isCI) {
			// Use a unique, disposable profile to avoid "user data directory is already in
			// use"
			String tag = env.getOrDefault("BUILD_TAG",
					env.getOrDefault("GITHUB_RUN_ID", java.util.UUID.randomUUID().toString()));
			String baseTmp = env.getOrDefault("RUNNER_TEMP", System.getProperty("java.io.tmpdir"));
			java.nio.file.Path profileDir = java.nio.file.Paths.get(baseTmp, "chrome-profile-" + tag);
			java.nio.file.Files.createDirectories(profileDir);
			options.addArguments("--user-data-dir=" + profileDir.toString());

			// CI-friendly flags
			options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
					"--remote-allow-origins=*", "--window-size=1920,1080", "--disable-gpu");
			// (Optional) you can keep your custom UA if needed, but not required
			// options.addArguments("--user-agent=Mozilla/5.0 ...");
		} else {
			// Local dev
			options.addArguments("--start-maximized");
			// Avoid disabling images/JS locally—they can break sites under test
			// Remove these two if you added them earlier:
			// --disable-images, --disable-javascript
		}

		driver = new ChromeDriver(options);
		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(ConfigReader.getBaseURL());

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			wait.until(ExpectedConditions.not(ExpectedConditions.titleContains("Just a moment")));
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Page load warning: " + e.getMessage());
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}