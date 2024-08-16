package com.E1i3.NoExit.domain.wishlist.repository;

import java.util.List;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.wishlist.domain.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
	Page<WishList> findByMemberAndDelYN(Pageable pageable, Member member, DelYN delYN);

}
