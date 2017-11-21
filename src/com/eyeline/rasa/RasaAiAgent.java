package com.eyeline.rasa;

import com.eyeline.miniapps.ai.model.AiAgent;
import com.eyeline.utils.MarshalUtils;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.resource.ResourceFactory;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by jeck on 20/11/17.
 */
public class RasaAiAgent implements AiAgent {
    private static final Logger log = Logger.getLogger(RasaAiAgent.class);

    private HttpClientBuilder builder;
    private String url;

    private String project;
    private String model;

    public RasaAiAgent(String url, String project, String model) {
        this.url = url;
        this.project = project;
        this.model = model;
        this.builder = HttpClientBuilder.create();
    }

    private ParseResponse parse(String query) throws IOException {
        StringBuilder logLine = new StringBuilder();
        logLine.append("usersay: ('").append(query).append("')");
        HttpClient client = builder.build();
        HttpPost method = new HttpPost(url+"/parse");
        HttpResponse httpResponse = null;
        try {
            String requestStr = MarshalUtils.marshal(new ParseRequest(query, model, project));
            logLine.append("\treq:").append(requestStr);
            StringEntity entity = new StringEntity(requestStr, "utf8");
            method.setEntity(entity);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            logLine.append("\tres:").append(responseString);
            return MarshalUtils.unmarshal(MarshalUtils.parse(responseString), ParseResponse.class);
        } finally {
            log.info(logLine);
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }

    @Override
    public List<Prediction> predict(String query) throws Exception {
        ParseResponse response = this.parse(query);
        List<ParseResponse.Intent> intents = response.getIntentRanking();
        List<Prediction> predictions = new ArrayList<>(intents.size());
        for (ParseResponse.Intent intent: intents) {
            predictions.add(new Prediction(intent.getName(), intent.getConfidence()));
        }
        return predictions;
    }

    public static class Factory implements ResourceFactory {

        @Override
        public RasaAiAgent build(String id, Properties properties, HierarchicalConfiguration config) throws Exception {
            String url = InitUtils.getString("url", properties);
            String project = InitUtils.getString("project", null, properties);
            String model = InitUtils.getString("model", null, properties);
            return new RasaAiAgent(url, project, model);
        }

        @Override
        public boolean isHeavyResource() {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        RasaAiAgent agent = new RasaAiAgent("http://expasoft.com:33113", "test_project", null);
        List<Prediction> predictions = agent.predict("Hello!");
        System.out.println(predictions);
    }

}
