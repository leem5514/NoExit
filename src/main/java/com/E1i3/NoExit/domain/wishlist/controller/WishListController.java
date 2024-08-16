package com.E1i3.NoExit.domain.wishlist.controller;

import java.util.List;

import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.common.dto.CommonErrorDto;
import com.E1i3.NoExit.domain.wishlist.dto.WishReqDto;
import com.E1i3.NoExit.domain.wishlist.dto.WishResDto;
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
	@PostMapping("/wishlist/add")
	public ResponseEntity<?> addWishList(@RequestBody WishReqDto dto) {
		wishListService.addWishList(dto);
		CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "찜 성공", null);
		return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
	}




	// 위시리스트 조회
	@GetMapping("/wishlist")
	public ResponseEntity<?> getWishList(@PageableDefault(size=10,sort = "createdTime", direction = Sort.Direction.ASC) Pageable pageable) {
//		List<GameResDto> gameList = wishListService.getWishList();
		Page<WishResDto> dtos = wishListService.getWishList(pageable);
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "찜 목록 조회 성공", dtos);
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}



	// 위시리스트 삭제
	@PatchMapping("/wishlist/delete/{gameId}")
	public ResponseEntity<?> deleteWishList(@PathVariable Long gameId) {
		wishListService.deleteWishList(gameId);
		CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "찜 삭제", null);
		return new ResponseEntity<>(commonResDto, HttpStatus.OK);
	}




}
