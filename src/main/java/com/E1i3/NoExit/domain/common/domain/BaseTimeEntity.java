package com.beyond.ordersystem.common.domain;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
	@CreationTimestamp
	private LocalDateTime createdTime;

	@UpdateTimestamp
	private LocalDateTime updateTime;
}
