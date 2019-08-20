package com.serj026.services.notification.websocket;

import com.serj026.services.notification.redis.RedisOps;
import com.serj026.session.service.SessionManager;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;

public class UserInterceptor extends ChannelInterceptorAdapter {

    private final SessionManager sessionManager;
    private final RedisOps redisOps;

    public UserInterceptor(SessionManager sessionManager, RedisOps redisOps) {
        this.sessionManager = sessionManager;
        this.redisOps = redisOps;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                Object userIdParam = ((Map) raw).get("userId");
                Object sessionIdParam = ((Map) raw).get("sessionId");
                if (userIdParam instanceof LinkedList && sessionIdParam instanceof LinkedList) {
                    Long userId = Long.parseLong((String) ((LinkedList) userIdParam).get(0));
                    Long sessionId = Long.parseLong((String) ((LinkedList) sessionIdParam).get(0));

                    sessionManager.validateSession(userId, sessionId);
                    User user = new User(userId, sessionId, Instant.now().getEpochSecond());
                    accessor.setUser(user);
                    redisOps.addUser(user);
                }
            }
        }
        return message;
    }
}