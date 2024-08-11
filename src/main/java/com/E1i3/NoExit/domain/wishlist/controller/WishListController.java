package com.E1i3.NoExit.domain.wishlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.dto.GameResDto;
import com.E1i3.NoExit.domain.wishlist.domain.WishList;
import com.E1i3.NoExit.domain.wishlist.service.WishListService;

@RestController
public class WishListController {
	private final WishListService wishListService;

	public WishListController(WishListService wishListService) {
		this.wishListService = wishListService;
	}

	// 위시리스트에 추가
	@PostMapping("/wishlist/{gameId}")
	public ResponseEntity<?> addWishList(@PathVariable Long gameId) {
		WishList wishList = wishListService.addWishList(gameId);
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "위시리스트에 추가 성공", wishList.getId()), HttpStatus.OK);
	}

	// 위시리스트 조회
	@GetMapping("/wishlist")
	public ResponseEntity<?> getWishList() {
		List<GameResDto> gemeList = wishListService.getWishList();
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "위시리스트에 추가 성공", gemeList), HttpStatus.OK);
	}
}
