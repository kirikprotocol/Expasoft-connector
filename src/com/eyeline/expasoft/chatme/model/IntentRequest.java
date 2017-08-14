package com.eyeline.expasoft.chatme.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeck on 26/06/17.
 */
public class IntentRequest {
    String name;
    List<UserSay> userSays = new ArrayList<UserSay>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(String text) {
        userSays.add(new UserSay(text));
    }

    public List<UserSay> getUserSays() {
        return userSays;
    }

    public void setUserSays(List<UserSay> userSays) {
        this.userSays = userSays;
    }
}
