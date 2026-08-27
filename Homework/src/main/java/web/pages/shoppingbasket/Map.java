package web.pages.shoppingbasket;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import web.core.BaseMap;

import java.util.List;

public class Map extends BaseMap {

    public By shoppingBasketHeaderBy = By.id("sc-active-items-header");

    public By emptyShoppingBasketTableBy = By.id("sc-empty-cart");

    public By basketSubTotalBy = By.id("sc-subtotal-label-activecart");

    public WebElement basketSubTotal() {
        return driver.findElement(basketSubTotalBy);
    }

    public By itemsInOrderBy = By.xpath("//div[@class='sc-item-content-group']");

    public List<WebElement> itemsInOrder() {
        return driver.findElements(itemsInOrderBy);
    }

    public WebElement itemInOrderByName(String title) {
        return itemsInOrder().stream()
                .filter(row -> itemInOrderTitle(row).getText().strip().contains(title.strip()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No basket item with title containing: " + title));
    }

    public WebElement itemInOrderTitle(WebElement row) {
        return row.findElement(By.xpath(".//span[@class='a-truncate-cut']"));
    }

    public WebElement itemInOrderGiftCheckBox(WebElement row) {
        return row.findElement(By.xpath(".//div[contains(@class,'a-checkbox')]//input"));
    }

    public WebElement itemInOrderQuantity(WebElement row) {
        return row.findElement(By.xpath(".//span[@data-a-selector='inner-value']"));
    }

    public WebElement itemFormat(WebElement row) {
        return row.findElement(By.xpath(".//ul[@id='sc-product-variation-list']"));
    }

    public WebElement itemInOrderPrice(WebElement row) {
        return row.findElement(
                By.xpath(".//span[contains(@class,'a-text-price sc-product-price')]"));
    }

    public WebElement availability(WebElement row) {
        return row.findElement(
                By.xpath(".//span[@data-csa-c-content-id='availability']"));
    }

    public WebElement itemDeleteButton(WebElement row) {
        return row.findElement(
                By.xpath(".//button[@data-action='a-stepper-decrement']"));
    }
}
