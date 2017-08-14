package com.eyeline.expasoft.chatme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by jeck on 26/06/17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    String text;
    List<Prediction> predictions;

    public static class Prediction{
        @XmlElement(name = "intent_id")
        @JsonProperty(value = "intent_id")
        String intentId;
        @JsonProperty(value = "intent_name")
        @XmlElement(name = "intent_name")
        String intentName;
        Double proba;

        public String getIntentId() {
            return intentId;
        }

        public void setIntentId(String intentId) {
            this.intentId = intentId;
        }

        public Double getProba() {
            return proba;
        }

        public void setProba(Double proba) {
            this.proba = proba;
        }

        public String getIntentName() {
            return intentName;
        }

        public void setIntentName(String intentName) {
            this.intentName = intentName;
        }

        @Override
        public String toString() {
            return "Prediction{" +
                    "intentId='" + intentId + '\'' +
                    ", intentName='" + intentName + '\'' +
                    ", proba=" + proba +
                    '}';
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", predictions=" + predictions +
                '}';
    }
}
