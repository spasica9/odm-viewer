package com.example.odm_viewer_backend.model;

import java.util.ArrayList;
import java.util.List;

public class StudyTiming {

    private String oid;
    private String name;
    private List<TransitionTimingConstraint> transitionTimingConstraints = new ArrayList<>();

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

    public List<TransitionTimingConstraint> getTransitionTimingConstraints() {
        return transitionTimingConstraints;
    }

    public void setTransitionTimingConstraints(List<TransitionTimingConstraint> transitionTimingConstraints) {
        this.transitionTimingConstraints = transitionTimingConstraints;
    }

}
