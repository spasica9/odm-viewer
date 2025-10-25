package com.example.odm_viewer_backend.model;

public class OdmStructure {
    private String xmlns;
    private String creationDateTime;
    private String fileOID;
    private String fileType;
    private String granularity;
    private String odmVersion;
    private String sourceSystem;
    private String sourceSystemVersion;

    private Study study;
    private ClinicalData clinicalData;

    private MetaDataVersion metaDataVersion;
    private Protocol protocol;

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getFileOID() {
        return fileOID;
    }

    public void setFileOID(String fileOID) {
        this.fileOID = fileOID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public String getOdmVersion() {
        return odmVersion;
    }

    public void setOdmVersion(String odmVersion) {
        this.odmVersion = odmVersion;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceSystemVersion() {
        return sourceSystemVersion;
    }

    public void setSourceSystemVersion(String sourceSystemVersion) {
        this.sourceSystemVersion = sourceSystemVersion;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public ClinicalData getClinicalData() {
        return clinicalData;
    }

    public void setClinicalData(ClinicalData clinicalData) {
        this.clinicalData = clinicalData;
    }

    public MetaDataVersion getMetaDataVersion() {
        return metaDataVersion;
    }

    public void setMetaDataVersion(MetaDataVersion metaDataVersion) {
        this.metaDataVersion = metaDataVersion;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

}
