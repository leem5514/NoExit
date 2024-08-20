package com.E1i3.NoExit.domain.game.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameSearchDto {

    private String gameName;
    private String price;
    private String difficult;

}
