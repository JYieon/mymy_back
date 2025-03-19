package com.trip.mymy.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.FollowerDTO;
import com.trip.mymy.service.FollowService;

import io.jsonwebtoken.MalformedJwtException;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FollowController {

    @Autowired
    private FollowService followService;
    @Autowired TokenProvider tp;

    // ✅ 팔로우 요청 (토큰 기반)
    @PutMapping("/{followingId}")
    public ResponseEntity<?> followUser(@PathVariable String followingId,
                                        @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
        }

        Authentication authentication = tp.getAuthentication(token);
        MemberDTO member = (MemberDTO) authentication.getPrincipal();
        String followerId = member.getId(); // ✅ 토큰에서 followerId 가져오기

        System.out.println("✅ followerId: " + followerId + " -> followingId: " + followingId); // 디버깅

        if (followerId == null || followingId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("🚨 팔로우 요청 데이터가 유효하지 않습니다.");
        }

        try {
            FollowingDTO followingDTO = new FollowingDTO();
            followingDTO.setFollowerId(followerId);
            followingDTO.setFollowingId(followingId);

            followService.followUser(followingDTO);
            return ResponseEntity.ok("팔로우 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("🚨 팔로우 실패: " + e.getMessage());
        }
    }


    // ✅ 언팔로우 요청 (토큰 기반)
 // ✅ 언팔로우 요청 (토큰 기반)
    @DeleteMapping("/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable String followingId,
                                          @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Authentication authentication = tp.getAuthentication(token);
        MemberDTO member = (MemberDTO) authentication.getPrincipal();
        String followerId = member.getId(); // ✅ 토큰에서 followerId 가져오기

        // ✅ 디버깅 로그 추가
        System.out.println("🚀 언팔로우 요청 - followerId: " + followerId + ", followingId: " + followingId);

        if (followerId == null || followingId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("🚨 언팔로우 요청 데이터가 유효하지 않습니다.");
        }

        try {
            FollowingDTO followingDTO = new FollowingDTO();
            followingDTO.setFollowerId(followerId);
            followingDTO.setFollowingId(followingId);

            followService.unfollowUser(followingDTO);
            return ResponseEntity.ok("✅ 언팔로우 성공!");
        } catch (Exception e) {
            System.out.println("🚨 언팔로우 실패 - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    // ✅ 팔로우 여부 확인 (토큰 기반)
    @GetMapping("/isFollowing/{followingId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String followingId,
                                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Authentication authentication = tp.getAuthentication(token);
        MemberDTO member = (MemberDTO) authentication.getPrincipal();
        String followerId = member.getId();

        System.out.println("✅ [팔로우 여부 확인] " + followerId + " -> " + followingId); // 디버깅 로그

        boolean result = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(result);
    }


    // 내가 팔로우하는 목록 조회
    @GetMapping("/following")
    public ResponseEntity<?> getFollowingList(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚨 인증 토큰이 없습니다.");
        }

        try {
            // ✅ "Bearer " 제거 후 토큰 검증
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " 제거
            }

            Authentication authentication = tp.getAuthentication(token);
            MemberDTO member = (MemberDTO) authentication.getPrincipal();

            return ResponseEntity.ok(followService.getFollowingList(member.getId()));
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚨 잘못된 JWT 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("🚨 서버 오류 발생: " + e.getMessage());
        }
    }


    @GetMapping("/followers")
    public ResponseEntity<?> getFollowerList(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚨 인증 토큰이 없습니다.");
        }

        try {
            // ✅ "Bearer " 제거 후 토큰 검증
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Authentication authentication = tp.getAuthentication(token);
            MemberDTO member = (MemberDTO) authentication.getPrincipal();

            return ResponseEntity.ok(followService.getFollowerList(member.getId()));
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚨 잘못된 JWT 형식입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("🚨 서버 오류 발생: " + e.getMessage());
        }
    }



//    @GetMapping("/followers/{token}")
//    public ResponseEntity<List<FollowerDTO>> getFollowerList(@PathVariable String token) {
//    	
//    	Authentication authentication = tp.getAuthentication(token);
//		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
//		
//		
//        return ResponseEntity.ok(followService.getFollowerList(member.getId()));
//    }
}