package com.trip.mymy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.AlarmSettingsDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.AlarmService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AlarmController {
	
	 @Autowired 
	 private AlarmService alarmService;
	 @Autowired TokenProvider tp;
	
	 @Autowired
	 private SimpMessagingTemplate messagingTemplate; // WebSocket 사용
	
	 /**
     * 특정 사용자의 알람 설정 정보를 조회하는 API
     * @param memberId 사용자의 ID
     * @return 사용자의 알람 설정 정보를 ResponseEntity 형태로 반환
     */
	@GetMapping("/alarm/settings")
	public ResponseEntity<AlarmSettingsDTO> getAlarmSettings(@RequestHeader("Authorization") String token) {
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
	    System.out.println("알람 설정 조회 요청: memberId = " + member.getId()); // 디버깅 로그

	    AlarmSettingsDTO settings = alarmService.getAlarmSettings(member.getId());
	    
	    if (settings == null) {
	        throw new RuntimeException("사용자의 알람 설정을 찾을 수 없습니다.");
	    }
	    
	    return ResponseEntity.ok(settings);
	}
	
	//알림 읽음 처리
	   @PostMapping("/alarm/mark-read")
	   public ResponseEntity<String> markAlarmsAsRead(@RequestHeader("Authorization") String token, @RequestBody Map<String, Integer> requestBody) {
	        System.out.println(" 알림 읽음 처리 API 호출됨!");
	        int no = requestBody.get("no");
	        
	        Authentication authentication = tp.getAuthentication(token);
			MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
//	        String memberId = extractUserIdFromToken(token);
	        if (member.getId() == null || member.getId().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body(" 유효하지 않은 토큰입니다.");
	        }

	        int updatedRows = alarmService.markAlarmsAsRead(no);
	        System.out.println(" 알림 읽음 처리 완료: " + updatedRows + "개의 행 업데이트됨");

	        return ResponseEntity.ok(" " + updatedRows + "개의 알람이 읽음 처리되었습니다.");
	    }
	
	/**
  * 특정 사용자의 알람 목록 조회 API
  * @param userId 알람을 조회할 사용자의 ID
  * @return 해당 사용자의 알람 리스트
  */
	@GetMapping("/alarm/list")
	public ResponseEntity<List<AlarmDTO>> getAlarms(@RequestHeader("Authorization") String token) {
	    System.out.println("받은 토큰: " + token);
	    Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
//	    String userId = extractUserIdFromToken(token);
	    System.out.println("추출된 userId: " + member.getId());
	    
	    if (member.getId() == null || member.getId().trim().isEmpty()) {
	        System.out.println("userId가 NULL이거나 공백입니다! API 요청 중단.");
	        return ResponseEntity.badRequest().build();
	    }

	    List<AlarmDTO> alarms = alarmService.getUserAlarms(member.getId());
	    System.out.println("알람" + alarms);
	    return ResponseEntity.ok(alarms);
	}


	// JWT에서 userId 추출하는 메서드 추가
//	private String extractUserIdFromToken(String token) {
//	    try {
//	        System.out.println("원본 토큰: " + token);  // 디버깅 추가
//
//	        token = token.replace("Bearer ", "").trim(); // "Bearer " 제거 및 공백 제거
//	        System.out.println("변환된 토큰: " + token);  //  디버깅 추가
//
//	        // ✅ SECRET_KEY 확인
//	        String secretKey = "Y29tcGxleCBkYXRhIGZvciBzZWN1cml0eSBhbmQgaGFzaC11c2Uga2V5IGNvbmZpZ3VyZWQgaW4gdGhlIGhlYWRlZCBtZWRpYSBvZiBqb3VybmFsIHJlY29nbml0aW9uLg\r\n"
//	        		+ ""; 
//	        System.out.println("사용 중인 SECRET_KEY: " + secretKey);
//
//	        Claims claims = Jwts.parser()
//	                .setSigningKey(secretKey) // SECRET_KEY 확인 필요!
//	                .parseClaimsJws(token)
//	                .getBody();
//
//	        System.out.println("추출된 userId: " + claims.getSubject()); // 🔥 디버깅 추가
//	        return claims.getSubject(); // userId
//	    } catch (Exception e) {
//	        System.out.println("토큰 파싱 오류: " + e.getMessage());
//	        return null;
//	    }
//	}


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
	@SubscribeMapping("/alarm/sendNotification")
//	@SendTo("/topic/notification")
	public AlarmDTO sendNotification(AlarmDTO a_dto) {
	    alarmService.insertAlarm(a_dto);
	    System.out.println(a_dto.getMemberId());
	    String destination = "/topic/user/" + a_dto.getMemberId() + "/queue/notification";
	    messagingTemplate.convertAndSend(destination, a_dto);
	    System.out.println("알람 보내기!!!!!");
	    return a_dto;
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