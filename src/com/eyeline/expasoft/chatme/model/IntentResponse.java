package com.eyeline.expasoft.chatme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jeck on 26/06/17.
 */
public class IntentResponse {
    Boolean success;
    @JsonProperty(value = "intent_id")
    String intentId;
    @JsonProperty(value = "intent_name")
    String intentName;
    String response;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getIntentId() {
        return intentId;
    }

    public void setIntentId(String intentId) {
        this.intentId = intentId;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "IntentResponse{" +
                "success=" + success +
                ", intentId='" + intentId + '\'' +
                ", intentName='" + intentName + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
