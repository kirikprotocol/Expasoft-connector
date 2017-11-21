package com.eyeline.rasa;

/**
 * Created by jeck on 20/11/17.
 */
public class ParseRequest {
    String q;
    String model;
    String project;

    public ParseRequest(String q, String model, String project) {
        this.q = q;
        this.model = model;
        this.project = project;
    }

    public String getQ() {
        return q;
    }

    public String getModel() {
        return model;
    }

    public String getProject() {
        return project;
    }

    @Override
    public String toString() {
        return "ParseRequest{" +
                "q='" + q + '\'' +
                ", model='" + model + '\'' +
                ", project='" + project + '\'' +
                '}';
    }
}
