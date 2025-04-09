package hw7_waits_windows_frames;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FramesTest {
    private WebDriver driver;

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/iframes.html";

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
    void framesTest() throws IOException {
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        Boolean isPageScrolledToBottom = (Boolean) new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions
                .jsReturnsValue("return window.scrollY + window.innerHeight >= document.body.scrollHeight;"));
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("src/test/resources/image.png"));
        assertTrue(isPageScrolledToBottom);

        driver.switchTo().defaultContent();
        assertThat(driver.findElement(By.className("display-6")).getText()).contains("IFrame");
    }

    @Test
    @Tag("negative")
    void framesNegativeTest() {
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.xpath("//p")));
    }

}
