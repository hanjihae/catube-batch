package com.sparta.catubebatch.itemprocessor;

import com.sparta.catubebatch.dto.AdStatData;
import com.sparta.catubebatch.entity.AdStat;
import com.sparta.catubebatch.entity.VideoAd;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdStatItemProcessor implements ItemProcessor<AdStatData, AdStat> {

    private final LocalDate today = LocalDate.now().minusDays(1);

    @Override
    public AdStat process(AdStatData data) throws Exception {
        VideoAd videoAd = data.getVideoAd();
        int dailyAdCount = data.getCount();

        // AdStat 객체 생성
        return AdStat.of(videoAd, today, dailyAdCount);
    }
}

