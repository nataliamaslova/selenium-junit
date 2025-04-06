import generators.RandomData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openqa.selenium.PageLoadStrategy.NONE;

public class SeleniumDriverTests {
    private WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";


    @AfterEach
    void close() {
        driver.quit();
    }

    @Test
    void chromeTest() {
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.setPlatformName("Windows 10"); //set remote OS
        chromeOptions.setBrowserVersion("123.0.6312.86");
        chromeOptions.setPageLoadStrategy(NONE);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setPageLoadTimeout(Duration.ofSeconds(60));
        chromeOptions.setScriptTimeout(Duration.ofSeconds(30));
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(5));
        driver = new ChromeDriver(chromeOptions);

        driver.get(BASE_URL);
        String html = driver.getPageSource();
        assertEquals("Hands-On Selenium WebDriver with Java", driver.getTitle());
        assertThat(html).contains("Hands-On Selenium WebDriver with Java");
    }

    @Test
    void iframeTest() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.get(BASE_URL + "/iframes.html");
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("lead")));
        WebElement iframeElement = driver.findElement(By.id("my-iframe"));

        driver.switchTo().frame(iframeElement);
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("display-6")));
        assertThat(driver.findElement(By.className("lead")).getText()).contains("Lorem ipsum dolor sit amet");
        driver.switchTo().defaultContent();
        assertThat(driver.findElement(By.className("display-6")).getText()).contains("IFrame");
    }

    @Test
    void dialogBoxesTest() {
        driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get(BASE_URL + "/dialog-boxes.html");
        driver.findElement(By.id("my-alert")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText()).isEqualTo("Hello world!");
        alert.accept();
        driver.findElement(By.id("my-confirm")).click();
        driver.switchTo().alert().accept();
        assertThat(driver.findElement(By.id("confirm-text")).getText()).isEqualTo("You chose: true");
        driver.findElement(By.id("my-confirm")).click();
        driver.switchTo().alert().dismiss();
        assertThat(driver.findElement(By.id("confirm-text")).getText()).isEqualTo("You chose: false");
        driver.findElement(By.id("my-prompt")).click();
        String testData = RandomData.getString();
        driver.switchTo().alert().sendKeys(testData);
        driver.switchTo().alert().accept();
        assertThat(driver.findElement(By.id("prompt-text")).getText()).isEqualTo("You typed: " + testData);
        WebElement modal = driver.findElement(By.id("my-modal"));
        wait.until(ExpectedConditions.elementToBeClickable(modal));
        modal.click();
        WebElement save = driver.findElement(By.xpath("//button[normalize-space() = 'Save changes']"));
        wait.until(ExpectedConditions.elementToBeClickable(save));
        save.click();
    }

    @Test
    void navigateTest() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        driver.navigate().to(BASE_URL + "/web-form.html");
        driver.navigate().back();
        assertEquals(BASE_URL, driver.getCurrentUrl());
        driver.navigate().forward();
        driver.navigate().refresh();
        assertEquals(BASE_URL + "/web-form.html", driver.getCurrentUrl());
    }

    @Test
    void testNewTab() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        String initHandle = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(BASE_URL +"/web-form.html");
        assertThat(driver.getWindowHandles()).hasSize(2);

        driver.switchTo().window(initHandle);
        driver.close();
        assertThat(driver.getWindowHandles()).hasSize(1);
    }

    @Test
    void testNewWindow() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        String initHandle = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(BASE_URL + "/web-form.html");
        assertThat(driver.getWindowHandles()).hasSize(2);

        driver.switchTo().window(initHandle);
        driver.close();
        assertThat(driver.getWindowHandles()).hasSize(1);
    }

    @Test
    void WindowTest() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        WebDriver.Window window = driver.manage().window();

        Point initialPosition = window.getPosition();
        Dimension initialSize = window.getSize();
        System.out.printf("Initial window: position {%s} -- size {%s}\n", initialPosition, initialSize);

        window.maximize();

        Point maximizedPosition = window.getPosition();
        Dimension maximizedSize = window.getSize();
        System.out.printf("Maximized window: position {%s} -- size {%s}\n", maximizedPosition, maximizedSize);

        assertThat(initialPosition).isNotEqualTo(maximizedPosition);
        assertThat(initialSize).isNotEqualTo(maximizedSize);
    }
}
