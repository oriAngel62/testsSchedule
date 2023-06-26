package api;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import io.restassured.response.Response;

import static api.MissionGenerator.*;
import static io.restassured.RestAssured.given;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class API {

    private static String token;
    private static String userId;

    private static int missionId = 1;

   private static  Mission mission = generateMissionFullTemplate("play basketball", "play", "sport",
           60, "2023-06-10T10:00:00", 1, Arrays.asList("Sunday", "Monday"), Arrays.asList("12:00:00", "16:00:00"));

    private static List<Mission> missionList;


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
    public static void createUserAndLogIn() {
        RestAssured.baseURI = "https://localhost:7204/api";
        RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation());
        userId = getSaltString();
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", userId);
        requestBody.setProperty("name", userId);
        requestBody.setProperty("email", userId +"@gmail.com");
        requestBody.setProperty("password", userId);

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
        requestBody = new RequestBody();
        requestBody.setProperty("id", userId);
        requestBody.setProperty("password", userId);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody.toJson())
                .when()
                .post("/Users/Login")
                .then()
                .extract().response();

        // Get the status code
        statusCode = response.getStatusCode();

        // Assert the status code
        Assert.assertEquals(200, statusCode);

        // Get the token from the response
        token = response.jsonPath().getString("token");

        // Print the token
        System.out.println("login success! token: " + token);

    }
    @Test
    public void basicFlow() {
//        createUser();
//        loginTest();
        addMissionTest(mission);
        getAndFindMission();
        changeMission();
        deleteMission();
//        deleteUser();
    }

    @Test
    public void findMissionFlow() {
        addMissionTest(mission);
        getAndFindMission();
        deleteMission();
    }

    @Test
    public void changeMissionFlow() {
        addMissionTest(mission);
        changeMission();
        deleteMission();
    }

    @Test
    public void algoFlow() {
        addMissionsFromListTest();
        testAlgoApi();
        deleteMissionsFromList();
    }

    @Test
    public void suggestMissionFlow() {
        addMissionTest(mission);
        //deleteUser();
        createUserAndLogIn();
        suggestMissionsTest();
    }


    @AfterClass
    public static void deleteUser() {
        // Send the DELETE request to delete the user
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/Users")
                .then()
                .extract().response();

        // Get the status code
        int statusCode = response.getStatusCode();
        // Assert the status code
        Assert.assertEquals(204, statusCode);
        System.out.println("Deleted User ID: " + userId);
    }

    public void loginTest() {

        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("id", "string");
        requestBody.setProperty("password", "string");

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


    public void addMissionTest(Mission mission) {
        // Prepare the request body
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("title", mission.getTitle());
        requestBody.setProperty("description", mission.getDescription());
        requestBody.setProperty("type", mission.getType());
        requestBody.setProperty("length", mission.getLength());
        requestBody.setProperty("deadLine", mission.getDeadLine());
        requestBody.setProperty("priority", mission.getPriority());

        // Prepare optional days
        List<OptionalDay> optionalDays = new ArrayList<>();
        for (OptionalDay optionalDay : mission.getOptionalDays()) {
            OptionalDay day = new OptionalDay();
            day.setId(optionalDay.getId());
            day.setDay(optionalDay.getDay());
            optionalDays.add(day);
        }
        requestBody.setProperty("optionalDays", optionalDays);

        // Prepare optional hours
        List<OptionalHour> optionalHours = new ArrayList<>();
        for (OptionalHour optionalHour : mission.getOptionalHours()) {
            OptionalHour hour = new OptionalHour();
            hour.setId(optionalHour.getId());
            hour.setHour(optionalHour.getHour());
            optionalHours.add(hour);
        }
        requestBody.setProperty("optionalHours", optionalHours);

        // Send the POST request to add the mission
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody.toJson())
                .when()
                .post("/Missions")
                .then()
                .extract().response();

        missionId = Integer.parseInt(response.getBody().asString());
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("mission added!");
    }


    public void addMissionsFromListTest() {
        missionList = generateMissionList();

        for (Mission mission : missionList) {
            // Prepare the request body
            RequestBody requestBody = new RequestBody();
            requestBody.setProperty("title", mission.getTitle());
            requestBody.setProperty("description", mission.getDescription());
            requestBody.setProperty("type", mission.getType());
            requestBody.setProperty("length", mission.getLength());
            requestBody.setProperty("deadLine", mission.getDeadLine());
            requestBody.setProperty("priority", mission.getPriority());

            // Prepare optional days
            List<OptionalDay> optionalDays = new ArrayList<>();
            for (OptionalDay optionalDay : mission.getOptionalDays()) {
                OptionalDay day = new OptionalDay();
                day.setId(optionalDay.getId());
                day.setDay(optionalDay.getDay());
                optionalDays.add(day);
            }
            requestBody.setProperty("optionalDays", optionalDays);

            // Prepare optional hours
            List<OptionalHour> optionalHours = new ArrayList<>();
            for (OptionalHour optionalHour : mission.getOptionalHours()) {
                OptionalHour hour = new OptionalHour();
                hour.setId(optionalHour.getId());
                hour.setHour(optionalHour.getHour());
                optionalHours.add(hour);
            }
            requestBody.setProperty("optionalHours", optionalHours);

            // Send the POST request to add the mission
            Response response = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(requestBody.toJson())
                    .when()
                    .post("/Missions")
                    .then()
                    .extract().response();

            mission.setId(Integer.parseInt(response.getBody().asString()));
            Assert.assertEquals(200, response.getStatusCode());
            System.out.println("Mission added!");
        }
    }


    public void getAndFindMission() {
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
            if (title.equals("play basketball") && type.equals("sport")) {
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


    //build 15 missions
    // call to the algo

    public void testAlgoApi() {
        // Prepare the request body
        RequestBody requestBody = new RequestBody();
        requestBody.setProperty("setting", createSetting());

        // Get the mission IDs from the missionList
        int[] missionIds = missionList.stream().mapToInt(Mission::getId).toArray();
        requestBody.setProperty("missionsId", missionIds);

        // Send the POST request to /api/algo and validate the response
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody.toJson()) // Pass the request body in the POST request
                .when()
                .post("/algo")
                .then()
                .extract().response();
        System.out.println("Algo input: " + response.getBody().asString());
        // Assert that the POST request was successful
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("Algo Success!");
    }


    private Map<String, Object> createSetting() {
        Map<String, Object> setting = new HashMap<>();
        setting.put("id", 0);
        setting.put("startHour", "2023-06-04T09:00:00.000Z");
        setting.put("endHour", "2023-06-10T18:00:00.000Z");
        setting.put("minGap", 15);
        setting.put("maxHoursPerDay", 3);
        setting.put("minTimeFrame", 15);
        return setting;
    }

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



        // Iterate through the missions
        for (Object mission : missions) {
            // Convert the mission object to a map for easy access to its properties
            Map<String, Object> missionMap = (Map<String, Object>) mission;
            int id = (int) missionMap.get("id");
            String title = missionMap.get("title").toString();
            String description = missionMap.get("description").toString();

            // Check if the mission matches the criteria
            if (missionId == id) {
                // Assert that the mission details have been updated
                Assert.assertEquals("Updated Title", title);
                Assert.assertEquals("Updated Description", description);
                break;
            }
        }
        System.out.println("Changed mission ID: " + missionId);
    }

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

    public void deleteMissionsFromList() {
        for (Mission mission : missionList) {
            // Send the DELETE request to delete the mission
            Response deleteResponse = given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete("/Missions/" + mission.getId())
                    .then()
                    .extract().response();

            // Assert the status code of the DELETE request
            int deleteStatusCode = deleteResponse.getStatusCode();
            Assert.assertEquals(204, deleteStatusCode);
            System.out.println("Deleted mission ID: " + mission.getId());
        }
    }

    public void suggestMissionsTest() {
        // Prepare the request parameters
        String type = "sport";

        // Send the GET request to suggest missions
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Missions/SuggestMissions?type=sport")
                .then()
                .extract().response();

        // Assert that the GET request was successful
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);

        // Get the list of suggested missions
        List<Object> suggestedMissions = response.jsonPath().getList("$");

        // Assert that the suggested missions list is not empty
        Assert.assertFalse(suggestedMissions.isEmpty());

        System.out.println("Suggested missions: " + suggestedMissions);
    }

}



