package tests;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ecommerce.BaseTest;
import pages.HomePage;

public class HomePageTests extends BaseTest {

	@Test
	public void verifyHomePageTitle() {
		HomePage home = new HomePage(driver);
		
		// Debug info
		System.out.println("=== Title Verification Test ===");
		String actualTitle = home.getPageTitle();
		String expectedTitle = home.getExpectedTitle();
		
		System.out.println("Expected: '" + expectedTitle + "'");
		System.out.println("Actual: '" + actualTitle + "'");
		
		Assert.assertFalse(actualTitle.isEmpty(), "Page title should not be empty");
		Assert.assertFalse(actualTitle.equals("Just a moment..."), "Page is still loading");
		Assert.assertTrue(home.isTitleCorrect(), 
			String.format("Title should contain '%s' but was '%s'", expectedTitle, actualTitle));
	}

	@Test
	public void verifyTopMenuCategories() {
		HomePage home = new HomePage(driver);
		
		System.out.println("=== Menu Categories Test ===");
		
		// First ensure the page loaded correctly
		Assert.assertTrue(home.isTitleCorrect(), "Page must be loaded correctly before checking menu");

		List<String> actualCategories = home.getTopMenuCategories();
		List<String> expectedCategories = List.of(
				"Computers", 
				"Electronics", 
				"Apparel", 
				"Digital downloads", 
				"Books",
				"Jewelry", 
				"Gift Cards"
		);
		
		System.out.println("Expected categories: " + expectedCategories);
		System.out.println("Actual categories: " + actualCategories);
		
		Assert.assertEquals(actualCategories.size(), expectedCategories.size(), 
			"Number of menu categories should match");
		Assert.assertEquals(actualCategories, expectedCategories, 
			"Top menu categories do not match expected.");
	}
}