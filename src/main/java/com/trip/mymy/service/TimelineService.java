package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.TimelineDTO;

public interface TimelineService {
	public List<TimelineDTO> TimelineList(int boardNo);
	public void addTimeline(TimelineDTO timeline);
	
}
