package com.eyeline.miniapps.ai.model;

import java.util.List;

/**
 * Created by jeck on 10/07/17.
 */
public interface AiAgent {
    List<Prediction> predict(String query) throws Exception;

    public static class Prediction {
        private String intent;
        private Double proba;

        public Prediction() {
        }

        public Prediction(String intent, Double proba) {
            this.intent = intent;
            this.proba = proba;
        }

        public String getIntent() {
            return intent;
        }

        public Double getProba() {
            return proba;
        }

        @Override
        public String toString() {
            return "Prediction{" +
                    "intent='" + intent + '\'' +
                    ", proba=" + proba +
                    '}';
        }
    }
}
