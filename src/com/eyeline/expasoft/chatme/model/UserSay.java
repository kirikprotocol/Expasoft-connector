package com.eyeline.expasoft.chatme.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeck on 26/06/17.
 */
public class UserSay {
    //String id;
    List<Data> data = new ArrayList<Data>();

    public UserSay() {
    }

    public UserSay(String text) {
        this.data.add(new Data(text));
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public void add(String text) {
        this.data.add(new Data(text));
    }

    public static class Data {
        String text;

        public Data() {
        }

        public Data(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
