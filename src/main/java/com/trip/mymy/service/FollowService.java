package com.trip.mymy.service;

import java.util.List;
import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.dto.FollowerDTO;

public interface FollowService {
    void followUser(FollowingDTO followDTO);
    void unfollowUser(FollowingDTO followDTO);
    boolean isFollowing(String followerId, String followingId); 
    List<FollowingDTO> getFollowingList(String followerId);
    List<FollowerDTO> getFollowerList(String followingId);
}
