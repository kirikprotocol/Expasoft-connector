package com.eyeline.expasoft.chatme.filter;

import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;
import com.eyelinecom.whoisd.sads2.common.SADSInitUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created by jeck on 19/07/17.
 */
public class ConditionFilter implements Filter, Initable{
    private String operation;
    private String argument;
    Filter filter;

    @Override
    public String filter(String userSay) {
        Pattern pattern;
        String[] words;
        switch (operation) {
            case "contains":
                if (userSay.contains(argument)) {
                    return filter.filter(userSay);
                }
            break;
            case "containsWord":
                words = StringUtils.split(userSay, ' ');
                for (String word: words) {
                    if (word.equals(argument)) {
                        return filter.filter(userSay);
                    }
                }
            break;
            case "matchPattern":
                pattern = Pattern.compile(argument, Pattern.CASE_INSENSITIVE|Pattern.DOTALL|Pattern.UNICODE_CASE);
                if (pattern.matcher(userSay).matches()) {
                    return filter.filter(userSay);
                }
            break;
            case "matchPatternWord":
                pattern = Pattern.compile(argument, Pattern.CASE_INSENSITIVE|Pattern.DOTALL|Pattern.UNICODE_CASE);
                words = StringUtils.split(userSay, ' ');
                for (String word: words) {
                    if (pattern.matcher(userSay).matches()) {
                        return filter.filter(userSay);
                    }
                }
            break;
        }
        return userSay;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(Properties config) throws Exception {
        operation = InitUtils.getString("op", config);
        argument = InitUtils.getString("arg", config);
        filter = SADSInitUtils.getResource("filter", config);
    }
}
