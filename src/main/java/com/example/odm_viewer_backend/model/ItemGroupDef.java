package com.example.odm_viewer_backend.model;

import java.util.List;

public class ItemGroupDef {

    private String oid;
    private String name;
    private String repeating;
    private String type;
    private List<ItemRef> itemRefs;

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

    public String getRepeating() {
        return repeating;
    }

    public void setRepeating(String repeating) {
        this.repeating = repeating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ItemRef> getItemRefs() {
        return itemRefs;
    }

    public void setItemRefs(List<ItemRef> itemRefs) {
        this.itemRefs = itemRefs;
    }

}
