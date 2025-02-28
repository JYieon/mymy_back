package com.trip.mymy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mymy.dto.ChatDTO;
import com.trip.mymy.mybatis.ChatMapper;

@Service
public class ChatServiceImpl {
	@Autowired ObjectMapper objectMapper;
	@Autowired ChatMapper cm;
	
	
	public void findChatList(int roomNum) {
		cm.findChatList(roomNum);
	}
	
//	public void findRoom(String roomNum) {
//		cm.findRom();
//	}
	
	//방 개설
	public void createRoom(String member, String roomName) {
		ChatDTO chatRoom = ChatDTO.builder()
				.member(member)
				.roomName(roomName)
				.role("방장")
				.build();
		System.out.println(chatRoom.getMember());
		 cm.createRoom(chatRoom);
		 cm.insertChatMember(chatRoom);
	}
}
