package com.eyeline.miniapps.ai.model;

import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import org.apache.commons.configuration.HierarchicalConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by jeck on 11/08/17.
 */
public class TestAiAgent implements AiAgent {
    @Override
    public List<Prediction> predict(String query) {
        List<Prediction> result = new ArrayList<>();
        result.add(new Prediction(query.toLowerCase(), 1.0));
        return result;
    }

    public static class Factory implements ResourceFactory {

        @Override
        public Object build(String id, Properties properties, HierarchicalConfiguration config) throws Exception {
            return new TestAiAgent();
        }

        @Override
        public boolean isHeavyResource() {
            return false;
        }
    }
}
