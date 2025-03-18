package com.trip.mymy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;
import com.trip.mymy.mybatis.AlarmMapper;


/**
 * 알람 서비스 구현체
 * - 알람 조회, 추가, 삭제, 설정 관리 및 실시간 WebSocket 전송 기능 포함
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket 사용

    /**
     * 특정 사용자의 알람 목록 조회
     * @param memberId 사용자 ID
     * @return 사용자의 알람 목록 리스트
     */
    @Override
    public List<AlarmDTO> getUserAlarms(String memberId) {
        return alarmMapper.getUserAlarms(memberId);
    }

    /**
     * 알람 저장 후 WebSocket을 통해 실시간 전송
     * @param a_dto 추가할 알람 데이터
     */
    @Override
    public void insertAlarm(AlarmDTO a_dto) {
    	//알람을 DB에 저장
        alarmMapper.insertAlarm(a_dto);
        
        //WebSocket을 통해 알람을 실시간 전송
        try {
            messagingTemplate.convertAndSend("/topic/notifications/" + a_dto.getMemberId(), a_dto);
        } catch (Exception e) {
            System.err.println("WebSocket 메시지 전송 실패: " + e.getMessage());
        }
    }

    /**
     * 특정 사용자의 모든 알람 삭제
     * @param memberId 알람을 삭제할 사용자 ID
     */
    @Override
    public void deleteUserAlarms(String memberId) {
        alarmMapper.deleteUserAlarms(memberId);
    }

    /**
     * 특정 사용자의 알람 설정 조회
     * - 설정이 없을 경우 기본값을 설정 후 반환
     * @param memberId 사용자 ID
     * @return 사용자의 알람 설정 정보
     */
    @Override
    public AlarmSettingsDTO getAlarmSettings(String memberId) {
        AlarmSettingsDTO settings = alarmMapper.getAlarmSettings(memberId);
        
        if (settings == null) {
            // 사용자 알람 설정이 없는 경우 기본값을 삽입
            settings = new AlarmSettingsDTO();
            settings.setMemberId(memberId);
            settings.setPostAlarm(false);
            settings.setCommentAlarm(false);
            settings.setChatAlarm(false);
            settings.setFollowAlarm(false);

            // 기본 알람 설정을 DB에 저장
            alarmMapper.insertDefaultAlarmSettings(settings);
        }

        return settings;
    }

    /**
     * 특정 사용자의 알람 설정 업데이트
     * @param settings 업데이트할 알람 설정 정보
     */
	@Override
	public void updateAlarmSettings(AlarmSettingsDTO settings) {
		alarmMapper.updateAlarmSettings(settings);
		
	}

	   /**
     * 특정 사용자의 알람 목록을 조회하고, 필요한 경우 알람 내용을 자동 설정
     * @param userId 알람을 조회할 사용자 ID
     * @return 해당 사용자의 알람 리스트
     */
	@Override
	public List<AlarmDTO> getAlarms(String userId) {
	    List<AlarmDTO> alarms = alarmMapper.getAlarms(userId);

	    // 알람 내용이 없으면 알람 타입에 따라 자동 설정
	    for (AlarmDTO alarm : alarms) {
	        if (alarm.getAlarmContent() == null || alarm.getAlarmContent().isEmpty()) {
	            switch (alarm.getAlarmTypeId()) {
	                case 1: 
	                    alarm.setAlarmContent("내 게시글에 댓글이 달렸습니다.");
	                    break;
	                case 2: 
	                    alarm.setAlarmContent("내가 팔로우한 사용자의 새 게시물이 게시되었습니다.");
	                    break;
	                case 3: 
	                    alarm.setAlarmContent("새로운 채팅이 도착했습니다.");
	                    break;
	                case 4: 
	                    alarm.setAlarmContent("새로운 팔로우 요청이 도착했습니다.");
	                    break;
	                default:
	                    alarm.setAlarmContent("새로운 알림이 있습니다.");
	                    break;
	            }
	        }
	    }

	    return alarms;
	}
	
	/**
     * 특정 사용자의 읽지 않은 알람 개수를 조회
     * @param memberId 사용자 ID
     * @return 읽지 않은 알람 개수
     */
	public List<AlarmDTO> getUnreadAlarms(int memberId){
		 return alarmMapper.getUnreadAlarms(memberId);
		
	}
	
	@Override
    public int markAlarmsAsRead(String memberId) {
        try {
            System.out.println("🚀 알림 읽음 처리 시작: userId = " + memberId);
            int updatedRows = alarmMapper.markAlarmsAsRead(memberId);
            System.out.println("✅ 알림 읽음 처리 완료: " + updatedRows + "개의 행 업데이트됨");
            return updatedRows;
        } catch (Exception e) {
            System.out.println("🚨 SQL 실행 오류: " + e.getMessage());
            return 0;
        }
    }
	
	
	




} 
