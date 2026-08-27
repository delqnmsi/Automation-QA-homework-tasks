package utils;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import web.core.ConfigReader;

import java.time.Duration;


public class WaitUtils {

    private final WebDriver driver;
    private final ConfigReader config = ConfigReader.getInstance();

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriverWait wait(int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    // -------------------------------------------------------------------
    // Presence / visibility / clickability
    // -------------------------------------------------------------------

    public WebElement waitForElementToBePresent(By locator) {
        return waitForElementToBePresent(locator, config.getElementTimeoutSeconds());
    }

    public WebElement waitForElementToBePresent(By locator, int timeoutSeconds) {
        return wait(timeoutSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        return waitForElementToBeVisible(locator, config.getElementTimeoutSeconds());
    }

    public WebElement waitForElementToBeVisible(By locator, int timeoutSeconds) {
        return wait(timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        return waitForElementToBeClickable(locator, config.getElementTimeoutSeconds());
    }

    public WebElement waitForElementToBeClickable(By locator, int timeoutSeconds) {
        return wait(timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForElementToBeInvisible(By locator) {
        return waitForElementToBeInvisible(locator, config.getElementTimeoutSeconds());
    }

    public boolean waitForElementToBeInvisible(By locator, int timeoutSeconds) {
        return wait(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean isElementVisibleWithinTimeout(By locator) {
        try {
            waitForElementToBeVisible(locator, config.getElementTimeoutSeconds());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isEitherElementPresentWithinTimeout(By locator, By orLocator) {
        try {
            wait(config.getElementTimeoutSeconds()).until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(locator),
                    ExpectedConditions.presenceOfElementLocated(orLocator)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // -------------------------------------------------------------------
    // Navigation / page state
    // -------------------------------------------------------------------

    public boolean waitForUrlContains(String fragment) {
        return wait(config.getPageLoadTimeoutSeconds()).until(ExpectedConditions.urlContains(fragment));
    }

    public void waitForPageToLoad() {
        waitForPageToLoad(config.getPageLoadTimeoutSeconds());
    }

    public void waitForPageToLoad(int timeoutSeconds) {
        wait(timeoutSeconds).until(webDriver ->
                "complete".equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
    }

    public void waitForElementToDisappear(By locator) {
        waitForElementToDisappear(locator, config.getElementTimeoutSeconds());
    }

    public void waitForElementToDisappear(By locator, int timeoutSeconds) {
        wait(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // -------------------------------------------------------------------
    // Escape hatches for anything not covered above
    // -------------------------------------------------------------------

    public FluentWait<WebDriver> fluentWait(int timeoutSeconds, int pollingMillis) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public FluentWait<WebDriver> fluentWait(int pollingMillis) {
        return fluentWait(config.getElementTimeoutSeconds(), pollingMillis);
    }

    public void waitForElementPositionToStabilize(WebElement element) {
        Point[] lastPosition = {null};
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofMillis(100))
                .until(d -> {
                    Point current = element.getLocation();
                    boolean stable = current.equals(lastPosition[0]);
                    lastPosition[0] = current;
                    return stable;
                });
    }
}
