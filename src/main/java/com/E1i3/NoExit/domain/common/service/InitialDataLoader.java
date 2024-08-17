package com.E1i3.NoExit.domain.common.service;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.domain.Role;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.owner.domain.Owner;
import com.E1i3.NoExit.domain.owner.repository.OwnerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//테스트 용이성을 위한 InitialDataLoader
@Component
public class InitialDataLoader implements CommandLineRunner {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		// Check and create OWNER account if not exis
		// ts
		if (ownerRepository.findByEmail("owner@test.com").isEmpty()) {
			Owner admin = Owner.builder()
				.username("owner")
				.email("owner@test.com")
				.password(passwordEncoder.encode("12341234")) // Encode password
				.storeName("Admin Store") // Assuming storeName is required for Owner
				.phoneNumber("010-1234-5678") // Example phone number
				.role(Role.OWNER) // Ensure Role is set appropriately
				.build();
			ownerRepository.save(admin);
		}

		if (ownerRepository.findByEmail("test@test.com").isEmpty()) {
			Owner test = Owner.builder()
				.username("test")
				.email("test@test.com")
				.password("12341234")
				.storeName("홍대의 유명한 추리 테마 방탈출")
				.phoneNumber("010-9999-9999")
				.role(Role.OWNER)
				.build();
			ownerRepository.save(test);
		}

		// Check and create USER account if not exists
		if (memberRepository.findByEmail("user@test.com").isEmpty()) {
			Member user = Member.builder()
				.username("user")
				.email("user@test.com")
				.password(passwordEncoder.encode("12341234")) // Encode password
				.role(Role.USER)
				.age(23)
				.phone_number("010-1234-5678") // Example phone number
				.nickname("user") // Example nickname
				.profileImage(
					"https://noexit-bucket.s3.ap-northeast-2.amazonaws.com/member/a547286f-7f94-4983-9856-c68039e1e515.png\n") // Set default or placeholder image URL
				.build();
			memberRepository.save(user);
		}
		if (memberRepository.findByEmail("user2@test.com").isEmpty()) {
			Member user = Member.builder()
				.username("user_test2")
				.email("user2@test.com")
				.password(passwordEncoder.encode("12341234")) // Encode password
				.role(Role.USER)
				.age(48)
				.phone_number("010-9876-9876") // Example phone number
				.nickname("user_test2") // Example nickname
				.profileImage(
					"https://noexit-bucket.s3.ap-northeast-2.amazonaws.com/member/393e9081-b2f1-4809-a7ce-833bfcb79ab8.png\n") // Set default or placeholder image URL
				.build();
			memberRepository.save(user);
		}
	}

}
