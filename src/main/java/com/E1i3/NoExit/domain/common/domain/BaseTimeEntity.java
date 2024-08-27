package com.E1i3.NoExit.domain.common.domain;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;

@Data
@MappedSuperclass
public abstract class BaseTimeEntity {
	@CreationTimestamp
	private LocalDateTime createdTime;

	@UpdateTimestamp
	private LocalDateTime updateTime;

	public LocalDateTime createdTime() {
		return createdTime;
	}

	public LocalDateTime updateTime() {
		return updateTime;
	}
}