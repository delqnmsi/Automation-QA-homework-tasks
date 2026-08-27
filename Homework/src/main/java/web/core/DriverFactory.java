package web.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static void initDriver() {
        ConfigReader config = ConfigReader.getInstance();
        WebDriver driver = createDriver(config.getBrowser().toLowerCase(), config.isHeadless());

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeoutSeconds()));
        driver.manage().window().maximize();

        DriverManager.setDriver(driver);
    }

    private static WebDriver createDriver(String browser, boolean headless) {
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                return new EdgeDriver(edgeOptions);

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--window-size=1920,1080");
                return new ChromeDriver(chromeOptions);
        }
    }

    public static void quitDriver() {
        WebDriver driver = DriverManager.peekDriver();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } finally {
            DriverManager.removeDriver();
        }
    }
}
