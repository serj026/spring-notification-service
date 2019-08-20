package com.serj026.services.notification.configuration;

import com.serj026.services.notification.redis.RedisOps;
import com.serj026.services.notification.websocket.UserInterceptor;
import com.serj026.services.notification.websocket.WebSocketEventListener;
import com.serj026.session.service.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private static final long HEART_BEAT_INTERVAL = 5000L;

    private final SessionManager sessionManager;
    private final RedisOps redisOps;

    @Autowired
    public WebSocketConfig(SessionManager sessionManager, RedisOps redisOps) {
        this.sessionManager = sessionManager;
        this.redisOps = redisOps;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/queue", "/user", "/channel")
                .setTaskScheduler(heartBeatScheduler());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS().setHeartbeatTime(HEART_BEAT_INTERVAL);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new UserInterceptor(sessionManager, redisOps));
    }

    @Bean
    public WebSocketEventListener webSocketEventListener() {
        return new WebSocketEventListener(redisOps);
    }

    @Bean
    public TaskScheduler heartBeatScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}