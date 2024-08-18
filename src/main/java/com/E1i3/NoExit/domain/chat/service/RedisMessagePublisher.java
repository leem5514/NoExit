package com.E1i3.NoExit.domain.chat.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

// 메세지 발행
@Service
public class RedisMessagePublisher {
    @Qualifier("chatRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    @Qualifier("chatTopic")
    private final ChannelTopic topic;

    public RedisMessagePublisher(
            @Qualifier("chatRedisTemplate") RedisTemplate<String, Object> redisTemplate,
            @Qualifier("chatTopic") ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(String message) {
        System.out.println("Publishing message to Redis topic: " + topic.getTopic());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}