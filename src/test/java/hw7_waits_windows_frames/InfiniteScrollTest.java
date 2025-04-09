package hw7_waits_windows_frames;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfiniteScrollTest {
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/infinite-scroll.html";

    @Test
    void infiniteScrollTest() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);

        By paragraphLocator = By.xpath("//p");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(paragraphLocator, 0));
        int scrollNumber = 3;

        for (int i = 0; i < scrollNumber; i++) {
            int paragraphSize = driver.findElements(paragraphLocator).size();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            assertTrue(wait.until(ExpectedConditions
                            .not(ExpectedConditions.numberOfElementsToBe(paragraphLocator, paragraphSize))),
                    "New paragraphs are no longer loading");
        }

        driver.quit();
    }
}
