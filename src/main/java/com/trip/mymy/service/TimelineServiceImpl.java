package com.trip.mymy.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trip.mymy.dto.TimelineDTO;
import com.trip.mymy.mybatis.TimelineMapper;

@Service
@Transactional
public class TimelineServiceImpl implements TimelineService{
	 @Autowired
	    private TimelineMapper timelineMapper;

	    @Override
	    public void insertTimeline(TimelineDTO timeline) {
	        timelineMapper.insertTimeline(timeline);
	    }

	    public TimelineDTO getTimeline(int boardNo) {
	        return timelineMapper.getTimeline(boardNo);
	    }

	    public void deleteTimeline(int timelineId) {
	        timelineMapper.deleteTimeline(timelineId);
	    }
	    
	    public void updateTimelineTodo(int boardNo, String todo, String location, String startDt, String endDt) {
	        timelineMapper.updateTimelineTodo(boardNo, todo, location, startDt, endDt);
	    }
	}