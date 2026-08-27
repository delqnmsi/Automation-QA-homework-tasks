package web.pages.books;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import web.core.BaseMap;

import java.util.List;

public class Map extends BaseMap {

    public WebElement pageLabel() {
        return driver.findElement(
                By.xpath("//div[@id='nav-progressive-subnav']//a[@aria-label='Books']"));
    }

    public WebElement bookTitle() {
        return driver.findElement(By.id("productTitle"));
    }

    public WebElement selectedBookType() {
        return selectedSellCharacteristics().getFirst()
                .findElement(
                        By.xpath(".//span[@class='slot-title']"));
    }

    public WebElement selectedBookPrice() {
        return selectedSellCharacteristics().getFirst()
                .findElement(
                        By.xpath(".//span[@class='slot-price']"));
    }

    public List<WebElement> selectedSellCharacteristics() {
        return driver.findElements(By.xpath("//span[contains(@class,'a-button-selected')]"));
    }

    public By selectAsAGiftButtonBy = By.xpath("//div[@class='offer-display-feature-text']");

    public WebElement selectAsAGiftButton() {
        return driver.findElement(selectAsAGiftButtonBy);
    }

    public By selectAsAGiftReverseButtonBy = By.xpath("//span[contains(@class,'gift-wrap-message')]");

    public WebElement selectAsAGiftReverseButton() {
        return driver.findElement(selectAsAGiftReverseButtonBy);
    }

    public By addToBasketButtonBy = By.id("add-to-cart-button");

    public WebElement addToBasketButton() {
        return driver.findElement(addToBasketButtonBy);
    }

    public By frameBuyNewBy = By.id("buyBoxAccordion");

    public By addedToBasketConfirmationBy = By.id("NATC_SMART_WAGON_CONF_MSG_SUCCESS");

    public WebElement addedToBasketConfirmation() {
        return driver.findElement(addedToBasketConfirmationBy);
    }

    public WebElement itemFormatType() {
        return driver.findElement(
                By.xpath("//div[@id='sw-all-product-variations']//span[@class='a-size-base']"));
    }

    public WebElement checkArtistByName(String expectedArtistsName) {
        return driver.findElement(
                By.xpath(
                        String.format("//span[contains(@class,'author notFaded')]//a[contains(text(),'%s')]",
                                expectedArtistsName)));
    }

    public WebElement checkArtistsContribution(String expectedArtistsName) {
        return checkArtistByName(
                expectedArtistsName).findElement(
                        By.xpath("./following-sibling::span[contains(@class,'contribution')]"));
    }

    public WebElement averageCustomerRating() {
        return driver.findElement(
                By.xpath("//div[@id='averageCustomerReviews']//span[@class='a-icon-alt']"));
    }
}
