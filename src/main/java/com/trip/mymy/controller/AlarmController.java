package com.trip.mymy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;
import com.trip.mymy.service.AlarmService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AlarmController {
	
	@Autowired 
	private AlarmService alarmService;
	
	 /**
     * 특정 사용자의 알람 설정 정보를 조회하는 API
     * @param memberId 사용자의 ID
     * @return 사용자의 알람 설정 정보를 ResponseEntity 형태로 반환
     */
	@GetMapping("/alarm/settings/{memberId}")
	public ResponseEntity<AlarmSettingsDTO> getAlarmSettings(@PathVariable String memberId) {
	    System.out.println("알람 설정 조회 요청: memberId = " + memberId); // 디버깅 로그

	    AlarmSettingsDTO settings = alarmService.getAlarmSettings(memberId);
	    
	    if (settings == null) {
	        throw new RuntimeException("사용자의 알람 설정을 찾을 수 없습니다.");
	    }
	    
	    return ResponseEntity.ok(settings);
	}
	/**
     * 사용자의 알람 설정을 업데이트하는 API
     * @param settings 업데이트할 알람 설정 정보 (RequestBody로 받음)
     * @return 성공 메시지 (ResponseEntity)
     */
	@PostMapping("/alarm/settings/update")
	public ResponseEntity<String> updateAlarmSettings(@RequestBody AlarmSettingsDTO settings) {
	    alarmService.updateAlarmSettings(settings);
	    return ResponseEntity.ok("알림 설정이 업데이트되었습니다.");
	}


	

    /**
     * 새로운 알림을 추가하는 API
     * @param a_dto 추가할 알림 데이터 (RequestBody로 받음)
     * @return 성공 메시지 (ResponseEntity)
     */
	@PostMapping("/alarm/add")
	public ResponseEntity<String> insertAlarm(@RequestBody AlarmDTO a_dto) {
	    alarmService.insertAlarm(a_dto);
	    return ResponseEntity.ok("알람이 성공적으로 추가되었습니다.");
	}
	
	 /**
     * 실시간 알림 전송을 위한 WebSocket 엔드포인트
     * @param a_dto 전송할 알림 데이터
     */
	@MessageMapping("/alarm/sendNotification")
	public void sendNotification(AlarmDTO a_dto) {
	    alarmService.insertAlarm(a_dto);
	}

	
	/**
     * 특정 사용자의 모든 알람을 삭제하는 API
     * @param memberId 알람을 삭제할 사용자의 ID
     * @return 성공 메시지 (ResponseEntity)
     */
	@DeleteMapping("/alarm/{memberId}")
	public ResponseEntity<String> deleteUserAlarms(@PathVariable String memberId) {
	    alarmService.deleteUserAlarms(memberId);
	    return ResponseEntity.ok("알람이 성공적으로 삭제되었습니다.");
	}

	
	
}
