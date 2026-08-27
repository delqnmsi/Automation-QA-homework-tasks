package selenium.base;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;
import web.core.ConfigReader;
import web.core.DriverFactory;
import web.core.DriverManager;

@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseTest {

    protected WebDriver driver;
    protected ConfigReader config;
    protected WaitUtils wait;


    @BeforeEach
    void setUp() {

        DriverFactory.initDriver();

        driver = DriverManager.getDriver();
        config = ConfigReader.getInstance();
        wait = new WaitUtils(driver);
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }
}
