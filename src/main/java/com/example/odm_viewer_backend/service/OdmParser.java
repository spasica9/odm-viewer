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
            WorkflowDef currentWorkflowDef = null;
            Branching currentBranching = null;
            Transition currentTransition = null;
            OdmElement currentOdmElement = null;
            Protocol currentProtocol = null;
            StudyTiming currentStudyTiming = null;
            TransitionTimingConstraint currentTimingConstraint = null;
            Arm currentArm = null;
            Epoch currentEpoch = null;
            StudyEventGroupDef currentSEGDef = null;

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

                                if (currentStudy != null) {
                                    currentStudy.setMetaDataVersion(currentMDV);
                                } else {
                                    odm.setMetaDataVersion(currentMDV);
                                }
                                break;

                            case "Protocol":
                                currentProtocol = new Protocol();
                                currentProtocol.setName(reader.getAttributeValue(null, "Name"));
                                currentProtocol.setWorkflows(new ArrayList<>());
                                currentProtocol.setArms(new ArrayList<>());
                                currentProtocol.setEpochs(new ArrayList<>());
                                odm.setProtocol(currentProtocol);
                                break;

                            case "Arm":
                                currentArm = new Arm();
                                currentArm.setOid(reader.getAttributeValue(null, "OID"));
                                currentArm.setName(reader.getAttributeValue(null, "Name"));
                                if (currentProtocol != null)
                                    currentProtocol.getArms().add(currentArm);
                                break;

                            case "Epoch":
                                currentEpoch = new Epoch();
                                currentEpoch.setOid(reader.getAttributeValue(null, "OID"));
                                currentEpoch.setName(reader.getAttributeValue(null, "Name"));
                                String seq = reader.getAttributeValue(null, "SequenceNumber");
                                currentEpoch.setSequenceNumber(seq != null ? Integer.parseInt(seq) : 0);
                                if (currentProtocol != null)
                                    currentProtocol.getEpochs().add(currentEpoch);
                                break;

                            case "StudyEventGroupDef":
                                currentSEGDef = new StudyEventGroupDef();
                                currentSEGDef.setOid(reader.getAttributeValue(null, "OID"));
                                currentSEGDef.setName(reader.getAttributeValue(null, "Name"));
                                currentSEGDef.setArmOID(reader.getAttributeValue(null, "ArmOID"));
                                currentSEGDef.setEpochOID(reader.getAttributeValue(null, "EpochOID"));
                                currentSEGDef.setDescription(""); // pripremi prazno, popuni u Description
                                if (currentMDV != null)
                                    currentMDV.getStudyEventGroupDefs().add(currentSEGDef);
                                break;

                            case "StudyEventGroupRef":
                                if (currentSEGDef != null) {
                                    currentSEGDef.setStudyEventGroupRefOID(
                                            reader.getAttributeValue(null, "StudyEventGroupOID"));
                                }
                                break;

                            case "Description":
                                if (reader.nextTag() == XMLStreamConstants.START_ELEMENT &&
                                        "TranslatedText".equals(reader.getLocalName())) {
                                    String text = reader.getElementText();
                                    if (currentSEGDef != null) {
                                        currentSEGDef.setDescription(text);
                                    } else if (currentArm != null) {
                                        currentArm.setDescription(text);
                                    } else if (currentEpoch != null) {
                                        currentEpoch.setDescription(text);
                                    } else if (currentProtocol != null) {
                                        currentProtocol.setDescription(text);
                                    }
                                }
                                break;
                            case "StudyTiming":
                                currentStudyTiming = new StudyTiming();
                                currentStudyTiming.setOid(reader.getAttributeValue(null, "OID"));
                                currentStudyTiming.setName(reader.getAttributeValue(null, "Name"));
                                if (currentProtocol != null)
                                    currentProtocol.getStudyTimings().add(currentStudyTiming);
                                break;

                            case "TransitionTimingConstraint":
                                currentTimingConstraint = new TransitionTimingConstraint();
                                currentTimingConstraint.setOid(reader.getAttributeValue(null, "OID"));
                                currentTimingConstraint.setName(reader.getAttributeValue(null, "Name"));
                                currentTimingConstraint
                                        .setTransitionOID(reader.getAttributeValue(null, "TransitionOID"));
                                currentTimingConstraint
                                        .setTimepointTarget(reader.getAttributeValue(null, "TimepointTarget"));
                                currentTimingConstraint
                                        .setTimepointPreWindow(reader.getAttributeValue(null, "TimepointPreWindow"));
                                currentTimingConstraint
                                        .setTimepointPostWindow(reader.getAttributeValue(null, "TimepointPostWindow"));
                                if (currentStudyTiming != null)
                                    currentStudyTiming.getTransitionTimingConstraints().add(currentTimingConstraint);
                                break;

                            case "TranslatedText":
                                String text = normalizeText(reader.getElementText());

                                if (currentArm != null) {
                                    currentArm.setDescription(text);
                                } else if (currentEpoch != null) {
                                    currentEpoch.setDescription(text);
                                } else if (currentProtocol != null && currentProtocol.getDescription() == null) {
                                    currentProtocol.setDescription(text);
                                } else if (currentSEGDef != null) {
                                    currentSEGDef.setDescription(text);
                                }
                                break;

                            case "WorkflowDef":
                                currentWorkflowDef = new WorkflowDef();
                                currentWorkflowDef.setOid(reader.getAttributeValue(null, "OID"));
                                currentWorkflowDef.setName(normalizeText(reader.getAttributeValue(null, "Name")));
                                currentWorkflowDef.setDescription("");
                                if (currentMDV != null)
                                    currentMDV.getWorkflowDefs().add(currentWorkflowDef);
                                break;

                            case "WorkflowStart":
                                if (currentWorkflowDef != null) {
                                    OdmElement start = new OdmElement();
                                    start.setTagName("WorkflowStart");
                                    start.getAttributes().put("StartOID", reader.getAttributeValue(null, "StartOID"));
                                    currentWorkflowDef.getGenericElements().add(start);
                                }
                                break;

                            case "Transition":
                                currentTransition = new Transition();
                                currentTransition.setOid(reader.getAttributeValue(null, "OID"));
                                currentTransition.setFrom(reader.getAttributeValue(null, "SourceOID"));
                                currentTransition.setTo(reader.getAttributeValue(null, "TargetOID"));
                                currentTransition.setConditionOID(reader.getAttributeValue(null, "ConditionOID"));
                                currentTransition.setName(normalizeText(reader.getAttributeValue(null, "Name")));
                                currentTransition.setDescription("");
                                if (currentWorkflowDef != null)
                                    currentWorkflowDef.getTransitions().add(currentTransition);
                                break;

                            case "Branching":
                                currentBranching = new Branching();
                                currentBranching.setOid(reader.getAttributeValue(null, "OID"));
                                currentBranching.setName(normalizeText(reader.getAttributeValue(null, "Name")));
                                currentBranching.setType(reader.getAttributeValue(null, "Type"));
                                if (currentWorkflowDef != null)
                                    currentWorkflowDef.getBranchings().add(currentBranching);
                                break;

                            case "TargetTransition":
                                if (currentBranching != null && currentTransition != null) {
                                    Transition target = new Transition();
                                    target.setOid(reader.getAttributeValue(null, "TargetTransitionOID"));
                                    target.setConditionOID(reader.getAttributeValue(null, "ConditionOID"));
                                    currentBranching.getTargetTransitions().add(target);
                                }
                                break;

                            case "DefaultTransition":
                                if (currentBranching != null) {
                                    Transition defaultTrans = new Transition();
                                    defaultTrans.setOid(reader.getAttributeValue(null, "TargetTransitionOID"));
                                    currentBranching.setDefaultTransition(defaultTrans);
                                }
                                break;

                            case "WorkflowEnd":
                                if (currentWorkflowDef != null) {
                                    OdmElement workflowEnd = new OdmElement();
                                    workflowEnd.setTagName("WorkflowEnd");
                                    workflowEnd.getAttributes().put("EndOID", reader.getAttributeValue(null, "EndOID"));
                                    currentWorkflowDef.getGenericElements().add(workflowEnd);
                                }
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

                            default:
                                currentOdmElement = new OdmElement();
                                currentOdmElement.setTagName(localName);
                                int attrCount = reader.getAttributeCount();
                                for (int i = 0; i < attrCount; i++) {
                                    currentOdmElement.getAttributes().put(reader.getAttributeLocalName(i),
                                            reader.getAttributeValue(i));
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
                            case "WorkflowDef":
                                currentWorkflowDef = null;
                                break;
                            case "Transition":
                                currentTransition = null;
                                break;
                            case "Branching":
                                currentBranching = null;
                                break;
                            case "StudyTiming":
                                currentStudyTiming = null;
                                break;
                            case "TransitionTimingConstraint":
                                currentTimingConstraint = null;
                                break;
                            case "Protocol":
                                currentProtocol = null;
                                break;
                            case "Arm":
                                currentArm = null;
                                break;
                            case "Epoch":
                                currentEpoch = null;
                                break;

                            case "StudyEventGroupDef":
                                currentSEGDef = null;
                                break;
                            default:
                                currentOdmElement = null;
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
