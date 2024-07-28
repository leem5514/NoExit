package com.E1i3.NoExit.domain.coupon.domain;


import com.E1i3.NoExit.domain.grade.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private Float salePercent;

    private String couponName;

    private LocalDateTime couponStartdate;

    private LocalDateTime couponEnddate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.INACTIVE;

    private String couponImage;

    // Grade와의 N:1 관계 추가
    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade;


}
