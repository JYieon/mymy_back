package com.trip.mymy.dto;
import java.time.LocalDateTime;

public class FollowerDTO {
    private int followId;  
    private String followerId;
    private LocalDateTime createdAt;

    public FollowerDTO() {}

    public FollowerDTO(int followId, String followerId, LocalDateTime createdAt) {
        this.followId = followId;
        this.followerId = followerId;
        this.createdAt = createdAt;
    }

    public int getFollowId() { return followId; }
    public void setFollowId(int followId) { this.followId = followId; }

    public String getFollowerId() { return followerId; }
    public void setFollowerId(String followerId) { this.followerId = followerId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}