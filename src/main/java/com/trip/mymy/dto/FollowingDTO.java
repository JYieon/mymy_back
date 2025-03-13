package com.trip.mymy.dto;
import java.time.LocalDateTime;

public class FollowingDTO {
    private int followId;  
    private String followerId;  
    private String followingId;
    private String createdAt;
    
	public int getFollowId() {
		return followId;
	}
	public void setFollowId(int followId) {
		this.followId = followId;
	}
	public String getFollowerId() {
		return followerId;
	}
	public void setFollowerId(String followerId) {
		this.followerId = followerId;
	}
	public String getFollowingId() {
		return followingId;
	}
	public void setFollowingId(String followingId) {
		this.followingId = followingId;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	} 

    
}