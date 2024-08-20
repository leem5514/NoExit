package com.E1i3.NoExit.domain.member.dto;

import com.E1i3.NoExit.domain.grade.domain.Grade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRankingResDto {
	private String nickname;
	private int point;
	private int reviewCount;
	// private Grade grade;
}
