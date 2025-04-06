import generators.RandomData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WebFormTests {
    private WebDriver driver;
    private Actions actions;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    private static final String EMPTY_TEXT = "";
    private final Path TXT_FILE = Paths.get("src/test/resources/hw5_locators.txt");

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get(BASE_URL);
        actions = new Actions(driver);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void textInputTest() throws InterruptedException {
        WebElement textInputField = driver.findElement(By.id("my-text-id"));
        String textInputData = RandomData.getString();

        textInputField.sendKeys(textInputData);
        Thread.sleep(2000);

        assertEquals(textInputData, textInputField.getDomProperty("value"), "The text is incorrect");
    }

    @Test
    void textInputClearTest() throws InterruptedException {
        WebElement textInputField = driver.findElement(By.id("my-text-id"));
        String textInputData = RandomData.getString();

        textInputField.sendKeys(textInputData);
        textInputField.clear();
        Thread.sleep(2000);

        assertEquals(EMPTY_TEXT, textInputField.getDomProperty("value"), "Text input should be cleared");
    }

    @Test
    void passwordInputTest() throws InterruptedException {
        WebElement passwordInputField = driver.findElement(By.cssSelector("input[name='my-password']"));
        String textInputData = RandomData.getString();

        passwordInputField.sendKeys(textInputData);
        Thread.sleep(2000);

        assertEquals(textInputData,
                passwordInputField.getDomProperty("value"), "The text is incorrect");
    }

    @Test
    void longTextAreaTest() throws IOException, InterruptedException {
        WebElement textArea = driver.findElement(By.cssSelector("textarea.form-control"));
        String textInputData =  Files.readString(TXT_FILE);
        textInputData = textInputData.replace("\r\n", "\n");

        textArea.sendKeys(textInputData);
        Thread.sleep(2000);

        assertEquals(textInputData, textArea.getDomProperty("value"), "Text should be from file");
    }

    @Test
    void disabledInputTest() throws InterruptedException {
        WebElement disabledElement = driver.findElement(By.name("my-disabled"));
        Thread.sleep(2000);

        assertFalse(disabledElement.isEnabled(), "Field should be turned off");
        assertEquals(
                "Disabled input", disabledElement.getDomProperty("placeholder"), "Wrong placeholder");
        assertThrows(
                ElementNotInteractableException.class, () -> disabledElement.sendKeys(RandomData.getString()));
    }

    @Test
    void readOnlyInputTest() throws InterruptedException {
        WebElement readonlyElement = driver.findElement(By.name("my-readonly"));
        actions.moveToElement(readonlyElement).perform();

        Assertions.assertTrue(
                readonlyElement.isEnabled(), "Field should not be turned off - it's readonly");
        Assertions.assertNotNull(readonlyElement.getDomAttribute("readonly"));

        readonlyElement.sendKeys(RandomData.getString());
        Thread.sleep(2000);

        assertEquals("Readonly input",
                readonlyElement.getDomProperty("value"),
                "Readonly field should not be changed");
    }

    @Test
    void selectDefaultTest() throws InterruptedException {
        WebElement dropdownSelectMenu = driver.findElement(By.name("my-select"));
        Select select = new Select(dropdownSelectMenu);
        Thread.sleep(2000);

        assertEquals("Open this select menu", select.getFirstSelectedOption().getText());
    }

    @Test
    void selectOptionsTest() throws InterruptedException {
        WebElement dropdownSelectMenu = driver.findElement(By.name("my-select"));
        Select select = new Select(dropdownSelectMenu);
        select.selectByVisibleText("One");
        Thread.sleep(2000);

        assertEquals("One", select.getFirstSelectedOption().getText());
        select.selectByValue("2");
        Thread.sleep(2000);

        assertEquals("Two", select.getFirstSelectedOption().getText());
        select.selectByIndex(3);
        Thread.sleep(2000);

        assertEquals("Three", select.getFirstSelectedOption().getText());
    }

    @Test
    void typingDropdownTest() throws InterruptedException {
        WebElement dynamicDropdown = driver.findElement(By.name("my-datalist"));
        dynamicDropdown.sendKeys("New Y");
        Thread.sleep(2000);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = 'New York'; arguments[0].dispatchEvent(new Event('change'));", dynamicDropdown);

        assertEquals("New York", dynamicDropdown.getDomProperty("value"), "City is chosen incorrectly");
    }

    @Test
    void selectAnotherValueDynamicDropdownTest() throws InterruptedException {
        WebElement dynamicDropdown = driver.findElement(By.name("my-datalist"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        dynamicDropdown.sendKeys("new yo");
        js.executeScript("arguments[0].value = 'New York'; arguments[0].dispatchEvent(new Event('change'));", dynamicDropdown);
        while (!dynamicDropdown.getDomProperty("value").equals(EMPTY_TEXT)) {
            dynamicDropdown.sendKeys(Keys.BACK_SPACE);
        }

        dynamicDropdown.sendKeys("sea");
        Thread.sleep(2000);

        js.executeScript("arguments[0].value = 'Seattle'; arguments[0].dispatchEvent(new Event('change'));", dynamicDropdown);
        assertEquals("Seattle", dynamicDropdown.getDomProperty("value"), "City is chosen incorrectly");
    }

    @Test
    void dropdownDatalistTest() throws InterruptedException {
        WebElement dropdown = driver.findElement(By.name("my-datalist"));
        List<String> initialOptions = new ArrayList<>(Arrays.asList("San Francisco", "New York", "Seattle", "Los Angeles", "Chicago"));
        List<String> actualOptions = dropdown.findElements(By.xpath(".//../datalist/option")).stream().map(x -> x.getDomAttribute("value")).toList();
        assertEquals(initialOptions, actualOptions);
        String cityToAdd = "Los Angeles";
        dropdown.sendKeys(cityToAdd);
        Thread.sleep(3000);
        assertEquals(cityToAdd, dropdown.getDomProperty("value"));
        }

    @Test
    void fileUploadTest() throws InterruptedException {
        // Get URL of resource
        URL url = WebFormTests.class.getClassLoader().getResource("hw5_locators.txt");

        String absolutePath = null;
        if (url != null) {
            // Get absolute path to file
            absolutePath = new File(url.getPath()).getAbsolutePath();
            System.out.println("Absolute path to file: " + absolutePath);
        } else {
            System.out.println("Resource not found");
        }

        WebElement fileUpload = driver.findElement(By.name("my-file"));
        fileUpload.sendKeys(absolutePath);
        Thread.sleep(5000);

        WebElement submit = driver.findElement(By.xpath("//button[@type='submit']"));
        actions.moveToElement(submit).perform();
        Thread.sleep(3000);
        submit.click();

        assertTrue(driver.getCurrentUrl().contains("hw5_locators.txt"), "File is not in URL");
    }

    @Test
    void checkboxTest(){
        Assertions.assertTrue(driver.findElement(By.id("my-check-1")).isSelected());
        Assertions.assertFalse(driver.findElement(By.id("my-check-2")).isSelected());
    }

    @Test
    void checkboxSelectOppositeValuesTest(){
        WebElement firstCheckbox = driver.findElement(By.id("my-check-1"));
        WebElement secondCheckbox = driver.findElement(By.id("my-check-2"));
        firstCheckbox.click();
        secondCheckbox.click();
        Assertions.assertFalse(firstCheckbox.isSelected());
        Assertions.assertTrue(secondCheckbox.isSelected());
    }

    @Test
    void radioBoxTest(){
        WebElement radio1 = driver.findElement(By.id("my-radio-1"));
        Assertions.assertTrue(radio1.isSelected());

        WebElement radio2 = driver.findElement(By.id("my-radio-2"));
        Assertions.assertFalse(radio2.isSelected());
    }

    @Test
    void radioBoxSelectOppositeValueTest() throws InterruptedException {
        WebElement radio1 = driver.findElement(By.id("my-radio-1"));
        WebElement radio2 = driver.findElement(By.id("my-radio-2"));

        actions.moveToElement(radio2).perform();
        radio2.click();
        Thread.sleep(2000);

        Assertions.assertFalse(radio1.isSelected());
        Assertions.assertTrue(radio2.isSelected());
    }

    @Test
    void colorPickerTest() throws InterruptedException {
        WebElement colorPicker = driver.findElement(By.cssSelector("input[type = 'color']"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Color redColor = new Color(255, 0, 0, 1); // #ff0000
        Color greenColor = new Color(0, 255, 0, 1); // #00ff00
        Color blueColor = new Color(0, 0, 255, 1); // #0000ff
        String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));";

        js.executeScript(script, colorPicker, redColor.asHex());
        Assertions.assertEquals("#ff0000", colorPicker.getDomProperty("value"));
        Thread.sleep(5000);

        js.executeScript(script, colorPicker, greenColor.asHex());
        Assertions.assertEquals("#00ff00", colorPicker.getDomProperty("value"));
        Thread.sleep(5000);

        js.executeScript(script, colorPicker, blueColor.asHex());
        Assertions.assertEquals("#0000ff", colorPicker.getDomProperty("value"));
        Thread.sleep(5000);
    }

    @Test
    void datePickerTest() throws InterruptedException {
        WebElement datePicker = driver.findElement(By.name("my-date"));
        String pattern = "MM/dd/yyyy";
        String currentDate = new SimpleDateFormat(pattern).format(new Date());
        datePicker.sendKeys(currentDate);
        Thread.sleep(2000);
        Assertions.assertEquals(currentDate, datePicker.getDomProperty("value"), "Date is not correct");
    }

    @Test
    void sliderWithKeyboardTest() throws InterruptedException {
        WebElement rangePicker = driver.findElement(By.name("my-range"));
        rangePicker.sendKeys(Keys.ARROW_RIGHT);
        Thread.sleep(5000);
        rangePicker.sendKeys(Keys.ARROW_RIGHT);
        Thread.sleep(5000);
        Assertions.assertEquals("7", rangePicker.getDomProperty("value"));
    }

    @Test
    void sliderWithMouthTest() throws InterruptedException {
        WebElement slider = driver.findElement(By.name("my-range"));
        int width = slider.getSize().getWidth();
        int x = slider.getLocation().getX();
        int y = slider.getLocation().getY();
        for (int i = 0; i < 10; i++) {
            actions.moveToElement(slider)
                    .clickAndHold()
                    .moveToLocation(x + width / 10 * i, y)
                    .release().perform();
            Thread.sleep(1000);
            assertEquals(String.valueOf(i), slider.getDomProperty("value"));
        }

    }
}
