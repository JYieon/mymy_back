package com.trip.mymy.service;

import com.trip.mymy.dto.MypageDTO;
import com.trip.mymy.mybatis.MypageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MypageServiceImpl implements MypageService {

    @Autowired
    private MypageMapper mypageMapper;

  


    // 회원 정보 조회
    @Override
    public MypageDTO getMember(String id) {
        System.out.println("회원 정보 조회: " + id);
        MypageDTO member = mypageMapper.getMember(id);  // MyBatis 매퍼 호출
    
        // 🔍 디버깅용 로그 추가
        System.out.println("🔍 DB 조회된 여행자 테스트 결과: " + member.getTestResult());

        if (member.getTestResult() == null || member.getTestResult().isEmpty()) {
            member.setTestResult("미설정");
        }

        return member;  // MyBatis 매퍼 호출
    }

    // 회원 정보 수정
    @Transactional
    @Override
    public boolean isUpdateMember(MypageDTO member) {
        System.out.println("수정 요청 데이터: " + member);
        int result = mypageMapper.isUpdateMember(member);
        return result > 0;
    }
    
    // 여행자 테스트 결과 저장
    public void updateTestResult(String id, String testResult) {
            mypageMapper.updateTestResult(id, testResult);
    }
    
    // 여행자 테스트 결과 조회
    public String getTestResult(String id) {
        return mypageMapper.getTestResult(id);
    }

}