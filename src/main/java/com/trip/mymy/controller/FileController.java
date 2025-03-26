//package com.trip.mymy.controller;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus; 
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/upload")
//public class FileController {
//
//    @Autowired
//    private S3Uploader s3Uploader;
//
//    @PostMapping("/summernote")
//    public ResponseEntity<Map<String, String>> uploadSummernoteImage(@RequestParam("file") MultipartFile file) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            String url = s3Uploader.upload(file, "summernote");
//            response.put("url", url); // Summernote는 "url" 키를 요구함
//            return ResponseEntity.ok(response);
//        } catch (IOException e) {
//            response.put("error", "S3 업로드 실패: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//}
