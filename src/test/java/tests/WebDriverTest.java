package tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebDriverTest extends TestBase {


    @Test
    public void testGithub() {
        String actualTitle = driver.getTitle();
        assertTrue(actualTitle.contains("GitHub"),
                "Заголовок страницы должен содержать 'GitHub'. Актуальный заголовок: " + actualTitle);
    }
}

