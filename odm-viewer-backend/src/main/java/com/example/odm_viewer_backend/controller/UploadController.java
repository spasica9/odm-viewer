package com.example.odm_viewer_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.odm.generated.ODM;
import com.example.odm.generated.Study;
import com.example.odm.generated.ClinicalData;
import com.example.odm.generated.MetaDataVersion;

import com.example.odm_viewer_backend.service.OdmParser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private OdmParser odmParserService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "showStructure", defaultValue = "true") boolean showStructure,
            @RequestParam(value = "showClinical", defaultValue = "true") boolean showClinical) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (fileName == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File name is missing.");
        }

        boolean isXml = fileName.toLowerCase().endsWith(".xml");
        boolean isContentTypeXml = contentType != null &&
                (contentType.equals("text/xml") ||
                        contentType.equals("application/xml"));

        if (!isXml && !isContentTypeXml) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Unsupported file type. Please upload a .xml file.");
        }

        try {
            ODM odmRoot = odmParserService.parseOdmFile(file);
            Map<String, Object> filteredData = new HashMap<>();
            filteredData.put("FileOID", odmRoot.getFileOID());
            filteredData.put("ODMVersion", odmRoot.getODMVersion());
            filteredData.put("CreationDateTime", odmRoot.getCreationDateTime());
            filteredData.put("FileType", odmRoot.getFileType());

            if (showStructure) {
                List<Study> studies = odmRoot.getStudies();

                if (studies != null && !studies.isEmpty()) {
                    List<Map<String, Object>> studyDataList = studies.stream().map(study -> {
                        Map<String, Object> studyMap = new HashMap<>();

                        studyMap.put("OID", study.getOID());
                        studyMap.put("StudyName", study.getStudyName());
                        studyMap.put("ProtocolName", study.getProtocolName());
                        studyMap.put("VersionID", study.getVersionID());
                        studyMap.put("VersionName", study.getVersionName());
                        studyMap.put("Status", study.getStatus());

                        List<MetaDataVersion> metaDataVersions = study.getMetaDataVersions();
                        if (metaDataVersions != null && !metaDataVersions.isEmpty()) {

                            studyMap.put("MetaDataVersions", metaDataVersions);
                        }

                        return studyMap;
                    }).collect(Collectors.toList());

                    filteredData.put("Study", studyDataList);
                }
            }

            if (showClinical) {
                List<ClinicalData> clinicalDataList = odmRoot.getClinicalDatas();

                if (clinicalDataList != null && !clinicalDataList.isEmpty()) {
                    filteredData.put("ClinicalData", clinicalDataList);
                }
            }

            return ResponseEntity.ok(filteredData);

        } catch (JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("XML Parsing Error (JAXB): The file is not a valid ODM structure or is malformed: "
                            + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("I/O Error: Could not read the uploaded file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}