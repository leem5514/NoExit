package com.E1i3.NoExit.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.E1i3.NoExit.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findBynickname(String nickName);
	Optional<Member> findByEmail(String email);
}
