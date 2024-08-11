package com.E1i3.NoExit.domain.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Store {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String storeName;

	@Column(nullable = false)
	private int storeRating;

	private String openingHours;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private OpenStatus openStatus = OpenStatus.OPEN;

	private String phoneNumber;
	private String address;
	private String info;

}
