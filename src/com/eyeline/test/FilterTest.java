package com.eyeline.test;

import com.eyeline.expasoft.chatme.filter.Filter;
import com.eyeline.expasoft.chatme.filter.PatternReplaceWordFilter;

import java.util.Properties;

/**
 * Created by jeck on 24/07/17.
 */
public class FilterTest {
    public static void main(String[] atgs){
        Properties props = new Properties();
        props.setProperty("to", "добровольная");
        props.setProperty("what.1", "(.)*страх(.)*");
        PatternReplaceWordFilter f = new PatternReplaceWordFilter();
        try {
            f.init(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String test = "расскажи про застрsаховалку";
        System.out.println(f.filter(test));

    }
}
