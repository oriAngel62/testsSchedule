package ui;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Connecting {
    private WebDriver driver;
    private String baseUrl;
    private static String userId = "string";


    @BeforeClass
    public void setUp() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ori\\Desktop\\tests\\chromedriver.exe");

        // Create a new instance of the Chrome driver
        driver = new ChromeDriver();

        // Set the base URL for the application
        baseUrl = "http://localhost:3000";
    }

    private static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() <= 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Test
    public void test1SignUp() {
        // Navigate to the sign-up page
        driver.get(baseUrl + "/in/sign-up");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        // Enter the sign-up details
        WebElement usernameField = driver.findElement(By.name("id"));
        WebElement fullNameField = driver.findElement(By.name("name"));
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement signUpButton = driver.findElement(By.xpath("//button[@type='submit']"));
        userId = getSaltString();
        usernameField.sendKeys(userId);
        fullNameField.sendKeys(userId);
        emailField.sendKeys(userId + "@example.com");
        passwordField.sendKeys(userId);

        // Click the sign-up button
        signUpButton.click();

        // Wait for the page to load after sign-up
        // You can add appropriate waits here if needed
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        WebElement signInLink = driver.findElement(By.xpath("//a[contains(@class, 'MuiTypography-root') and contains(@class, 'MuiLink-root') and contains(@href, '/in/sign-in/')]"));
        Assert.assertNotNull(signInLink, "Element found");

    }

    @Test
    public void testSignIn() {
        // Navigate to the login page
        driver.get(baseUrl + "/in/sign-in");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        // Enter the username and password
        WebElement usernameField = driver.findElement(By.name("id"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

        usernameField.sendKeys(userId);
        passwordField.sendKeys(userId);

        // Click the login button
        loginButton.click();

        // Wait for the page to load after login
        // You can add appropriate waits here if needed
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        // Search for the element after login
        WebElement elementAfterLogin = driver.findElement(By.xpath("//*[@id='__next']/div/header/div/div[2]/a[1]"));
        Assert.assertNotNull(elementAfterLogin, "Element found after login");
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        elementAfterLogin.click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
