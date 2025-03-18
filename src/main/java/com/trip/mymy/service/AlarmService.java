package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;

/**
 * 알람 서비스 인터페이스
 * - 알람 조회, 추가, 삭제, 설정 관리 기능 제공
 */
public interface AlarmService {
	
	/**
     * 특정 사용자의 알람 설정을 조회하는 메서드
     * @param memberId 사용자 ID
     * @return 사용자의 알람 설정 정보
     */
	AlarmSettingsDTO getAlarmSettings(String memberId);
	
	//특정 알림만 읽음 처리
	int markAlarmsAsRead(String memberId);
	
	/**
     * 특정 사용자의 알람 목록을 조회하는 API
     * @param userId 알람을 조회할 사용자 ID
     * @return 해당 사용자의 알람 리스트
     */
	List<AlarmDTO> getAlarms(String userId);
	
	
	/**
     * 특정 사용자의 알람 설정을 업데이트하는 메서드
     * @param settings 업데이트할 알람 설정 정보
     */
	void updateAlarmSettings(AlarmSettingsDTO settings);
	
	
	/**
     * 새로운 알람을 추가하고 실시간 전송하는 메서드
     * @param a_dto 추가할 알람 데이터 (알림 메시지, 수신자 ID 등 포함)
     */
	void insertAlarm(AlarmDTO a_dto);
	
	 /**
     * 특정 사용자의 모든 알람을 삭제하는 메서드
     * @param memberId 알람을 삭제할 사용자 ID
     */
	void deleteUserAlarms(String memberId);
	
	
	
	/**
     * 특정 사용자의 알람 목록을 조회하는 메서드
     * @param memberId 사용자 ID
     * @return 사용자의 알람 목록
     */
	List<AlarmDTO> getUserAlarms(String memberId);
	
	

	//읽지 않은 알림 가져오기
	List<AlarmDTO> getUnreadAlarms(int memberId);

	
}
