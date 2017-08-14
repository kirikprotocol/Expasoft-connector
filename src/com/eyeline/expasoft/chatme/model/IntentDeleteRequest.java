package com.eyeline.expasoft.chatme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jeck on 27/06/17.
 */
public class IntentDeleteRequest {
    @JsonProperty(value = "intent_id")
    private String intentId;

    public IntentDeleteRequest() {
    }

    public IntentDeleteRequest(String intentId) {
        this.intentId = intentId;
    }

    public String getIntentId() {
        return intentId;
    }

    public void setIntentId(String intentId) {
        this.intentId = intentId;
    }
}
