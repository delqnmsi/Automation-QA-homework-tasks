package web.pages.result;

import org.openqa.selenium.WebElement;
import web.core.BaseAssertions;
import web.enums.BookType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions extends BaseAssertions<Map> {

    public Assertions(Map map) {
        super(map);
    }

    public void isLoaded() {
        waitUtils.waitForElementToBePresent(map().searchResultContainerBy);

        assertTrue(map().resultsHeader().isDisplayed(),
                "The element was not displayed");
    }

    public void searchItemFoundByTitleIsLoaded(String partialTitleContent) {
        int resultToBeChecked = 1;
        assertTrue(
                map().searchResultByTitleText(partialTitleContent).get(resultToBeChecked).isDisplayed(),
                "The element was not displayed");
    }

    public void firstResultTitleContainsText(String expectedText) {
        String actualText = map().firstResultTitle().getText();
        assertTrue(actualText.contains(expectedText),
                actualText + " Does not contain " + expectedText);
    }

    public void firstResultOfferOneTypeIs(BookType type) {
        String actual = map().firstResultOfferOneType().getText();
        assertEquals(type.getBookType(), actual,
                "Expected: first result offer type to be " + type.getBookType() + ", but actually: " + actual);
    }

    public void firstResultOfferOnePriceIsPresent() {
        String actualPriceText = map().firstResultOfferOnePrice().getText();
        String price = actualPriceText.replaceAll("\\s+", ".");
        assertNotNull(map().firstResultOfferOnePrice(),
                "The price was null");
        assertTrue(price.matches("EUR\\d+\\.\\d+"),
                "The price actually:" + price);
    }

    public void allResultsInEnglishAreRelatedToSearchTerm(String searchTerm, String resultTitle) {
        assertTrue(resultTitle.contains(searchTerm),
                resultTitle + " does not contain " + searchTerm);
    }

    public void firstResultOfferOneAddToBasketButtonIsDisplayed() {
        assertTrue(map().firstResultOfferOneAddToBasketButton().isDisplayed(),
                "The element was not displayed");
    }

    public void firstResultAuthorIs(String expectedAuthorName) {
        assertTrue(map().firstResultAuthor(expectedAuthorName).isDisplayed(),
                "The element was not displayed");
    }

    public void firstResultHasPositiveRating() {
        String attributeText = map().firstResultRating().getAttribute("textContent").strip();

        assertFalse(attributeText.isEmpty(), "No Rating");

        double rating = Double.parseDouble(attributeText.split(" ")[0]);

        assertTrue(rating > 0.0, "The rating should be positive, but was: " + rating);
        assertTrue(rating <= 5.0, "The rating can't be higher than 5.0, but was: " + rating);
    }

    public void firstResultHasDeliveryDates() {
        String deliveryDatePattern = "[A-Za-z]{3} \\d{1,2} [A-Za-z]{3,4}";

        List<WebElement> deliveryDates = map().firstResultDeliveryDates();

        for (WebElement deliveryDate : deliveryDates) {

            assertFalse(deliveryDate.getText().isEmpty());
            assertTrue(deliveryDate.getText().matches(deliveryDatePattern),
                    "Delivery date doesn't match expected format 'Ddd D Mmm': " + deliveryDate);
        }
    }
}
