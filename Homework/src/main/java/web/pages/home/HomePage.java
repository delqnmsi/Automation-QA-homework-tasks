package web.pages.home;

import web.core.BasePage;
import web.core.ConfigReader;

public class HomePage extends BasePage<Map, Assertions> {

    public HomePage() {
        super(new Map(), new Assertions(new Map()));
    }

    public void navigate() {
        navigateTo(ConfigReader.getInstance().getBaseUrl());
        clickIfPresent(map().continueShoppingButtonBy);
        clickIfPresent(map().acceptCookiesButtonBy);
        clickIfPresent(map().shippingDialogDismissButtonBy);
    }
}