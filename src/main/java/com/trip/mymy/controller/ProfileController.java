package com.trip.mymy.controller;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.ProfileDTO;
import com.trip.mymy.service.ProfileService;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProfileController {

    @Autowired
    private S3Uploader s3Uploader;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TokenProvider tp;

    // 프로필 이미지 업로드 (토큰 인증)
    @PostMapping("/profile/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                     @RequestHeader("Authorization") String token) {
        try {
            Authentication authentication = tp.getAuthentication(token);
            MemberDTO member = (MemberDTO) authentication.getPrincipal();
            String id = member.getId();

            String imageUrl = s3Uploader.upload(file, "profile");
            profileService.updateProfileImage(id, imageUrl);
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
    }

 // 내 프로필 조회
    @GetMapping("/profile/me")
    public ResponseEntity<ProfileDTO> getMyProfile(@RequestHeader("Authorization") String token) {
        Authentication authentication = tp.getAuthentication(token);
        MemberDTO member = (MemberDTO) authentication.getPrincipal();
        String id = member.getId();

        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    // 다른 유저 프로필 조회
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileDTO> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.getProfileById(userId));
    }
}
