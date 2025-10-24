package com.example.odm_viewer_backend.model;

import java.util.List;

public class ItemDef {

    private String oid;
    private String name;
    private String dataType;
    private int length;
    private String questionText;
    private List<CodeListRef> codeListRefs;

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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<CodeListRef> getCodeListRefs() {
        return codeListRefs;
    }

    public void setCodeListRefs(List<CodeListRef> codeListRefs) {
        this.codeListRefs = codeListRefs;
    }

}
