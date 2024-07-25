package com.E1i3.NoExit.domain.review.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 리뷰 내용 등 필요한 필드 추가

    @ManyToOne
    private Member member; // Review는 Member와 Many-to-One 관계를 가짐

    // 다른 필드 및 메소드
}
