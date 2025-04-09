package hw7_waits_windows_frames;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DialogBoxesTest {
    private WebDriver driver;

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html";
    private static final String DIALOG_BOX_XPATH = "//button[@id='my-%s']";
    private static final String ALERT_RESULTS_XPATH = DIALOG_BOX_XPATH + "/following-sibling::p";

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
    void defaultViewTest() {
        List<String> buttons = driver.findElements(By.xpath("//div[@class='col-sm-2 py-2']/button"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        assertEquals(List.of("Launch alert", "Launch confirm", "Launch prompt", "Launch modal"), buttons, "Incorrect launch buttons display");
    }

    @Test
    void launchAlertTest() {
        clickLaunchButton("alert", false);
        assertEquals("Hello world!", driver.switchTo().alert().getText(), "Incorrect alert text");
    }

    @Test
    void launchConfirmTest() {
        String button = "confirm";
        clickLaunchButton(button, false);
        Alert alert = driver.switchTo().alert();
        assertEquals("Is this correct?", alert.getText(), "Incorrect alert text");

        alert.accept();
        assertEquals("You chose: true", driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText());

        clickLaunchButton(button, false);
        driver.switchTo().alert().dismiss();
        assertEquals("You chose: false", driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText());
    }

    @Test
    void launchPromptTest() {
        String button = "prompt";
        clickLaunchButton(button, false);
        Alert alert = driver.switchTo().alert();
        assertEquals("Please enter your name", alert.getText(), "Incorrect alert text");

        alert.accept();
        assertEquals("You typed:", driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText());

        clickLaunchButton(button, false);
        String nameToEnter = "NAME";
        alert.sendKeys(nameToEnter);
        alert.accept();
        assertEquals("You typed: " + nameToEnter, driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText());

        clickLaunchButton(button, false);
        driver.switchTo().alert().dismiss();
        assertEquals("You typed: null", driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText());
    }

    @Test
    void launchModalTest() {
        String button = "modal";
        clickLaunchButton(button, true);
        assertAll(
                () -> assertEquals("Modal title", driver.findElement(By.xpath("//h5[@class='modal-title']")).getText(),
                        "Incorrect modal title"),
                () -> assertEquals("This is the modal body", driver.findElement(By.xpath("//div[@class='modal-body']")).getText(),
                        "Incorrect modal body")
        );

        driver.findElement(By.xpath("//button[contains(@class, 'secondary')]")).click();
        assertEquals("You chose: Close", driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText(),
                "Selection is wrong");

        clickLaunchButton(button, true);
        driver.findElement(By.xpath("//button[contains(@class, 'btn-primary')]")).click();
        assertEquals("You chose: Save changes", driver.findElement(By.xpath(String.format(ALERT_RESULTS_XPATH, button))).getText(),
                "Selection is wrong");
    }

    private void clickLaunchButton(String button, Boolean isModal) {
        driver.findElement(By.xpath(String.format(DIALOG_BOX_XPATH, button))).click();
        if (isModal) {
            new WebDriverWait(driver, Duration.ofSeconds(1)).until(ExpectedConditions
                    .visibilityOf(driver.findElement(By.xpath("//div[@class='modal-content']"))));
        }
    }
}
