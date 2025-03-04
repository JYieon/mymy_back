package com.trip.mymy.mybatis;

import java.util.List;

import com.trip.mymy.dto.TimelineDTO;

public interface TimelineMapper {
	public List<TimelineDTO> TimelineList(int boardNo);
	public void addTimeline(TimelineDTO timeline);
	
//	public void updateTimeline(TimelineDTO timeline);
//	public void deleteTimeline(TimelineDTO timeline);
	
}
