package com.E1i3.NoExit.domain.wishlist.controller;

import java.util.List;

import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//		List<GameResDto> gameList = wishListService.getWishList();
		List<WishList> wishLists = wishListService.getWishList();
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "위시리스트에 조회 성공", wishLists), HttpStatus.OK);
	}




	// 위시리스트 삭제
	@PatchMapping("/wishlist/delete/{gameId}")
	public ResponseEntity<?> deleteWishList(@PathVariable Long gameId) {
		WishList wishList = wishListService.deleteWishList(gameId);
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "위시리스트에 삭제 성공", wishList.getId()), HttpStatus.OK);
	}


}
