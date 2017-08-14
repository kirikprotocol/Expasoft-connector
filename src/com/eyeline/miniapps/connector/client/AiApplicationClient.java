package com.eyeline.miniapps.connector.client;

import com.eyeline.expasoft.chatme.utils.MarshalUtils;
import com.eyeline.miniapps.ai.model.AiAgent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jeck on 24/07/17.
 */
public class AiApplicationClient implements AiAgent {
    private HttpClientBuilder builder;
    private String url;
    private String id;


    public AiApplicationClient(String url, String id) {
        this.url = url;
        this.id = id;
        this.builder = HttpClientBuilder.create();
    }

    @Override
    public List<Prediction> predict(String query) throws Exception {
        HttpClient client = builder.build();
        HttpGet method = new HttpGet(url+"/"+id+"?q="+ URLEncoder.encode(query, "UTF-8"));
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            Prediction[] response = MarshalUtils.unmarshal(MarshalUtils.parse(responseString), Prediction[].class);
            return Arrays.asList(response);
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }
}
