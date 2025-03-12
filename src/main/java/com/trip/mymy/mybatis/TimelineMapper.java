package com.trip.mymy.mybatis;

import com.trip.mymy.dto.TimelineDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TimelineMapper {
    public void insertTimeline(TimelineDTO timeline);
    public TimelineDTO getTimeline(@Param("boardNo") int boardNo);
    public void deleteTimeline(@Param("timelineId") int timelineId);
    public void updateTimelineTodo(@Param("boardNo") int boardNo, @Param("updatedTodo") String updatedTodo);
}
