package web.pages.books;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import web.core.BasePage;
import web.enums.BookType;
import web.modal.selectitemasagift.ItemAsGiftPage;

public class BookPage extends BasePage<Map, Assertions> {


    public BookPage() {
        super(new Map(), new Assertions(new Map()));
    }

    ItemAsGiftPage itemAsGiftPage = new ItemAsGiftPage();

    public void selectGiftOption() {

        scrollIntoView(map().selectAsAGiftButtonBy);

        map().selectAsAGiftButton().click();

        By modalBy = itemAsGiftPage.map().giftOptionModalBy;
        waitUtils.waitForElementToBeVisible(modalBy);

        checkCheckBox(itemAsGiftPage.map().giftOptionModalCheckBoxBy);

        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        waitUtils.waitForElementToDisappear(modalBy);
        waitUtils.waitForElementToDisappear(itemAsGiftPage.map().giftOptionModalBackdropBy);
    }

    private void checkCheckBox(By locator) {
        waitUtils.waitForElementToBeClickable(locator);
        waitUtils.fluentWait(500).until(d -> {
            WebElement checkBox = d.findElement(locator);
            if (!checkBox.isSelected()) {
                checkBox.click();
            }
            return checkBox.isSelected();
        });
    }

    public void addItemToBasket(BookType type) {
        scrollIntoView(map().frameBuyNewBy);

        waitUtils.waitForElementToBeVisible(map().selectAsAGiftReverseButtonBy);
        assertion().selectedAsAGiftReverseButtonIsPresent("This item is a gift. Change");

        waitUtils.waitForElementToDisappear(itemAsGiftPage.map().giftOptionModalBackdropBy);

        waitUtils.waitForElementToBeClickable(map().addToBasketButtonBy);
        waitUtils.waitForElementPositionToStabilize(map().addToBasketButton());
        map().addToBasketButton().click();

        waitUtils.waitForUrlContains("https://www.amazon.co.uk/cart");
        waitUtils.waitForElementToBePresent(map().addedToBasketConfirmationBy);

        assertion().addToBasketMessageIsCorrect("Added to basket");
        assertion().correctItemAddedToBasket(type);
    }
}
