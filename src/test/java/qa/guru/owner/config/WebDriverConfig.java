package qa.guru.owner.config;

import org.aeonbits.owner.Config;

import java.net.URL;

public interface WebDriverConfig extends Config {

    @Key("baseUrl")
    @DefaultValue("https://github.com")
    String getBaseUrl();

    @Key("browser")
    @DefaultValue("CHROME")
    Browser getBrowser();

    @Key("remote.Url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String getRemoteUrl();

    @Key("browser.Version")
    @DefaultValue("latest")
    String getBrowserVersion();

}
