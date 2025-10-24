package com.example.odm_viewer_backend.model;

import java.util.List;

public class ClinicalData {

    private String studyOID;
    private String metaDataVersionOID;
    private List<SubjectData> subjects;

    public String getStudyOID() {
        return studyOID;
    }

    public void setStudyOID(String studyOID) {
        this.studyOID = studyOID;
    }

    public String getMetaDataVersionOID() {
        return metaDataVersionOID;
    }

    public void setMetaDataVersionOID(String metaDataVersionOID) {
        this.metaDataVersionOID = metaDataVersionOID;
    }

    public List<SubjectData> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectData> subjects) {
        this.subjects = subjects;
    }

}
