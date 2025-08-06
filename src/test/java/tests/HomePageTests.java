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
		String actualTitle = home.getPageTitle();
		String expectedTitle = home.getExpectedTitle();

		Assert.assertFalse(actualTitle.isEmpty());
		Assert.assertEquals(actualTitle, expectedTitle, "Title does not match expected value");
	}

	@Test
	public void verifyTopMenuCategories() {
		HomePage home = new HomePage(driver);

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
		
		Assert.assertEquals(actualCategories, expectedCategories, "Top menu categories do not match expected.");
	}
}