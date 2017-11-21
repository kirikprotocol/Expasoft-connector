package com.eyeline.miniapps.filter;

import com.eyelinecom.whoisd.sads2.common.InitUtils;
import com.eyelinecom.whoisd.sads2.common.Initable;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by jeck on 19/07/17.
 */
public class PatternReplaceWordFilter extends WordsFilter implements Initable {
    private Collection<Pattern> patterns;
    private String to;

    @Override
    protected String filterWord(String word) {
        for (Pattern pattern: patterns) {
            if (pattern.matcher(word).matches()) {
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
        List<String> patternsList = InitUtils.getOrderedStringsStartsWith("what.", config);
        patterns = new ArrayList<>();
        for (String pattern: patternsList) {
            patterns.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE|Pattern.DOTALL|Pattern.UNICODE_CASE));
        }
    }
}
