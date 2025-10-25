package com.example.odm_viewer_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.odm_viewer_backend.model.OdmStructure;
import com.example.odm_viewer_backend.service.OdmParser;
import org.springframework.beans.factory.annotation.Autowired;

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

            OdmStructure odmStructure = odmParserService.parseOdmFile(file);

            if (odmStructure.getStudy() != null) {
                System.out.println("ODM parsing completed - Study: " + odmStructure.getStudy().getStudyName());
            } else if (odmStructure.getMetaDataVersion() != null) {
                System.out.println(
                        "ODM parsing completed - MetaDataVersion: " + odmStructure.getMetaDataVersion().getName());
            } else {
                System.out.println("ODM parsing completed - No Study or MetaDataVersion found.");
            }

            return ResponseEntity.ok(odmStructure);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }
}