package com.damoa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * WebSocketMessageBrokerConfigurer 인터페이스를 구현하여 메시지 브로커 및 STOMP 엔드포인트를 설정
 */
@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화 (STOMP 프로토콜 사용)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 클라이언트가 접근할 엔드포인트 설정
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();             // SockJS 대체 연결 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic"); // 1대1, 1대N
        registry.setApplicationDestinationPrefixes("/app");   // 메시지 수신 경로 설정
    }

}
