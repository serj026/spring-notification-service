package com.serj026.services.notification.client.springkafka;

import com.serj026.services.notification.Message;
import com.serj026.services.notification.NotificationApi;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "service.notification.client", name = "enabled", havingValue = "true", matchIfMissing = true)
public class NotificationClientConfiguration {

    @Value("${kafka.bootstrapServers}")
    private String bootstrapServers;
    @Value("${spring.application.name}")
    private String senderServiceId;
    @Value("${spring.cloud.consul.discovery.instanceId}")
    private String senderInstanceId;

    @Bean
    public Map<String, Object> producerConfiguration() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, Message> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfiguration());
    }

    @Bean
    public KafkaTemplate<String, Message> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NotificationApi notificationApi(KafkaTemplate<String, Message> kafkaTemplate) {
        return new NotificationClient(kafkaTemplate, senderServiceId, senderInstanceId);
    }
}