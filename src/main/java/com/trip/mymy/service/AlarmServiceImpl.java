package com.trip.mymy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.mybatis.AlarmMapper;

@Service
public class AlarmServiceImpl implements AlarmService{
	private final AlarmMapper alarmMapper;
	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	public AlarmServiceImpl(AlarmMapper alarmMapper, 
							SimpMessagingTemplate messagingTemplate) {
		this.alarmMapper = alarmMapper;
		this.messagingTemplate = messagingTemplate;
	}

	//특정 사용자의 알림 목록 조회
	@Override
	public List<AlarmDTO> getUserAlarms(String memberId) {
		return alarmMapper.getUserAlarms(memberId);
	}

	//알림 저장 후 websocket을 통해 실시간 전송
	@Override
	public void insertAlarm(AlarmDTO alarm) {
		alarmMapper.insertAlarm(alarm);
		//실시간 알람 전송
		messagingTemplate.convertAndSend("/topic/notifications/" 
										+ alarm.getMemberId(), alarm );		
	}

	//알람 삭제(읽음 처리)
	@Override
	public void deleteUserAlarms(String memberId) {
		alarmMapper.deleteUserAlarms(memberId);
		
	}
	

}
