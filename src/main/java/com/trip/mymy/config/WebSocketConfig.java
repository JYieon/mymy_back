package com.trip.mymy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * WebSocket 설정 클래스
 * - STOMP 프로토콜을 기반으로 WebSocket을 사용하여 클라이언트와 서버 간의 실시간 메시지 전송을 지원
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
	/**
     * 클라이언트가 WebSocket 서버에 연결할 수 있도록 STOMP 엔드포인트 등록
     * - "/ws" 엔드포인트를 생성하여 WebSocket 연결을 허용
     * - setAllowedOrigins("http://localhost:3000"): 프론트엔드 도메인 허용 (CORS 문제 해결)
     * - withSockJS(): WebSocket을 지원하지 않는 환경에서도 사용 가능하도록 SockJS 활성화
     */
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }
	

    /**
     * 메시지 브로커 구성
     * - enableSimpleBroker("/topic"): 클라이언트가 구독할 수 있는 메시지 브로커 설정 (ex. "/topic/notifications")
     * - setApplicationDestinationPrefixes("/app"): 클라이언트가 서버로 메시지를 보낼 때 사용하는 prefix 설정
     *   (ex. "/app/alarm" -> 컨트롤러에서 @MessageMapping("/alarm")으로 처리 가능)
     */

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
