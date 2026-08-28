package web.core;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized for this thread. Call DriverFactory.initDriver() first (BaseTest does this in @BeforeEach).");
        }
        return driver;
    }

    static WebDriver peekDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    static void removeDriver() {
        DRIVER_THREAD_LOCAL.remove();
    }
}
