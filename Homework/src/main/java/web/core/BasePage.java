package web.core;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public abstract class BasePage<M extends BaseMap, A extends BaseAssertions<M>> {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;
    private final M map;
    private final A assertions;

    protected BasePage(M map, A assertions) {
        this.driver = DriverManager.getDriver();
        this.map = map;
        this.assertions = assertions;
        this.waitUtils = new WaitUtils(driver);
    }

    public M map() {
        return map;
    }

    public A assertion() {
        return assertions;
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected boolean clickIfPresent(By locator) {
        if (waitUtils.isElementVisibleWithinTimeout(locator)) {
            driver.findElement(locator).click();
            return true;
        }
        return false;
    }

    protected void scrollIntoView(By locator) {
        scrollIntoView(driver.findElement(locator));
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}
