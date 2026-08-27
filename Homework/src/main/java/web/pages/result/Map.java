package web.pages.result;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import web.core.BaseMap;
import web.enums.ResultsLanguage;

import java.util.List;

public class Map extends BaseMap {

    public WebElement resultsHeader() {
        return driver.findElement(By.xpath("//h2[contains(text(),'Results')]"));
    }

    public By searchResultContainerBy = By.id("search");

    public List<WebElement> searchResultByTitleText(String partialTex) {
        return driver.findElements(By.xpath(String.format("//h2//span[contains(text(),'%s')]", partialTex)));
    }

    public WebElement filterOnlyResultIn(ResultsLanguage language) {
        return driver.findElement(
                By.cssSelector(String.format("a[aria-label*='filter %s']", language.getLanguage())));
    }

    public By bookLanguageFilterClearButtonBy =
            By.xpath("//div[@id='p_n_feature_browse-bin/400529011']//span[contains(text(),'Clear')]");

    public WebElement bookLanguageFilterClearButton() {
        return driver.findElement(
                bookLanguageFilterClearButtonBy);
    }

    public WebElement firstResultTitle() {
        return driver.findElement(By.cssSelector("h2[aria-label]"));
    }

    public List<WebElement> allResultTitle() {
        return driver.findElements(By.cssSelector("h2[aria-label]"));
    }

    public WebElement firstResultWrapContainer() {
        return firstResultTitle().findElement(By.xpath("ancestor::div[contains(@class,'puisg-row')]"));
    }

    public WebElement firstResultOfferOneAddToBasketButton() {
        return firstResultWrapContainer().findElement(By.cssSelector("div.a-button-stack"));
    }

    public WebElement firstResultAuthor(String authorName) {
        return firstResultWrapContainer().findElement(By.xpath(String.format(".//a[contains(text(),'%s')]", authorName)));
    }

    public WebElement firstResultOfferOnePricingContainer() {
        return firstResultWrapContainer().findElement(By.cssSelector("div[data-cy='price-recipe']"));
    }

    public WebElement firstResultOfferOnePrice() {
        return firstResultOfferOnePricingContainer().findElement(By.cssSelector("span.a-price"));
    }

    public WebElement firstResultOfferOneType() {
        return firstResultOfferOnePricingContainer().findElement(
                By.xpath(".//a[contains(@class,'s-underline-link-text null s-link-style')]"));
    }

    public WebElement firstResultRating() {
        return firstResultWrapContainer().findElement(
                By.cssSelector("i[data-cy='reviews-ratings-slot'] span.a-icon-alt"));
    }

    public List<WebElement> firstResultDeliveryDates() {
        return firstResultWrapContainer().findElements(
                By.cssSelector("span#WVCRIAFWG"));
    }
}
