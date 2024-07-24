package com.E1i3.NoExit.domain.findboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    // FK 설정하기 , 이거 나중에 하자 CRUD 확인하고
//    @ManyToOne(fetch = FetchType.LAZY) //참조 안 하면 안 나가게.
//    @JoinColumn(name = "member_id")
    //member_id fk쪽에 oneToMany 설정이랑 해야하는거 잊지마셈.
    private Long memberId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 만료 시간 정보 저장
    private LocalDateTime expirationDate;

    private int currentCount;
    private int totalCapacity;
    private int participantCount;

    @Lob
    @Column(name = "image", nullable = true)
    private byte[] image;

//    @Column(nullable = true, columnDefinition = "varchar(1) default 'Y'")
    private String view;

    public void incrementCurrentCount() {
        this.currentCount++;
    }

}
