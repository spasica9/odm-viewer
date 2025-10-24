package com.example.odm_viewer_backend.model;

public class Study {

    private String oid;
    private String studyName;
    private String protocolName;
    private MetaDataVersion metaDataVersion;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public MetaDataVersion getMetaDataVersion() {
        return metaDataVersion;
    }

    public void setMetaDataVersion(MetaDataVersion metaDataVersion) {
        this.metaDataVersion = metaDataVersion;
    }

}
