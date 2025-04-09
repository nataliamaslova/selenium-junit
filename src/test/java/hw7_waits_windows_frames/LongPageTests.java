package hw7_waits_windows_frames;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongPageTests {
    private WebDriver driver;
    private Actions actions;

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/long-page.html";
    public static final String FOOTER_LINK_TXT = "Copyright © 2021-2025";

    @BeforeEach
    void start() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        actions = new Actions(driver);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void longPageActionsScrollToFooterTest(){
        WebElement footerLink = driver.findElement(By.className("text-muted"));
        actions.scrollToElement(footerLink).perform();

        assertEquals("Copyright © 2021-2025 Boni García", footerLink.getText());
    }

    @Test
    void longPageScrollWithPageDownTest() {

        WebElement footerLink = driver.findElement(By.className("text-muted"));
        int maxPageDownSteps = 3;
        for (int i = 0; i < maxPageDownSteps; i++) {
            actions.keyDown(Keys.PAGE_DOWN).perform();
        }

        assertThat(footerLink.getText()).contains(FOOTER_LINK_TXT);
    }

    @Test
    void longPageScrollWithJSTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement footerLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("text-muted")));
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, footerLink);

        assertThat(footerLink.getText()).contains(FOOTER_LINK_TXT);
    }
}
