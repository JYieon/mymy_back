package com.trip.mymy.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.chat.ChatDTO;
import com.trip.mymy.dto.chat.ChatMemberDTO;
import com.trip.mymy.dto.chat.ChatMessageDTO;

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
