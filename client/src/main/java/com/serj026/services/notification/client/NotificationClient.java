package com.serj026.services.notification.client;

import com.serj026.services.notification.ClientMessage;
import com.serj026.services.notification.Message;
import com.serj026.services.notification.NotificationApi;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class NotificationClient implements NotificationApi {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);

    private final ProducerTemplate notificationKafkaProducer;
    private final String senderServiceId;
    private final String senderInstanceId;

    public NotificationClient(ProducerTemplate notificationKafkaProducer, String senderServiceId, String senderInstanceId) {
        this.notificationKafkaProducer = notificationKafkaProducer;
        this.senderServiceId = senderServiceId;
        this.senderInstanceId = senderInstanceId;
    }

    @Override
    public void sendMessage(Long userId, ClientMessage clientMessage) {
        if (clientMessage != null && clientMessage.getType() != null) {
            Message message = Message.builder()
                    .userId(userId)
                    .senderServiceId(senderServiceId)
                    .senderInstanceId(senderInstanceId)
                    .type(clientMessage.getType())
                    .payload(clientMessage.getPayload())
                    .createdTs(Instant.now().getEpochSecond())
                    .build();

            try {
                String header = userId != null ? String.valueOf(userId) : null;
                notificationKafkaProducer.sendBodyAndHeader(message, KafkaConstants.KEY, header);
                logger.debug("Notification {} has been sent to kafka", message);
            } catch (Exception e) {
                logger.error("Error during send notification: message={}", message, e);
            }
        }
    }

    @Override
    public void sendMessage(ClientMessage clientMessage) {
        sendMessage(null, clientMessage);
    }
}