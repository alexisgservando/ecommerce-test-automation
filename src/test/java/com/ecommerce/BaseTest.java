package com.ecommerce;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeClass
    public void setUp() {
        log.info("Setting up WebDriver");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled",
                             "--disable-extensions",
                             "--disable-plugins");
        // NOTE: If the site needs JS, do not disable it. Keep this off unless truly needed.
        // options.addArguments("--disable-javascript");

        if (System.getenv("CI") != null) {
            log.info("CI detected: running headless with CI-safe flags");
            options.addArguments("--headless=new",
                                 "--no-sandbox",
                                 "--disable-dev-shm-usage",
                                 "--remote-allow-origins=*",
                                 "--window-size=1920,1080",
                                 "--disable-gpu",
                                 "--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
        } else {
            log.info("Local run: headed browser");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        String url = ConfigReader.getBaseURL();
        log.info("Navigating to {}", url);
        driver.get(url);

        // wait out any "Just a moment..." pages
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.not(ExpectedConditions.titleContains("Just a moment")));
            log.info("Landing page title: {}", driver.getTitle());
        } catch (Exception e) {
            log.warn("Page load wait finished with warning: {}", e.getMessage());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            log.info("Quitting WebDriver");
            driver.quit();
        }
    }
}
