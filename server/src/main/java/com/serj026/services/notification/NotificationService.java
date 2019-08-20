package com.serj026.services.notification;

import com.serj026.services.notification.redis.RedisOps;
import com.serj026.services.notification.websocket.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String USER_CHANNEL = "/queue/notification";
    private static final String CUSTOM_CHANNEL = "/channel/notification/";

    private final RedisOps redisOps;
    private final SimpMessageSendingOperations messagingTemplate;

    public NotificationService(RedisOps redisOps, SimpMessageSendingOperations messagingTemplate) {
        this.redisOps = redisOps;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(Message message) {
        UserMessage userMessage = createUserMessage(message);
        if (message.getChannelId() != null) {
            try {
                messagingTemplate.convertAndSend(CUSTOM_CHANNEL + message.getChannelId(), userMessage);
            } catch (Exception e) {
                logger.error("Can't send custom notification to channel: {}", message);
            }
        } else {
            User user = redisOps.getUser(message.getUserId());
            if (user != null) {
                try {
                    messagingTemplate.convertAndSendToUser(user.getName(), USER_CHANNEL, userMessage);
                } catch (Exception e) {
                    logger.error("Can't send notification to user: {}", message);
                }
            }
        }
    }

    private UserMessage createUserMessage(Message message) {
        UserMessage userMessage = new UserMessage();
        userMessage.setType(message.getType());
        userMessage.setPayload(message.getPayload());
        userMessage.setCreatedTs(message.getCreatedTs());
        return userMessage;
    }

    private static class UserMessage {
        private String type;
        private Object payload;
        private long createdTs;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }

        public long getCreatedTs() {
            return createdTs;
        }

        public void setCreatedTs(long createdTs) {
            this.createdTs = createdTs;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("type", type)
                    .append("payload", payload)
                    .append("createdTs", createdTs)
                    .toString();
        }
    }

}