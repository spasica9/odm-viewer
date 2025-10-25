package com.example.odm_viewer_backend.model;

import java.util.ArrayList;
import java.util.List;

public class Branching {

    private String oid;
    private String name;
    private String type;
    private List<Transition> targetTransitions = new ArrayList<>();
    private Transition defaultTransition;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Transition> getTargetTransitions() {
        return targetTransitions;
    }

    public void setTargetTransitions(List<Transition> targetTransitions) {
        this.targetTransitions = targetTransitions;
    }

    public Transition getDefaultTransition() {
        return defaultTransition;
    }

    public void setDefaultTransition(Transition defaultTransition) {
        this.defaultTransition = defaultTransition;
    }

}
