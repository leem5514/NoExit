package com.E1i3.NoExit.domain.wishlist.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.repository.GameRepository;
import com.E1i3.NoExit.domain.wishlist.dto.WishResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.wishlist.domain.WishList;
import com.E1i3.NoExit.domain.wishlist.repository.WishListRepository;

@Service
@Transactional
public class WishListService {

	private final WishListRepository wishListRepository;
	private final MemberRepository memberRepository;
	private final GameRepository gameRepository;


	public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository, GameRepository gameRepository) {
		this.wishListRepository = wishListRepository;
		this.memberRepository = memberRepository;
		this.gameRepository = gameRepository;
	}


	public WishList addWishList(Long gameId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));
		List<WishList> wishLists = wishListRepository.findByMember(member);

		for(int i=0; i<wishLists.size(); i++) {
			if (wishLists.get(i).getGameId().equals(gameId) && wishLists.get(i).getDelYN().equals(DelYN.N)) {
				throw new IllegalArgumentException("이미 위시리스트에 존재하는 게임입니다.");
			}
		}
		WishList wishList = WishList.builder()
				.gameId(gameId)
				.member(member)
				.build();
		Game game = gameRepository.findById(gameId).orElseThrow(()->new EntityNotFoundException("없는 게임정보입니다."));
		game.updateWishCount(true);
		gameRepository.save(game);
		return wishListRepository.save(wishList);
	}


//	public List<GameResDto> getWishList() {
//		String email = SecurityContextHolder.getContext().getAuthentication().getName();
//		Member member = memberRepository.findByEmail(email)
//			.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));
//		List<WishList> wishLists = wishListRepository.findByMemberAndDelYN(member, DelYN.N);
//
//		return wishLists.stream()
//				.map(wishList -> gameRepository.findById(wishList.getGameId())
//						.orElseThrow(() -> new EntityNotFoundException("일치하는 정보가 존재하지 않습니다.")))
//				.map(Game::fromEntity)
//				.collect(Collectors.toList());
//	}


	public Page<WishResDto> getWishList(Pageable pageable) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));
		Page<WishList> wishLists = wishListRepository.findByMemberAndDelYN(pageable, member, DelYN.N);
		Page<WishResDto> dtos = wishLists.map(WishList::fromEntity);


		return dtos;
	}





	public void deleteWishList(Long gameId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("회원정보가 존재하지 않습니다."));

		List<WishList> wishLists = wishListRepository.findByMember(member);
		for(int i=0; i<wishLists.size(); i++) {
			if (wishLists.get(i).getGameId().equals(gameId) && wishLists.get(i).getDelYN().equals(DelYN.N)) {
				wishLists.get(i).deleteEntity();
				wishListRepository.save(wishLists.get(i));
			}
		}
		Game game = gameRepository.findById(gameId).orElseThrow(()->new EntityNotFoundException("없는 게임정보입니다."));
		game.updateWishCount(false);
		gameRepository.save(game);
	}
}