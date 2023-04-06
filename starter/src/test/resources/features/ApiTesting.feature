Feature: Api Testing

  Scenario: Register User with same email
    Given I get all the users from "https://reqres.in/api/users"
    And I save an email from a random user
    When I register another user using the same email at "https://reqres.in/api/register"
    And I login with the newly created user at "https://reqres.in/api/login"
    Then The register user token and login token should be the same
