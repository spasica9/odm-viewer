package com.example.odm_viewer_backend.model;

import java.util.List;

public class SubjectData {

    private String subjectKey;
    private List<StudyEventData> studyEventDataList;

    public String getSubjectKey() {
        return subjectKey;
    }

    public void setSubjectKey(String subjectKey) {
        this.subjectKey = subjectKey;
    }

    public List<StudyEventData> getStudyEventDataList() {
        return studyEventDataList;
    }

    public void setStudyEventDataList(List<StudyEventData> studyEventDataList) {
        this.studyEventDataList = studyEventDataList;
    }

}
