package web.pages.common;

import web.core.BaseAssertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions extends BaseAssertions<Map> {

    public Assertions(Map map) {
        super(map);
    }

    public void isLoaded() {
        assertTrue(map().navigationBar().isDisplayed() && map().amazonLogo().isDisplayed(),
                "The page didn't load");
    }

    public void numberOfItemInBasketIs(int expectedNumberOfItemsInBasket) {
        int actualNumberOfItemsInBasket = Integer.parseInt(map().numberOfItemsInBasket().getText());
        assertEquals(expectedNumberOfItemsInBasket, actualNumberOfItemsInBasket,
                "Actual number of Items is: " + actualNumberOfItemsInBasket);
    }
}
