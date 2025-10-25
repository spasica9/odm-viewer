package com.example.odm_viewer_backend.model;

import java.util.HashMap;
import java.util.Map;

public class TransitionTimingConstraint {

    private String oid;
    private String name;
    private String transitionOID;
    private String timepointTarget;
    private String timepointPreWindow;
    private String timepointPostWindow;
    private Map<String, String> description = new HashMap<>();

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransitionOID() {
        return transitionOID;
    }

    public void setTransitionOID(String transitionOID) {
        this.transitionOID = transitionOID;
    }

    public String getTimepointTarget() {
        return timepointTarget;
    }

    public void setTimepointTarget(String timepointTarget) {
        this.timepointTarget = timepointTarget;
    }

    public String getTimepointPreWindow() {
        return timepointPreWindow;
    }

    public void setTimepointPreWindow(String timepointPreWindow) {
        this.timepointPreWindow = timepointPreWindow;
    }

    public String getTimepointPostWindow() {
        return timepointPostWindow;
    }

    public void setTimepointPostWindow(String timepointPostWindow) {
        this.timepointPostWindow = timepointPostWindow;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

}
