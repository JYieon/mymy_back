package com.trip.mymy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.mybatis.AuthMapper;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired AuthMapper mapper;
	
	public MemberDTO loginCheck(Map<String, String> loginData) {
		MemberDTO dto = mapper.getUser(loginData.get("id"));
		
		return dto;
	}
	
	public void insertUser(MemberDTO data) {
		
	}

}
