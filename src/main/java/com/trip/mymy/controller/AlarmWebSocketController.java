//package com.trip.mymy.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import com.trip.mymy.common.jwt.TokenProvider;
//import com.trip.mymy.dto.AlarmDTO;
//import com.trip.mymy.dto.MemberDTO;
//
//
///**
// * WebSocket을 통해 실시간 알람을 전송하는 컨트롤러
// */
//@Controller
//public class AlarmWebSocketController {
//	@Autowired
//    private TokenProvider tokenProvider;// JWT 토큰을 검증하는 객체
//
//
//	 /**
//     * WebSocket을 통해 알람을 전송하는 메서드
//     * 
//     * @param token 클라이언트에서 전송한 Authorization 헤더 (Bearer Token)
//     * @param alarm 클라이언트에서 전송한 알람 데이터
//     * @return 알람 데이터 반환 (프론트엔드에서 사용)
//     */
//	@MessageMapping("/sendAlarm")  // 클라이언트가 "/app/sendAlarm"으로 메시지를 보낼 때 호출됨
//    @SendTo("/queue/alarms/{userId}")  // 특정 유저의 구독 큐로 알람 전송
//    public AlarmDTO sendAlarm(@Header("Authorization") String token, AlarmDTO alarm) {
//		// 토큰이 없거나 "Bearer "로 시작하지 않으면 예외 발생
//		if (token == null || !token.startsWith("Bearer ")) {
//            throw new RuntimeException("🚨 유효하지 않은 토큰!");
//        }
//
//		 // "Bearer " 문자열 제거 후 실제 토큰만 추출
//        token = token.substring(7);
//        
//        //토큰을 검증하고 사용자 인증 정보를 가져옴
//        Authentication authentication = tokenProvider.getAuthentication(token);
//        MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
//
//        // 서버 로그에 알림 전송 정보를 출력 (디버깅용)
//        System.out.println("📢 WebSocket 알림 전송: " + alarm.getAlarmContent() + " (받는 유저: " + member.getId() + ")");
//        // 알람 객체 반환 (프론트엔드에서 사용 가능)
//        return alarm;
//	    }
//	}
