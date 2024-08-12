package com.E1i3.NoExit.domain.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.E1i3.NoExit.domain.common.dto.CommonResDto;
import com.E1i3.NoExit.domain.game.dto.GameResDto;
import com.E1i3.NoExit.domain.game.service.GameService;

@RestController
public class GameController {
	private final GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/game/list")
	public ResponseEntity<?> productList() {
		List<GameResDto> games = gameService.gameList();
		return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "OK", games), HttpStatus.OK);
	}

}
