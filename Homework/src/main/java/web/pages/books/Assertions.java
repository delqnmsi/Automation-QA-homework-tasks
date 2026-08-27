package web.pages.books;

import web.core.BaseAssertions;
import web.enums.BookType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions extends BaseAssertions<Map> {

    public Assertions(Map map) {
        super(map);
    }

    public void isLoaded() {
        waitUtils.waitForUrlContains("dp");
        assertTrue(map().pageLabel().isDisplayed(), "The element was not displayed");
    }

    public void bookTypeIsSelected(BookType type) {
        String selectedBookType = map().selectedBookType().getText();
        assertEquals(type.getBookType(), selectedBookType,
                "Expected: selected book type to be " + type.getBookType() + ", but actually: " + selectedBookType);
    }

    public void selectedBookPriceIsCorrect(String expectedPrice) {
        String actualPrice = map().selectedBookPrice().getText().replaceAll(" ", "");
        assertEquals(expectedPrice, actualPrice,
                "Expected: selected book price to be " + expectedPrice + ", but actually: " + actualPrice);
        assertTrue(actualPrice.matches("EUR\\d+\\.\\d+"),
                "The price actually:" + actualPrice);
    }

    public void selectedBookTitleIsCorrect(String expectedBookTitle) {
        String actualTitle = map().bookTitle().getText();
        assertEquals(expectedBookTitle, actualTitle,
                "Expected: selected book title to be " + expectedBookTitle + ", but actually: " + actualTitle);
    }

    public void correctItemAddedToBasket(BookType type) {
        boolean confirmationDisplayed = map().addedToBasketConfirmation().isDisplayed();
        assertTrue(confirmationDisplayed,
                "The element was not displayed");
        String actualType = map().itemFormatType().getText();
        assertEquals(type.getBookType(), actualType,
                "Expected: added item format type to be " + type.getBookType() + ", but actually: " + actualType);
    }

    public void addToBasketMessageIsCorrect(String expectedMessage) {
        String actualMessage = map().addedToBasketConfirmation().getText().strip();
        assertEquals(expectedMessage, actualMessage,
                "Expected: add to basket message to be " + expectedMessage + ", but actually: " + actualMessage);
    }

    public void selectedAsAGiftReverseButtonIsPresent(String linkText) {
        String expected = linkText.strip();
        String actual = map().selectAsAGiftReverseButton().getText().strip();
        assertEquals(expected, actual,
                "Expected: gift reverse button text to be " + expected + ", but actually: " + actual);
    }

    public void singleSellCharacteristicsSetIsSelected() {
        assertEquals(1, map().selectedSellCharacteristics().size(),
                "More than one sell characteristics appears selected");
    }

    public void itemIsPurchaseReady() {
        assertTrue(map().addToBasketButton().isDisplayed(), "The element is not displayed");
        assertTrue(map().addToBasketButton().isEnabled(), "The element is not enabled");
    }

    public void theBookHasExpectedArtist(String artistsName, String contribution) {
        assertTrue(map().checkArtistByName(artistsName).isDisplayed(), "Expected artist's name is not present");
        assertEquals(
                contribution,
                map().checkArtistsContribution(artistsName).getText().replaceAll(",", ""),
                "This artist is not " + contribution);
    }

    public void theBookHasPositiveRating() {
        String attributeText = map().averageCustomerRating().getAttribute("textContent").strip();

        assertFalse(attributeText.isEmpty(), "No Rating");

        double rating = Double.parseDouble(attributeText.split(" ")[0]);

        assertTrue(rating > 0.0, "The rating should be positive, but was: " + rating);
        assertTrue(rating <= 5.0, "The rating can't be higher than 5.0, but was: " + rating);
    }
}
