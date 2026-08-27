package web.pages.home;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import web.core.BaseMap;

public class Map extends BaseMap {

    public By continueShoppingButtonBy = By.xpath("//button[contains(text(),'Continue shopping')]");

    public By acceptCookiesButtonBy = By.xpath("//input[@id='sp-cc-accept']");

    public By tableOfContentBy = By.xpath("//div[@id='gw-card-layout']");

    public WebElement tableOfContent() {
        return driver.findElement(tableOfContentBy);
    }

    public By shippingDialogDismissButtonBy = By.xpath("//input[@data-action-type='DISMISS']");

}
