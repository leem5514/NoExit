package com.E1i3.NoExit.domain.game.domain;

import com.E1i3.NoExit.domain.common.domain.BaseTimeEntity;
import com.E1i3.NoExit.domain.game.dto.GameResDto;
import com.E1i3.NoExit.domain.store.domain.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameName; // 게임명

    @Column(nullable = false)
    private String runningTime; // 러닝 타임

    @Enumerated(EnumType.STRING)
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

    @Column(name = "image_path", nullable = true)
    private String imagePath; // 사진 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // StoreInfo와의 관계 설정
//    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
//    private List<Review> reviews;
    public GameResDto fromEntity(){
        return GameResDto.builder()
            .gameName(this.gameName)
            .difficult(this.difficult)
            .price(this.price)
            // .store(this.store)
            .imagePath(this.imagePath)
            .build();
    }
}
