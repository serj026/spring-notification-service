package com.serj026.services.notification.client;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.apache.camel.component.kafka.KafkaEndpoint;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationClientKafkaConfiguration extends CamelConfiguration {

    private static final String TOPIC_NAME = "notification";
    private static final String KAFKA_SERIALIZER = "kafka.serializer.StringEncoder";
    private static final String ENDPOINT_URI = "direct:" + TOPIC_NAME;

    @Value("${kafka.brokerList}")
    private String kafkaBrokerList;
    @Value("${kafka.zookeeperHost}")
    private String zookeeperHost;
    @Value("${kafka.zookeeperPort}")
    private int zookeeperPort;
    @Value("${kafka.zookeeperConnect}")
    private String zookeeperConnect;

    @Bean(name = "notificationKafkaConfiguration")
    public KafkaConfiguration notificationKafkaConfiguration() {
        KafkaConfiguration config = new KafkaConfiguration();
        config.setZookeeperHost(zookeeperHost);
        config.setZookeeperPort(zookeeperPort);
        config.setBrokers(kafkaBrokerList);
        config.setTopic(TOPIC_NAME);
        config.setSerializerClass(KAFKA_SERIALIZER);
        config.setProducerType("sync");
        return config;
    }

    @Bean(name = "notificationKafkaEndpoint")
    public KafkaEndpoint notificationKafkaEndpoint(@Qualifier("notificationKafkaConfiguration") KafkaConfiguration notificationKafkaConfiguration) {
        KafkaEndpoint endpoint = new KafkaEndpoint();
        endpoint.setConfiguration(notificationKafkaConfiguration);
        return endpoint;
    }

    @Bean(name = "notificationSenderRouteBuilder")
    public RouteBuilder notificationSenderRouteBuilder(@Qualifier("notificationKafkaEndpoint") KafkaEndpoint notificationKafkaEndpoint) {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(ENDPOINT_URI)
                        .autoStartup(true)
                        .marshal()
                        .json(JsonLibrary.Jackson)
                        .convertBodyTo(String.class)
                        .to(notificationKafkaEndpoint.getEndpointUri())
                        .end();
            }
        };
    }

    @Bean(name = "notificationKafkaProducer")
    public ProducerTemplate notificationKafkaProducer() throws Exception {
        ProducerTemplate producerTemplate = camelContext().createProducerTemplate();
        producerTemplate.setDefaultEndpointUri(ENDPOINT_URI);
        return producerTemplate;
    }

}