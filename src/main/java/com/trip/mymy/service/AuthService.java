package com.trip.mymy.service;

import java.util.Map;

import com.trip.mymy.dto.MemberDTO;

public interface AuthService {
	public MemberDTO loginCheck(Map<String, String> loginData);
	public void insertUser(MemberDTO data);
}
