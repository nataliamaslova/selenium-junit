import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SeleniumWaitTests {
    private WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @BeforeEach
    void start() {
        driver = new ChromeDriver();
        driver.get(BASE_URL + "/loading-images.html");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void loadingImagesImplicitWaitTest() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement landscape = driver.findElement(By.id("landscape"));
        assertThat(landscape.getDomProperty("src")).containsIgnoringCase("landscape");
    }

    @Test
    void loadingImagesExplicitWaitTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement landscape = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("landscape")));
        assertThat(landscape.getDomProperty("src")).containsIgnoringCase("landscape");
    }

    @Test
    void loadingImagesFluentWaitTest() {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement landscape = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("landscape")));
        assertThat(landscape.getDomProperty("src")).containsIgnoringCase("landscape");
    }
}
