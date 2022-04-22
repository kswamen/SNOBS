package com.back.snobs.config;

import com.back.snobs.service.redispubsub.StompConnectionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // WebSocket 커넥션 구축 EndPoint
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/stomp/chat")
                .setAllowedOrigins("http://localhost:3099", "http://snobs.co.kr", "https://snobs.co.kr")
                .withSockJS();
    }

    // 어플리케이션 내부에서 사용할 경로 지정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트의 send 요청 처리
        registry.setApplicationDestinationPrefixes("/ws/pub");
        // 해당 경로를 구독하는 구독자에게 메시지 전달
        registry.enableSimpleBroker("/ws/sub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new StompConnectionHandler());
    }
}
