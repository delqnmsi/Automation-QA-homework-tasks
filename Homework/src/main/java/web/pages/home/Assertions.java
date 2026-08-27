package web.pages.home;

import web.core.BaseAssertions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions extends BaseAssertions<Map> {

    public Assertions(Map map) {
        super(map);
    }

    public void isLoaded() {
        assertTrue(map().tableOfContent().isDisplayed(), "The page didn't load");
    }
}
