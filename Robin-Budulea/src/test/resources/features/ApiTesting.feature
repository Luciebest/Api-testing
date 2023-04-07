Feature: Api Testing

  Scenario: Register User with same email
    Given I get all the users from "https://reqres.in/api/users"
    And I save an email from a random user
    When I register another user using the same email at "https://reqres.in/api/register"
    And I login with the newly created user at "https://reqres.in/api/login"
    Then The register user token and login token should be the same

    When Verify the status code for REGISTER - UNSUCCESSFUL
    Then verify the status code for LOGIN - UNSUCCESSFUL
    When Take a random SINGLE USER and UPDATE his data using PUT
    Then Verify status code when using DELETE for a random user
