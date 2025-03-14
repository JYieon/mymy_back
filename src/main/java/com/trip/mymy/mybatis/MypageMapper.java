package com.trip.mymy.mybatis;

import com.trip.mymy.dto.MypageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MypageMapper {
    // 회원 정보 조회
    MypageDTO getMember(@Param("id") String id);

    // 회원 정보 수정
    int isUpdateMember(MypageDTO mdto);
}

