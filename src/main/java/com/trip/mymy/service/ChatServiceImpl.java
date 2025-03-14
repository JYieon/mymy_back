package com.trip.mymy.service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.chat.ChatDTO;
import com.trip.mymy.dto.chat.ChatMemberDTO;
import com.trip.mymy.dto.chat.ChatMessageDTO;
import com.trip.mymy.dto.chat.ChatResDTO;
import com.trip.mymy.mybatis.ChatMapper;

@Service
public class ChatServiceImpl implements ChatService{
	@Autowired ChatMapper cm;
	@Autowired SimpMessagingTemplate messagingTemplate;
	
	
	public List<ChatDTO> findChatList(String member) {
		System.out.println("find: "+member);
		List<ChatDTO> chatList = cm.findChatList(member);
		
		return chatList;
	}
	
	public ChatResDTO joinRoom(long roomNum) {
		ChatDTO chat = cm.findChatRoom(roomNum);
		List<ChatMemberDTO> member = cm.findRoomMember(roomNum);
		if (member == null) {
	        member = new ArrayList<>();  // 빈 리스트로 초기화
	    }
		List<ChatMessageDTO> messages = cm.getRoomMessages(roomNum);
		if (messages == null) {
	        messages = new ArrayList<>();  // 빈 리스트로 초기화
	    }
		
		ChatResDTO res = new ChatResDTO();
		res.setChat(chat);
		res.setMember(member);
		res.setMessages(messages);
		return res; 
	}
	
	//방 개설
	public int createRoom(String member, String roomName) {
		ChatDTO chatRoom = ChatDTO.builder()
				.member(member)
				.roomName(roomName)
				.build();
		System.out.println(chatRoom.getMember());
	    int result = cm.createRoom(chatRoom);
		System.out.println(chatRoom.getRoomNum());
		ChatMemberDTO chatMember = ChatMemberDTO.builder()
				.roomNum(chatRoom.getRoomNum())
				.member(member)
				.role("방장")
				.build();
		 cm.insertChatMember(chatMember);
		 
		 return result;
	}
	
	//방 초대하기
	public int inviteMember(String member, Long roomNum) {
		ChatMemberDTO chatMember = ChatMemberDTO.builder()
				.roomNum(roomNum)
				.member(member)
				.role("멤버")
				.build();
		 int result = cm.insertChatMember(chatMember);
		 enterLeaveMsg(member, roomNum, "enter");
		 return result;
	}
	
	public int removeRoom(Long roomNum, String member) {
		String memberRole = cm.getUserChatRole(roomNum, member);
		System.out.println(memberRole);
		int result = 0;
		
		if(memberRole.equals("방장")) {
			cm.removeRoomMember(roomNum);
			cm.deleteMessage(roomNum);
			result = cm.removeRoom(roomNum);	
		}else {
			System.out.println("멤버 나가기");
			System.out.println(roomNum);
			result = cm.deleteMember(roomNum, member);
			enterLeaveMsg(member, roomNum, "leave");
		}
		System.out.println("service" + result);
		return result;
	}
	
	public void enterLeaveMsg(String member, Long roomNum, String type) {
		// 입장 메시지 생성 및 DB에 저장
		ChatMessageDTO enterMsg = new ChatMessageDTO();
		enterMsg.setMember(member);
		enterMsg.setRoomNum(roomNum);
		if(type.equals("enter")) {
			enterMsg.setMsg(member + "님이 입장하셨습니다.");	
			enterMsg.setType(ChatMessageDTO.MessageType.ENTER);
		}else if(type.equals("leave")) {
			enterMsg.setMsg(member + "님이 퇴장하셨습니다.");	
			enterMsg.setType(ChatMessageDTO.MessageType.LEAVE);
		}
		 // 현재 시간을 밀리초까지 포함한 포맷으로 설정
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//	    String formattedDate = LocalDateTime.now().format(formatter);
//	    enterMsg.setMsgDate(formattedDate);

		// DB에 메시지 저장
		cm.saveMsg(enterMsg);

		// WebSocket을 통해 입장 메시지 전송
		messagingTemplate.convertAndSend("/chat/chatRoomNo/" + roomNum + "/message", enterMsg);
		
		List<ChatMemberDTO> chatMember = cm.findRoomMember(roomNum);
		messagingTemplate.convertAndSend("/chat/chatRoomNo/" + roomNum + "/enternleave", chatMember);
	}
	
	public List<ChatMessageDTO> getMessages(long roomNum){
		return cm.getRoomMessages(roomNum);
	}
	
	@Transactional
	public void saveMessage(ChatMessageDTO chatMsg) {
		cm.saveMsg(chatMsg);
	}
	
	
}
