package com.serj026.services.notification;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class NotificationCamelProcessor implements Processor {

    private final NotificationService notificationService;

    public NotificationCamelProcessor(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn().getBody(Message.class);
        notificationService.sendMessage(message);
    }
}