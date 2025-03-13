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
        return mypageMapper.getMember(id);  // MyBatis 매퍼 호출
    }

    // 회원 정보 수정
    @Transactional
    @Override
    public boolean isUpdateMember(MypageDTO member) {
        System.out.println("수정 요청 데이터: " + member);
        int result = mypageMapper.isUpdateMember(member);
        return result > 0;
    }

}