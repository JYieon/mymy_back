package com.trip.mymy.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
	public enum MessageType{
		ENTER, TALK, CLOSE
	}
	private MessageType type;
	private int roomNum;
	private int msgNum;
	private String member;
	private String msg;
	private Date msgDate;
}
