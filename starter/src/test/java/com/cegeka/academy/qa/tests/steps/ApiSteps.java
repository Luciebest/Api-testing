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
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Random;
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
        Assert.assertEquals(registeToken, loginToken);
    }
}
