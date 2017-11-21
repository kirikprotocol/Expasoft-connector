package com.eyeline.miniapps.ai.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jeck on 10/07/17.
 */
public interface AiAgent {
    List<Prediction> predict(String query) throws Exception;

    public static class Prediction {
        private String intent;
        private Double proba;
        private Map<String, Object> entities;

        public Prediction() {
        }

        public Prediction(String intent, Double proba) {
            this(intent, proba, Collections.emptyMap());
        }

        public Prediction(String intent, Double proba, Map<String,Object> entities) {
            this.intent = intent;
            this.proba = proba;
            this.entities = entities;
        }

        public String getIntent() {
            return intent;
        }

        public Double getProba() {
            return proba;
        }

        public Map<String, Object> getEntities() {
            return entities;
        }


        @Override
        public String toString() {
            return "Prediction{" +
                    "intent='" + intent + '\'' +
                    ", proba=" + proba +
                    ", entities=" + entities +
                    '}';
        }
    }
}
