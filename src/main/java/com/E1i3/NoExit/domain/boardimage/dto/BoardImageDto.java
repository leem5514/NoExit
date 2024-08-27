package com.E1i3.NoExit.domain.boardimage.dto;

import com.E1i3.NoExit.domain.common.domain.DelYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImageDto {
    private Long id;
    private String imageUrl;
    private DelYN delYN;
}
