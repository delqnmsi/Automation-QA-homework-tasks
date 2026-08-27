package web.core;

import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public abstract class BaseAssertions<M extends BaseMap> {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;
    private final M map;

    protected BaseAssertions(M map) {
        this.driver = DriverManager.getDriver();
        this.waitUtils = new WaitUtils(driver);
        this.map = map;
    }

    protected M map() {
        return map;
    }
}
