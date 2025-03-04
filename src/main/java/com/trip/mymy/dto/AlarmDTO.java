package com.trip.mymy.dto;

import java.time.LocalDateTime;

public class AlarmDTO {
	private String alarmId;
	private String senderId;
	private String memberId;
	private String alarmTypeId;
	private String alarmTypeName;
	private String alarmContent;
	private LocalDateTime createAt;
	
	
	public String getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getAlarmTypeId() {
		return alarmTypeId;
	}
	public void setAlarmTypeId(String alarmTypeId) {
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
	public LocalDateTime getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}

}
