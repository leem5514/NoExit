package com.E1i3.NoExit.domain.common.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	public LettuceConnectionFactory redisConnectionFactory(int index) {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setDatabase(index);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	// 이메일 인증 (0번 데이터베이스)
	@Bean
	@Primary
	LettuceConnectionFactory connectionFactoryEmail() { return redisConnectionFactory(0); }

	@Bean
	@Primary
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactoryEmail());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	// 예약 시스템 (1번 데이터베이스)
	@Bean
	@Qualifier("2")
	LettuceConnectionFactory connectionFactoryNotification() { return redisConnectionFactory(1); }

	@Bean
	@Qualifier("2")
	public RedisTemplate<String, Object> redisTemplate1() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactoryNotification());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return redisTemplate;
	}

	// @Bean
	// public RedisConnectionFactory redisConnectionFactory() {
	// 	// 추후에 알림처리에서 데이터베이스를 사용하니깐 데이터베이스 설정하는 식으로 코드 수정
	// 	return new LettuceConnectionFactory(host, port);
	// }
	//
	// @Bean
	// public RedisConnectionFactory redisConnectionFactory(int dbIndex) {
	// 	RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
	// 	configuration.setHostName(host);
	// 	configuration.setPort(port);
	// 	configuration.setDatabase(dbIndex);
	// 	// configuration.setPassword("123456");
	// 	return new LettuceConnectionFactory(configuration);
	// }
	//
	// @Bean
	// public RedisTemplate<String, Object> redisTemplate() {
	// 	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	// 	redisTemplate.setConnectionFactory(redisConnectionFactory(0));
	//
	// 	redisTemplate.setKeySerializer(new StringRedisSerializer());
	// 	redisTemplate.setValueSerializer(new StringRedisSerializer());
	//
	// 	return redisTemplate;
	// }

}