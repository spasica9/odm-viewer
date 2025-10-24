package com.example.odm_viewer_backend.model;

import java.util.List;

public class MetaDataVersion {
    private String oid;
    private String name;
    private List<StudyEventDef> studyEventDefs;
    private List<ItemGroupDef> itemGroupDefs;
    private List<ItemDef> itemDefs;
    private List<CodeList> codeLists;

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

    public List<StudyEventDef> getStudyEventDefs() {
        return studyEventDefs;
    }

    public void setStudyEventDefs(List<StudyEventDef> studyEventDefs) {
        this.studyEventDefs = studyEventDefs;
    }

    public List<ItemGroupDef> getItemGroupDefs() {
        return itemGroupDefs;
    }

    public void setItemGroupDefs(List<ItemGroupDef> itemGroupDefs) {
        this.itemGroupDefs = itemGroupDefs;
    }

    public List<ItemDef> getItemDefs() {
        return itemDefs;
    }

    public void setItemDefs(List<ItemDef> itemDefs) {
        this.itemDefs = itemDefs;
    }

    public List<CodeList> getCodeLists() {
        return codeLists;
    }

    public void setCodeLists(List<CodeList> codeLists) {
        this.codeLists = codeLists;
    }

}