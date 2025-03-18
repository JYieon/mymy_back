package com.trip.mymy.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public class AlarmDTO {
	private int alarmNo;
	private String senderId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") //LocalDateTime을 JSON으로 변환할 때 포맷이 깨지지 않도록 설정
	private LocalDateTime createdAt;
	private String memberId;
	private int alarmTypeId;
	private int isRead;
	private int addr;
	private String alarmTypeName;
	private String alarmContent;
	

	
	
	public int getAlarmNo() {
		return alarmNo;
	}
	public void setAlarmNo(int alarmNo) {
		this.alarmNo = alarmNo;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getAlarmTypeId() {
		return alarmTypeId;
	}
	public void setAlarmTypeId(int alarmTypeId) {
		this.alarmTypeId = alarmTypeId;
	}
	public String getAlarmTypeName() {
		return alarmTypeName;
	}
	public void setAlarmTypeName(String alarmTypeName) {
		this.alarmTypeName = alarmTypeName;
	}
	public String getAlarmContent() {
		return alarmContent;
	}
	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int inRead) {
		this.isRead = inRead;
	}
	public int getAddr() {
		return addr;
	}
	public void setAddr(int addr) {
		this.addr = addr;
	}

}
