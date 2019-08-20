package com.serj026.services.notification.client.springkafka;

import com.serj026.services.notification.ClientMessage;
import com.serj026.services.notification.Message;
import com.serj026.services.notification.NotificationApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;

public class NotificationClient implements NotificationApi {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);
    private static final String TOPIC_NAME = "notification";

    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final String senderServiceId;
    private final String senderInstanceId;

    public NotificationClient(KafkaTemplate<String, Message> kafkaTemplate,
                              String senderServiceId,
                              String senderInstanceId) {
        this.kafkaTemplate = kafkaTemplate;
        this.senderServiceId = senderServiceId;
        this.senderInstanceId = senderInstanceId;
    }

    @Override
    public void sendMessage(Long userId, ClientMessage clientMessage) {
        Message message = createMessage(userId, clientMessage);
        try {
            kafkaTemplate.send(TOPIC_NAME, message);
            logger.info("Notification sent: message={}", clientMessage);
        } catch (Exception e) {
            logger.error("Can't send kafka notification: message={}", clientMessage, e);
        }
    }

    @Override
    public void sendMessage(ClientMessage clientMessage) {
        sendMessage(null, clientMessage);
    }

    private Message createMessage(Long userId, ClientMessage clientMessage) {
        return Message.builder()
                .userId(userId)
                .senderServiceId(senderServiceId)
                .senderInstanceId(senderInstanceId)
                .type(clientMessage.getType())
                .payload(clientMessage.getPayload())
                .createdTs(Instant.now().getEpochSecond())
                .channelId(clientMessage.getChannelId())
                .build();
    }
}