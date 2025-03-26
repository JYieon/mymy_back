package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.MyBoardDTO;

public interface MyBoardService {

	List<MyBoardDTO> getMyPosts(String userId);

	List<MyBoardDTO> getMyComments(String userId);
	
	// 특정 유저의 게시글 목록 조회
	List<MyBoardDTO> getUserPosts(String userId);


}
