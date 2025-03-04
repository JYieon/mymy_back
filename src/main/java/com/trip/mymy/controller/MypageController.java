package com.trip.mymy.controller;

import com.trip.mymy.dto.MypageDTO;
import com.trip.mymy.service.MypageService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // allowCredentials = "true"는 쿠키나 인증 정보(세션, 토큰 등)를 포함하는 요청을 허용
public class MypageController {

    @Autowired
    private MypageService mypageService;
    
    //로그인 없이 aaa계정 테스트, 합친 뒤 삭제
    @GetMapping("/test/setDummyUser")
    public MypageDTO setDummyUser(HttpSession session) {
        MypageDTO dummyUser = mypageService.getMember("aaa"); // aaa 계정 정보 가져오기
        if (dummyUser != null) {
            session.setAttribute("user", dummyUser);
            return dummyUser;
        }
        return null;
    }
    
    @GetMapping("/userinfo/me")
    public MypageDTO getCurrentUser(HttpSession session) {
        MypageDTO user = (MypageDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요");
        }
        return user;
    } 

    //회원 정보 조회
    @GetMapping("/userinfo")
    public MypageDTO getUserInfo(@RequestParam("id") String id) {
        System.out.println("조회 ID: " + id);
        return mypageService.getMember(id);
    }

    //회원 정보 수정
    @PostMapping("/modify")
    public String modify(@RequestBody MypageDTO dto) {
        System.out.println("수정 요청 데이터: " + dto);
        if (dto.getId() == null || dto.getId().isEmpty()) {
            return "ID가 누락되었습니다.";
        }

        boolean success = mypageService.isUpdateMember(dto);
        return success ? "회원 정보 수정 성공!" : "회원 정보 수정 실패!";
    }
}
