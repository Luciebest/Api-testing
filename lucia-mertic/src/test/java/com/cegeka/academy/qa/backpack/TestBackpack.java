package com.cegeka.academy.qa.backpack;

import com.cegeka.academy.qa.json.model.LoginUserRequest;
import com.cegeka.academy.qa.json.model.RegisterUserRequest;
import com.cegeka.academy.qa.json.model.User;
import org.springframework.stereotype.Component;

@Component
public class TestBackpack {

    private int id;
    private String email;
    private String password;

    private String firstName;
    private String registerToken;
    private String loginToken;
    private RegisterUserRequest registerUserRequest;
    private String lastResponseBody;
    private LoginUserRequest loginUserRequest;

    private User selectedUser;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }
    public RegisterUserRequest getRegisterUserRequest() {
        return registerUserRequest;
    }

    public void setRegisterUserRequest(RegisterUserRequest registerUserRequest) {
        this.registerUserRequest = registerUserRequest;
    }
    public String getLastResponseBody() {
        return lastResponseBody;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastResponseBody(String lastResponseBody) {
        this.lastResponseBody = lastResponseBody;
    }
    public LoginUserRequest getLoginUserRequest() {
        return loginUserRequest;
    }

    public void setLoginUserRequest(LoginUserRequest loginUserRequest) {
        this.loginUserRequest = loginUserRequest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setSelectedUser(User user) {
        this.selectedUser = user;

    }

    public User getSelectedUser() {
        return selectedUser;
    }
}
