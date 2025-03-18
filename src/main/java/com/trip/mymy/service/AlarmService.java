package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;

public interface AlarmService {
	// 특정 사용자의 알람 목록 조회
	List<AlarmDTO> getUserAlarms(String memberId);
	
	// 알림 추가 및 실시간 전송
	void insertAlarm(AlarmDTO a_dto);
	
	// 특정 사용자의 모든 알림 삭제
	void deleteUserAlarms(String memberId);

	// 특정 사용자의 알람 설정 조회
	AlarmSettingsDTO getAlarmSettings(String memberId);

	//특정 사용자의 알람 설정 업데이트
	void updateAlarmSettings(AlarmSettingsDTO settings);
}
