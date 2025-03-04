package com.trip.mymy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.TimelineDTO;
import com.trip.mymy.mybatis.TimelineMapper;

@Service
@Transactional
public class TimelineServiceImpl implements TimelineService{
	@Autowired TimelineMapper tMapper;
	@Override
	public List<TimelineDTO> TimelineList(int boardNo){
		return tMapper.TimelineList(boardNo);
	}
	
	public void addTimeline(TimelineDTO timeline) {
		tMapper.addTimeline(timeline);
    }
	
}
