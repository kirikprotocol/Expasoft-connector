package com.eyeline.miniapps.filter;

/**
 * Created by jeck on 24/07/17.
 */
public class BlankFilter implements Filter {
    @Override
    public String filter(String userSay) {
        return userSay;
    }
}
