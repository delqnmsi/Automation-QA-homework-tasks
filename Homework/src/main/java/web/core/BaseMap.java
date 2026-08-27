package web.core;

import org.openqa.selenium.WebDriver;

public abstract class BaseMap {

    protected final WebDriver driver;

    protected BaseMap() {
        this.driver = DriverManager.getDriver();
    }
}
