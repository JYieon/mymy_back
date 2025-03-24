package com.trip.mymy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.MyBoardDTO;
import com.trip.mymy.service.MyBoardService;

@RestController
@RequestMapping("/myboard")  // ✅ 공통 경로 설정
@CrossOrigin(origins = "http://localhost:3000")
public class MyBoardController {

    @Autowired 
    private MyBoardService myBoardService;
    @Autowired TokenProvider tp;

    // 내가 쓴 글 목록 조회
    @GetMapping("/my-posts")  // ✅ @PathVariable 사용
    public ResponseEntity<List<MyBoardDTO>> getMyPosts(@RequestHeader("Authorization") String token) {
    	Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
        List<MyBoardDTO> myPosts = myBoardService.getMyPosts(member.getId());
        return ResponseEntity.ok(myPosts);
    }

    
    // 내가 쓴 댓글 조회 API
    @GetMapping("/my-comments")
    public ResponseEntity<List<MyBoardDTO>> getMyComments(@RequestHeader("Authorization") String token) {
    	Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
        List<MyBoardDTO> myComments = myBoardService.getMyComments(member.getId());
        return ResponseEntity.ok(myComments);
    }
    
    // 특정 유저의 게시글 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<MyBoardDTO>> getUserPosts(@PathVariable String userId) {
        List<MyBoardDTO> posts = myBoardService.getUserPosts(userId);
        return ResponseEntity.ok(posts);
    }


}
