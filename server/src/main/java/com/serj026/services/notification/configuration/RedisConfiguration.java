package com.serj026.services.notification.configuration;

import com.serj026.services.notification.redis.JsonLettuceCodec;
import com.serj026.services.notification.redis.JsonRedisSerializer;
import com.serj026.services.notification.redis.LettuceRedisOps;
import com.serj026.services.notification.redis.RedisOps;
import com.serj026.services.notification.websocket.User;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.codec.RedisCodec;
import com.lambdaworks.redis.codec.Utf8StringCodec;
import com.lambdaworks.redis.resource.ClientResources;
import com.lambdaworks.redis.resource.DefaultClientResources;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RedisConfiguration {

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.timeoutInSeconds}")
    private int redisTimeout;

    @Bean(destroyMethod = "shutdown")
    public ClientResources redisClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    public RedisClient redisClient(ClientResources redisClientResources) {
        RedisURI uri = RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .withTimeout(redisTimeout, TimeUnit.SECONDS)
                .build();
        return RedisClient.create(redisClientResources, uri);
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, User> redisConnection(RedisClient redisClient) {
        RedisCodec<String, User> codec = new JsonLettuceCodec(new Utf8StringCodec(), new JsonRedisSerializer());
        return redisClient.connect(codec);
    }

    @Bean
    public RedisOps redisOps(StatefulRedisConnection<String, User> redisConnection) {
        return new LettuceRedisOps(redisConnection);
    }

}