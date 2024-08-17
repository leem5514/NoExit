package com.E1i3.NoExit.domain.wishlist.dto;

import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.wishlist.domain.WishList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishReqDto {
    private Long gameId;

       public WishList toEntity(Member member) {

        WishList wishList = WishList.builder()
                .member(member)
                .gameId(this.gameId)
                .build();

        return wishList;
    }
}
