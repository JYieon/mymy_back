package com.trip.mymy.controller;

import com.trip.mymy.dto.MypageDTO;
import com.trip.mymy.service.MypageService;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // allowCredentials = "true"는 쿠키나 인증 정보(세션, 토큰 등)를 포함하는 요청을 허용
public class MypageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);

    @Autowired
    MypageService mypageService;
    
//    //로그인 없이 aaa계정 테스트, 합친 뒤 삭제
//    @GetMapping("/test/setDummyUser")
//    public ResponseEntity<MypageDTO> setDummyUser(HttpSession session) {
//        MypageDTO dummyUser = mypageService.getMember("aaa");
//        if (dummyUser != null) {
//            session.setAttribute("user", dummyUser);
//            return ResponseEntity.ok(dummyUser);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
    
    @GetMapping("/userinfo/me")
    public ResponseEntity<MypageDTO> getCurrentUser(HttpSession session) {
        MypageDTO user = (MypageDTO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(user);
    }

    //회원 정보 조회
    @GetMapping("/{usereId}")
    public ResponseEntity<MypageDTO> getUserInfo(@PathVariable("userId") String userId) {
        logger.info("회원 정보 조회 요청: ID = {}", userId);
        MypageDTO userInfo = mypageService.getMember(userId);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(userInfo);
    }

    //회원 정보 수정
    @PostMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody MypageDTO dto) {
        logger.info("회원 정보 수정 요청: {}", dto);

        if (dto.getId() == null || dto.getId().isEmpty()) {
            return ResponseEntity.badRequest().body("ID가 누락되었습니다.");
        }

        boolean success = mypageService.isUpdateMember(dto);
        return success
            ? ResponseEntity.ok("회원 정보 수정 성공!")
            : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 수정 실패!");
    }
}