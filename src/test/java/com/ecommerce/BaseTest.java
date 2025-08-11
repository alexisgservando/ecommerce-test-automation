package com.ecommerce;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {
	protected WebDriver driver;

	@BeforeClass
	public void setUp() throws IOException, InterruptedException {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();

		// Stable defaults
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.addArguments("--disable-extensions");
		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		options.addArguments("--lang=en-US,en");

		// Detect CI (Jenkins, GitHub Actions, or generic CI)
		Map<String, String> env = System.getenv();
		boolean isCI = env.containsKey("JENKINS_URL") 
				|| "true".equalsIgnoreCase(env.get("GITHUB_ACTIONS"))
				|| "true".equalsIgnoreCase(env.get("CI"));

		if (isCI) {
			// Put the Chrome profile INSIDE the Jenkins workspace, and make it unique per
			// build.
			String workspace = env.getOrDefault("WORKSPACE", System.getProperty("java.io.tmpdir"));
			String tag = env.getOrDefault("BUILD_NUMBER", UUID.randomUUID().toString());
			Path profileDir = Paths.get(workspace, "chrome-profile-" + tag);
			Files.createDirectories(profileDir);
			options.addArguments("--user-data-dir=" + profileDir.toString());

			// A few extras help on Windows CI
			options.addArguments("--no-first-run", "--no-default-browser-check");

			options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage",
					"--remote-allow-origins=*", "--window-size=1920,1080", "--disable-gpu");
		} else {
			options.addArguments("--start-maximized");
		}

		driver = new ChromeDriver(options);
		driver.manage().window().setSize(new Dimension(1920, 1080));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(isCI ? 60 : 30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get(ConfigReader.getBaseURL());

		// Resilient wait for Cloudflare interstitial + DOM readiness
		int waitSeconds = isCI ? 90 : 30;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
		try {
			// Wait until title is not "Just a moment..."
			wait.until(d -> {
				String title = driver.getTitle();
				return title != null && !title.contains("Just a moment");
			});

			// Wait for document.readyState === "complete"
			long end = System.currentTimeMillis() + (waitSeconds * 1000L);
			while (System.currentTimeMillis() < end) {
				String ready = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
				if ("complete".equals(ready))
					break;
				Thread.sleep(500);
			}

			// One gentle refresh if still not complete
			String ready = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
			if (!"complete".equals(ready)) {
				driver.navigate().refresh();
				long end2 = System.currentTimeMillis() + 30_000L;
				while (System.currentTimeMillis() < end2) {
					ready = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
					if ("complete".equals(ready))
						break;
					Thread.sleep(500);
				}
			}

			Thread.sleep(1000); // small settle time
		} catch (Exception e) {
			System.out.println("Page load warning: " + e.getMessage());
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
