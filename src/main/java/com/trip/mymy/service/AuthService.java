package com.trip.mymy.service;

import java.util.Map;

import com.trip.mymy.dto.LoginReqDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.TokenDTO;

public interface AuthService {
	public MemberDTO loginCheck(LoginReqDTO loginData);
	public int insertUser(MemberDTO data);
	public String findId(String name, String email);
	public int findPwd(String id, String email);
	public int sendMail(String toEmail);
	public int resetPwd(String id, String pwd);
	public boolean authPwd(String userAuth);
	public MemberDTO checkId(String id);
}
