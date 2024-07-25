package com.E1i3.NoExit.domain.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long id; // 댓글 아이디
    private Long boardId; // 댓글 단 게시글 아이디
    private Long memberId; // 댓글 작성자 아이디
    private String content; // 댓글 내용
    private int likes; // 좋아요 수
    private int dislikes; // 싫어요 수
    @CreationTimestamp
    private LocalDateTime createdTime; // 작성시간
    @UpdateTimestamp
    private LocalDateTime updatedTime; // 수정시간
    @Enumerated(EnumType.STRING)
    private DelYN delYN; // 삭제 여부
}
