package com.trip.mymy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.mymy.dto.ChatMessageDTO;
import com.trip.mymy.service.ChatServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

	@Autowired ObjectMapper objectMapper;
	@Autowired ChatServiceImpl chatService;
    
    @Bean
    public ObjectMapper objectMapper() {
    	return new ObjectMapper();
    }
    
    // 세션과 채팅방을 매핑하는 데 사용 (사용자가 어떤 채팅방에 속해 있는지)
    private final Map<WebSocketSession, String> sessionRoomIdMap = new ConcurrentHashMap<>();
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload(); // 클라이언트가 전송한 메시지
        log.warn("{}", payload);
        // JSON 문자열을 ChatMessageDto 객체로 변환
        ChatMessageDTO chatMessage = objectMapper.readValue(payload, ChatMessageDTO.class);
         int roomNum = chatMessage.getRoomNum(); 

        if (chatMessage.getType() == ChatMessageDTO.MessageType.ENTER) { // 메시지 타입이 ENTER이면
            sessionRoomIdMap.put(session, String.valueOf(chatMessage.getRoomNum())); // 세션과 채팅방 ID를 매핑
            chatService.addSessionAndHandleEnter(roomNum, session, chatMessage); // 채팅방에 입장한 세션 추가
        } else if (chatMessage.getType() == ChatMessageDTO.MessageType.CLOSE) {
            chatService.removeSessionAndHandleExit(roomNum, session, chatMessage);
        } else {
            chatService.sendMessageToAll(roomNum, chatMessage);
        }

    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 세션과 매핑된 채팅방 ID 가져오기
        log.warn("afterConnectionClosed : {}", session);
        int roomNum = sessionRoomIdMap.remove(session);
        if (roomNum != null) {
        	ChatMessageDTO chatMessage = new ChatMessageDTO();
            chatMessage.getType(ChatMessageDTO.MessageType.CLOSE);
            chatService.removeSessionAndHandleExit(roomNum, session, chatMessage);
        }
    }
}