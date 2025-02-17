package com.trip.mymy.service;

import java.util.Map;

import com.trip.mymy.dto.LoginReqDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.TokenDTO;

public interface AuthService {
	public MemberDTO loginCheck(LoginReqDTO loginData);
	public int insertUser(MemberDTO data);
}
