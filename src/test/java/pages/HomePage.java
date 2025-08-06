package pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class HomePage {
	private WebDriver driver;

	// 1. Constructor
	public HomePage(WebDriver driver) {
		this.driver = driver;
	}

	// 2. Locators
	private final String expectedTitle = "nopCommerce demo store";
	private final By topMenuLinks = By.cssSelector("ul.top-menu.notmobile > li > a");

	// 3. Methods
	public String getPageTitle() {
		String title = driver.getTitle();
		System.out.println("Current page title: '" + title + "'");
		return title;
	}

	public String getExpectedTitle() {
		return expectedTitle;
	}

	public boolean isTitleCorrect() {
		String currentTitle = getPageTitle();
		// Check if title contains expected text (more flexible than exact match)
		return currentTitle != null && currentTitle.contains(expectedTitle);
	}

	public List<String> getTopMenuCategories() {
		try {
			System.out.println("Current URL: " + driver.getCurrentUrl());
			System.out.println("Page title before waiting: " + driver.getTitle());
			
			List<WebElement> elements = WaitUtils.waitForAllVisible(driver, topMenuLinks);
			List<String> categoryTexts = new ArrayList<>();
			System.out.println("Top Menu Categories Found:");
			for (WebElement el : elements) {
				String text = el.getText().trim();
				categoryTexts.add(text);
				System.out.println("- " + text);
			}
			return categoryTexts;
		} catch (TimeoutException e) {
			System.out.println("Timeout waiting for menu elements. Current title: " + driver.getTitle());
			System.out.println("Current URL: " + driver.getCurrentUrl());
			System.out.println("Page source preview: " + driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));
			throw e;
		}
	}
}