package com.E1i3.NoExit.domain.grade.domain;

import com.E1i3.NoExit.domain.coupon.domain.Coupon;
import com.E1i3.NoExit.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gradeName;

    // Coupon과의 1:N 관계 추가
    @OneToMany(mappedBy = "grade")
    private List<Coupon> coupons;

    // Member와의 1:1 관계 추가
    @OneToOne(mappedBy = "grade")
    private Member member;
}
