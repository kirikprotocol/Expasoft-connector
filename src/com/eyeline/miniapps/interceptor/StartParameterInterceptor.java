package com.eyeline.miniapps.interceptor;

import com.eyeline.miniapps.ai.model.HostingApi;
import com.eyeline.miniapps.ai.model.Page;
import com.eyeline.miniapps.ai.model.ServiceAiHelper;
import com.eyeline.miniapps.connector.TgStartLinkServlet;
import com.eyeline.miniapps.resource.StartLinkProvider;
import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.SADSLogger;
import com.eyelinecom.whoisd.sads2.common.UrlUtils;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.exception.NotFoundResourceException;
import com.eyelinecom.whoisd.sads2.executors.connector.SADSInitializer;
import com.eyelinecom.whoisd.sads2.interceptor.BlankInterceptor;
import com.eyelinecom.whoisd.sads2.profile.ProfileStorage;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jeck on 22/11/17.
 */
public class StartParameterInterceptor extends BlankInterceptor implements Initable {
    private final Logger log = Logger.getLogger(TgStartLinkServlet.class);

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

        String eventText = request.getParameters().get("event.text");
        if (StringUtils.isNotBlank(eventText)) {
            if (eventText.startsWith(START_COMMAND)) {
                processStartCommand(request, log, eventText);
            }
        }

    }

    private void processStartCommand(SADSRequest request, Log log, String eventText) {
        final String startParameter = eventText.substring(START_COMMAND.length());
        if (StringUtils.isBlank(startParameter)) {
            return;
        }

        log.info("Found start request parameter: " + startParameter);

        if (!startParameter.startsWith(StartLinkProvider.PREFIX_HASH)) {
            tryRedirect(request, log, startParameter);
            return;
        }

        final String hash = startParameter.substring(StartLinkProvider.PREFIX_HASH.length());
        if (StringUtils.isNotBlank(hash)) {
            try {
                final Map<String, ?> params = new StartLinkProvider(getProfileStorage()).unpack(hash);
                params.forEach((s, o) -> request.getSession().setAttribute("session." + s, o));

                final String pageId = (String) params.get(TgStartLinkServlet.PARAM_START_PAGE);
                if (pageId != null) {
                    tryRedirect(request, log, pageId);
                }

                String pageUrl = (String)params.get(TgStartLinkServlet.PARAM_START_PAGE_URL);
                if (pageUrl != null) {
                    this.redirectRequest(request, pageUrl, log);
                }

            } catch (Exception e) {
                log.warn("Start link parsing failed, falling back to page redirect:" +
                    " start = [" + startParameter + "]", e);

                tryRedirect(request, log, startParameter);
            }
        }
    }

    private void tryRedirect(SADSRequest request, Log log, String pageId) {
        if (request.getResourceURI().startsWith(hostingApiBaseUrl)) {
            HostingApi api = buildHostingApi(request);
            if (api == null) return;
            try {
                ServiceAiHelper helper = new ServiceAiHelper(api.getPages());
                Page targetPage = helper.lookupGlobal(pageId);
                log.info("found page: " + targetPage);
                if (targetPage != null) {
                    this.redirectRequest(request, targetPage, log);
                }
            } catch (Exception e) {
                log.warn("Can't get pages from hosting API", e);
            }
        }
    }

    private void redirectRequest(SADSRequest request, String pageUri, Log log) throws Exception {
        String serviceRoot = request.getServiceScenario().getAttributes().getProperty("start-page");
        String redirectTarget = UrlUtils.merge(serviceRoot, pageUri);
        this.overrideRequestParams(log, request, pageUri);
        if (log.isDebugEnabled()) {
            log.debug("Redirect attribute found in the request: [" + pageUri + "]," + " redirecting to [" + redirectTarget + "]");
        }

        request.setResourceURI(redirectTarget);
    }

    private void overrideRequestParams(Log log, SADSRequest request, String pageId) {
        boolean doOverride = InitUtils.getBoolean("override.page.id.request.parameters", true, request.getServiceScenario().getAttributes());
        if (doOverride) {
            Map<String, String> newParams = UrlUtils.getParametersMap(pageId);
            Map<String, String> requestParams = request.getParameters();
            Iterator var7 = newParams.entrySet().iterator();

            while(var7.hasNext()) {
                Map.Entry<String, String> newParam = (Map.Entry)var7.next();
                String name = (String)newParam.getKey();
                if (requestParams.containsKey(name)) {
                    String oldValue = (String)requestParams.get(name);
                    String newValue = (String)newParam.getValue();
                    if (log.isDebugEnabled()) {
                        log.debug("Overriding request parameter [" + name + "]: [" + oldValue + "] -> [" + newValue + "]");
                    }

                    requestParams.replace(name, newValue);
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

    private ProfileStorage getProfileStorage() {
        try {
            return (ProfileStorage) SADSInitializer.getResourceStorage().get("profile-storage");

        } catch (NotFoundResourceException e) {
            log.info("Profile storage unavailable", e);
            return null;
        }
    }

    @Override
    public void destroy() {}
}
