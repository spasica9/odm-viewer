package com.example.odm_viewer_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

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

            return ResponseEntity.ok("File validated and received successfully. File name: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing file: " + e.getMessage());
        }
    }
}