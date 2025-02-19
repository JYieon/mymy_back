package com.trip.mymy.service;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.LoginReqDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.TokenDTO;
import com.trip.mymy.mybatis.AuthMapper;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired AuthMapper mapper;
	@Autowired TokenProvider tp;
	int authNum = 0;
	
	@Autowired JavaMailSender mailSender;
	
	public MemberDTO loginCheck(LoginReqDTO loginData) {
		MemberDTO dto = mapper.getUser(loginData.getId());
		return dto;
	}
	
	public String findId(String name, String email) {
		MemberDTO dto = mapper.findId(name, email);
		return dto.getId();
	}
	
	public int findPwd(String id, String email) {
		MemberDTO dto = mapper.findPwd(id, email);
		makeAuthNum();
		
		String subject = "Mymy 비밀번호 설정";
		String text = "비밀번호 설정 인증번호: " + authNum;
		
		if(dto == null) {
			return 0;
		}else {
			int authMailNum = sendAuthMail(email, subject, text);
			return authMailNum;
		}	
	}
	
	public void makeAuthNum() {
		authNum = (int)(Math.random() * 9000) + 1000;
	}
	
	public void sendMail(String toEmail, String subject, String text) {
		MimeMessage message = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setSubject(subject);
			helper.setTo(toEmail);
			helper.setText(text);
			mailSender.send(message);
		}catch(Exception e) {
			System.out.println("이메일 전송 실패");
			e.printStackTrace();
		}
	}
	
	public int sendAuthMail(String toEmail, String subject, String text) {
		sendMail(toEmail, subject, text);
		
		return authNum;
	}
	
	public boolean authMail(String userAuth) {
		
		return (String.valueOf(authNum).equals(userAuth));
	}
	
	public int resetPwd(String id, String pwd) {
		int result = mapper.resetPwd(id, pwd);
		System.out.println(result);
		return result;
	}
	
	public MemberDTO checkId(String id) {
		
		return mapper.getUser(id);
	}
	
	public int insertUser(MemberDTO data) {
		
		int result = mapper.insertUser(data);
		System.out.println(result);
		
		return result;
	}
	
	public int sendSignupAuth(String toEmail) {
		makeAuthNum();
		String subject = "Mymy 회원가입 인증번호";
		String text = "회원가입 이메일 인증번호 : " + authNum;
		
		sendAuthMail(toEmail, subject, text);
		
		return authNum;
	}
	
	public MemberDTO checkEmail(String email) {
		return mapper.checkEmail(email);
	}

}
