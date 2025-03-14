package com.trip.mymy.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.dto.FollowerDTO;
import com.trip.mymy.mybatis.FollowMapper;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Transactional
    @Override
    public void followUser(FollowingDTO followingDTO) {
        if (!followMapper.isFollowing(followingDTO)) {
            followMapper.followUser(followingDTO);
        } else {
            throw new RuntimeException("이미 팔로우한 사용자입니다.");
        }
    }

    @Transactional
    @Override
    public void unfollowUser(FollowingDTO followingDTO) {
        if (followMapper.isFollowing(followingDTO)) {
            followMapper.unfollowUser(followingDTO);
        } else {
            throw new RuntimeException("팔로우하지 않은 사용자입니다.");
        }
    }

    @Override
    public boolean isFollowing(String followerId, String followingId) {
        return followMapper.isFollowing(followerId, followingId);
    }



    @Override
    public List<FollowingDTO> getFollowingList(String followerId) {
        System.out.println("getFollowingList - followerId: " + followerId);

        // ✅ followerId가 null이면 빈 리스트 반환
        if (followerId == null || followerId.trim().isEmpty()) {
            System.out.println("followerId가 null이거나 비어 있음!");
            return new ArrayList<>(); 
        }
        
        List<FollowingDTO> result = followMapper.getFollowingList(followerId);
        
        System.out.println("getFollowingList 결과: " + result);

        // ✅ MyBatis에서 NULL 반환 방지
        if (result == null) {
            System.out.println("followMapper.getFollowingList()가 null을 반환함!");
            return new ArrayList<>(); // 빈 리스트 반환
        }

        return result;
    }


    @Override
    public List<FollowerDTO> getFollowerList(String followingId) {
        return followMapper.getFollowerList(followingId);
    }
}
