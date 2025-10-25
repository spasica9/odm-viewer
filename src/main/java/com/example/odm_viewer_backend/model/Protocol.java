package com.example.odm_viewer_backend.model;

import java.util.ArrayList;
import java.util.List;

public class Protocol {

    private String name;
    private List<WorkflowDef> workflows = new ArrayList<>();
    private List<StudyTiming> studyTimings = new ArrayList<>();

    private String description;
    private List<Arm> arms = new ArrayList<>();
    private List<Epoch> epochs = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkflowDef> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<WorkflowDef> workflows) {
        this.workflows = workflows;
    }

    public List<StudyTiming> getStudyTimings() {
        return studyTimings;
    }

    public void setStudyTimings(List<StudyTiming> studyTimings) {
        this.studyTimings = studyTimings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Arm> getArms() {
        return arms;
    }

    public void setArms(List<Arm> arms) {
        this.arms = arms;
    }

    public List<Epoch> getEpochs() {
        return epochs;
    }

    public void setEpochs(List<Epoch> epochs) {
        this.epochs = epochs;
    }

}
