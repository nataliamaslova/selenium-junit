package hw7_waits_windows_frames;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShadowDOMTest {
    private WebDriver driver;

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/shadow-dom.html";
    private static final By SHADOW_ROOT_LOCATOR = By.cssSelector("p");

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @Tag("positive")
    void shadowDOMTest() {
        WebElement content = driver.findElement(By.id("content"));
        SearchContext searchContext = content.getShadowRoot();
        WebElement shadowRootText = searchContext.findElement(SHADOW_ROOT_LOCATOR);

        assertEquals("Hello Shadow DOM", shadowRootText.getText(), "Shadow DOM text is incorrect");
    }

    @Test
    @Tag("negative")
    void shadowDOMNegativeTest() {
        assertThrows(NoSuchElementException.class, () -> driver.findElement(SHADOW_ROOT_LOCATOR));
    }

}
