package com.serj026.services.notification.configuration;

import com.serj026.services.notification.Message;
import com.serj026.services.notification.NotificationCamelProcessor;
import com.serj026.services.notification.NotificationService;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration extends CamelConfiguration {

    private static final String GROUP_ID = "notification_service";
    private static final String TOPIC_NAME = "notification";
    private static final String ROUTE_ID = "notificationRouteId";

    @Value("${kafka.brokerList}")
    private String kafkaBrokerList;
    @Value("${kafka.zookeeperConnect}")
    private String zookeeperConnect;

    @Bean
    public Processor notificationCamelProcessor(NotificationService notificationService) {
        return new NotificationCamelProcessor(notificationService);
    }

    @Bean
    public RouteBuilder receiverRouteBuilder(Processor notificationCamelProcessor) {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(getEndpointUri(TOPIC_NAME) + getOptions(GROUP_ID))
                        .log(LoggingLevel.INFO, "Received body ${body}")
                        .unmarshal(new JacksonDataFormat(Message.class))
                        .process(notificationCamelProcessor)
                        .setId(ROUTE_ID);
            }
        };
    }

    private String getEndpointUri(String topicName) {
        return String.format("kafka:%s?zookeeperConnect=%s&topic=%s", kafkaBrokerList, zookeeperConnect, topicName);
    }

    private String getOptions(String groupId) {
        return String.format("&autoCommitEnable=true&groupId=%s&serializerClass=kafka.serializer.StringEncoder&autoOffsetReset=largest&consumersCount=1&consumerStreams=1", groupId);
    }
}