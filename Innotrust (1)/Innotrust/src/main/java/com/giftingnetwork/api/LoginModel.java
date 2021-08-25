package com.giftingnetwork.api;

import org.springframework.stereotype.Component;

@Component
public class LoginModel {

    String username;
    String password;
    String client_id;
    String access_token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public LoginModel() {
    }

    @Override
    public String toString() {
        return "LoginModel [access_token=" + access_token + ", client_id=" + client_id + ", password=" + password
                + ", username=" + username + "]";
    }

}
