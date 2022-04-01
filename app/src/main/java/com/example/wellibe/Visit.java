package com.example.wellibe;

import java.util.HashMap;
import java.util.Map;

public class Visit {

    long time_stamp;
    String doc_name;
    String summary;
    boolean loved;
    String doc_id;

    public Visit(long time_stamp, String doc_name, String summary, boolean loved, String doc_id) {
        this.time_stamp = time_stamp;
        this.doc_name = doc_name;
        this.summary = summary;
        this.loved = loved;
        this.doc_id = doc_id;
    }

    public Visit() {
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isLoved() {
        return loved;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }
}
