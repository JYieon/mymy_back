package com.trip.mymy.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;

/**
 * 알람 관련 데이터베이스 연동을 위한 MyBatis 매퍼 인터페이스
 */
public interface AlarmMapper {
	
	/**
     * 특정 사용자의 알람 목록 조회
     * @param memberId 사용자 ID
     * @return 사용자의 알람 리스트
     */
	List<AlarmDTO> getUserAlarms(String memberId);
	
	/**
     * 새로운 알람 추가
     * @param a_dto 추가할 알람 데이터
     */
	void insertAlarm(AlarmDTO a_dto);
	
	/**
     * WebSocket 실패 시 알람 상태 업데이트
     * @param alarmNo 알람 ID
     * @param status 업데이트할 상태 값
     */
    void updateAlarmStatus(@Param("alarmNo") int alarmNo, @Param("status") String status);

    /**
     * 특정 사용자의 알람 설정 조회
     * @param memberId 사용자 ID
     * @return 사용자의 알람 설정 정보
     */
	void deleteUserAlarms(String memberId);

	/**
     * 특정 사용자의 알람 설정 조회
     * @param memberId 사용자 ID
     * @return 사용자의 알람 설정 정보
     */
	AlarmSettingsDTO getAlarmSettings(String memberId);

	/**
     * 특정 사용자의 기본 알람 설정 추가 (설정이 없을 경우 기본값 삽입)
     * @param settings 기본 알람 설정 정보
     */
	void insertDefaultAlarmSettings(AlarmSettingsDTO settings);

	/**
     * 특정 사용자의 알람 설정 업데이트
     * @param settings 업데이트할 알람 설정 정보
     */
	void updateAlarmSettings(AlarmSettingsDTO settings);

	/**
     * 특정 사용자의 알람 목록 조회 (API에서 사용)
     * @param userId 사용자 ID
     * @return 사용자의 알람 리스트
     */
	List<AlarmDTO> getAlarms(@Param("userId") String userId);

	/**
     * 특정 사용자의 읽지 않은 알람 개수 조회
     * @param memberId 사용자 ID
     * @return 읽지 않은 알람 개수
     */
	int getUnreadAlarmCount(String memberId);
	
	
}
