package com.eyeline.expasoft.chatme.filter;

import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;

import java.util.Collection;
import java.util.Properties;

/**
 * Created by jeck on 19/07/17.
 */
public class StringReplaceWordFilter extends WordsFilter implements Initable {
    private Collection<String> whats;
    private String to;

    @Override
    protected String filterWord(String word) {
        for (String what: whats) {
            if (what.equalsIgnoreCase(word)) {
                return to;
            }
        }
        return word;
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
