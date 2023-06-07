package login;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import org.junit.Assert;
import org.junit.Test;
import com.google.gson.Gson;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class API {

    private static String token;
    private static String userId;
    private static int missionId = 1;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://localhost:7204/api";
        RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation());
        userId  = getSaltString();
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
    public void flow(){
        createUser();
        loginTest();
        addMissionTest();
        findMission();
        changeMission();

        deleteMission();
    }

    @Order(1)
    @Test
    public void createUser(){
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", userId);
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
        System.out.println("Create User ID: " + userId);
    }

    @Order(2)
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
        System.out.println("login success! token: " + token);
    }

    @Order(2)
    @Test
    public void addMissionTest() {
        // Prepare the request body
        RequestBody requestBody = new RequestBody();
        //requestBody.setProperty("id", 99);
        requestBody.setProperty("title", "playing basketball");
        requestBody.setProperty("description", "play");
        requestBody.setProperty("type", "sport");
        requestBody.setProperty("length", 2);
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
        hour.setHour("12:00:00");
        optionalHours.add(hour);
        requestBody.setProperty("optionalHours", optionalHours);

        requestBody.setProperty("deadLine", "2023-06-04T10:55:34.824Z");
        requestBody.setProperty("priority", 1);

        // Send the POST request to add the mission
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody.toJson())
                .when()
                .post("/Missions")
                .then()
                .extract().response();

        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("mission added!");

    }


    @Test
    @Order(4)
    public void findMission() {
        // Fetch the missions using a GET request
        Response getResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Missions")
                .then()
                .extract().response();

        // Assert that the GET request was successful
        Assert.assertEquals(200, getResponse.getStatusCode());

        // Get the list of missions
        List<Object> missions = getResponse.jsonPath().getList("$");

        // Assert that the missions list is not empty
        Assert.assertFalse(missions.isEmpty());


        boolean missionFound = false;


        // Iterate through the missions
        for (Object mission : missions) {
            // Convert the mission object to a map for easy access to its properties
            Map<String, Object> missionMap = (Map<String, Object>) mission;
            String title = missionMap.get("title").toString();
            String type = missionMap.get("type").toString();

            // Check if the mission matches the criteria
            if (title.equals("playing basketball") && type.equals("sport")) {
                missionFound = true;
                missionId = (int) missionMap.get("id");
                break;
            }
        }

        // Assert that the mission was found
        Assert.assertTrue("Mission not found", missionFound);

        // Print the mission ID
        System.out.println("Found mission ID: " + missionId);
    }


//    public Response addMission() {
//        // Prepare the request body
//        RequestBody requestBody = new RequestBody();
//        missionId = 99;
//        requestBody.setProperty("id", 99);
//        requestBody.setProperty("title", "play basketball");
//        requestBody.setProperty("description", "play");
//        requestBody.setProperty("type", "sport");
//        requestBody.setProperty("length", 1);
//        // Prepare optional days
//        List<OptionalDay> optionalDays = new ArrayList<>();
//        OptionalDay day = new OptionalDay();
//        day.setId(0);
//        day.setDay("Sunday");
//        optionalDays.add(day);
//        requestBody.setProperty("optionalDays", optionalDays);
//
//        // Prepare optional hours
//        List<OptionalHour> optionalHours = new ArrayList<>();
//        OptionalHour hour = new OptionalHour();
//        hour.setId(0);
//        hour.setHour("12");
//        optionalHours.add(hour);
//        requestBody.setProperty("optionalHours", optionalHours);
//
//        requestBody.setProperty("deadLine", "2023-06-04T10:55:34.824Z");
//        requestBody.setProperty("priority", 1);
//
//        // Send the POST request to add the mission
//        return given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .body(requestBody.toJson())
//                .when()
//                .post("/Missions")
//                .then()
//                .extract().response();
//    }

    @Test
    @Order(5)
    public void changeMission() {
        // Prepare the request body with the updated mission details
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", missionId);
        requestBody.setProperty("title", "Updated Title");
        requestBody.setProperty("description", "Updated Description");
        requestBody.setProperty("type", "sport");
        requestBody.setProperty("startDate", "2023-06-04T10:55:34.824Z");
        requestBody.setProperty("endDate", "2023-06-04T10:55:34.824Z");
        requestBody.setProperty("allDay", false);

        // Send the PUT request to update the mission
        Response putResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody.toJson())
                .when()
                .put("/Missions/" + missionId)
                .then()
                .extract().response();

        // Assert the status code of the PUT request
        int putStatusCode = putResponse.getStatusCode();
        Assert.assertEquals(204, putStatusCode);

        Response getResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Missions")
                .then()
                .extract().response();

        // Assert that the GET request was successful
        int getStatusCode = getResponse.getStatusCode();
        Assert.assertEquals(200, getStatusCode);

        // Get the list of missions
        List<Object> missions = getResponse.jsonPath().getList("$");

        // Assert that the missions list is not empty
        Assert.assertFalse(missions.isEmpty());

        boolean missionFound = false;


        // Iterate through the missions
        for (Object mission : missions) {
            // Convert the mission object to a map for easy access to its properties
            Map<String, Object> missionMap = (Map<String, Object>) mission;
            int id = (int) missionMap.get("id");
            String title = missionMap.get("title").toString();
            String description = missionMap.get("description").toString();

            // Check if the mission matches the criteria
            if (missionId == id ) {
                missionFound = true;
                // Assert that the mission details have been updated
                Assert.assertEquals("Updated Title", title);
                Assert.assertEquals("Updated Description", description);
                break;
            }
        }
        System.out.println("Changed mission ID: " + missionId);
    }



    @Test
    @Order(6)
    public void deleteMission() {
        // Send the DELETE request to delete the mission
        Response deleteResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/Missions/" + missionId)
                .then()
                .extract().response();

        // Assert the status code of the DELETE request
        int deleteStatusCode = deleteResponse.getStatusCode();
        Assert.assertEquals(204, deleteStatusCode);
        System.out.println("Delete mission ID: " + missionId);

    }



//    @Test
//    public void US(){
//        // Set the path to the ChromeDriver executable
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ori\\Desktop\\tests\\chromedriver.exe");
//
//        // Configure Chrome options
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // Run Chrome in headless mode (without opening a browser window)
//
//        // Create a new instance of the ChromeDriver
//        WebDriver driver = new ChromeDriver(options);
//
//        // Navigate to the URL
//        driver.get("http://localhost:3000");
//
//        // Find the <p> element with the text '[Your Self Introduction]'
//        WebElement introElement = driver.findElement(By.xpath("//a[contains(text(), 'schedule')]"));
//        introElement.click();
//
//        // Wait for the page to load
////        WebDriverWait wait = new WebDriverWait(driver, 10);
////        wait.until(ExpectedConditions.urlContains("/dd"));
//
//        // Find the <div> element with the specified attributes
//        WebElement divElement = driver.findElement(By.xpath("//div[@tabindex='0' and @role='button' and @aria-expanded='false' and @aria-haspopup='listbox']"));
//
//        // Assert that the <div> element exists
//        Assert.assertNotNull(divElement);
//
//        // Wait for some time to see the selected element
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Close the browser
////        driver.quit();
//    }

}

