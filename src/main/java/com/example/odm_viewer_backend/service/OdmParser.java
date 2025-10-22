package com.example.odm_viewer_backend.service;

import com.example.odm_viewer_backend.model.OdmStructure;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class OdmParser {

    public OdmStructure parseOdmFile(MultipartFile file) {
        String odmVersion = "Unknown";
        String studyOid = "Unknown";
        String studyName = "Unknown";
        String metadataVersion = "Unknown";
        List<String> formOids = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

            boolean insideStudy = false;
            boolean insideMetaDataVersion = false;

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String localName = reader.getLocalName();

                        if ("ODM".equals(localName)) {
                            odmVersion = reader.getAttributeValue(null, "ODMVersion");
                        } else if ("Study".equals(localName)) {
                            insideStudy = true;
                            studyOid = reader.getAttributeValue(null, "OID");
                            studyName = reader.getAttributeValue(null, "StudyName");
                        } else if ("MetaDataVersion".equals(localName) && insideStudy) {
                            insideMetaDataVersion = true;
                            metadataVersion = reader.getAttributeValue(null, "OID");
                        } else if ("ItemGroupDef".equals(localName) && insideMetaDataVersion) {
                            String groupOid = reader.getAttributeValue(null, "OID");
                            String groupName = reader.getAttributeValue(null, "Name");
                            String repeating = reader.getAttributeValue(null, "Repeating");

                            String groupInfo = groupOid + " - " + groupName + " (Repeating: " + repeating + ")";
                            formOids.add(groupInfo);
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        localName = reader.getLocalName();
                        if ("Study".equals(localName)) {
                            insideStudy = false;
                        } else if ("MetaDataVersion".equals(localName)) {
                            insideMetaDataVersion = false;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing ODM file: " + e.getMessage());
            e.printStackTrace();
            return new OdmStructure("Error", "Error", "Error", "Error", new ArrayList<>());
        }

        return new OdmStructure(
                odmVersion != null && !odmVersion.isEmpty() ? odmVersion : "Unknown",
                studyOid != null && !studyOid.isEmpty() ? studyOid : "Unknown",
                studyName != null && !studyName.isEmpty() ? studyName : "Unknown",
                metadataVersion != null && !metadataVersion.isEmpty() ? metadataVersion : "Unknown",
                formOids);
    }
}