package selenium.base;


import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.core.DriverManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotOnFailureExtension.class);
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        try {
            WebDriver driver = DriverManager.getDriver();
            Files.createDirectories(SCREENSHOT_DIR);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String fileName = context.getDisplayName().replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = SCREENSHOT_DIR.resolve(fileName);
            Files.copy(source.toPath(), destination);

            log.error("Test failed: {} — screenshot saved to {}", context.getDisplayName(), destination);
        } catch (IOException | IllegalStateException | ClassCastException e) {
            log.warn("Could not capture screenshot on failure: {}", e.getMessage());
        }
    }
}
