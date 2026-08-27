package selenium.amazontests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import selenium.base.BaseTest;
import web.enums.BookType;
import web.enums.ResultsLanguage;
import web.pages.books.BookPage;
import web.pages.common.CommonPage;
import web.pages.home.HomePage;
import web.pages.result.ResultPage;
import web.pages.shoppingbasket.ShoppingBasketPage;

public class AmazonBookBuyTests extends BaseTest {

    private HomePage homePage;
    private CommonPage commonPage;
    private BookPage bookPage;
    private ShoppingBasketPage shoppingBasketPage;
    private ResultPage resultPage;

    private final String additionalTitlePart = "Parts One and Two";
    private final String bookTitle = "Harry Potter and the Cursed Child";
    private final String expectedAuthorName = "J.K. Rowling";

    @BeforeEach
    public void initPages() {

        homePage = new HomePage();
        commonPage = new CommonPage();
        bookPage = new BookPage();
        shoppingBasketPage = new ShoppingBasketPage();
        resultPage = new ResultPage();

        homePage.navigate();
        wait.waitForPageToLoad();
    }

    @Test
    public void correctPageLoads_whenTheUserNavigatesToAmazon() {

        homePage.assertion().isLoaded();
        commonPage.assertion().isLoaded();
    }

    @Test
    public void bookSearchWorksAsExpected_when_searchByBookTitle() {

        String resultsRelatedTo = "Harry Potter";

        commonPage.searchByAnyText(bookTitle);

        resultPage.assertion().isLoaded();
        resultPage.assertion().searchItemFoundByTitleIsLoaded(bookTitle);

        resultPage.assertion().firstResultTitleContainsText(bookTitle);
        resultPage.assertion().firstResultTitleContainsText(additionalTitlePart);
        resultPage.assertion().firstResultOfferOneTypeIs(BookType.PAPERBACK);
        resultPage.assertion().firstResultOfferOnePriceIsPresent();
        //additional validation :
        resultPage.assertion().firstResultOfferOneAddToBasketButtonIsDisplayed();
        resultPage.assertion().firstResultAuthorIs(expectedAuthorName);
        resultPage.assertion().firstResultHasPositiveRating();
        resultPage.assertion().firstResultHasDeliveryDates();

        resultPage.selectResultsLanguage(ResultsLanguage.ENGLISH);
        resultPage.checkingAllResultTitlesInEnglish(resultsRelatedTo, resultPage.map().allResultTitle());
        //at the moment of creation of this test , the Language filter was buggy:
        // after clearing the filter a result set was different from the initially loaded one
        // and the list of languages was different also so I left out Language filter validation

    }

    @Test
    public void correctItemAttributesLoad_when_anItemIsSelectedFromTheResultList() {

        String artistsContribution = "(Author)";

        commonPage.searchByAnyText(bookTitle);

        resultPage.assertion().isLoaded();
        resultPage.assertion().searchItemFoundByTitleIsLoaded(bookTitle);
        resultPage.assertion().firstResultTitleContainsText(additionalTitlePart);

        String expectedTitle = resultPage.map().firstResultTitle().getText();
        String firstOfferPrice = resultPage.getFirstResultOfferOnePrice();

        resultPage.selectFirstResultOfferOneItemType();

        bookPage.assertion().isLoaded();

        bookPage.assertion().selectedBookTitleIsCorrect(expectedTitle);
        bookPage.assertion().bookTypeIsSelected(BookType.PAPERBACK);
        bookPage.assertion().selectedBookPriceIsCorrect(firstOfferPrice);
        //additional validation :
        bookPage.assertion().singleSellCharacteristicsSetIsSelected();
        bookPage.assertion().itemIsPurchaseReady();
        bookPage.assertion().theBookHasExpectedArtist(expectedAuthorName, artistsContribution);
        bookPage.assertion().theBookHasPositiveRating();

    }

    @Test
    public void itemIsSuccessfullyAddedAndRemovedFromBasket_when_itIsSelectedAsAGift() {

        commonPage.searchByAnyText(bookTitle);
        resultPage.assertion().isLoaded();

        resultPage.assertion().searchItemFoundByTitleIsLoaded(bookTitle);

        String expectedTitle = resultPage.map().firstResultTitle().getText();
        String firstOfferPrice = resultPage.getFirstResultOfferOnePrice();
        String expectedItemFormat = "Format: " + BookType.PAPERBACK.getBookType();
        String availability = " In stock ";
        int expectedQuantity = 1;

        resultPage.selectFirstResultOfferOneItemType();

        bookPage.selectGiftOption();
        bookPage.addItemToBasket(BookType.PAPERBACK);

        commonPage.navigateToShoppingBasket();
        wait.waitForPageToLoad();

        shoppingBasketPage.assertion().isLoaded();
        shoppingBasketPage.assertion().onlyOneItemInOrder();
        //additional validations added in the following assertions
        shoppingBasketPage.assertion().orderComponentsAreCorrect(
                expectedTitle,
                expectedItemFormat,
                availability,
                true,
                expectedQuantity,
                firstOfferPrice);

        commonPage.assertion().numberOfItemInBasketIs(expectedQuantity);

        shoppingBasketPage.removeItemByName(expectedTitle);
        shoppingBasketPage.assertion().itemsAreRemoved();
    }
}
