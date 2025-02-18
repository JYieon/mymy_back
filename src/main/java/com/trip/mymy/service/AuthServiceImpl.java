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
		
		if(dto == null) {
			return 0;
		}else {
			int authMailNum = sendMail(email);
			return authMailNum;
		}
		
	}
	
	public int sendMail(String toEmail) {
		MimeMessage message = mailSender.createMimeMessage();
		double authNum = Math.random() * 9000 + 1000;
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setSubject("Mymy 비밀번호 설정");
			helper.setTo(toEmail);
			helper.setText("비밀번호 설정 인증번호: " + (int)authNum);
			mailSender.send(message);
		}catch(Exception e) {
			System.out.println("이메일 전송 실패");
			e.printStackTrace();
		}
		
		return (int)authNum;
	}
	
	public int insertUser(MemberDTO data) {
		
		int result = mapper.insertUser(data);
		System.out.println(result);
		
		return result;
	}

}
