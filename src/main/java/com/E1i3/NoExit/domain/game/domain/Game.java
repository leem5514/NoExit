package com.E1i3.NoExit.domain.game.domain;

import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Game extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameName; // 게임명

    @Column(nullable = false)
    private String runningTime; // 러닝 타임

    //@Enumerated(EnumType.STRING)
    private Difficult difficult; // 난이도

    // (수정) 인원수에 따른 가격 차이 有
    @Column(nullable = false)
    private int price; // 가격 - > 이것도 Enum으로 받아야할까?

    @Column(nullable = false)
    private int maximunPerson; // 최대인원

    //@Column(nullable = false, length = 500)
    private String gameInfo; // 게임설명

    @Enumerated(EnumType.STRING)
    private AgeLimit ageLimit; // 나이제한( 성인 / 미성년자 )

    //@Column(name = "image_path", nullable = false)
    private String imagePath; // 사진 경로

//    @ManyToOne
//    @JoinColumn(name = "store_id")
//    private Store store;
//
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
//    private List<Review> reviews;
}
