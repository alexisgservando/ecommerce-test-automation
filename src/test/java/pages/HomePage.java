package pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
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
	private final String expectedTitle = "nopCommerce demo store. Home page title";
	private final By topMenuLinks = By.cssSelector("ul.top-menu.notmobile > li > a");

	// 3. Methods
	public String getPageTitle() {
		return driver.getTitle();
	}

	public String getExpectedTitle() {
		return expectedTitle;
	}

	public List<String> getTopMenuCategories() {
		List<WebElement> elements = WaitUtils.waitForAllVisible(driver, topMenuLinks);
	    List<String> categoryTexts = new ArrayList<>();
	    System.out.println("Top Menu Categories Found:");
	    for (WebElement el : elements) {
	        String text = el.getText().trim();
	        categoryTexts.add(text);
	        System.out.println("- " + text); // 👈 Print each category to the console
	    }
	    return categoryTexts;
	}
}