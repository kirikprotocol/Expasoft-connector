package com.eyeline.expasoft.chatme.filter;

import java.util.List;

/**
 * Created by jeck on 19/07/17.
 */
public class ChainFilter implements Filter {
    private List<Filter> filters;

    public ChainFilter(List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public String filter(String userSay) {
        for (Filter filter: filters) {
            userSay = filter.filter(userSay);
        }
        return userSay;
    }
}
