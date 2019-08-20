package com.serj026.services.notification.websocket;

import com.serj026.services.notification.redis.RedisOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final RedisOps redisOps;

    public WebSocketEventListener(RedisOps redisOps) {
        this.redisOps = redisOps;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection: {}", event);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("Web socket connection disconnected: {}", event);
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getUser() != null && headerAccessor.getUser().getName() != null) {
            long userId = Long.parseLong(headerAccessor.getUser().getName().split("_")[0]);
            redisOps.deleteUser(userId);
        }
    }

}