package web.pages.common;

import web.core.BasePage;

public class CommonPage extends BasePage<Map, Assertions> {

    public CommonPage() {
        super(new Map(), new Assertions(new Map()));
    }

    public void searchByAnyText(String text) {
        map().searchField().sendKeys(text);
        map().submitSearchButton().click();
    }

    public void navigateToShoppingBasket() {
        map().goToBasketButton().click();
    }
}
