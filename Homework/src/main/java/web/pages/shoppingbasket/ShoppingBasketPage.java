package web.pages.shoppingbasket;

import org.openqa.selenium.WebElement;
import web.core.BasePage;

public class ShoppingBasketPage extends BasePage<Map, Assertions> {

    public ShoppingBasketPage() {
        super(new Map(), new Assertions(new Map()));
    }

    public void removeItemByName(String title) {
        WebElement deleteButton = map().itemDeleteButton(map().itemInOrderByName(title));
        scrollIntoView(deleteButton);
        deleteButton.click();
    }
}
