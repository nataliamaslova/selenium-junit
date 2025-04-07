package hw7_waits_windows_frames;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CookiesTest {
    private WebDriver driver;

    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/cookies.html";
    private static final By DISPLAY_BUTTON_LOCATOR = By.xpath("//button");
    private static final By COOKIES_LIST_LOCATOR = By.xpath("//p[@id='cookies-list']");
    private static final Cookie USERNAME_COOKIE = new Cookie("username", "John Doe", null);
    private static final Cookie DATE_COOKIE = new Cookie("date", "10/07/2018", null);

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
    void cookiesDefaultStateTest() {
        assertAll(
                () -> assertTrue(driver.findElement(DISPLAY_BUTTON_LOCATOR).isDisplayed(),
                        "Display cookies button is missing"),
                () -> assertEquals("Display cookies", driver.findElement(DISPLAY_BUTTON_LOCATOR).getText(),
                        "Button text is wrong"),
                () -> assertTrue(driver.findElement(COOKIES_LIST_LOCATOR).getText().isEmpty(),
                        "Cookies are shown on default page")
        );
    }

    @Test
    void showCookiesTest() {
        driver.findElement(DISPLAY_BUTTON_LOCATOR).click();
        List<String> cookiesDisplayed = Arrays.asList(driver.findElement(COOKIES_LIST_LOCATOR).getText().split("\n"));
        assertAll(
                () -> assertEquals(Set.of(USERNAME_COOKIE, DATE_COOKIE), driver.manage().getCookies(),
                        "Browser cookies set incorrectly"),
                () -> assertEquals(List.of(getStringFormattedCookie(USERNAME_COOKIE), getStringFormattedCookie(DATE_COOKIE)),
                        cookiesDisplayed, "Incorrect cookies display")
        );
    }

    @Test
    void manageCookiesTest() {
        driver.manage().deleteAllCookies();

        Cookie newCookie = new Cookie("newKey", "newValue");
        driver.manage().addCookie(newCookie);
        driver.findElement(DISPLAY_BUTTON_LOCATOR).click();
        String cookiesDisplayed = driver.findElement(COOKIES_LIST_LOCATOR).getText();

        assertEquals(getStringFormattedCookie(newCookie), cookiesDisplayed, "Incorrect cookies display");
    }

    private String getStringFormattedCookie(Cookie cookie) {
        return String.format("%s=%s", cookie.getName(), cookie.getValue());
    }
}
