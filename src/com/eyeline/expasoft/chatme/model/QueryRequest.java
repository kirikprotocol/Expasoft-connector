package com.eyeline.expasoft.chatme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by jeck on 26/06/17.
 */
@XmlRootElement
public class QueryRequest {
    @JsonProperty
    Integer top = 3;
    @JsonProperty
    List<UserSay> userSays;

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public List<UserSay> getUserSays() {
        return userSays;
    }

    public void setUserSays(List<UserSay> userSays) {
        this.userSays = userSays;
    }
}
