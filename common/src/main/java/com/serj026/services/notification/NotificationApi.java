package com.serj026.services.notification;

public interface NotificationApi {

    void sendMessage(Long userId, ClientMessage clientMessage);

    void sendMessage(ClientMessage clientMessage);
}