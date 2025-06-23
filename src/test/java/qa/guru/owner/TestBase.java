package qa.guru.owner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import qa.guru.owner.config.WebDriverProvider;

public class TestBase {
    protected WebDriver driver;

    @BeforeEach
    public void startDriver() {
        driver = new WebDriverProvider().get();
    }

    @AfterEach
    public void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
