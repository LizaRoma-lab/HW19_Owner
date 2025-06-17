package qa.guru.owner.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.function.Supplier;

public class WebDriverProvider implements Supplier<WebDriver> {
    private final WebDriverConfig config;
    private final String environment;

    public WebDriverProvider() {
        this.environment = System.getProperty("env", "local");
        this.config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());
    }

    @Override
    public WebDriver get() {
        WebDriver driver = createWebDriver();
        driver.get(config.getBaseUrl());
        return driver;
    }

    private WebDriver createWebDriver() {
        return "remote".equals(environment) ? createRemoteDriver() : createLocalDriver();
    }

    private WebDriver createLocalDriver() {
        switch (config.getBrowser()) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser type");
        }
    }

    private WebDriver createRemoteDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserName", "chrome");
        options.setCapability("browserVersion", "latest");
        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            put("enableVNC", true);
            put("enableVideo", false);
        }});

        try {
            URL remoteUrl = new URL("http://localhost:4444/wd/hub");
            System.out.println("Trying to connect to: " + remoteUrl);
            RemoteWebDriver driver = new RemoteWebDriver(remoteUrl, options);
            System.out.println("Session ID: " + driver.getSessionId());
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create remote driver. Check Selenoid connection.", e);
        }
    }
}