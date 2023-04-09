package com.cegeka.academy.qa.tests.steps;
import org.json.JSONObject;
import com.cegeka.academy.qa.backpack.TestBackpack;
import com.cegeka.academy.qa.configurations.FrameworkConfiguration;
import com.cegeka.academy.qa.json.model.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

@CucumberContextConfiguration
@ContextConfiguration(classes = FrameworkConfiguration.class)
public class ApiSteps {

    private List<User> users;
    private int statusCode;
    @Autowired
    private TestBackpack testBackpack;

    @Given("I get all the users from {string}")
    public void iGetAllTheUsersFrom(String url) {
        Data data = RestAssured.given().get(url).as(Data.class);
        users = data.getUsers();
       // System.out.println(users.size());
    }

    @And("I save an email from a random user")
    public void iSaveAnEmailFromARandomUser() {
        User user = users.get(new Random().nextInt(users.size()));
        testBackpack.setEmail(user.getEmail());
        testBackpack.setPassword("parola1234");
    }

    @When("I register another user using the same email at {string}")
    public void iRegisterAnotherUserUsingTheSameEmailAt(String url) {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(testBackpack.getEmail());
        registerUserRequest.setPassword(testBackpack.getPassword());
        String registerToken = RestAssured.given().contentType("application/json")
                .body(registerUserRequest)
                .post(url)
                .jsonPath().getString("token");
        testBackpack.setRegisterToken(registerToken);
    }

    @And("I login with the newly created user at {string}")
    public void iLoginWithTheNewlyCreatedUserAt(String url) {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(testBackpack.getEmail());
        loginUserRequest.setPassword(testBackpack.getPassword());
        String loginToken = RestAssured.given().contentType("application/json")
                .body(loginUserRequest)
                .post(url)
                .jsonPath().getString("token");
        testBackpack.setLoginToken(loginToken);
    }

    @Then("The register user token and login token should be the same")
    public void theRegisterUserTokenAndLoginTokenShouldBeTheSame() {
        String registerToken = testBackpack.getRegisterToken();
        String loginToken = testBackpack.getLoginToken();
        Assert.assertEquals(registerToken, loginToken);
    }

    @Given("I have an invalid registration request because the password is missing")
    public void iHaveAnInvalidRegistrationRequestBecauseThePasswordIsMissing() {
        User user = users.get(new Random().nextInt(users.size()));
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(user.getEmail());

        // Not setting the password here because it's the missing field that we want to test
        testBackpack.setRegisterUserRequest(registerUserRequest);
    }

    @Given("I have an invalid registration request because the email is missing")
    public void iHaveAnInvalidRegistrationRequestBecauseTheEmailIsMissing() {
        //User user = users.get(new Random().nextInt(users.size()));
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();

        //Obs: If we dont set a password the response status code and the error message would be the same
        registerUserRequest.setPassword("user.getPassword()");

        // Not setting the email here because it's the missing field that we want to test
        testBackpack.setRegisterUserRequest(registerUserRequest);
    }

    @When("I make a POST request to {string} with invalid registration request without email or password")
    public void iMakeAPostRequestToWithInvalidRegistrationRequestWithoutPassword(String url) {
        RegisterUserRequest registerUserRequest = testBackpack.getRegisterUserRequest();
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(registerUserRequest)
                .post(url);
        statusCode = response.getStatusCode();
        testBackpack.setLastResponseBody(response.getBody().asString());
    }

    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, statusCode);
    }

    @Then("The error message should be {string}")
    public void theErrorMessageShouldBe(String expectedErrorMessage) {
        String actualErrorMessage = testBackpack.getLastResponseBody();
        Assert.assertEquals(expectedErrorMessage, actualErrorMessage);
    }

    @Given("I have an invalid login request because the password is missing")
    public void iHaveAnInvalidLoginRequestBecauseThePasswordIsMissing() {
        User user = users.get(new Random().nextInt(users.size()));
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail(user.getEmail());

        // Not setting the password here because it's the missing field that we want to test
        testBackpack.setLoginUserRequest(loginUserRequest);
    }

    @Given("I have an invalid login request because the email is missing")
    public void iHaveAnInvalidLoginRequestBecauseTheEmailIsMissing() {
        //User user = users.get(new Random().nextInt(users.size()));
        LoginUserRequest loginUserRequest = new LoginUserRequest();

        //Obs: If we dont set a password the response status code and the error message would be the same
        loginUserRequest.setPassword("user.getPassword()");

        // Not setting the email here because it's the missing field that we want to test
        testBackpack.setLoginUserRequest(loginUserRequest);
    }

    @When("I make a POST request to {string} with invalid login request without email or password")
    public void iMakeAPostRequestToWithInvalidLoginRequestWithoutPassword(String url) {
        LoginUserRequest loginUserRequest = testBackpack.getLoginUserRequest();
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginUserRequest)
                .post(url);
        statusCode = response.getStatusCode();
        testBackpack.setLastResponseBody(response.getBody().asString());
    }

    @And("I select a random user by id")
    public void iSelectARandomUserById() {
        User user = users.get(new Random().nextInt(users.size()));
        testBackpack.setId(user.getId());
        testBackpack.setSelectedUser(user);
    }

    @Given("I set the new user data with request:$")
    public void iSetTheNewUserData(String request) {
        User selectedUser = testBackpack.getSelectedUser();

        //JSONObject is used to hold the response body of a REST API call
        JSONObject requestData = new JSONObject(request);
        selectedUser.setFirstName(requestData.getString("first_name"));
        selectedUser.setEmail(requestData.getString("email"));
    }

    @When("I update the user data using PUT at {string}")
    public void iUpdateTheUserDataUsingPUT(String url) {
        User selectedUser = testBackpack.getSelectedUser();
        int userId = selectedUser.getId();
        Response response=
                RestAssured.given()
                        .contentType("application/json")
                        .pathParam("userId", userId)
                        .body(selectedUser)
                        .put(url);
        statusCode = response.getStatusCode();
        selectedUser.setUpdatedAt(new Date());
    }

    @Then("The updated user data should match the new data with response:$")
    public void theUpdatedUserDataShouldMatchTheNewData(String response) {
        User selectedUser = testBackpack.getSelectedUser();
        JSONObject expectedResponse = new JSONObject(response);
        String url = "https://reqres.in/api/users/" + selectedUser.getId();

        Assert.assertEquals(expectedResponse.getString("first_name"), selectedUser.getFirstName());
        Assert.assertEquals(expectedResponse.getString("email"), selectedUser.getEmail());

        //Check if the updatedAt field from the expectedTime is !=null
        //System.out.println(selectedUser.getUpdatedAt());
        //System.out.println(expectedResponse.getString("updatedAt"));
        //Assert.assertTrue(selectedUser.getUpdatedAt() != null);

        // OR: Check the difference in time between updatedAt fields and don't let it be greater that 60 seconds
        LocalDateTime expectedTime = Instant.parse(expectedResponse.getString("updatedAt")).atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime actualTime = Instant.parse(selectedUser.getUpdatedAt()).atZone(ZoneOffset.UTC).toLocalDateTime();
        System.out.println(expectedTime);
        System.out.println(actualTime);

        long diffSeconds = Math.abs(Duration.between(expectedTime, actualTime).getSeconds());
        System.out.println(diffSeconds);

        Assert.assertTrue("The difference in updatedAt time is greater than 60 seconds", diffSeconds <= 60);
    }

    @When("I make a DELETE request to {string} with the stored id")
    public void iMakeADELETERequestToWithTheStoredId(String url) {
        int userId = testBackpack.getSelectedUser().getId();
        Response response = RestAssured.given()
                .pathParam("userId", userId)
                .delete(url + "/" + userId);
        statusCode = response.getStatusCode();
    }
}


