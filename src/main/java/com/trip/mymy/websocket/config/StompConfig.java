package com.trip.mymy.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*
 * Stomp
 * - MessageBroker방식 처리
 * - publih 발행 / subscribe 구독 패턴
 *   - 특정 url을 '구독'하는 사용자들에게 메세지를 '발행'해줌
 *   
 */

@Configuration
@EnableWebSocketMessageBroker  // 메시지를 대신 전달해주는 중개인
public class StompConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
		.addEndpoint("/ws")
		.setAllowedOrigins("*")  // 프록시 설정을 위해서 설정
		.withSockJS();
		
		// ✅ 추가: 알림 WebSocket 엔드포인트
		registry
        .addEndpoint("/alarm-ws")  
        .setAllowedOrigins("*")
        .withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// /chat으로 시작하는 url이 발행가능하도록 설정
		registry.enableSimpleBroker("/topic", "/queue");//"/queue" 알림 기능으로 추가
		
		// 접두어 자동 추가 기능
		registry.setApplicationDestinationPrefixes("/app");  // 구독 url에 /chat을 자동으로 붙임
	}

}
