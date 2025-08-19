package pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.WaitUtils;

public class HomePage {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    private final String expectedTitle = "nopCommerce demo store";
    private final By topMenuLinks = By.cssSelector("ul.top-menu.notmobile > li > a");

    public String getPageTitle() {
        String title = driver.getTitle();
        log.info("Current page title: '{}'", title);
        return title;
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }

    public boolean isTitleCorrect() {
        String t = getPageTitle();
        boolean ok = t != null && t.contains(expectedTitle);
        log.debug("Title contains expected? {}", ok);
        return ok;
    }

    public List<String> getTopMenuCategories() {
        try {
            log.debug("URL before reading menu: {}", driver.getCurrentUrl());
            List<WebElement> els = WaitUtils.waitForAllVisible(driver, topMenuLinks);
            List<String> out = new ArrayList<>();
            for (WebElement el : els) {
                String text = el.getText().trim();
                out.add(text);
                log.info("Top menu item: {}", text);
            }
            return out;
        } catch (TimeoutException e) {
            log.error("Timeout waiting for top menu. Title='{}' URL='{}'",
                      driver.getTitle(), driver.getCurrentUrl());
            int len = Math.min(500, driver.getPageSource().length());
            log.debug("Page source preview: {}", driver.getPageSource().substring(0, len));
            throw e;
        }
    }
}
