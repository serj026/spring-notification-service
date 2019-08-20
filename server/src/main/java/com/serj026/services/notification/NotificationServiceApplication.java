package com.serj026.services.notification;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Notification Service.
 *
 * @author Serhii Franchuk
 */
@SpringBootApplication(exclude = {
        LiquibaseAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        SessionAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class,
        RedisAutoConfiguration.class
})
@EnableDiscoveryClient
public class NotificationServiceApplication {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}