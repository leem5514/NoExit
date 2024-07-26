package com.E1i3.NoExit.domain.findboard.dto;
import com.E1i3.NoExit.domain.findboard.domain.FindBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindBoardSaveReqDto {

    private String title;
    private String contents;
    private LocalDateTime expirationDate;
    private int totalCapacity;
    private byte[] image;

    public FindBoard toEntity() {
        return FindBoard.builder()
                .title(title)
                .contents(contents)
                .expirationDate(expirationDate)
                .totalCapacity(totalCapacity)
                .image(image)
                .delYn("Y")
                .build();
    }

}
