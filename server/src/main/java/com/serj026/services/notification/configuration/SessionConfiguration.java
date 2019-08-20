package com.serj026.services.notification.configuration;

import com.serj026.common.DefaultRedisConfig;
import com.serj026.common.RedisConfig;
import com.serj026.session.configuration.SessionManagerFactory;
import com.serj026.session.configuration.SessionsConfig;
import com.serj026.session.service.SessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfiguration {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.timeoutInSeconds}")
    private int redisTimeout;
    @Value("${session.expirationEnabled}")
    private boolean sessionExpirationEnabled;
    @Value("${session.expirationInMinutes}")
    private Long sessionExpirationInMinutes;

    @Bean(destroyMethod = "destroy")
    public SessionManagerFactory sessionManagerFactory() {
        return new SessionManagerFactory();
    }

    @Bean
    public SessionManager sessionManager(SessionManagerFactory sessionManagerFactory) {
        RedisConfig redisConfig = new DefaultRedisConfig(redisHost, redisPort, redisTimeout);
        SessionsConfig sessionsConfig = new SessionsConfig(redisConfig, sessionExpirationEnabled, sessionExpirationInMinutes);
        return sessionManagerFactory.create(sessionsConfig);
    }

}