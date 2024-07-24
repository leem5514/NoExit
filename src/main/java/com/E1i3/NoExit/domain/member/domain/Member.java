package com.E1i3.NoExit.domain.member.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 50, nullable = false)
	private String username;
	@Column(length = 50, nullable = false)
	private String password;
	@Column(length = 100, nullable = false)
	private String email;

	private int point;
	private int age;
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(length = 255, nullable = false)
	private String phone_number;
	@Column(length = 100, nullable = false)
	private String nickname;

	@CreationTimestamp
	private LocalDateTime createdTime;
	@UpdateTimestamp
	private LocalDateTime updateTime;
}

