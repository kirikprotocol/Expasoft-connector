package com.eyeline.test;

import com.eyeline.miniapps.ai.model.HostingApi;
import com.eyeline.miniapps.ai.model.ServiceAiHelper;

/**
 * Created by jeck on 10/07/17.
 */
public class HostingApiTest {
    public static void main(String[] args) throws Exception {
        HostingApi api = new HostingApi("http://hosting-api-test.eyeline.mobi/json?sid=686");
        ServiceAiHelper helper = new ServiceAiHelper(api.getPages());
    }
}
