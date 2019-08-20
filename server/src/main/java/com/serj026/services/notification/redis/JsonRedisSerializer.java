package com.serj026.services.notification.redis;

import com.serj026.services.notification.websocket.User;
import com.serj026.session.errors.SessionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

public class JsonRedisSerializer implements RedisSerializer<User> {

    private static final Logger logger = LoggerFactory.getLogger(JsonRedisSerializer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public byte[] serialize(User user) throws SerializationException {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            String errorMsg = String.format("Error during user serialization: %s. Error: %s", user, e);
            logger.error(errorMsg);
            throw new SessionException(errorMsg);
        }
    }

    @Override
    public User deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, User.class);
        } catch (IOException e) {
            String errorMsg = String.format("Error during user deserialization. Error: %s", e);
            logger.error(errorMsg);
            throw new SessionException(errorMsg);
        }
    }
}
