package com.trip.mymy.dto.chat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
	public enum MessageType{
		ENTER, TALK, LEAVE
	}
	private Long roomNum;
	private int msgNum;
	private String member;
	private String msg;
	private String msgDate;
	private MessageType type;
	
	@Override
	public String toString() {
		return "MessageDTO{" +
				"roomNum=" + roomNum +
				"msgNum=" + msgNum +
				"member=" + member +
				"msg=" + msg +
				"msgDate" + msgDate +
				"type" + type + "}";
	}
}
