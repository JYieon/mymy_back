package com.trip.mymy.mybatis;

import java.util.List;

import com.trip.mymy.dto.ChatDTO;
import com.trip.mymy.dto.ChatMemberDTO;
import com.trip.mymy.dto.ChatMessageDTO;

public interface ChatMapper {
	public int createRoom(ChatDTO chatRoom);
	public int insertChatMember(ChatMemberDTO chatMember);
	public List<ChatDTO> findChatList(String member);
	public int inviteMember(String member, Long roomNum);
	public int removeRoom(Long roomNum);
	public int removeRoomMember(Long roomNum);
	public int deleteMember(Long roomNum, String member);
	public int saveMsg(ChatMessageDTO msg);
	public List<ChatMessageDTO> getRoomMessages(long roomNum);
	public List<ChatMemberDTO> findRoomMember(long roomNum);
	public ChatDTO findChatRoom(long roomNum);
}
