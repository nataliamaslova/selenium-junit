import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BonigarciaTests {
    private WebDriver driver;
    private Actions actions;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @BeforeAll
    void setup() {
        driver = new ChromeDriver();
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }

    @Test
    @Order(1)
    public void testAll6Chapters() {
        driver.get(BASE_URL);
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='card-body']"));

        Assertions.assertEquals(6, list.size());
    }

    @ParameterizedTest
    @Order(2)
    @CsvFileSource(resources = "/testdata.csv", numLinesToSkip = 1)
    void openLinkTest(String chapterName,
                      String linkUrl, String expectedHeaderName) {

        driver.get(BASE_URL);
        WebElement linkInChapter = driver.findElement(By
                .xpath("//h5[text()='" + chapterName +
                        "']/../a[contains(@href, '" + linkUrl + "')]"));

        actions.moveToElement(linkInChapter).click().perform();

        String actualUrl = driver.getCurrentUrl();
        WebElement actualHeaderName = driver.findElement(By.cssSelector(".display-6"));
        actions.moveToElement(actualHeaderName).perform();

        assertAll(
                () -> assertEquals(BASE_URL + linkUrl, actualUrl, "Url doesn't match"),
                () -> assertEquals(expectedHeaderName, actualHeaderName.getText(), "Header doesn't match")
        );
    }

    @Test
    @Order(3)
    void openLinkFrameTest() {
        // Chapter 4. Browser-Agnostic Features,frames.html,Frames
        String chapterName = "Chapter 4. Browser-Agnostic Features";
        String linkUrl = "frames.html";
        String expectedHeaderName = "Frames";

        driver.get(BASE_URL);
        WebElement linkInChapter = driver.findElement(By
                .xpath("//h5[text()='" + chapterName +
                        "']/../a[contains(@href, '" + linkUrl + "')]"));
        linkInChapter.click();

        // Frame handling
        WebElement frameElement = driver.findElement(By
                    .cssSelector("frame[name='frame-header']"));
        driver.switchTo().frame(frameElement);

        String actualUrl = driver.getCurrentUrl();
        WebElement actualHeaderName = driver.findElement(By.className("display-6"));


        assertAll(
                () -> assertEquals(BASE_URL + linkUrl, actualUrl, "Url doesn't match"),
                () -> assertEquals(expectedHeaderName, actualHeaderName.getText(), "Header doesn't match")
        );
    }

    @Disabled
    @ParameterizedTest
    @CsvFileSource(resources = "/testdata.csv", numLinesToSkip = 1)
    void openLinksByTextTest(String chapterName, String linkUrl, String headerName) {
        driver.get(BASE_URL);
        WebElement chapterSection = driver.findElement(By.xpath("//div[h5[text()='" + chapterName + "']]"));
        Assertions.assertEquals(
                chapterName,
                chapterSection.findElement(By.tagName("h5")).getText(),
                "Chapter name должен быть " + chapterName);

        WebElement linkInChapter = chapterSection.findElement(By.linkText(headerName));

        linkInChapter.click();
        Assertions.assertEquals(BASE_URL + linkUrl, driver.getCurrentUrl(), "URL должен соответствовать ожидаемому значению");

        String actualHeading = driver.findElement(By.cssSelector("h1.display-6")).getText();
        Assertions.assertTrue(
                actualHeading.contains(headerName), "Заголовок должен содержать текст: " + headerName);
    }
}
