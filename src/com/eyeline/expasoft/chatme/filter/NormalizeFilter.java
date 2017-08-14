package com.eyeline.expasoft.chatme.filter;

import org.apache.commons.lang.StringUtils;

/**
 * Created by jeck on 19/07/17.
 */
public class NormalizeFilter implements Filter {
    @Override
    public String filter(String userSay) {
        String[] words = StringUtils.split(userSay, ' ');
        return StringUtils.join(words, ' ');
    }
}
