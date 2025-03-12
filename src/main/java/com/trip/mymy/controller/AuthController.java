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
    
    @Value("${kakao.logoutRedirectUrl}")
    private String logoutRedirectUrl;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginCheck(@RequestBody LoginReqDTO loginData) {
		
		Map<String, String> result = ls.loginCheck(loginData); //�븘�씠�뵒 �쑀�슚�꽦 寃��궗
	    
		if(result.containsKey("status")) {
			// 濡쒓렇�씤 �꽦怨� �떆 �넗�겙 �깮�꽦
			MemberDTO member = ls.checkId(loginData.getId());
			Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), null, member.getAuthorities());
		    return ResponseEntity.ok(tp.generateTokenDto(authentication));
		}else {
			// 濡쒓렇�씤 �떎�뙣 �떆 �떎�뙣 硫붿꽭吏� �쟾�떖
			return ResponseEntity.status(400).body(result);
		}
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
			String resetToken = tp.generateResetToken(id); //鍮꾨�踰덊샇 �옱�꽕�젙�슜 �넗�겙
			return ResponseEntity.ok(resetToken);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("�씤利앹떎�뙣");
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
	        return ResponseEntity.ok().body("�궗�슜 媛��뒫�븳 �븘�씠�뵒�엯�땲�떎."); 
	    } else {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("�씠誘� �궗�슜 以묒씤 �븘�씠�뵒�엯�땲�떎."); 
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
	
	//移댁뭅�삤 濡쒓렇�씤
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
		
		System.out.println("*******移댁뭅�삤 �쑀�� �젙蹂�" + userInfo);
		MemberDTO member = MemberDTO.builder()
			    .nick((String) userInfo.get("nickname"))  // �삎 蹂��솚 �븘�슂
			    .email((String) userInfo.get("email"))    // �삎 蹂��솚 �븘�슂
			    .id(String.valueOf(userInfo.get("id")))   // �닽�옄�씪 �닔�룄 �엳�쑝誘�濡� String 蹂��솚
			    .name("")
			    .phone("")
			    .pwd("")
			    .build();
		
		MemberDTO existingMember = ls.checkEmail(member.getEmail());
		if(existingMember == null) { //湲곗〈 �쉶�썝�씠 �븘�땲�씪硫� �옄�룞 �쉶�썝媛��엯
			int result = ls.insertUser(member);
			if(result == 1) {
				System.out.println("�옄�룞�쉶�썝媛��엯 �셿猷�");
				existingMember = member;
			}else {
				System.out.println("�쉶�썝媛��엯 �떎�뙣");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
		 // 濡쒓렇�씤 �꽦怨� �떆 �넗�겙 �깮�꽦
		Authentication authentication = new UsernamePasswordAuthenticationToken(existingMember.getEmail(), null, existingMember.getAuthorities());
	    return ResponseEntity.ok(tp.generateTokenDto(authentication)); // 濡쒓렇�씤 �꽦怨�, �넗�겙 諛섑솚
	}
	
	@GetMapping("/kakao/logout")
	public ResponseEntity<String> kakaoLogout() {
		String location = "https://kauth.kakao.com/oauth/logout?client_id=" + clientId + "&logout_redirect_uri=" + logoutRedirectUrl;
		
		return ResponseEntity.ok(location);
	}
}
