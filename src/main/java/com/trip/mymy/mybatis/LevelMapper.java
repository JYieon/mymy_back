package com.trip.mymy.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LevelMapper {
    int getTravelRecordPostCount(String id);
    int getCommentCount(String id);
    int updateMemberLevel(@Param("id") String id, @Param("level") int level);
}