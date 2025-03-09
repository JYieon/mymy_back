package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.ChatDTO;
import com.trip.mymy.dto.ChatMemberDTO;
import com.trip.mymy.dto.ChatMessageDTO;
import com.trip.mymy.dto.ChatResDTO;

public interface ChatService {
	public List<ChatDTO> findChatList(String member);
	public ChatResDTO joinRoom(long roomNum);
	public int createRoom(String member, String roomName);
	public int inviteMember(String member, Long roomNum);
	public void removeRoom(Long roomNum, String member, String role);
	public void enterLeaveMsg(String member, Long roomNum, String type);
	public List<ChatMessageDTO> getMessages(long roomNum);
	public void saveMessage(ChatMessageDTO chatMsg);
	
}
