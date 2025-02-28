package com.trip.mymy.mybatis;

import com.trip.mymy.dto.ChatDTO;

public interface ChatMapper {
	public ChatDTO findChatList(int roomnNum);
	public int createRoom(ChatDTO chatRoom);
	public int insertChatMember(ChatDTO chat);
}
