package com.E1i3.NoExit.domain.member.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.member.dto.MemberListResDto;
import com.E1i3.NoExit.domain.member.dto.MemberSaveReqDto;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.review.domain.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String username;

	@Column(length = 255, nullable = false)
	private String password;

	@Column(length = 100, unique = true)
	private String email;

	private int point;
	private int age;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Role role = Role.USER;

	@Column(length = 255, nullable = false)
	private String phone_number;

	@Column(length = 100, nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	private DelYN delYN = DelYN.N;

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
	private List<Review> reviews;

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
	private List<Reservation> reservations;

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
	private List<Board> boards;

	public Member updateMember(MemberUpdateDto dto, String encodedPassword) {
		this.username = dto.getUsername();
		this.password =  encodedPassword;
		this.age = dto.getAge();
		this.phone_number = dto.getPhone_number();
		this.nickname = dto.getNickname();
		return this;
	}

	public Member saveMember(MemberSaveReqDto dto,  String encodedPassword) {
		this.username = dto.getUsername();
		this.password = encodedPassword;
		this.age = dto.getAge();
		this.phone_number = dto.getPhone_number();
		this.nickname = dto.getNickname();
		return this;
	}

	public Member updateDelYN() {
		this.delYN = DelYN.Y;
		return this;
	}

	public MemberListResDto fromEntity(){
		return MemberListResDto.builder()
			.email(this.email)
			.id(this.id)
			.nickname(this.nickname)
			.username(this.username)
			.build();
	}

}
