Feature: Api Testing

  Scenario: Register User with same email
    Given I get all the users from "https://reqres.in/api/users"
    And I save an email from a random user
    When I register another user using the same email at "https://reqres.in/api/register"
    And I login with the newly created user at "https://reqres.in/api/login"
    Then The register user token and login token should be the same


  Scenario: Verify the status code and the error message for REGISTER - UNSUCCESSFUL when the password is missing
    Given I get all the users from "https://reqres.in/api/users"
    And I have an invalid registration request because the password is missing
    When I make a POST request to "https://reqres.in/api/register" with invalid registration request without email or password
    Then The response status code should be 400
    And The error message should be "{\"error\":\"Missing password\"}"

    #OR:
  Scenario: Verify the status code and the error message for REGISTER - UNSUCCESSFUL when the email is missing
    #Given I get all the users from "https://reqres.in/api/users"
    Given I have an invalid registration request because the email is missing
    When I make a POST request to "https://reqres.in/api/register" with invalid registration request without email or password
    Then The response status code should be 400
    And The error message should be "{\"error\":\"Missing email or username\"}"


  Scenario: Verify the status code and the error message for LOGIN - UNSUCCESSFUL when the password is missing
    Given I get all the users from "https://reqres.in/api/users"
    And I have an invalid login request because the password is missing
    When I make a POST request to "https://reqres.in/api/login" with invalid login request without email or password
    Then The response status code should be 400
    And The error message should be "{\"error\":\"Missing password\"}"

    #OR:
  Scenario: Verify the status code and the error message for LOGIN - UNSUCCESSFUL when the email is missing
    #Given I get all the users from "https://reqres.in/api/users"
    Given I have an invalid login request because the email is missing
    When I make a POST request to "https://reqres.in/api/login" with invalid login request without email or password
    Then The response status code should be 400
    And The error message should be "{\"error\":\"Missing email or username\"}"

  Scenario: Take a random SINGLE USER and UPDATE his data using PUT
    Given I get all the users from "https://reqres.in/api/users"
    And I select a random user by id
    And I set the new user data with request:
        """
        {
            "first_name": "Lucia",
            "email": "lucia@gmail.com"
        }
        """
    When I update the user data using PUT at "https://reqres.in/api/users/{userId}"
    Then The response status code should be 200
    And The updated user data should match the new data with response:
    #!!!
        # Dont forget to add the current time when testing this step
    #!!!
        """
        {
            "first_name": "Lucia",
            "email": "lucia@gmail.com",
            "updatedAt": "2023-04-09T17:00:00.918Z"
        }
        """

  Scenario: Verify status code when using DELETE for a random user
    Given I get all the users from "https://reqres.in/api/users"
    And I select a random user by id
    When I make a DELETE request to "https://reqres.in/api/users/{userId}" with the stored id
    Then The response status code should be 204
