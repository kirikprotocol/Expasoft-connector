package com.eyeline.miniapps.interceptor;

import com.eyelinecom.whoisd.sads2.RequestDispatcher;
import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.connector.SADSRequest;
import com.eyelinecom.whoisd.sads2.exception.InterceptionException;
import com.eyelinecom.whoisd.sads2.interceptor.BlankInterceptor;

import java.util.Properties;

public class AddParameterInterceptor extends BlankInterceptor implements Initable{
    String name;
    String value;

    @Override
    public void destroy() {

    }

    @Override
    public void onRequest(SADSRequest request, RequestDispatcher dispatcher) throws InterceptionException {
        request.getParameters().put(name, value);
    }

    @Override
    public void init(Properties config) throws Exception {
        this.name = InitUtils.getString("name", config);
        this.value = InitUtils.getString("value", config);
    }
}
