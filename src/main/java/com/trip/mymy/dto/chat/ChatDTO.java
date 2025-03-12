package com.trip.mymy.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatDTO {

	@Builder.Default
	private Long roomNum = null;
	
	private String member;
	private String roomName;
	
	@Builder.Default
	private String lastChat = null; //(마지막) 채팅날짜
	
}
