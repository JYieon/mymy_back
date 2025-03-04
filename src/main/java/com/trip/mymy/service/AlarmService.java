package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.AlarmDTO;

public interface AlarmService {

	public List<AlarmDTO> getUserAlarms(String memberId);
	public void insertAlarm(AlarmDTO alarm);
	public void deleteUserAlarms(String memberId);
}
