package web.pages.shoppingbasket;

import org.openqa.selenium.WebElement;
import web.core.BaseAssertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions extends BaseAssertions<Map> {

    public Assertions(Map map) {
        super(map);
    }

    public void isLoaded() {
        boolean loadedElement = waitUtils.isEitherElementPresentWithinTimeout(
                map().shoppingBasketHeaderBy, map().emptyShoppingBasketTableBy);
        assertTrue(loadedElement,
                "Expected: shopping basket header or empty shopping basket table to be present," +
                        " but actually: neither appeared");
    }

    public void onlyOneItemInOrder() {
        int actualItemCount = map().itemsInOrder().size();
        assertEquals(1, actualItemCount,
                "Expected: exactly one item in the basket, but actually: " + actualItemCount);
    }

    public void itemsAreRemoved() {
        String subTotalZero = "Subtotal (0 items):";

        waitUtils.waitForElementToBePresent(map().basketSubTotalBy);
        String actualSubTotal = map().basketSubTotal().getText().strip();

        assertEquals(subTotalZero, actualSubTotal,
                "Actual SubTotal is " + actualSubTotal);
    }

    public void orderComponentsAreCorrect(String title,
                                          String itemFormat,
                                          String availability,
                                          boolean isAGift,
                                          int expectedQuantityOfThisItem,
                                          String expectedPrice) {

        WebElement row = map().itemInOrderByName(title);

        String actualTitle = map().itemInOrderTitle(row).getText().strip();
        String actualItemFormat = map().itemFormat(row).getText().strip();
        String actualAvailability = map().availability(row).getText().strip();
        int actualQuantity = Integer.parseInt(map().itemInOrderQuantity(row).getText());
        String actualPrice = map().itemInOrderPrice(row).getText();
        boolean actualIsAGift = map().itemInOrderGiftCheckBox(row).isSelected();

        assertEquals(title.strip(), actualTitle,
                "Expected: item title to be " + title + ", but actually: " + actualTitle);
        assertEquals(itemFormat.strip(), actualItemFormat,
                "Expected: item format to be " + itemFormat + ", but actually: " + actualItemFormat);
        assertEquals(availability.strip(), actualAvailability,
                "Expected: item availability to be " + availability + ", but actually: " + actualAvailability);
        assertEquals(expectedQuantityOfThisItem, actualQuantity,
                "Expected: item quantity to be " + expectedQuantityOfThisItem + ", but actually: " + actualQuantity);
        assertEquals(expectedPrice, actualPrice,
                "Expected: item price to be " + expectedPrice + ", but actually: " + actualPrice);
        assertEquals(isAGift, actualIsAGift,
                "Expected: item gift checkbox to be " + isAGift + ", but actually: " + actualIsAGift);
    }
}
