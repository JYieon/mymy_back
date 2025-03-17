package com.trip.mymy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.dto.MyBoardDTO;
import com.trip.mymy.service.MyBoardService;

@RestController
@RequestMapping("/myboard")  // ✅ 공통 경로 설정
@CrossOrigin(origins = "http://localhost:3000")
public class MyBoardController {

    @Autowired 
    private MyBoardService myBoardService;

    // 내가 쓴 글 목록 조회
    @GetMapping("/my-posts/{userId}")  // ✅ @PathVariable 사용
    public ResponseEntity<List<MyBoardDTO>> getMyPosts(@PathVariable("userId") String userId) {
        List<MyBoardDTO> myPosts = myBoardService.getMyPosts(userId);
        return ResponseEntity.ok(myPosts);
    }

    
    // 내가 쓴 댓글 조회 API
    @GetMapping("/my-comments/{userId}")
    public ResponseEntity<List<MyBoardDTO>> getMyComments(@PathVariable("userId") String userId) {
        List<MyBoardDTO> myComments = myBoardService.getMyComments(userId);
        return ResponseEntity.ok(myComments);
    }

}
