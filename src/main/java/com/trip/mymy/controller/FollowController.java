package com.trip.mymy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.dto.FollowerDTO;
import com.trip.mymy.service.FollowService;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FollowController {

    @Autowired
    private FollowService followService;

    // 팔로우 추가 (PUT 사용)
    @PutMapping("/{followerId}/{followingId}")
    public ResponseEntity<?> followUser(@PathVariable String followerId, @PathVariable String followingId) {
        try {
            FollowingDTO followingDTO = new FollowingDTO();
            followingDTO.setFollowerId(followerId);
            followingDTO.setFollowingId(followingId);
            
            followService.followUser(followingDTO);
            return ResponseEntity.ok("팔로우 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 언팔로우 (DELETE 사용)
    @DeleteMapping("/{followerId}/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable String followerId, @PathVariable String followingId) {
        try {
            FollowingDTO followingDTO = new FollowingDTO();
            followingDTO.setFollowerId(followerId);
            followingDTO.setFollowingId(followingId);
            
            followService.unfollowUser(followingDTO);
            return ResponseEntity.ok("언팔로우 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 팔로우 여부 확인 (GET 사용)
 // 팔로우 여부 확인
    @GetMapping("/isFollowing/{followerId}/{followingId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String followerId, @PathVariable String followingId) {
        boolean result = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(result);
    }


    // 내가 팔로우하는 목록 조회
    @GetMapping("/following/{followerId}")
    public ResponseEntity<List<FollowingDTO>> getFollowingList(@PathVariable String followerId) {
    	 List<FollowingDTO> list = followService.getFollowingList(followerId);
    	 System.out.println("list" + list);
        return ResponseEntity.ok(followService.getFollowingList(followerId));
    }

    // 나를 팔로우하는 목록 조회 (FollowerDTO 사용)
    @GetMapping("/followers/{followingId}")
    public ResponseEntity<List<FollowerDTO>> getFollowerList(@PathVariable String followingId) {
        return ResponseEntity.ok(followService.getFollowerList(followingId));
    }
}
