package com.eyeline.expasoft.chatme.filter;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeck on 19/07/17.
 */
public abstract class WordsFilter implements Filter {

    protected abstract String filterWord(String word);

    @Override
    public String filter(String userSay) {
        String[] words = StringUtils.split(userSay, ' ');
        List<String> result = new ArrayList<>(words.length);
        for (String word: words) {
            result.add(filterWord(word));
        }
        return StringUtils.join(result, ' ');

    }
}
