package com.back.snobs.service.redispubsub;

import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StompConnectionHandler implements ChannelInterceptor {

    private final RedisTemplate redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println(1);
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        if (accessor.getCommand() == StompCommand.DISCONNECT) {
            try (RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
                redisConnection.getSubscription();
            } catch (Exception e) {

            }
        }

        return message;
    }
}
