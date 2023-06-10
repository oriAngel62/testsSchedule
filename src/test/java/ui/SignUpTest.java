package ui;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

//import org.openqa.seleniu m.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


public class SignUpTest {
    private WebDriver driver;
    private String baseUrl;

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

    @BeforeClass
    public void setUp() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ori\\Desktop\\tests\\chromedriver.exe");

        // Create a new instance of the Chrome driver
        driver = new ChromeDriver();

        // Set the base URL for the application
        baseUrl = "http://localhost:3000";
    }

    @Test
    public void testSignUp() {
        // Navigate to the sign-up page
        driver.get(baseUrl + "/in/sign-up");

        // Enter the sign-up details
        WebElement usernameField = driver.findElement(By.name("id"));
        WebElement fullNameField = driver.findElement(By.name("name"));
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement signUpButton = driver.findElement(By.xpath("//button[@type='submit']"));
        String userId = getSaltString();
        usernameField.sendKeys(userId);
        fullNameField.sendKeys(userId);
        emailField.sendKeys(userId + "@example.com");
        passwordField.sendKeys("your_password");

        // Click the sign-up button
        signUpButton.click();

        // Wait for the page to load after sign-up
        // You can add appropriate waits here if needed

        WebElement signInLink = driver.findElement(By.xpath("//a[contains(@class, 'MuiTypography-root') and contains(@class, 'MuiLink-root') and contains(@href, '/in/sign-in/')]"));
        Assert.assertNotNull(signInLink, "Element found");
    }


    @AfterClass
    public void tearDown() {
        // Quit the browser
        driver.quit();
    }
}
