package com.serj026.services.notification.redis;

import com.serj026.services.notification.websocket.User;
import com.lambdaworks.redis.api.StatefulRedisConnection;

public class LettuceRedisOps implements RedisOps {

    private static final String KEY = "ws_%d";

    private final StatefulRedisConnection<String, User> redisConnection;

    public LettuceRedisOps(StatefulRedisConnection<String, User> redisConnection) {
        this.redisConnection = redisConnection;
    }

    @Override
    public void addUser(User user) {
        redisConnection.sync().set(String.format(KEY, user.getUserId()), user);
    }

    @Override
    public User getUser(long userId) {
        return redisConnection.sync().get(String.format(KEY, userId));
    }

    @Override
    public void deleteUser(long userId) {
        redisConnection.sync().del(String.format(KEY, userId));
    }
}