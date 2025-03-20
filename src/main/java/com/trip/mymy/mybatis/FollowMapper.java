package com.trip.mymy.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.dto.FollowerDTO;


public interface FollowMapper {
    void followUser(FollowingDTO followDTO);
//    void unfollowUser(FollowingDTO followDTO);
    List<FollowingDTO> getFollowingList(String followerId);
    List<FollowerDTO> getFollowerList(String followingId);
	Integer isFollowing(@Param("followerId") String followerId, @Param("followingId") String followingId);
	boolean isFollowing(FollowingDTO followingDTO);
//	public void unfollowUser(@Param("followerId") String followerId, @Param("followingId") String followingId);
	int deleteFollow(FollowingDTO followingDTO);
	
}