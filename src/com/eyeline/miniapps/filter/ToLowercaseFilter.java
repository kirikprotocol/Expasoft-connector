package com.eyeline.miniapps.filter;

/**
 * Created by jeck on 19/07/17.
 */
public class ToLowercaseFilter implements Filter {
    @Override
    public String filter(String userSay) {
        return userSay.toLowerCase();
    }
}
