package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecommerce.BaseTest;
import pages.HomePage;

public class HomePageTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(HomePageTests.class);

    @Test
    public void verifyHomePageTitle() {
        log.info("=== Title Verification Test ===");
        HomePage home = new HomePage(driver);
        String actual = home.getPageTitle();
        String expected = home.getExpectedTitle();

        log.info("Expected contains: '{}'", expected);
        Assert.assertFalse(actual.isEmpty(), "Page title should not be empty");
        Assert.assertFalse(actual.equals("Just a moment..."), "Page is still loading");
        Assert.assertTrue(home.isTitleCorrect(),
                String.format("Title should contain '%s' but was '%s'", expected, actual));
    }

    @Test
    public void verifyTopMenuCategories() {
        log.info("=== Menu Categories Test ===");
        HomePage home = new HomePage(driver);
        Assert.assertTrue(home.isTitleCorrect(), "Page must be loaded before checking menu");

        List<String> actual = home.getTopMenuCategories();
        List<String> expected = List.of("Computers", "Electronics", "Apparel",
                "Digital downloads", "Books", "Jewelry", "Gift Cards");

        log.info("Expected: {}", expected);
        log.info("Actual  : {}", actual);

        Assert.assertEquals(actual.size(), expected.size(), "Menu size mismatch");
        Assert.assertEquals(actual, expected, "Top menu categories do not match expected.");
    }
}
