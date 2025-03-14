package com.trip.mymy.service;

import com.trip.mymy.dto.MypageDTO;

public interface MypageService {

	public MypageDTO getMember(String id);
	boolean isUpdateMember(MypageDTO mdto);
	


}