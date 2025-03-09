package com.trip.mymy.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.LoginReqDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.token.KakaoUserInfoResDTO;
import com.trip.mymy.dto.token.TokenDTO;
import com.trip.mymy.service.AuthServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@PropertySource("classpath:application.properties")
public class AuthController {
	
	@Autowired AuthServiceImpl ls;
	@Autowired TokenProvider tp;
	
	@Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirectUrl}")
    private String redirectUrl;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> loginCheck(@RequestBody LoginReqDTO loginData) {
		
		MemberDTO member = ls.loginCheck(loginData); //아이디 유효성 검사
	
		if (member == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 로그인 실패
	    }
	    
	    // 로그인 성공 시 토큰 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), null, member.getAuthorities());
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
	
	@PostMapping("/mail_auth")
	public ResponseEntity<String> authMail(@RequestParam String userAuth, String id) {
		if(ls.authMail(userAuth)) {
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
	public ResponseEntity<Integer> signup(@RequestBody MemberDTO signupData){
		//String msg = ls.insertUser(signupData);
		System.out.println(signupData);
		
		return ResponseEntity.ok(ls.insertUser(signupData));
	}
	
	@PostMapping("/id_check")
	public ResponseEntity<?> checkId(@RequestParam String id) {
	    MemberDTO dto = ls.checkId(id);
	    
	    if (dto == null) {
	        return ResponseEntity.ok().body("사용 가능한 아이디입니다."); 
	    } else {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다."); 
	    }
	}
	
	@PostMapping("/signup_email_send")
	public ResponseEntity<Integer> signupEmailAuth(@RequestParam String toEmail) {
		MemberDTO mailDto = ls.checkEmail(toEmail);
		
		if(mailDto == null) {
			return ResponseEntity.ok(ls.sendSignupAuth(toEmail));
		}else {
			return ResponseEntity.ok(0);
		}	
	}
	
	//카카오 로그인
	@GetMapping("/kakao")
	public ResponseEntity<String> kakaoLogin(){
		String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUrl;
		
		return ResponseEntity.ok(location);
	}
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<TokenDTO> kakaoLoginCallback(@RequestParam String code) throws Exception{
		System.out.println("callback");
		String accessToken = tp.getKakaoAccessToken(code);
		HashMap<String, Object> userInfo = tp.getUserKakaoInfo(accessToken);
		
		System.out.println("*******카카오 유저 정보" + userInfo);
		MemberDTO member = MemberDTO.builder()
			    .nick((String) userInfo.get("nickname"))  // 형 변환 필요
			    .email((String) userInfo.get("email"))    // 형 변환 필요
			    .id(String.valueOf(userInfo.get("id")))   // 숫자일 수도 있으므로 String 변환
			    .name("")
			    .phone("")
			    .pwd("")
			    .build();
		
		MemberDTO checkMember = ls.checkEmail(member.getEmail());
		if(checkMember == null) { //기존 회원이 아니라면 자동 회원가입
			int result = ls.insertUser(member);
			if(result == 1) {
				System.out.println("자동회원가입 완료");
			}else {
				System.out.println("기존 회원 로그인입니다.");
			}
		}
		
		 // 로그인 성공 시 토큰 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), null, member.getAuthorities());
	    return ResponseEntity.ok(tp.generateTokenDto(authentication)); // 로그인 성공, 토큰 반환
	}
}
