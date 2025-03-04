package com.trip.mymy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.service.AlarmService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // allowCredentials = "true"는 쿠키나 인증 정보(세션, 토큰 등)를 포함하는 요청을 허용
public class AlarmController {
	@Autowired
	private AlarmService alarmService;
	
	//알림 전송 api
	@PostMapping("/send")
	public String sendAlarm(@RequestBody AlarmDTO alarm) {
		alarmService.insertAlarm(alarm);
		return "알림 전송 완료";	
	}
	
	//특정 사용자의 알람을 조회
	@GetMapping("/list/{userId}")
	public List<AlarmDTO> getUserAlarms(@PathVariable String userId){
		return alarmService.getUserAlarms(userId);
	}
	
	@DeleteMapping("/delete/{userId}")
	public String delelteUserAlarms(@PathVariable String userId) {
		alarmService.deleteUserAlarms(userId);
		return "알림 삭제 완료";
	}

}
