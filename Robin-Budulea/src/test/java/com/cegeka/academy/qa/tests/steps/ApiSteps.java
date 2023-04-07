package com.cegeka.academy.qa.tests.steps;

import com.cegeka.academy.qa.backpack.TestBackpack;
import com.cegeka.academy.qa.configurations.FrameworkConfiguration;
import com.cegeka.academy.qa.json.model.Data;
import com.cegeka.academy.qa.json.model.LoginUserRequest;
import com.cegeka.academy.qa.json.model.RegisterUserRequest;
import com.cegeka.academy.qa.json.model.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@CucumberContextConfiguration
@ContextConfiguration(classes = FrameworkConfiguration.class)
public class ApiSteps {

    private List<User> users;

    @Autowired
    private TestBackpack testBackpack;

    @Given("I get all the users from {string}")
    public void iGetAllTheUsersFrom(String url) {
        Data data = RestAssured.given().get(url).as(Data.class);
        users = data.getUsers();
        System.out.println(users.size());
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
        String registeToken = testBackpack.getRegisterToken();
        String loginToken = testBackpack.getLoginToken();
        assertEquals(registeToken, loginToken);
    }

    @When("Verify the status code for REGISTER - UNSUCCESSFUL")
    public void verifyTheStatusCodeForREGISTERUNSUCCESSFUL(String url) {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("future@ScrumMaster"); /* :P  */
        Response response = RestAssured.given().contentType("application/json").body(registerUserRequest).post(url);
        String errorResponce = response.jsonPath().getString("error");  /* in case of error message */
        System.out.println(errorResponce);
        int receivedStatusNumber = response.getStatusCode();
        assertEquals(400, receivedStatusNumber);
    }


    @Then("verify the status code for LOGIN - UNSUCCESSFUL")
    public void verifyTheStatusCodeForLOGINUNSUCCESSFUL(String url) {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("batman&robin@RightBehindYou");
        Response responseForLogin = RestAssured.given().contentType("application/json").body(loginUserRequest).post(url);
        String errorResponce = responseForLogin.jsonPath().getString("error");
        System.out.println(errorResponce);
        int receivedStatusNumber = responseForLogin.getStatusCode();
        assertEquals(400, receivedStatusNumber);
    }


    @When("Take a random SINGLE USER and UPDATE his data using PUT")
    public void takeARandomSINGLEUSERAndUPDATEHisDataUsingPUT(String url) {
        Response getUser = RestAssured.given().get(url);
        User user = getUser.jsonPath().getObject("data", User.class);
        // create the next Scrum Master's user
        user.setFirstName("Robbin'");
        user.setLastName("DaBank");
        assertEquals("Robbin'",user.getFirstName());
        assertEquals("DaBank", user.getLastName());
        
        //User PUT request to send the user
        Response requestTypePUT = RestAssured.given().contentType("application/json").body(user).put(url);
        int receivedStatusNumber = getUser.getStatusCode();
        assertEquals(200, receivedStatusNumber);
    }

    @Then("Verify status code when using DELETE for a random user")
    public void verifyStatusCodeWhenUsingDELETEForARandomUser(String url) {
        User user = users.get(new Random().nextInt(users.size()));
        Response responseForDeleteRequest = RestAssured.given().contentType("application/json").delete(url +"/" + user.getId());
        System.out.println(user.getId());
        int receivedStatusNumber = responseForDeleteRequest.getStatusCode();
        assertEquals(204, receivedStatusNumber);

    }
}

