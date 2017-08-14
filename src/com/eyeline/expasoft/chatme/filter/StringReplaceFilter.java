package com.eyeline.expasoft.chatme.filter;

import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jeck on 19/07/17.
 */
public class StringReplaceFilter implements Filter, Initable{
    private Collection<String> whats;
    private String to;

    @Override
    public String filter(String userSay) {
        for (String what: whats) {
            userSay = StringUtils.replace(userSay, what, to);
        }
        return userSay;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(Properties config) throws Exception {
        to = InitUtils.getString("to", config);
        whats = InitUtils.getOrderedStringsStartsWith("what.", config);
    }
}
