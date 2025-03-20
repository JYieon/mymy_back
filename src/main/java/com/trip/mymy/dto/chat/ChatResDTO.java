package com.trip.mymy.dto.chat;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResDTO {
	private long roomNum;
	private ChatDTO chat;
	private List<ChatMemberDTO> member;
	private List<ChatMessageDTO> messages;
	
}
