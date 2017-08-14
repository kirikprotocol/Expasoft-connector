package com.eyeline.expasoft.chatme.filter;

import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;

import java.util.Collection;
import java.util.Properties;

/**
 * Created by jeck on 19/07/17.
 */
public class PatternReplaceFilter implements Filter, Initable {
    private Collection<String> patterns;
    private String to;

    @Override
    public String filter(String userSay) {
        for (String pattern: patterns) {
            userSay = userSay.replaceAll(pattern, to);
        }
        return userSay;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(Properties config) throws Exception {
        to = InitUtils.getString("to", config);
        patterns = InitUtils.getOrderedStringsStartsWith("what.", config);
    }
}
