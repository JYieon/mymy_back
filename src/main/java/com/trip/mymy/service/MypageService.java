package com.trip.mymy.service;

import com.trip.mymy.dto.MypageDTO;

public interface MypageService {

	public MypageDTO getMember(String id);
	boolean isUpdateMember(MypageDTO mdto);
	
	public void updateTestResult(String id, String testResult);
	public String getTestResult(String id);
	public void deleteUser(MypageDTO dto, boolean keepPosts);
	


}