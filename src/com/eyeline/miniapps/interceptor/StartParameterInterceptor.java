package com.eyeline.miniapps.interceptor;

import com.eyeline.miniapps.ai.model.HostingApi;
import com.eyeline.miniapps.ai.model.Page;
import com.eyeline.miniapps.ai.model.ServiceAiHelper;
import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.SADSLogger;
import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.interceptor.BlankInterceptor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import java.util.Properties;

/**
 * Created by jeck on 22/11/17.
 */
public class StartParameterInterceptor extends BlankInterceptor implements Initable {
    private static final String START_COMMAND = "/start ";

    private String hostingApiBaseUrl;
    private String hostingApiEndpointBase;

    private HostingApi buildHostingApi(SADSRequest request) {
        String hostingApiServiceId = request.getParameters().get("sid");
        if (StringUtils.isBlank(hostingApiServiceId)) {
            String startPage = InitUtils.getString("start-page",
                    request.getResourceURI(),
                    request.getServiceScenario().getAttributes()
            );
            hostingApiServiceId = UrlUtils.getParameter(startPage, "sid");
            if (StringUtils.isBlank(hostingApiServiceId)) return null;
        }
        String endpoint = UrlUtils.addParameter(hostingApiEndpointBase, "sid", hostingApiServiceId);
        return new HostingApi(endpoint);
    }

    @Override
    public void onRequest(SADSRequest request, RequestDispatcher dispatcher) throws InterceptionException {
        Log log = SADSLogger.getLogger(request.getServiceId(), request.getAbonent(), this.getClass());
        if (request.getResourceURI().startsWith(hostingApiBaseUrl)) {
            String eventText = request.getParameters().get("event.text");
            if (StringUtils.isNotBlank(eventText)) {
                if (eventText.startsWith(START_COMMAND)) {
                    String startParameter = eventText.substring(START_COMMAND.length());
                    log.info("found start request parameter: "+startParameter);
                    HostingApi api = buildHostingApi(request);
                    if (api == null) return;
                    try {
                        ServiceAiHelper helper = new ServiceAiHelper(api.getPages());
                        Page targetPage = helper.lookupGlobal(startParameter);
                        log.info("found page: "+targetPage);
                        if (targetPage!=null) {
                            this.redirectRequest(request, targetPage, log);
                        }
                    } catch (Exception e) {
                        log.warn("Can't get pages from hosting API", e);
                    }
                }
            }
        }
    }

    private void redirectRequest(SADSRequest request, Page toPage, Log log){
        String requestUrl = request.getResourceURI();
        requestUrl = UrlUtils.addParameters(requestUrl, UrlUtils.getParametersMap(toPage.getUrl()));
        requestUrl = StringUtils.replace(requestUrl, "/start?","/index?");
        request.setResourceURI(requestUrl);
        log.info("redirect to: "+requestUrl);
        request.getParameters().put("pid", toPage.getId());
    }


    @Override
    public void init(Properties config) throws Exception {
        hostingApiBaseUrl = InitUtils.getString("hosting-api-base", config);
        hostingApiEndpointBase = InitUtils.getString("hosting-api-endpoint", config);
    }

    @Override
    public void destroy() {}
}
