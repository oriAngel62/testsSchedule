package ui;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {
    private WebDriver driver;
    private String baseUrl;

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
    public void testLogin() {
        // Navigate to the login page
        driver.get(baseUrl + "/in/sign-in");

        // Enter the username and password
        WebElement usernameField = driver.findElement(By.name("id"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

        usernameField.sendKeys("string");
        passwordField.sendKeys("string");

        // Click the login button
        loginButton.click();

        // Wait for the page to load after login
        // You can add appropriate waits here if needed

        // Search for the element after login
        WebElement elementAfterLogin = driver.findElement(By.xpath("//*[@id='__next']/div/header/div/div[2]/a[1]"));
        Assert.assertNotNull(elementAfterLogin, "Element found after login");
    }

    @AfterClass
    public void tearDown() {
        // Quit the browser
        driver.quit();
    }
}
