package com.trip.mymy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.LoginReqDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.TokenDTO;
import com.trip.mymy.service.AuthServiceImpl;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
	
	@Autowired AuthServiceImpl ls;
	@Autowired TokenProvider tp;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> loginCheck(@RequestBody LoginReqDTO loginData) {
		
		MemberDTO member = ls.loginCheck(loginData);
	
		if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 로그인 실패
	    }
	    
	    // 로그인 성공 시 토큰 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
	    return ResponseEntity.ok(tp.generateTokenDto(authentication)); // 로그인 성공, 토큰 반환
	}
	
	@PostMapping("/find_id")
	public ResponseEntity<String> findId(@RequestParam String name, String email) {
		System.out.println(name + email);
		return ResponseEntity.ok(ls.findId(name, email));
	}
	
	@PostMapping("/find_pwd")
	public ResponseEntity<Integer> findPwd(@RequestParam String id, String email) {
		
		return ResponseEntity.ok(ls.findPwd(id, email));
	}
	
	@PostMapping("/find_pwd/auth")
	public ResponseEntity<String> authPwd(@RequestParam String userAuth, String id) {
		if(ls.authPwd(userAuth)) {
			String resetToken = tp.generateResetToken(id);
			return ResponseEntity.ok(resetToken);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증실패");
		}
	}
	
	@PostMapping("/reset_pwd")
	public void resetPwd(@RequestParam String id, String pwd) {
		ls.resetPwd(id, pwd);
	}
	
	@PostMapping("/signup")
	public int signup(@RequestBody MemberDTO signupData){
		//String msg = ls.insertUser(signupData);
		
		return ls.insertUser(signupData);
//		return ResponseEntity.ok(ls.insertUser(signupData));
	}
	
}
