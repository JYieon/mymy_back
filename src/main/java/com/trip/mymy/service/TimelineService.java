package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.TimelineDTO;

public interface TimelineService {
	public void insertTimeline(TimelineDTO timeline);
	public TimelineDTO getTimeline(int boardNo);
	public void deleteTimeline(int timelineId);
	public void updateTimelineTodo(int boardNo, String updatedTodo);

}
