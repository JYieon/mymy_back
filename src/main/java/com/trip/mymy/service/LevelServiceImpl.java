package com.trip.mymy.service;

import com.trip.mymy.mybatis.LevelMapper;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl implements LevelService {

    private final LevelMapper levelMapper;

    public LevelServiceImpl(LevelMapper levelMapper) {
        this.levelMapper = levelMapper;
    }

    @Override
    public void updateMemberLevel(String id) {
    	 // 글 수 (여행 기록 게시글만 계산)
        int travelRecordCount = levelMapper.getTravelRecordPostCount(id); // 게시글 (기록 게시판만)
        // 댓글 수 (모든 댓글 합산)
        int commentCount = levelMapper.getCommentCount(id); // 댓글 수

        // 레벨 계산 조건 (각 기준을 만족해야 다음 단계로 올라감)
        int level = 1;

        if (travelRecordCount >= 20) {
            level = 4;
        } else if (travelRecordCount >= 3 && commentCount >= 8) {
            level = 3;
        } else if (travelRecordCount == 0 && commentCount >= 5) {
            level = 2;
        }

        System.out.println("여행기록 글 수: " + travelRecordCount);
        System.out.println("댓글 수: " + commentCount);
        System.out.println("계산된 레벨: " + level);

        // DB에 사용자 레벨 업데이트
        int result = levelMapper.updateMemberLevel(id, level);
        System.out.println("UPDATE 수행 결과: " + result);
    }
}