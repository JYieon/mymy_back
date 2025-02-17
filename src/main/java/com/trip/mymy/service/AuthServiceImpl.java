package com.trip.mymy.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public MemberDTO loginCheck(LoginReqDTO loginData) {
		MemberDTO dto = mapper.getUser(loginData.getId());
		return dto;
	}
	
	public int insertUser(MemberDTO data) {
		
		
		int result = mapper.insertUser(data);
		System.out.println(result);
		
		return result;
	}

}
