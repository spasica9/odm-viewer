package com.example.odm_viewer_backend.model;

import java.util.ArrayList;
import java.util.List;

public class WorkflowDef {

    private String oid;
    private String name;
    private String description;

    private List<Transition> transitions = new ArrayList<>();
    private List<Branching> branchings = new ArrayList<>();

    private List<OdmElement> genericElements = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public List<Branching> getBranchings() {
        return branchings;
    }

    public void setBranchings(List<Branching> branchings) {
        this.branchings = branchings;
    }

    public List<OdmElement> getGenericElements() {
        return genericElements;
    }

    public void setGenericElements(List<OdmElement> genericElements) {
        this.genericElements = genericElements;
    }

}
