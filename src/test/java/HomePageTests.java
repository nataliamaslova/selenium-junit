import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomePageTests {
    private WebDriver driver;
    private Actions actions;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        actions = new Actions(driver);
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void openHomePageTest() {
        driver.get(BASE_URL);
        driver.manage().window().maximize();
        String actualTitle = driver.getTitle();

        assertEquals("Hands-On Selenium WebDriver with Java", actualTitle);
    }

    @Test
    void openWebFormTest() {
        String webFormUrl = "web-form.html";
        //driver.findElement(By.linkText("Web form")).click();
        driver.findElement(By.xpath("//h5[text()='Chapter 3. WebDriver Fundamentals']/../a[contains(@href, 'web-form')]")).click();
        String currentUrl = driver.getCurrentUrl();
        WebElement title = driver.findElement(By.className("display-6"));

        assertEquals(BASE_URL + webFormUrl, currentUrl);
        assertEquals("Web form", title.getText());
    }

    @Test
    void openAllLinksTest() {
        int linksCount = 0;
        driver.get(BASE_URL);
        List<WebElement> chapters = driver.findElements(By.xpath("//h5[@class='card-title']")); //cssSelector("h5.card-title")
        for (WebElement chapter : chapters) {
            System.out.println(chapter.getText());
            List<WebElement> links = chapter.findElements(By.xpath("./../a"));
            linksCount += links.size();
            for (WebElement link : links) {
                System.out.println(link.getText());
                actions.moveToElement(link).click().perform();
                driver.navigate().back();
            }
        }
        assertEquals(6, chapters.size());
        assertEquals(27, linksCount);
    }

    @Test
    void classesTest() {
        List<WebElement> links = driver.findElements(By.cssSelector(".btn.btn-outline-primary.mb-2"));
        assertEquals(27, links.size());
    }
}
