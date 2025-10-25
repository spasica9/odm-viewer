package com.example.odm_viewer_backend.model;

public class Transition {

    private String oid;
    private String from;
    private String to;
    private String conditionOID;
    private String name;
    private String description;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getConditionOID() {
        return conditionOID;
    }

    public void setConditionOID(String conditionOID) {
        this.conditionOID = conditionOID;
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

}
