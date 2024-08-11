package com.E1i3.NoExit.domain.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.wishlist.domain.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
	List<WishList> findByMember(Member member);
}
