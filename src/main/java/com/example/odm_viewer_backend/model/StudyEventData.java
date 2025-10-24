package com.example.odm_viewer_backend.model;

import java.util.List;

public class StudyEventData {

    private String studyEventOID;
    private String studyEventRepeatKey;
    private List<ItemGroupData> itemGroupDataList;

    public String getStudyEventOID() {
        return studyEventOID;
    }

    public void setStudyEventOID(String studyEventOID) {
        this.studyEventOID = studyEventOID;
    }

    public String getStudyEventRepeatKey() {
        return studyEventRepeatKey;
    }

    public void setStudyEventRepeatKey(String studyEventRepeatKey) {
        this.studyEventRepeatKey = studyEventRepeatKey;
    }

    public List<ItemGroupData> getItemGroupDataList() {
        return itemGroupDataList;
    }

    public void setItemGroupDataList(List<ItemGroupData> itemGroupDataList) {
        this.itemGroupDataList = itemGroupDataList;
    }

}
