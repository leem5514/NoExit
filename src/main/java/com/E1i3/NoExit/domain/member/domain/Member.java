package com.E1i3.NoExit.domain.member.domain;

import java.time.LocalDateTime;
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
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import com.E1i3.NoExit.domain.member.dto.MemberUpdateDto;
import com.E1i3.NoExit.domain.reservation.domain.Reservation;
import com.E1i3.NoExit.domain.review.domain.Review;
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

	@Column(length = 100, unique = true)
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

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
	private List<Review> reviews;

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
	private List<Reservation> reservations;

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
	private List<Board> boards;

	@OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST) // cascadetype 해야 FK 삭제 가능
	private List<FindBoard> FindBoards;

	public Member updateMember(MemberUpdateDto dto) {
		// 이메일은 수정 x
		this.username = dto.getUsername();
		this.password = dto.getPassword();
		this.age = dto.getAge();
		this.phone_number = dto.getPhone_number();
		this.nickname = dto.getNickname();
		return this;
	}
}
