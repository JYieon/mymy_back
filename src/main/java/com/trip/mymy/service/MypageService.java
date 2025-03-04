package com.trip.mymy.service;

import com.trip.mymy.dto.MypageDTO;

public interface MypageService {

	public String modify(MypageDTO dto);
	public MypageDTO getMember(String id);
	boolean isUpdateMember(MypageDTO member);
	


}
