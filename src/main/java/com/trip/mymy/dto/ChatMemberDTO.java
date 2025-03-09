package com.trip.mymy.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMemberDTO {
	
	private String member;
	private String enterDate;
	private String role;
	
	@Builder.Default
	private Long roomNum = null;

	@Builder.Default
	private String leaveDate = null; //퇴장
}
