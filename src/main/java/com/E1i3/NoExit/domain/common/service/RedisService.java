package com.E1i3.NoExit.domain.common.service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisService {

	private final RedisTemplate<String, Object> redisTemplate;
	private static final String RESERVATION_LOCK_PREFIX = "reservation:lock:";

	@Autowired
	public RedisService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setValues(String key, String data, Duration duration) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		values.set(key, data, duration);
	}

	@Transactional(readOnly = true)
	public String getValues(String key) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		if (values.get(key) == null) {
			return "false";
		}
		return (String) values.get(key);
	}

	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}

	public boolean checkExistsValue(String value) {
		// 조회하려는 데이터가 존재하는지
		return !value.equals("false");
	}

	//
	// @EventListener
	// public void onApplicationEvent(ContextRefreshedEvent event) {
	// 	Set<String> keys = redisTemplate1.keys(RESERVATION_LOCK_PREFIX + "*");
	// 	if (keys != null) {
	// 		redisTemplate1.delete(keys);
	// 	}
	// }

}

