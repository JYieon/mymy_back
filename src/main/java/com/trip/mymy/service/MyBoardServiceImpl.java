package com.trip.mymy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.MyBoardDTO;
import com.trip.mymy.mybatis.MyBoardMapper;

@Service
public class MyBoardServiceImpl implements MyBoardService {
	 @Autowired
	    private MyBoardMapper myBoardMapper;

	    // 내가 쓴 글 목록 조회
	 	@Override
	    public List<MyBoardDTO> getMyPosts(String userId) {
	        if (userId == null || userId.trim().isEmpty()) {
	            throw new IllegalArgumentException("userId가 비어 있습니다!");
	        }
	        return myBoardMapper.findMyPosts(userId);
	    }

	    // 내가 쓴 댓글 목록 조회
	 	@Override
	 	public List<MyBoardDTO> getMyComments(String userId) {
	 	    return myBoardMapper.findMyComments(userId);
	 	}

	}