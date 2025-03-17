package com.trip.mymy.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.trip.mymy.dto.chat.ChatMessageDTO;
import com.trip.mymy.service.ChatService;

@Controller
public class StompController {
	
	@Autowired private ChatService cs;
	private final Logger logger = LoggerFactory.getLogger(StompController.class);
	
	@MessageMapping("/sendMessage/chatRoomNo/{chatRoomNum}")
	@SendTo("/topic/chatRoomNo/{chatRoomNum}/message")
	public ChatMessageDTO insertChatMessage(ChatMessageDTO chatMessage) {
		
		System.out.println(chatMessage);
		chatMessage.setType(ChatMessageDTO.MessageType.TALK);
		cs.saveMessage(chatMessage);
		
		return chatMessage;
	}
	
	//알람
	

}
