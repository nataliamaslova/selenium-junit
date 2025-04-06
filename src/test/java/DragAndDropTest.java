import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DragAndDropTest {
    private WebDriver driver;
    private Actions actions;
    private static final String DRAG_AND_DROP_URL =
            "https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.get(DRAG_AND_DROP_URL);
    }

    @AfterEach
    void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void dragAndDropTest() throws InterruptedException {
        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement target = driver.findElement(By.id("target"));
        Thread.sleep(3000);

        actions.dragAndDrop(draggable, target).perform();
        Thread.sleep(3000);

        assertEquals(draggable.getLocation(), target.getLocation(),"Element is not moved to target");
    }
}
