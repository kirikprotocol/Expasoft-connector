package com.eyeline.rasa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by jeck on 20/11/17.
 */
public class ParseResponse {
    private String text;
    @JsonProperty(value = "intent_ranking")
    private List<Intent> intentRanking;
    private Intent intent;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Intent> getIntentRanking() {
        return intentRanking;
    }

    public void setIntentRanking(List<Intent> intentRanking) {
        this.intentRanking = intentRanking;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public static class Intent {
        private String name;
        private Double confidence;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
    }
}
