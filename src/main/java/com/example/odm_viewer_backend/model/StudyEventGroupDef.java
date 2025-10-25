package com.example.odm_viewer_backend.model;

public class StudyEventGroupDef {

    private String oid;
    private String name;
    private String armOID;
    private String epochOID;
    private String description;
    private String studyEventGroupRefOID;

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

    public String getArmOID() {
        return armOID;
    }

    public void setArmOID(String armOID) {
        this.armOID = armOID;
    }

    public String getEpochOID() {
        return epochOID;
    }

    public void setEpochOID(String epochOID) {
        this.epochOID = epochOID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudyEventGroupRefOID() {
        return studyEventGroupRefOID;
    }

    public void setStudyEventGroupRefOID(String studyEventGroupRefOID) {
        this.studyEventGroupRefOID = studyEventGroupRefOID;
    }

}
