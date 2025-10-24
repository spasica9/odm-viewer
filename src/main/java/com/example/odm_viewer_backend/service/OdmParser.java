package com.example.odm_viewer_backend.service;

import com.example.odm_viewer_backend.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;

@Service
public class OdmParser {

    public OdmStructure parseOdmFile(MultipartFile file) {
        OdmStructure odm = new OdmStructure();
        try (InputStream inputStream = file.getInputStream()) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            Study currentStudy = null;
            MetaDataVersion currentMDV = null;
            StudyEventDef currentStudyEventDef = null;
            ItemGroupDef currentItemGroupDef = null;
            ItemDef currentItemDef = null;
            CodeList currentCodeList = null;
            CodeListItem currentCodeListItem = null;
            ClinicalData currentClinicalData = null;
            SubjectData currentSubjectData = null;
            StudyEventData currentStudyEventData = null;
            ItemGroupData currentItemGroupData = null;
            ItemData currentItemData = null;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String localName = reader.getLocalName();

                        switch (localName) {
                            case "ODM":
                                odm.setOdmVersion(reader.getAttributeValue(null, "ODMVersion"));
                                odm.setXmlns(reader.getNamespaceURI());
                                odm.setCreationDateTime(reader.getAttributeValue(null, "CreationDateTime"));
                                odm.setFileOID(reader.getAttributeValue(null, "FileOID"));
                                odm.setFileType(reader.getAttributeValue(null, "FileType"));
                                odm.setGranularity(reader.getAttributeValue(null, "Granularity"));
                                odm.setSourceSystem(reader.getAttributeValue(null, "SourceSystem"));
                                odm.setSourceSystemVersion(reader.getAttributeValue(null, "SourceSystemVersion"));
                                break;

                            case "Study":
                                currentStudy = new Study();
                                currentStudy.setOid(reader.getAttributeValue(null, "OID"));
                                currentStudy.setStudyName(reader.getAttributeValue(null, "StudyName"));
                                currentStudy.setProtocolName(reader.getAttributeValue(null, "ProtocolName"));
                                odm.setStudy(currentStudy);
                                break;

                            case "MetaDataVersion":
                                currentMDV = new MetaDataVersion();
                                currentMDV.setOid(reader.getAttributeValue(null, "OID"));
                                currentMDV.setName(reader.getAttributeValue(null, "Name"));
                                currentMDV.setStudyEventDefs(new ArrayList<>());
                                currentMDV.setItemGroupDefs(new ArrayList<>());
                                currentMDV.setItemDefs(new ArrayList<>());
                                currentMDV.setCodeLists(new ArrayList<>());
                                if (currentStudy != null)
                                    currentStudy.setMetaDataVersion(currentMDV);
                                break;

                            case "StudyEventDef":
                                currentStudyEventDef = new StudyEventDef();
                                currentStudyEventDef.setOid(reader.getAttributeValue(null, "OID"));
                                currentStudyEventDef.setName(reader.getAttributeValue(null, "Name"));
                                currentStudyEventDef.setRepeating(reader.getAttributeValue(null, "Repeating"));
                                currentStudyEventDef.setType(reader.getAttributeValue(null, "Type"));
                                currentStudyEventDef.setItemGroupRefs(new ArrayList<>());
                                if (currentMDV != null)
                                    currentMDV.getStudyEventDefs().add(currentStudyEventDef);
                                break;

                            case "ItemGroupRef":
                                if (currentStudyEventDef != null) {
                                    ItemGroupRef igr = new ItemGroupRef();
                                    igr.setItemGroupOID(reader.getAttributeValue(null, "ItemGroupOID"));
                                    igr.setMandatory(reader.getAttributeValue(null, "Mandatory"));
                                    currentStudyEventDef.getItemGroupRefs().add(igr);
                                }
                                break;

                            case "ItemGroupDef":
                                currentItemGroupDef = new ItemGroupDef();
                                currentItemGroupDef.setOid(reader.getAttributeValue(null, "OID"));
                                currentItemGroupDef.setName(reader.getAttributeValue(null, "Name"));
                                currentItemGroupDef.setRepeating(reader.getAttributeValue(null, "Repeating"));
                                currentItemGroupDef.setType(reader.getAttributeValue(null, "Type"));
                                currentItemGroupDef.setItemRefs(new ArrayList<>());
                                if (currentMDV != null)
                                    currentMDV.getItemGroupDefs().add(currentItemGroupDef);
                                break;

                            case "ItemRef":
                                if (currentItemGroupDef != null) {
                                    ItemRef ir = new ItemRef();
                                    ir.setItemOID(reader.getAttributeValue(null, "ItemOID"));
                                    ir.setMandatory(reader.getAttributeValue(null, "Mandatory"));
                                    ir.setOrderNumber(reader.getAttributeValue(null, "OrderNumber"));
                                    ir.setRepeat(reader.getAttributeValue(null, "Repeat"));
                                    currentItemGroupDef.getItemRefs().add(ir);
                                }
                                break;

                            case "ItemDef":
                                currentItemDef = new ItemDef();
                                currentItemDef.setOid(reader.getAttributeValue(null, "OID"));
                                currentItemDef.setName(normalizeText(reader.getAttributeValue(null, "Name")));
                                currentItemDef.setDataType(reader.getAttributeValue(null, "DataType"));
                                String lengthStr = reader.getAttributeValue(null, "Length");
                                currentItemDef.setLength(lengthStr != null ? Integer.parseInt(lengthStr) : 0);
                                currentItemDef.setQuestionText("");
                                currentItemDef.setCodeListRefs(new ArrayList<>());
                                if (currentMDV != null)
                                    currentMDV.getItemDefs().add(currentItemDef);
                                break;

                            case "CodeListRef":
                                if (currentItemDef != null) {
                                    CodeListRef clr = new CodeListRef();
                                    clr.setCodeListOID(reader.getAttributeValue(null, "CodeListOID"));
                                    clr.setMethodOID(reader.getAttributeValue(null, "MethodOID"));
                                    clr.setOrderNumber(reader.getAttributeValue(null, "OrderNumber"));
                                    currentItemDef.getCodeListRefs().add(clr);
                                }
                                break;

                            case "Question":
                                break;

                            case "CodeList":
                                currentCodeList = new CodeList();
                                currentCodeList.setOid(reader.getAttributeValue(null, "OID"));
                                currentCodeList.setName(reader.getAttributeValue(null, "Name"));
                                currentCodeList.setDataType(reader.getAttributeValue(null, "DataType"));
                                currentCodeList.setItems(new ArrayList<>());
                                if (currentMDV != null)
                                    currentMDV.getCodeLists().add(currentCodeList);
                                break;

                            case "CodeListItem":
                                if (currentCodeList != null) {
                                    currentCodeListItem = new CodeListItem();
                                    currentCodeListItem
                                            .setCodedValue(normalizeText(reader.getAttributeValue(null, "CodedValue")));
                                    currentCodeListItem.setRank(normalizeText(reader.getAttributeValue(null, "Rank")));
                                    currentCodeListItem.setOrderNumber(
                                            normalizeText(reader.getAttributeValue(null, "OrderNumber")));
                                    currentCodeListItem.setTranslatedText("");
                                    currentCodeList.getItems().add(currentCodeListItem);
                                }
                                break;

                            case "TranslatedText":
                                String text = reader.getElementText();
                                if (currentItemDef != null) {
                                    currentItemDef.setQuestionText(normalizeText(text));
                                } else if (currentCodeListItem != null) {
                                    currentCodeListItem.setTranslatedText(normalizeText(text));
                                }
                                break;

                            case "ClinicalData":
                                currentClinicalData = new ClinicalData();
                                currentClinicalData.setStudyOID(reader.getAttributeValue(null, "StudyOID"));
                                currentClinicalData
                                        .setMetaDataVersionOID(reader.getAttributeValue(null, "MetaDataVersionOID"));
                                currentClinicalData.setSubjects(new ArrayList<>());
                                odm.setClinicalData(currentClinicalData);
                                break;

                            case "SubjectData":
                                currentSubjectData = new SubjectData();
                                currentSubjectData.setSubjectKey(reader.getAttributeValue(null, "SubjectKey"));
                                currentSubjectData.setStudyEventDataList(new ArrayList<>());
                                if (currentClinicalData != null)
                                    currentClinicalData.getSubjects().add(currentSubjectData);
                                break;

                            case "StudyEventData":
                                currentStudyEventData = new StudyEventData();
                                currentStudyEventData.setStudyEventOID(reader.getAttributeValue(null, "StudyEventOID"));
                                currentStudyEventData
                                        .setStudyEventRepeatKey(reader.getAttributeValue(null, "StudyEventRepeatKey"));
                                currentStudyEventData.setItemGroupDataList(new ArrayList<>());
                                if (currentSubjectData != null)
                                    currentSubjectData.getStudyEventDataList().add(currentStudyEventData);
                                break;

                            case "ItemGroupData":
                                currentItemGroupData = new ItemGroupData();
                                currentItemGroupData.setItemGroupOID(reader.getAttributeValue(null, "ItemGroupOID"));
                                currentItemGroupData
                                        .setItemGroupRepeatKey(reader.getAttributeValue(null, "ItemGroupRepeatKey"));
                                currentItemGroupData.setItemDataList(new ArrayList<>());
                                if (currentStudyEventData != null)
                                    currentStudyEventData.getItemGroupDataList().add(currentItemGroupData);
                                break;

                            case "ItemData":
                                currentItemData = new ItemData();
                                currentItemData.setItemOID(reader.getAttributeValue(null, "ItemOID"));
                                break;

                            case "Value":
                                if (currentItemData != null) {
                                    String val = reader.getElementText();
                                    currentItemData.setValue(val != null ? val.trim() : null);
                                }
                                break;
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        String endLocalName = reader.getLocalName();
                        switch (endLocalName) {
                            case "StudyEventDef":
                                currentStudyEventDef = null;
                                break;
                            case "ItemGroupDef":
                                currentItemGroupDef = null;
                                break;
                            case "ItemDef":
                                currentItemDef = null;
                                break;
                            case "CodeList":
                                currentCodeList = null;
                                break;
                            case "CodeListItem":
                                currentCodeListItem = null;
                                break;
                            case "SubjectData":
                                currentSubjectData = null;
                                break;
                            case "StudyEventData":
                                currentStudyEventData = null;
                                break;
                            case "ItemGroupData":
                                currentItemGroupData = null;
                                break;
                            case "ItemData":
                                if (currentItemGroupData != null && currentItemData != null)
                                    currentItemGroupData.getItemDataList().add(currentItemData);
                                currentItemData = null;
                                break;
                        }
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return odm;
    }

    private String normalizeText(String text) {
        if (text == null)
            return "";
        return text.trim().replaceAll("\\s+", " ");
    }
}
