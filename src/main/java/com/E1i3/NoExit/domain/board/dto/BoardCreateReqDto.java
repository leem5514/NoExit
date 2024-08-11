package com.E1i3.NoExit.domain.board.dto;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.domain.BoardType;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCreateReqDto {
//    private Long memberId; // 작성자 멤버 아이디 // 로그인하면 필요없음
//    private String writer; // 작성자
    private String title; // 제목
    private String content; // 내용
//    private List<BoardImg> boardImages; // 이미지
    private BoardType boardType; // 게시판 유형 (자유, 전략)


//
//    public Board toEntity(Member member, List<String> imgUrls) {
//
//        List<String> imgs = new ArrayList<>();
//        for(String s : imgUrls) {
//            imgs.add(s);
//        }
//
//        Board board = Board.builder()
//                .member(member) // member를 설정 // 로그인하면 필요없음
////                .writer(this.writer)
//                .title(this.title)
//                .content(this.content)
//                .boardType(this.boardType)
////                .imgUrls(imgs)
//                .imgUrls()
//                .build();
//
//        return board;
//    }

}
