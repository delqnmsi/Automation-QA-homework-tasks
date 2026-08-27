package web.pages.result;

import org.openqa.selenium.WebElement;
import web.core.BasePage;
import web.enums.ResultsLanguage;

import java.util.List;

public class ResultPage extends BasePage<Map, Assertions> {

    public ResultPage() {
        super(new Map(), new Assertions(new Map()));
    }

    public void selectResultsLanguage(ResultsLanguage language) {
        scrollIntoView(map().filterOnlyResultIn(language));
        map().filterOnlyResultIn(language).click();
        waitUtils.waitForPageToLoad();
    }

    public void selectFirstResultOfferOneItemType() {
        map().firstResultOfferOneType().click();
        waitUtils.waitForPageToLoad();
    }

    public String getFirstResultOfferOnePrice() {
        return (map().firstResultOfferOnePrice().getText().replaceAll("\\s+", "."));
    }

    public void checkingAllResultTitlesInEnglish(String searchTerm, List<WebElement> resultTitle) {
        for (WebElement title : resultTitle) {
            String titleIs = title.getText();
            assertion().allResultsInEnglishAreRelatedToSearchTerm(searchTerm, titleIs);
        }
    }

    //Currently not in use but left here - not in use because of the issue with language filtering
    // - it is a valid search scenario so the method could be used
    public void clearBookLanguageFilter() {
        scrollIntoView(map().bookLanguageFilterClearButtonBy);
        map().bookLanguageFilterClearButton().click();
        waitUtils.waitForPageToLoad();
    }
}
