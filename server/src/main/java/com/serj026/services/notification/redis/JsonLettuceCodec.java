package com.serj026.services.notification.redis;

import com.serj026.services.notification.websocket.User;
import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.codec.Utf8StringCodec;

import java.nio.ByteBuffer;

public class JsonLettuceCodec implements RedisCodec<String, User> {

    private final Utf8StringCodec utf8StringCodec;
    private final JsonRedisSerializer jsonRedisSerializer;

    public JsonLettuceCodec(Utf8StringCodec utf8StringCodec, JsonRedisSerializer jsonRedisSerializer) {
        this.utf8StringCodec = utf8StringCodec;
        this.jsonRedisSerializer = jsonRedisSerializer;
    }

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return utf8StringCodec.decodeKey(bytes);
    }

    @Override
    public User decodeValue(ByteBuffer bytes) {
        if (bytes == null) {
            return null;
        }
        byte[] array = new byte[bytes.remaining()];
        bytes.get(array);
        return jsonRedisSerializer.deserialize(array);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return utf8StringCodec.encodeKey(key);
    }

    @Override
    public ByteBuffer encodeValue(User value) {
        byte[] data = jsonRedisSerializer.serialize(value);
        return ByteBuffer.wrap(data);
    }

}