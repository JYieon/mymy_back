package com.trip.mymy.dto;

public class AlarmSettingsDTO {
	private String memberId;
    private boolean postAlarm;
    private boolean commentAlarm;
    private boolean chatAlarm;
    private boolean followAlarm;
    
    
    
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public boolean isPostAlarm() {
		return postAlarm;
	}
	public void setPostAlarm(boolean postAlarm) {
		this.postAlarm = postAlarm;
	}
	public boolean isCommentAlarm() {
		return commentAlarm;
	}
	public void setCommentAlarm(boolean commentAlarm) {
		this.commentAlarm = commentAlarm;
	}
	public boolean isChatAlarm() {
		return chatAlarm;
	}
	public void setChatAlarm(boolean chatAlarm) {
		this.chatAlarm = chatAlarm;
	}
	public boolean isFollowAlarm() {
		return followAlarm;
	}
	public void setFollowAlarm(boolean followAlarm) {
		this.followAlarm = followAlarm;
	}
	

}
