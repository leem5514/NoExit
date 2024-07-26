package com.E1i3.NoExit.domain.findboard.domain;

//import com.E1i3.NoExit.domain.findboard.dto.FindBoardDetailResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardListResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardResDto;
import com.E1i3.NoExit.domain.findboard.dto.FindBoardUpdateReqDto;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FindBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    // wirter = nickname 이라면 사용자에게 데이터
    @Column(length = 100, nullable = true)
    private String writer;

    @Column(length = 3000)
    private String contents;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    private LocalDateTime expirationTime;
    private int currentCount;
    private int totalCapacity;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYn delYn = DelYn.Y;

    @Lob
    @Column(name = "image", nullable = true)
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY) //참조 안 하면 안 나가게.
    @JoinColumn(name = "member_id")
    private Member member;

    public FindBoardResDto ResDtoFromEntity() {
        return FindBoardResDto.builder()
                .member_id(this.member.getId()) // 작성자 ID 추가
                .id(this.id)
                .writer(this.member.getNickname())
                .title(this.title)
                .contents(this.contents)
                .createdTime(this.createdTime)
                .updateTime(this.updateTime)
                .expirationTime(this.expirationTime)
                .currentCount(this.currentCount)
                .totalCapacity(this.totalCapacity)
                .image(this.image)
                .build();
    }


    // detailResDtoFromEntity 이게 필요할까? 중복되는것같은데 논의 해보자!!
//    public FindBoardDetailResDto detailResDtoFromEntity(){
//        return FindBoardDetailResDto.builder()
//                .id(this.id)
//                .writer(this.member.getNickname())
//                .title(this.title)
//                .contents(this.contents)
//                .createdAt(this.cratedTime)
//                .updatedAt(this.updateTime)//나중에 뺴던지? 일단 주자
//                .expirationDate(this.expirationTime)
//                .totalCapacity(this.totalCapacity)
//                .image(this.image)
//                .build();
//    }

    //생각해보니까 FindBoard에는 리스트만 잇으면 되지않나? 어차피 글 안에 들어가는게 아니잖아
    //ResDto도 필요없을 것 같은데.. 생각해보길 바람.
    public FindBoardListResDto listFromEintity(){
        return FindBoardListResDto.builder()
                .member_id(this.member.getId()) // 작성자 ID 추가
                .id(this.id)
                .writer(this.member.getNickname())
                .title(this.title)
                .contents(this.contents)
                .createdTime(this.createdTime)
                .expirationTime(this.expirationTime)
                .currentCount(this.currentCount)
                .totalCapacity(this.totalCapacity)
                .image(this.image)
                .build();
    }

    public void markAsDeleted() {
        this.delYn = DelYn.N; // delYn을 N으로 변경
    }

    public void incrementCurrentCount() {
        this.currentCount++;
    }

    public void updateFromDto(FindBoardUpdateReqDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.expirationTime = dto.getExpirationDate();
        this.totalCapacity = dto.getTotalCapacity();
        this.image = dto.getImage();
        this.updateTime = LocalDateTime.now();
    }
}