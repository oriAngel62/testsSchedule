package login;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class API {

    private static String token;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://localhost:7204/api";
        RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation());

    }

    @Test
    public void createUser(){
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", "8");
        requestBody.setProperty("name", "ori");
        requestBody.setProperty("email", "ori@gmail.com");
        requestBody.setProperty("password", "1234");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody.toJson())
                .when()
                .post("/Users")
                .then()
                .extract().response();

        // Get the status code
        int statusCode = response.getStatusCode();

        // Assert the status code
        Assert.assertEquals(200, statusCode);
    }


    @BeforeClass
    public static void loginTest() {

        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", "8");
        requestBody.setProperty("password", "1234");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody.toJson())
                .when()
                .post("/Users/Login")
                .then()
                .extract().response();

        // Get the status code
        int statusCode = response.getStatusCode();

        // Assert the status code
        Assert.assertEquals(200, statusCode);

        // Get the token from the response
        token = response.jsonPath().getString("token");

        // Print the token
        System.out.println("Token: " + token);
    }

    @Test
    public void findMission() {
        // Add the mission
        //Response addResponse = addMission();
        //Assert.assertEquals(200, addResponse.getStatusCode());

        // Get the added mission ID from the response
        //int missionId = addResponse.jsonPath().getInt("id");

        // Fetch the missions using a GET request
        Response getResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Missions")
                .then()
                .extract().response();

        // Assert that the added mission is present in the response
        List<Object> missions = getResponse.jsonPath().getList("$");
        // Assert that the missions list is not empty
        Assert.assertFalse(missions.isEmpty());

        // Print all the missions
        for (Object mission : missions) {
            System.out.println("Mission: " + mission);
        }
    }

    private Response addMission() {
        // Prepare the request body
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", 0);
        requestBody.setProperty("title", "play basketball");
        requestBody.setProperty("description", "play");
        requestBody.setProperty("type", "sport");
        requestBody.setProperty("length", 1);

        // Prepare optional days
        List<OptionalDay> optionalDays = new ArrayList<>();
        OptionalDay day = new OptionalDay();
        day.setId(0);
        day.setDay("Sunday");
        optionalDays.add(day);
        requestBody.setProperty("optionalDays", optionalDays);

        // Prepare optional hours
        List<OptionalHour> optionalHours = new ArrayList<>();
        OptionalHour hour = new OptionalHour();
        hour.setId(0);
        hour.setHour("12");
        optionalHours.add(hour);
        requestBody.setProperty("optionalHours", optionalHours);

        requestBody.setProperty("deadLine", "2023-06-04T10:55:34.824Z");
        requestBody.setProperty("priority", 1);

        // Send the POST request to add the mission
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody.toJson())
                .when()
                .post("/Missions")
                .then()
                .extract().response();
    }


    @Test
    public void ChangeMission(){
        loginTest();

    }


    @Test
    public void US(){
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ori\\Desktop\\tests\\chromedriver.exe");

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode (without opening a browser window)

        // Create a new instance of the ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        // Navigate to the URL
        driver.get("http://localhost:3000");

        // Find the <p> element with the text '[Your Self Introduction]'
        WebElement introElement = driver.findElement(By.xpath("//a[contains(text(), 'schedule')]"));
        introElement.click();

        // Wait for the page to load
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.urlContains("/dd"));

        // Find the <div> element with the specified attributes
        WebElement divElement = driver.findElement(By.xpath("//div[@tabindex='0' and @role='button' and @aria-expanded='false' and @aria-haspopup='listbox']"));

        // Assert that the <div> element exists
        Assert.assertNotNull(divElement);

        // Wait for some time to see the selected element
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close the browser
//        driver.quit();
    }

}

