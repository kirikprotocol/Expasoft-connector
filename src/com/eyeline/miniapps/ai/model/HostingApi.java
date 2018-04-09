package com.eyeline.miniapps.ai.model;

import com.eyeline.utils.MarshalUtils;
import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by jeck on 10/07/17.
 */
public class HostingApi {
    private static final Logger log = Logger.getLogger(HostingApi.class);
    private static final String PARAM_CUSTOM_ID = "custom_id";

    private String url;

    private HttpClientBuilder builder;

    private String serviceId;

    public HostingApi(String endpoint) {
        this.url = endpoint;
        this.builder = HttpClientBuilder.create();
        serviceId = UrlUtils.getParameter(url, "sid");
    }

    public Set<Page> getPages() throws Exception {
        Set<Page> result = new HashSet<>();
        HttpClient client = builder.build();
        HttpGet method = new HttpGet(url);
        HttpResponse httpResponse = null;
        try {
            client.execute(method);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("HostingAPI >> "+responseString);
            }
            Page[] pages = MarshalUtils.unmarshal(MarshalUtils.parse(responseString), Page[].class);
            for (Page page: pages) {
                page.setServiceId(serviceId);
            }
            Collections.addAll(result, pages);

            return result;
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }
}
