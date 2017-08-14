package com.eyeline.expasoft.chatme.model;

import java.util.List;

/**
 * Created by jeck on 26/06/17.
 */
public class QueryResponse {
    Boolean success;
    List<Message> messages;
    Integer top;
    String response;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "success=" + success +
                ", messages=" + messages +
                ", top=" + top +
                ", response='" + response + '\'' +
                '}';
    }
}
