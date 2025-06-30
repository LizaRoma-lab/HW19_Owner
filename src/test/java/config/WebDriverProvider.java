package qa.guru.owner.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.http.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WebDriverProvider implements Supplier<WebDriver> {
    private final WebDriverConfig config;

    public WebDriverProvider() {
        this.config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());
    }

    @Override
    public WebDriver get() {
        WebDriver driver = createDriver();
        driver.get(config.getBaseUrl());
        return driver;
    }

    private WebDriver createDriver() {
        return config.isRemote() ? createRemoteDriver() : createLocalDriver();
    }

    private WebDriver createLocalDriver() {
        switch (config.getBrowser()) {
            case CHROME:
                WebDriverManager.chromedriver().browserVersion(config.getBrowserVersion()).setup();
                return new ChromeDriver();
            case FIREFOX:
                WebDriverManager.firefoxdriver().browserVersion(config.getBrowserVersion()).setup();
                return new FirefoxDriver();
            default:
                throw new WebDriverException("Unsupported browser: " + config.getBrowser());
        }
    }

    private WebDriver createRemoteDriver() {
        try {
            Map<String, Object> selenoidOptions = new HashMap<>();
            selenoidOptions.put("enableVNC", true);
            selenoidOptions.put("enableVideo", false);

            switch (config.getBrowser()) {
                case CHROME:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("browserName", "chrome");
                    chromeOptions.setCapability("browserVersion", config.getBrowserVersion());
                    chromeOptions.setCapability("selenoid:options", selenoidOptions);
                    return new RemoteWebDriver(config.remoteUrl(), chromeOptions);
                case FIREFOX:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setCapability("browserName", "firefox");
                    firefoxOptions.setCapability("browserVersion", config.getBrowserVersion());
                    firefoxOptions.setCapability("selenoid:options", selenoidOptions);
                    return new RemoteWebDriver(config.remoteUrl(), firefoxOptions);
                default:
                    throw new WebDriverException("Unsupported remote browser: " + config.getBrowser());
            }
        } catch (Exception e) {
            throw new WebDriverException("\n\n!!! Failed to create remote driver !!!\n" +
                    "URL: " + config.remoteUrl() + "\n" +
                    "Browser: " + config.getBrowser() + "\n" +
                    "Version: " + config.getBrowserVersion() + "\n" +
                    "Error: " + e.getMessage(), e);
        }
    }
}