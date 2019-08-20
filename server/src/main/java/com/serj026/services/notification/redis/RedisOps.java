package com.serj026.services.notification.redis;

import com.serj026.services.notification.websocket.User;

public interface RedisOps {

    void addUser(User user);

    User getUser(long userId);

    void deleteUser(long userId);

}