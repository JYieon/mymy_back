package com.trip.mymy.mybatis;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.ProfileDTO;

public interface ProfileMapper {

   void updateProfileImage(@Param("memberId") String memberId, @Param("imageUrl") String imageUrl);

   ProfileDTO getProfileById(@Param("memberId") String memberId);

}
