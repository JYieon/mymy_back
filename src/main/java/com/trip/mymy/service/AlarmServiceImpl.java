package com.trip.mymy.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;
import com.trip.mymy.mybatis.AlarmMapper;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket 사용

    // 특정 사용자의 알람 조회
    @Override
    public List<AlarmDTO> getUserAlarms(String memberId) {
        return alarmMapper.getUserAlarms(memberId);
    }

    // 알람 저장 후 WebSocket을 통해 실시간 전송
    @Override
    public void insertAlarm(AlarmDTO a_dto) {
        alarmMapper.insertAlarm(a_dto);
        try {
            messagingTemplate.convertAndSend("/topic/notifications/" + a_dto.getMemberId(), a_dto);
        } catch (Exception e) {
            System.err.println("WebSocket 메시지 전송 실패: " + e.getMessage());
        }
    }

    // 특정 사용자의 모든 알람 삭제
    @Override
    public void deleteUserAlarms(String memberId) {
        alarmMapper.deleteUserAlarms(memberId);
    }

    //특정 사용자의 알람 설정 조회
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

            alarmMapper.insertDefaultAlarmSettings(settings);
        }

        return settings;
    }

    //특정 사용자의 알람 설정 업데이트
	@Override
	public void updateAlarmSettings(AlarmSettingsDTO settings) {
		alarmMapper.updateAlarmSettings(settings);
		
	}


} 
