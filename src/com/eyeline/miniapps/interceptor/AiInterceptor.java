package com.eyeline.miniapps.interceptor;

import com.eyeline.miniapps.filter.Filter;
import com.eyeline.miniapps.ai.model.AiAgent;
import com.eyeline.miniapps.ai.model.HostingApi;
import com.eyeline.miniapps.ai.model.Page;
import com.eyeline.miniapps.ai.model.ServiceAiHelper;
import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.*;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.content.ContentRequest;
import com.eyelinecom.whoisd.sads2.content.ContentResponse;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.interceptor.BlankInterceptor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import java.util.*;

/**
 * Created by jeck on 10/07/17.
 */
public class AiInterceptor extends BlankInterceptor implements Initable {
    private static final String REQ_ATTR_PROCESSED = "ai.processed";
    private static final String SESS_ATTR_CURRENT_PAGE = "ai.current_page";
    private static final String REQ_ATTR_HELPER = "ai.helper";

    private AiAgent ai;
    private Filter chatFilter;
    private boolean localPriority;
    private boolean relativeFilter;
    private double threshold;
    //private ServiceAiHelper helper;

    private Set<String> skipPages;
    private Set<String> hardSkipPages;
    private Set<String> forwardPages;

    private Set<String> skipUrls;
    private Map<String, String> replacePages;

    String hostingApiEndpoint;
    String hostingApiBaseurl;

    private boolean unknownEnabled;
    private double thresholdUnknown;
    private String forwardUnknownPage;

    private String ignoreText;
    private String ignorePage;

    @Override
    public void afterContentResponse(SADSRequest request, ContentRequest contentRequest, ContentResponse content, RequestDispatcher dispatcher) throws InterceptionException {
        //In this method we will try to define current page and store it to the session
        Log log = SADSLogger.getLogger(request, this.getClass());
        String idAttribute = (String) content.getAttributes().get("resource-id");
        if (StringUtils.isNotBlank(idAttribute)) {
            String[] idArray = StringUtils.split(idAttribute, '/');
            String id = idArray[idArray.length-1];
            ServiceAiHelper helper = (ServiceAiHelper) request.getAttributes().get(REQ_ATTR_HELPER);
            if (helper!=null) {
                Page currentPage = helper.getById(id);
                request.getSession().setAttribute(SESS_ATTR_CURRENT_PAGE, currentPage);
                log.info("Storing current page in session: "+currentPage);
            }
        }
    }

    private boolean isNeedToSkipUrl(SADSRequest request){
        String url = UrlUtils.removeAllParameters(request.getResourceURI());
        for (String u: skipUrls) {
            if (url.startsWith(u)) {
                return true;
            }
        }
        return false;
    }

    private boolean switchProcessedRequest(SADSRequest req) {
        Object processed = req.getAttributes().get(REQ_ATTR_PROCESSED);
        if (processed==null) {
            req.getAttributes().put(REQ_ATTR_PROCESSED, new Object());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequest(SADSRequest request, RequestDispatcher dispatcher) throws InterceptionException {
        Log log = SADSLogger.getLogger(request, this.getClass());
        if (!switchProcessedRequest(request)) {
            log.info("request is already processed (probably 302 redirect). DONE");
            return;
        }
        ServiceAiHelper helper;
        try {
            HostingApi hostingApi = new HostingApi(hostingApiEndpoint);
            helper = new ServiceAiHelper(hostingApi.getPages());
            request.getAttributes().put(REQ_ATTR_HELPER, helper);
        } catch (Exception e) {
            throw new InterceptionException(e);
        }
        String event = request.getParameters().get("event");
        log.info("incoming event: "+event);
        if ("message".equals(event)) {
            String text = request.getParameters().get("event.text");
            log.info("it is a text event! user said: "+text);
            if (StringUtils.isNotBlank(text)) {
                boolean isStartDialog = false;
                Page currentPage = (Page) request.getSession().getAttribute(SESS_ATTR_CURRENT_PAGE);
                if (currentPage == null) {
                    currentPage = helper.getByUrl(request.getResourceURI());
                    isStartDialog = true;
                }
                if (currentPage!=null) {
                    log.info("current page: "+currentPage);
                    if (isNeedToSkipUrl(request)) {
                        log.info("the page is in skip url list. DONE");
                        return;
                    }
                    if (hardSkipPages.contains(currentPage.getId())) {
                        log.info("the page is in HARD prohibited list! DONE");
                        request.getParameters().put("pid", currentPage.getId());
                        //this.redirectRequest(request, currentPage, true, log);
                        return;
                    }

                    if (skipPages.contains(currentPage.getId())) {
                        log.info("the page is in prohibited list! DONE");
                        this.redirectRequest(request, currentPage, true, log);
                        return;
                    }
                    if (text.startsWith(ignoreText)) {
                        log.info("currentText is started with ignore phrase moving to special page: "+ignorePage);
                        this.redirectRequest(request, helper.getById(ignorePage), true, log);
                        return;
                    }
                    List<AiAgent.Prediction> predictions;
                    try {
                        String filteredText = chatFilter.filter(text);
                        log.info("filtered text: "+filteredText);
                        predictions = ai.predict(filteredText);
                    } catch (Exception e) {
                        throw new InterceptionException(e);
                    }
                    log.info("predictions: "+predictions);
                    if (predictions.size()==0) {
                        log.info("No predictions aquired. return");
                        return;
                    }
                    String intentName;
                    if (localPriority) {
                        log.info("trying to get best intents with thr="+threshold);
                        List<AiAgent.Prediction> best;
                        if (relativeFilter) {
                            log.info("using relative filter: relative proportion of prediction must be greater than "+threshold);
                            best = helper.filterByRelative(predictions, threshold, log);
                        } else {
                            log.info("using average filter: score of prediction must be greater than average score + ("+threshold+")");
                            best = helper.filterByAvg(predictions, threshold, log);
                        }
                        log.info("got"+best);
                        intentName = helper.bestIntent(currentPage, best, log);
                    } else {
                        intentName = predictions.get(0).getIntent();
                    }
                    log.info("determined intent: "+intentName);
                    if (unknownEnabled) {
                        for (AiAgent.Prediction prediction: predictions) {
                            if (intentName.equals(prediction.getIntent())) {
                                log.info("Test probability of: "+intentName+", proba="+prediction.getProba()+" with "+thresholdUnknown);
                                if (prediction.getProba()<=thresholdUnknown) {
                                    log.info("redirecting to page: "+forwardUnknownPage);
                                    this.redirectRequest(request, helper.getById(forwardUnknownPage), false, log);
                                    return;
                                }
                            }
                        }
                    }
                    if (isStartDialog) {
                        log.info("It is a start of the dialog. Trying to find global intent: "+intentName);
                        Page nextPage = helper.lookupGlobal(intentName);
                        log.info("Found: "+nextPage);
                        if (nextPage!=null) {
                            this.redirectRequest(request, nextPage, false, log);
                            log.info("Redirected to: "+nextPage.getUrl()+" redirected by global intent (at the start of dialog). Done");
                            return;
                        } else {
                            log.info("Page is not found by global intent: "+intentName);
                            return;
                        }
                    } else {
                        log.info("It is continue of the dialog. Trying to find local intent: "+intentName);
                        Page nextPage = helper.lookupLocal(currentPage, intentName);
                        log.info("Got next page: "+nextPage);
                        if (nextPage!=null) {
                            this.redirectRequest(request, nextPage, false, log);
                            log.info("Redirected to: "+nextPage.getUrl()+" redirected by local intent. Done");
                            return;
                        } else {
                            log.info("It is continue of the dialog. Trying to find tree intent: "+intentName);
                            nextPage = helper.lookupTree(currentPage, intentName, log);
                            if (nextPage!=null) {
                                this.redirectRequest(request, nextPage, false, log);
                                log.info("url: "+nextPage.getUrl()+" redirected by tree intent. Done");
                                return;
                            } else {
                                log.info("It is continue of the dialog. Trying to find global intent: "+intentName);
                                nextPage = helper.lookupGlobal(intentName);
                                if (nextPage!=null) {
                                    this.redirectRequest(request, nextPage, false, log);
                                    log.info("url: "+nextPage.getUrl()+" redirected by global intent (continue dialog). Done");
                                    return;
                                } else {
                                    log.info("Can't found any page by intent: "+intentName+" and page: "+currentPage);
                                    this.redirectRequest(request, currentPage, true, log);
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    log.info("current page is null. skip");
                }
            }
        } else if ("link".equals(event)){
            log.info("it is a link event! current link: "+request.getResourceURI());
            Page currentPage = (Page) request.getSession().getAttribute(SESS_ATTR_CURRENT_PAGE);
            if (currentPage == null) {
                currentPage = helper.getByUrl(request.getResourceURI());
            }
            if (currentPage!=null) {
                if (replacePages.containsKey(currentPage.getId())) {
                    String url = replacePages.get(currentPage.getId());
                    log.info("the page is in replace list. Replace to URL: "+url);
                    this.redirectRequest(request, currentPage, url, log);
                    return;
                }
            }
        }
    }

    private void redirectRequest(SADSRequest request, Page toPage, boolean defaultLink, Log log){
        if (replacePages.containsKey(toPage.getId())) {
            String url = replacePages.get(toPage.getId());
            log.info("the page is in replace list. Replace to URL: "+url);
            this.redirectRequest(request, toPage, url, log);
            return;
        }
        Page currentPage = (Page) request.getSession().getAttribute(SESS_ATTR_CURRENT_PAGE);
        if (currentPage!=null && forwardPages.contains(currentPage.getId())) {
            String newUrl = toPage.getUrl();
            Map<String,String> newParams = UrlUtils.getParametersMap(newUrl);
            Map<String,String> currentParams = UrlUtils.getParametersMap(newUrl);
            String targetUrl = UrlUtils.removeAllParameters(newUrl);
            targetUrl = UrlUtils.addParameters(targetUrl, currentParams);
            targetUrl = UrlUtils.addParameters(targetUrl, newParams);
            targetUrl = UrlUtils.removeParameter(targetUrl, "input_answer");
            request.getParameters().remove("input_answer");

            request.getParameters().putAll(newParams);
            request.setResourceURI(targetUrl);

            return;
        }
        String requestUrl = request.getResourceURI();
        requestUrl = UrlUtils.addParameters(requestUrl, UrlUtils.getParametersMap(toPage.getUrl()));
        requestUrl = StringUtils.replace(requestUrl, "/start?","/index?");
        if (!defaultLink) {
            requestUrl = UrlUtils.removeParameter(requestUrl, "input_answer");
            request.getParameters().remove("input_answer");
        }
        request.setResourceURI(requestUrl);
        request.getParameters().put("pid", toPage.getId());
    }

    private void redirectRequest(SADSRequest request, Page currentPage, String url, Log log){
        String requestUrl = url;
        requestUrl = UrlUtils.addParameter(requestUrl, "lk_url", currentPage.getUrl());
        request.setResourceURI(requestUrl);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(Properties config) throws Exception {
        ai = SADSInitUtils.getResource("agent", config);
        chatFilter = SADSInitUtils.getResource("filter", config);
        //String hostingApiEndpoint = InitUtils.getString("hosting-api-endpoint", config);
        //String hostingApiBaseurl = InitUtils.getString("hosting-api-baseurl", config);
        //HostingApi hostingApi = new HostingApi(hostingApiEndpoint, hostingApiBaseurl);
        //helper = new ServiceAiHelper(hostingApi.getPages());
        hostingApiEndpoint = InitUtils.getString("hosting-api-endpoint", config);
        hostingApiBaseurl = InitUtils.getString("hosting-api-baseurl", config);
        skipPages = new HashSet<>();
        String skipPagesParam = InitUtils.getString("skip-pages", "", config);
        if (StringUtils.isNotBlank(skipPagesParam)) {
            Collections.addAll(skipPages, skipPagesParam.split(" "));
        }

        hardSkipPages = new HashSet<>();
        String hardSkipPagesParam = InitUtils.getString("hard-skip-pages", "", config);
        if (StringUtils.isNotBlank(hardSkipPagesParam)) {
            Collections.addAll(hardSkipPages, hardSkipPagesParam.split(" "));
        }

        forwardPages = new HashSet<>();
        String forwardPagesParam = InitUtils.getString("forward-pages", "", config);
        if (StringUtils.isNotBlank(forwardPagesParam)) {
            Collections.addAll(forwardPages, forwardPagesParam.split(" "));
        }

        localPriority = InitUtils.getBoolean("local-priority", true, config);
        relativeFilter = InitUtils.getBoolean("relative-filter", true, config);
        threshold = InitUtils.getDouble("threshold", 0.25, config);

        replacePages = InitUtils.getMapStringsStartsWith("replace.", config);

        skipUrls = new HashSet<>();
        String skipUrlsParam = InitUtils.getString("skip-urls", "", config);
        if (StringUtils.isNotBlank(skipUrlsParam)) {
            Collections.addAll(skipUrls, skipUrlsParam.split(" "));
        }
        unknownEnabled = InitUtils.getBoolean("unknown-enabled", false, config);
        thresholdUnknown = InitUtils.getDouble("unknown-threshold", 0.01, config);
        forwardUnknownPage = InitUtils.getString("unknown-page", "71", config);

        ignoreText = InitUtils.getString("ignore-text", "пользователь nickname=", config);
        ignorePage = InitUtils.getString("ignore-page", "117", config);

    }
}
