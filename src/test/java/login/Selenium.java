package login;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Selenium {
    private WebDriver driver;

    @Before
    public void setUp() {
        // Set up WebDriver
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void successfulLoginTest() {
        // Open the login page
        driver.get("https://example.com/login");

        // Enter username and password
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password");

        // Click the login button
        driver.findElement(By.id("login-button")).click();

        // Assert successful login
        String welcomeMessage = driver.findElement(By.id("welcome-message")).getText();
        Assert.assertEquals("Welcome, testuser!", welcomeMessage);
    }

    @Test
    public void invalidCredentialsTest() {
        // Open the login page
        driver.get("https://example.com/login");

        // Enter invalid username and password
        driver.findElement(By.id("username")).sendKeys("invaliduser");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");

        // Click the login button
        driver.findElement(By.id("login-button")).click();

        // Assert error message
        String errorMessage = driver.findElement(By.id("error-message")).getText();
        Assert.assertEquals("Invalid username or password", errorMessage);
    }

    @After
    public void tearDown() {
        // Clean up and close the WebDriver
        driver.quit();
    }
}
