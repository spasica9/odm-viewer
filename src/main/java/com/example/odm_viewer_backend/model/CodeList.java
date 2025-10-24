package com.example.odm_viewer_backend.model;

import java.util.List;

public class CodeList {

    private String oid;
    private String name;
    private String dataType;
    private List<CodeListItem> items;

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

    public List<CodeListItem> getItems() {
        return items;
    }

    public void setItems(List<CodeListItem> items) {
        this.items = items;
    }

}
