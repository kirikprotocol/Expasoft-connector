package com.eyeline.test;

import com.eyeline.miniapps.ai.model.AiAgent;
import com.eyeline.miniapps.connector.client.AiApplicationClient;

import java.util.List;

/**
 * Created by jeck on 24/07/17.
 */
public class AiClientTest {
    public static void main(String[] args) throws Exception {
        AiApplicationClient client = new AiApplicationClient("http://devel.globalussd.mobi/ai/ai", "ai-expasoft-chatme");
        List<AiAgent.Prediction> res = client.predict("Привет");
        System.out.println(res);
    }
}
