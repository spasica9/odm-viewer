package com.example.odm_viewer_backend.model;

import java.util.HashMap;
import java.util.Map;

public class Condition {

    private Map<String, String> attributes = new HashMap<>();
    private String text;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
