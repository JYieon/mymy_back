package com.trip.mymy.service;

import java.util.List;
import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.dto.FollowerDTO;

public interface FollowService {
    void followUser(String sender, FollowingDTO followDTO);
    boolean isFollowing(String followerId, String followingId); 
    List<FollowingDTO> getFollowingList(String followerId);
    List<FollowerDTO> getFollowerList(String followingId);
//	void unfollowUser(String followerId, String followingId);
	void unfollowUser(FollowingDTO followingDTO);
	
}
