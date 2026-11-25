package com.vstay.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-End Selenium UI Automation Tests for Vstay.vn.
 *
 * Named *IT.java so Maven Surefire skips it during plain `mvn test` (unit tests)
 * and executes it in CI when running `-Dtest="*IT"`.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Vstay.vn — Selenium UI Automation Tests")
class VstaySeleniumIT {

    private static final String BASE_URL =
            System.getProperty("vstay.baseUrl", "http://localhost:8080/vstay");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("UI-01: Homepage loads successfully with hero search form")
    void homepageLoads() {
        driver.get(BASE_URL + "/");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("heroSearchForm")));
        assertTrue(driver.getTitle().contains("Vstay"),
                "Homepage title should contain 'Vstay'");
        assertTrue(driver.findElement(By.id("inputLocation")).isDisplayed());
    }

    @Test
    @Order(2)
    @DisplayName("UI-02: Search hotels by location from homepage")
    void searchHotelsByLocation() {
        driver.get(BASE_URL + "/");

        WebElement locationInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("inputLocation")));
        locationInput.clear();
        locationInput.sendKeys("Đà Nẵng");
        driver.findElement(By.id("btnSubmitHeroSearch")).click();

        wait.until(ExpectedConditions.urlContains("/search"));
        WebElement countEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("totalHotelsCount")));
        long count = Long.parseLong(countEl.getText().trim());
        assertTrue(count > 0, "Expected seeded hotels in Đà Nẵng");
    }

    @Test
    @Order(3)
    @DisplayName("UI-03: Login fails with wrong password and shows error banner")
    void loginFailsWithWrongPassword() {
        driver.get(BASE_URL + "/login");

        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("login-email")));
        WebElement passInput = driver.findElement(By.id("login-password"));

        emailInput.clear();
        emailInput.sendKeys("demo@vstay.vn");
        passInput.clear();
        passInput.sendKeys("wrongpassword");
        driver.findElement(By.id("btn-login-submit")).click();

        WebElement errorAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#panel-login .alert-error")));
        assertFalse(errorAlert.getText().isBlank(), "Error alert should have message");
    }

    @Test
    @Order(4)
    @DisplayName("UI-04: Login succeeds as standard USER and redirects to homepage")
    void loginSuccessAsUser() {
        driver.get(BASE_URL + "/login");

        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("login-email")));
        WebElement passInput = driver.findElement(By.id("login-password"));

        emailInput.clear();
        emailInput.sendKeys("demo@vstay.vn");
        passInput.clear();
        passInput.sendKeys("demo123");
        driver.findElement(By.id("btn-login-submit")).click();

        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        assertFalse(driver.getCurrentUrl().contains("/admin"),
                "Standard USER must not redirect to /admin");
    }

    @Test
    @Order(5)
    @DisplayName("UI-05: Admin login and Create + Delete hotel CRUD flow")
    void adminCreateAndDeleteHotel() {
        driver.get(BASE_URL + "/login");

        WebElement emailInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("login-email")));
        WebElement passInput = driver.findElement(By.id("login-password"));

        emailInput.clear();
        emailInput.sendKeys("admin@vstay.vn");
        passInput.clear();
        passInput.sendKeys("admin123");
        driver.findElement(By.id("btn-login-submit")).click();

        wait.until(ExpectedConditions.urlContains("/admin"));

        String uniqueHotelName = "Selenium CI Hotel " + System.currentTimeMillis();
        driver.findElement(By.id("propName")).sendKeys(uniqueHotelName);
        driver.findElement(By.id("propAddress")).sendKeys("123 Automation Street");
        driver.findElement(By.id("propPrice")).sendKeys("1500000");
        driver.findElement(By.id("propImage")).sendKeys("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=500");
        driver.findElement(By.id("btnSubmitForm")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("adminHotelsTable"), uniqueHotelName));

        // Clean up the created hotel row via its delete button
        WebElement row = driver.findElement(By.xpath(
                "//table[@id='adminHotelsTable']//tr[contains(., '" + uniqueHotelName + "')]"));
        row.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        wait.until(ExpectedConditions.invisibilityOfElementWithText(
                By.id("adminHotelsTable"), uniqueHotelName));
    }
}
