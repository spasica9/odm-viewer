package com.example.odm_viewer_backend.model;

import java.util.ArrayList;
import java.util.List;

public class MethodDef {

    private String oid;
    private String name;
    private List<Parameter> parameters = new ArrayList<>();
    private List<ReturnValue> returnValues = new ArrayList<>();
    private String formalExpression;

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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<ReturnValue> getReturnValues() {
        return returnValues;
    }

    public void setReturnValues(List<ReturnValue> returnValues) {
        this.returnValues = returnValues;
    }

    public String getFormalExpression() {
        return formalExpression;
    }

    public void setFormalExpression(String formalExpression) {
        this.formalExpression = formalExpression;
    }

}
