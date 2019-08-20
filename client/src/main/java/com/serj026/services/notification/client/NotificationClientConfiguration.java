package com.serj026.services.notification.client;

import com.serj026.services.notification.NotificationApi;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationClientConfiguration {

    @Value("${spring.application.name}")
    private String senderServiceId;
    @Value("${spring.cloud.consul.discovery.instanceId}")
    private String senderIntsanceId;

    @Bean
    public NotificationApi notificationApi(ProducerTemplate notificationKafkaProducer) {
        return new NotificationClient(notificationKafkaProducer, senderServiceId, senderIntsanceId);
    }

}