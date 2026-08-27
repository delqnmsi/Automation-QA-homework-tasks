package web.pages.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import web.core.BaseMap;

public class Map extends BaseMap {

    public WebElement navigationBar() {
        return driver.findElement(By.id("nav-belt"));
    }

    public WebElement amazonLogo() {
        return driver.findElement(By.id("nav-logo-sprites"));
    }

    public WebElement searchField() {
        return driver.findElement(By.id("twotabsearchtextbox"));
    }

    public WebElement submitSearchButton() {
        return driver.findElement(By.id("nav-search-submit-button"));
    }

    public WebElement goToBasketButton() {
        return driver.findElement(By.id("nav-cart"));
    }

    public WebElement numberOfItemsInBasket() {
        return driver.findElement(By.id("nav-cart-count"));
    }
}
