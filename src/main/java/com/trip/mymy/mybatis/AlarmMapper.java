package com.trip.mymy.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;

//@Mapper
public interface AlarmMapper {
	
	// 특정 사용자의 알람 목록 조회
	List<AlarmDTO> getUserAlarms(String memberId);
	
	// 알림 추가
	void insertAlarm(AlarmDTO a_dto);
	
	// WebSocket 실패 시 알람 상태 업데이트
    void updateAlarmStatus(@Param("alarmNo") int alarmNo, @Param("status") String status);

	// 특정 사용자의 모든 알림 삭제
	void deleteUserAlarms(String memberId);

	//특정 사용자의 알람 설정 조회
	AlarmSettingsDTO getAlarmSettings(String memberId);

	//특정 사용자의 기본 알람 설정 추가
	void insertDefaultAlarmSettings(AlarmSettingsDTO settings);

	//특정 사용자의 알람 설정 업데이트
	void updateAlarmSettings(AlarmSettingsDTO settings);
	
	
}
