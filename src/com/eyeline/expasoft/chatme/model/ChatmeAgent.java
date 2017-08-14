package com.eyeline.expasoft.chatme.model;

import com.eyeline.expasoft.chatme.filter.BlankFilter;
import com.eyeline.expasoft.chatme.filter.Filter;
import com.eyeline.expasoft.chatme.utils.MarshalUtils;
import com.eyeline.miniapps.ai.model.AiAgent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeck on 04/07/17.
 */
public class ChatmeAgent implements AiAgent {
    private static final Logger log = Logger.getLogger(ChatmeAgent.class);

    private String url = "http://ubuntu@expasoft.com:5000/chatme/pilot/api/v1.0";
    //private String auth = "chatme:thebest";

    private String login = "test0";
    private String password = "test0";

    private String auth;
    private HttpClientBuilder builder;

    private int defaultPredictionNumber;

    public ChatmeAgent(String url, String login, String password, int defaultPredictionNumber) {
        this.url = url;
        this.login = login;
        this.password = password;
        this.defaultPredictionNumber = defaultPredictionNumber;

        this.builder = HttpClientBuilder.create();
        try {
            this.initAuth();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public ChatmeAgent(String url, String login, String password) {
        this(url, login, password, 3);
    }

    private void initAuth() throws Exception {
        HttpClient client = builder.build();
        HttpPost method = new HttpPost(url+"/auth");
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity(MarshalUtils.marshal(new AuthRequest(login, password)), "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            AuthResponse response = MarshalUtils.unmarshal(MarshalUtils.parse(responseString), AuthResponse.class);
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+response);
            }
            this.auth = response.getToken();
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }

    public QueryResponse query(String query) throws Exception {
        return this.query(query, 3);
    }

    public QueryResponse query(String userQuery, int top) throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("usersay: ('").append(userQuery).append("')\ttop=").append(top);
        QueryRequest request = new QueryRequest();
        request.setTop(top);

        UserSay say = new UserSay();
        UserSay.Data data = new UserSay.Data();
        data.setText(userQuery);
        List<UserSay.Data> dataList = new ArrayList<UserSay.Data>();
        dataList.add(data);
        say.setData(dataList);

        List<UserSay> userSays = new ArrayList<UserSay>();
        userSays.add(say);
        request.setUserSays(userSays);

        HttpClient client = builder.build();
        HttpPost method = new HttpPost(url+"/query");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            String requestStr = MarshalUtils.marshal(request);
            sb.append("\treq:").append(requestStr);
            StringEntity entity = new StringEntity(requestStr, "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            sb.append("\tres:").append(responseString);
            return MarshalUtils.unmarshal(MarshalUtils.parse(responseString), QueryResponse.class);
        } finally {
            log.info(sb);
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }

    public IntentResponse addIntent(IntentRequest request) throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        HttpClient client = builder.build();
        HttpPost method = new HttpPost(url+"/intents");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity(MarshalUtils.marshal(request), "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+responseString);
            }
            return MarshalUtils.unmarshal(MarshalUtils.parse(responseString), IntentResponse.class);
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
/*        WebResource resource = this.client.resource(url).path("/intents");
        return resource.
                header("Authorization", auth).
                type(MediaType.APPLICATION_JSON).
                post(IntentResponse.class, request); */
    }

    public IntentResponse  deleteIntent(String name) throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        HttpClient client = builder.build();
        HttpWithBody method = new HttpWithBody("DELETE", url+"/intents");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity(MarshalUtils.marshal(new IntentDeleteRequest(name)), "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            client.execute(method);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+responseString);
            }
            return MarshalUtils.unmarshal(MarshalUtils.parse(responseString), IntentResponse.class);
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }

    public IntentResponse  deleteIntentById(String id) throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        HttpClient client = builder.build();
        HttpWithBody method = new HttpWithBody("DELETE", url+"/intents");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity(MarshalUtils.marshal(new IntentDeleteRequest(id)), "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            client.execute(method);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+responseString);
            }
            return MarshalUtils.unmarshal(MarshalUtils.parse(responseString), IntentResponse.class);
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }

    public Intents getIntents() throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        HttpClient client = builder.build();
        HttpWithBody method = new HttpWithBody("GET", url+"/intents");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity("{}", "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            client.execute(method);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+responseString);
            }
            return MarshalUtils.unmarshal(MarshalUtils.parse(responseString), Intents.class);
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }
    }

    public Map<String,String> getUsersays(int id) throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        HttpClient client = builder.build();
        HttpWithBody method = new HttpWithBody("GET", url+"/intents/id");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity("{\"intent_id\":"+id+"}", "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            client.execute(method);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+responseString);
            }
            //SuppressWarning "unchecked"
            return (Map<String,String>) MarshalUtils.unmarshal(MarshalUtils.parse(responseString), LinkedHashMap.class);
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }

/*        WebResource resource = this.client.resource(url).path("/intents/id");
        ClientResponse cr = resource.
                header("Authorization", auth).
                type(MediaType.APPLICATION_JSON).
                get(ClientResponse.class);
        cr.bufferEntity();
        String x = cr.getEntity(String.class); */
    }

    public void doSave() throws Exception {
        if (this.auth == null) {
            this.initAuth();
        }
        HttpClient client = builder.build();
        HttpPost method = new HttpPost(url+"/save");
        method.addHeader("Authorization", this.auth);
        HttpResponse httpResponse = null;
        try {
            StringEntity entity = new StringEntity("", "utf8");
            entity.setContentType(MediaType.APPLICATION_JSON);
            method.setEntity(entity);
            client.execute(method);
            httpResponse = client.execute(method);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            if (log.isTraceEnabled()) {
                log.trace("Expasoft Auth "+responseString);
            }
        } finally {
            if (httpResponse != null) {
                HttpClientUtils.closeQuietly(httpResponse);
            }
        }

/*        WebResource resource = this.client.resource(url).path("/save");
        ClientResponse cr = resource.
                header("Authorization", auth).
                type(MediaType.APPLICATION_JSON).
                post(ClientResponse.class);
        cr.bufferEntity();
        String x = cr.getEntity(String.class); */
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    @Override
    public List<Prediction> predict(String query) throws Exception {
        List<Prediction> result = new ArrayList<>();
        QueryResponse response = this.query(query, defaultPredictionNumber);
        if(!response.getSuccess()) return null;
        for (Message message: response.getMessages()) {
            for (Message.Prediction prediction: message.getPredictions()) {
                result.add(new Prediction(prediction.getIntentName(), prediction.getProba()));
            }
        }
        return result;
    }

    private static class HttpWithBody extends HttpEntityEnclosingRequestBase {
        private final String methodName;

        public String getMethod() { return methodName; }

        HttpWithBody(String methodName, final String uri) {
            super();
            this.methodName = methodName;
            setURI(URI.create(uri));
        }
    }
}
