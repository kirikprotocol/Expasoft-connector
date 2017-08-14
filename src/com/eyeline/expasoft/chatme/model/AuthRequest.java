package com.eyeline.expasoft.chatme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jeck on 06/07/17.
 */
public class AuthRequest {
    @JsonProperty(value = "user_name")
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
