package com.trip.mymy.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.FollowingDTO;
import com.trip.mymy.controller.AlarmController;
import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.FollowerDTO;
import com.trip.mymy.mybatis.FollowMapper;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    
    @Override
    public void followUser(FollowingDTO followingDTO) {
        followMapper.followUser(followingDTO);
    }


//    @Transactional
//    public void unfollowUser(String followerId, String followingId) {
//        followMapper.unfollowUser(followerId, followingId); // ✅ 직접 String 값 전달
//    }


    @Override
    public boolean isFollowing(String followerId, String followingId) {
        Integer result = followMapper.isFollowing(followerId, followingId);
        return result != null && result > 0;  // ✅ 1이면 true, 0이면 false 반환
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

    @Override
    public void unfollowUser(FollowingDTO followingDTO) {
        try {
            System.out.println("🚀 언팔로우 요청: " + followingDTO.getFollowerId() + " → " + followingDTO.getFollowingId());

            // MyBatis Mapper를 호출하여 언팔로우 실행
            int deletedRows = followMapper.deleteFollow(followingDTO);

            if (deletedRows > 0) {
                System.out.println("✅ 언팔로우 성공: " + followingDTO.getFollowerId() + " → " + followingDTO.getFollowingId());
            } else {
                System.out.println("⚠️ 언팔로우 실패: 해당 팔로우 관계가 존재하지 않음.");
            }
        } catch (Exception e) {
            System.out.println("❌ 언팔로우 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
