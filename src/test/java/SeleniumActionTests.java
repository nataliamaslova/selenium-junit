import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumActionTests {
    private WebDriver driver;
    private Actions actions;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";

    @BeforeEach
    void start() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        actions = new Actions(driver);
    }

    @AfterEach
    void close() {
        driver.close();
    }

    @Test
    void submitFormTest() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

//        driver.findElement(By.xpath("//form")).submit();

        WebElement button = driver.findElement(By.xpath("//button"));
        actions.moveToElement(button).click().perform();
        Thread.sleep(1000);
    }

    @Test
    void selectDropdownTest() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        WebElement selectElement = driver.findElement(By.name("my-select"));
        Select select = new Select(selectElement);
        String actualText = select.getFirstSelectedOption().getText();
        assertEquals("Open this select menu", actualText);

        select.selectByValue("2");
        assertEquals("Two", select.getFirstSelectedOption().getText());
        select.selectByIndex(0);
        assertEquals("Open this select menu", actualText);
        Thread.sleep(1000);
        List<WebElement> options = select.getOptions();
        assertEquals(4, options.size());
    }

    @Test
    void selectFromListTests() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        //select by id
        WebElement dropdownSelectMenu = driver.findElement(By.name("my-select"));
        Select select = new Select(dropdownSelectMenu);
        Thread.sleep(1000);
        select.selectByIndex(1);
        Thread.sleep(1000);
        select.selectByIndex(0);
        //select by value
        Thread.sleep(1000);
        select.selectByValue("2");
        //select by text
        Thread.sleep(1000);
        select.selectByVisibleText("Three");

        //first selected option
        assertEquals("Three", select.getFirstSelectedOption().getText());
        assertTrue(select.getFirstSelectedOption().isSelected());

        //get all selected options
        List<WebElement> selectedOptions = select.getAllSelectedOptions();
        for (WebElement selectedOption : selectedOptions) {
            System.out.println("Selected option: " + selectedOption.getText());
        }
        Thread.sleep(1000);

        //get all options
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            System.out.printf("Available Option: %s isSelected = %s%n", option.getText(), option.isSelected());
        }
        Thread.sleep(1000);

        //deselecting
        if (select.isMultiple()) {
            select.deselectByIndex(1);
            select.deselectByValue("1");
            select.deselectByVisibleText("One");
            select.deselectAll();
        } else {
            System.out.println("You may only deselect all options of a multi-select");
        }
        Thread.sleep(1000);
    }

    @Test
    void getInfoTests() {
        //get isDisplayed
        WebElement webFormButton = driver.findElement(By.xpath("//a[@href = 'web-form.html']"));
        assertTrue(webFormButton.isDisplayed());
        webFormButton.click();

        //get isEnabled
        WebElement disabledInput = driver.findElement(By.name("my-disabled"));
        assertFalse(disabledInput.isEnabled());

        //check exception
        assertThrows(ElementNotInteractableException.class, () -> disabledInput.sendKeys("test"));

        //get tag name
        assertEquals("input", disabledInput.getTagName());

        //get rect
        Rectangle rec = disabledInput.getRect();
        System.out.printf("Dimension %s, Height %s, Width %s, Point %s, X: %s, Y: %s\n", rec.getDimension(), rec.getHeight(), rec.getWidth(), rec.getPoint(), rec.getX(), rec.getY());

        //get css values
        System.out.println(disabledInput.getCssValue("background-color"));
        System.out.println(disabledInput.getCssValue("opacity"));
        System.out.println(disabledInput.getCssValue("font-size"));
        System.out.println(disabledInput.getCssValue("color"));

        //get text
        assertEquals("Disabled input", disabledInput.findElement(By.xpath("..")).getText());

        //get attribute
        assertEquals("Disabled input", disabledInput.getAttribute("placeholder"));
        assertEquals("Disabled input", disabledInput.getDomAttribute("placeholder"));
    }

    @Test
    void fileUploadTest() throws IOException, InterruptedException {
        String filePath = "src/test/resources/hw5_locators.txt";

        // Чтение содержимого файла в виде строки
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        // Используйте содержимое файла в вашем коде, например, вывод на экран
        System.out.println("Содержимое файла: " + content);

        // Получаем URL ресурса
        URL url = SeleniumActionTests.class.getClassLoader().getResource("hw5_locators.txt");

        String absolutePath = null;
        if (url != null) {
            // Получаем абсолютный путь к файлу
            absolutePath = new File(url.getPath()).getAbsolutePath();
            System.out.println("Абсолютный путь к файлу: " + absolutePath);
        } else {
            System.out.println("Ресурс не найден.");
        }
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        WebElement fileUpload = driver.findElement(By.name("my-file"));
        fileUpload.sendKeys(absolutePath);
        Thread.sleep(5000);
        WebElement submit = driver.findElement(By.xpath("//button[text()='Submit']"));
        submit.click();
        Thread.sleep(5000);
        assertTrue(driver.getCurrentUrl().contains("hw5_locators.txt"));
    }

    @Test
    void actionAPIMouseOverTests() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/mouse-over.html");

        List<WebElement> images = driver.findElements(By.className("img-fluid"));
        for (WebElement image : images) {
            Actions actions = new Actions(driver);
            actions.moveToElement(image).perform();
            Thread.sleep(1000);
        }
    }

    @Test
    void actionAPIMouseClickTests() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html");
        Thread.sleep(2000);

        WebElement dropdown1 = driver.findElement(By.id("my-dropdown-1"));
        actions.click(dropdown1).perform();
        Thread.sleep(2000);

        WebElement dropdown2 = driver.findElement(By.id("my-dropdown-2"));
        actions.contextClick(dropdown2).perform();
        Thread.sleep(2000);

        WebElement dropdown3 = driver.findElement(By.id("my-dropdown-3"));
        actions.doubleClick(dropdown3).perform();
        Thread.sleep(2000);
    }

    @Test
    void actionAPIScrollTests() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
        Thread.sleep(2000);

        WebElement footerLink = driver.findElement(By.className("text-muted"));
        actions.scrollToElement(footerLink).perform();
        Thread.sleep(2000);
    }

    @Test
    void actionAPIDragAndDropTests() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html");
        Thread.sleep(2000);

        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("target"));
        actions
                .scrollToElement(draggable)
//                .scrollByAmount(0, 300)
                .dragAndDrop(draggable, droppable).perform();
        Thread.sleep(2000);
    }

    @Test
    void colorPickerTest() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement colorPicker = driver.findElement(By.name("my-colors"));
        String initColor = colorPicker.getDomAttribute("value");
        System.out.println("Initial color is: " + initColor);

        Color red = new Color(255, 0, 0, 1);
        String script = String.format("arguments[0].setAttribute('value', '%s');", red.asHex());
        Thread.sleep(3000);
        js.executeScript(script, colorPicker);

        String finalColor = colorPicker.getDomAttribute("value");
        System.out.println("Final color is: " + finalColor);
        assertNotEquals(finalColor, initColor);
        assertEquals(Color.fromString(finalColor), red);
        Thread.sleep(3000);
    }

    @Test
    void actionAPIKeyboardTests() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        WebElement input = driver.findElement(By.id("my-text-id"));
        input.click();

        actions
                .keyDown(Keys.SHIFT)
                .sendKeys("upper-case")
                .keyUp(Keys.SHIFT)
                .sendKeys("lower-case")
                .perform();
        Thread.sleep(2000);
        assertEquals("UPPER_CASElower-case", input.getAttribute("value"));

        WebElement password = driver.findElement(By.name("my-password"));
        actions
                .sendKeys(password, "admin123")
                .perform();
        Thread.sleep(2000);
        assertEquals("admin123", password.getAttribute("value"));
    }

    @Test
    void spaceTest() throws InterruptedException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
        Thread.sleep(2000);

        actions
                .keyDown(Keys.SPACE)
                .perform();

        Thread.sleep(2000);
    }
}
