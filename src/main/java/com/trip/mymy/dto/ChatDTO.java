package com.trip.mymy.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatDTO {
	
	private String member;
	private String roomName;
	private String role;
	private Date enterDate; //입장
	
	@Builder.Default
	private int roomNum = 0;
	
	@Builder.Default
	private Date lastChat = null; //(마지막) 채팅날짜
	
	@Builder.Default
	private Date leaveDate = null; //퇴장
}
