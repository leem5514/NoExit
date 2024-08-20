package com.E1i3.NoExit.domain.findboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindBoardSearchDto {
    private String title;
    private String contents;
}
