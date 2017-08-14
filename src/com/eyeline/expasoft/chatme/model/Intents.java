package com.eyeline.expasoft.chatme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by jeck on 07/07/17.
 */
public class Intents {
    @JsonProperty(value = "intent_counter")
    Integer intentCounter;
    List<Info> intents;

    public Integer getIntentCounter() {
        return intentCounter;
    }

    public void setIntentCounter(Integer intentCounter) {
        this.intentCounter = intentCounter;
    }

    public List<Info> getIntents() {
        return intents;
    }

    public void setIntents(List<Info> intents) {
        this.intents = intents;
    }

    public static class Info {
        @JsonProperty(value = "intent_id")
        Integer id;
        @JsonProperty(value = "intent_name")
        String name;
        @JsonProperty(value = "number_of_messages")
        Integer messageNumber;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMessageNumber() {
            return messageNumber;
        }

        public void setMessageNumber(Integer messageNumber) {
            this.messageNumber = messageNumber;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", messageNumber=" + messageNumber +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Intents{" +
                "intentCounter=" + intentCounter +
                ", intents=" + intents +
                '}';
    }
}
