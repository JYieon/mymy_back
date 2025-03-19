package com.trip.mymy.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimelineDTO {
	public int timelineId, boardNo;
	public String id, startDt, endDt, todo, location;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getTimelineId() {
		return timelineId;
	}
	public void setTimelineId(int timelineId) {
		this.timelineId = timelineId;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(Timestamp startDt) {
		SimpleDateFormat fo= new SimpleDateFormat("YYYY-MM-dd");
		this.startDt = fo.format(startDt);
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(Timestamp endDt) {
		SimpleDateFormat fo= new SimpleDateFormat("YYYY-MM-dd");
		this.endDt = fo.format(endDt);
	}
	public String getTodo() {
		return todo;
	}
	public void setTodo(String todo) {
		this.todo = todo;
	}
	
}
