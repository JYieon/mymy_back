package com.trip.mymy.controller;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.LevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LevelController {

    private final LevelService levelService;
    private final TokenProvider tp;

    public LevelController(LevelService levelService, TokenProvider tokenProvider) {
        this.levelService = levelService;
        this.tp = tokenProvider;
    }

    @PostMapping("/level/update")
    public ResponseEntity<String> updateLevel(HttpServletRequest request) {
        //토큰 꺼내기
    	String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 토큰을 통해 사용자 정보 얻기
        Authentication authentication = tp.getAuthentication(token);
        if (authentication == null) {
            return ResponseEntity.status(401).body("인증 실패");
        }

        MemberDTO member = (MemberDTO) authentication.getPrincipal();
        if (member == null) {
            return ResponseEntity.status(500).body("member null");
        }

        try {
        	// 레벨 계산해주는 서비스 호출
            levelService.updateMemberLevel(member.getId());
            return ResponseEntity.ok("레벨 갱신 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("레벨 갱신 중 오류: " + e.getMessage());
        }
    }
}