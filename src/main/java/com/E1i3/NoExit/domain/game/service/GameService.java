package com.E1i3.NoExit.domain.game.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.E1i3.NoExit.domain.game.domain.Game;
import com.E1i3.NoExit.domain.game.dto.GameResDto;
import com.E1i3.NoExit.domain.game.repository.GameRepository;

@Service
@Transactional(readOnly = true)
public class GameService {
	private final GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public List<GameResDto> gameList() {
		List<Game> gameList = gameRepository.findAll();
		List<GameResDto> gameResDtolist = new ArrayList<>();
		for (Game game : gameList) {
			gameResDtolist.add(game.fromEntity());
		}
		return gameResDtolist;
	}
}
