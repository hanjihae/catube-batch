package com.sparta.catubebatch.dto;

import com.sparta.catubebatch.entity.VideoAd;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdStatData {
    private VideoAd videoAd;  // 쿼리에서 가져온 VideoAd 객체
    private int count;       // COUNT(adView) 값
}