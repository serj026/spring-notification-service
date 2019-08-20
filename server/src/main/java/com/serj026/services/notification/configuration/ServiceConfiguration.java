package com.serj026.services.notification.configuration;

import com.serj026.services.notification.NotificationService;
import com.serj026.services.notification.redis.RedisOps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
public class ServiceConfiguration {

    @Bean
    public NotificationService notificationService(RedisOps redisOps, SimpMessageSendingOperations messagingTemplate) {
        return new NotificationService(redisOps, messagingTemplate);
    }

}