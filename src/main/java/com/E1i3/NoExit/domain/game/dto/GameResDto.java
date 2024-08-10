package com.E1i3.NoExit.domain.game.dto;

import com.E1i3.NoExit.domain.game.domain.Difficult;
import com.E1i3.NoExit.domain.store.domain.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResDto {
	private String gameName; // 게임명
	private Difficult difficult; // 난이도
	private int price;
	private String imagePath; // 사진 경로
	private Store store;
}
