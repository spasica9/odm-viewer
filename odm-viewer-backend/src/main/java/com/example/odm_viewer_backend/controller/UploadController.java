package com.example.odm_viewer_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.odm.generated.ODM;
import com.example.odm.generated.Study;

import com.example.odm_viewer_backend.service.OdmParser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private OdmParser odmParserService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

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
            long fileSize = file.getSize();
            System.out.println("Upload successful:");
            System.out.println("File name: " + fileName);
            System.out.println("Size: " + fileSize + " bytes");
            System.out.println("Content type: " + contentType);

            ODM odmRoot = odmParserService.parseOdmFile(file);

            List<Study> studies = odmRoot.getStudies();

            if (!studies.isEmpty()) {
                Study firstStudy = studies.get(0);
                System.out.println("ODM parsing completed - Study: " + firstStudy.getStudyName());
            } else {
                System.out.println("ODM parsing completed - No Study element found at the root level.");
            }

            return ResponseEntity.ok(odmRoot);
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