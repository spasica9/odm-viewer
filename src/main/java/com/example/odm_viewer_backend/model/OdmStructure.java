package com.example.odm_viewer_backend.model;

import java.util.List;

public class OdmStructure {

    private final String odmVersion;
    private final String studyOid;
    private final String studyName;
    private final String metadataVersion;
    private final List<String> formOids;

    public OdmStructure(String odmVersion, String studyOid, String studyName, String metadataVersion,
            List<String> formOids) {
        this.odmVersion = odmVersion;
        this.studyOid = studyOid;
        this.studyName = studyName;
        this.metadataVersion = metadataVersion;
        this.formOids = formOids;
    }

    public String getOdmVersion() {
        return odmVersion;
    }

    public String getStudyOid() {
        return studyOid;
    }

    public String getStudyName() {
        return studyName;
    }

    public String getMetadataVersion() {
        return metadataVersion;
    }

    public List<String> getFormOids() {
        return formOids;
    }
}
