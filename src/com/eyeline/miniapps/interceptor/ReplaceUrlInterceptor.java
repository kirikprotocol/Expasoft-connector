package com.eyeline.miniapps.interceptor;

import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.interceptor.BlankInterceptor;

import java.util.Properties;

/**
 * Created by jeck on 11/08/17.
 */
public class ReplaceUrlInterceptor extends BlankInterceptor implements Initable{
    private String what;
    private String to;

    @Override
    public void destroy() {

    }

    @Override
    public void init(Properties config) throws Exception {
        what = InitUtils.getString("what", config);
        to = InitUtils.getString("to", config);
    }

    @Override
    public void onRequest(SADSRequest request, RequestDispatcher dispatcher) throws InterceptionException {
        if (what!=null && to!=null) {
            String url = request.getResourceURI();
            request.setResourceURI(url.replaceAll(what, to));
        }
    }
}
