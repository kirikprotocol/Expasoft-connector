package com.eyeline.test;

import com.eyeline.miniapps.ai.model.ServiceAiHelper;

import java.util.List;

/**
 * Created by jeck on 12/07/17.
 */
public class IntentsTreeTest {
    public static void main(String[] args) {
        String page = "op";
        String intent = "get";
        List<String> strings = ServiceAiHelper.buildContextIntentsList(page, intent);
        System.out.print(strings);
    }
}
