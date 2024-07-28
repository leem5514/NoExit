package com.E1i3.NoExit.domain.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisInitializer {
    @Autowired
    private RedisTemplate<String, Object> reservationRedisTemplate;

    private static final String RESERVATION_LOCK_PREFIX = "reservation:lock:";

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Set<String> keys = reservationRedisTemplate.keys(RESERVATION_LOCK_PREFIX + "*");
        if (keys != null) {
            reservationRedisTemplate.delete(keys);
        }
    }
}
