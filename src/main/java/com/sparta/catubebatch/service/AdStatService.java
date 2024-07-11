package com.sparta.catubebatch.service;

import com.sparta.catubebatch.entity.AdStat;
import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.AdStatRepository;
import com.sparta.catubebatch.repository.VideoAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdStatService {

    private final VideoAdRepository videoAdRepository;
    private final AdStatRepository adStatRepository;

    public void saveAdViewCount() {
        LocalDate today = LocalDate.now().minusDays(1);
        List<Object[]> dailyAdList = videoAdRepository.countGroupedByVideoIdAndVideoAdId(today);
        List<AdStat> adStatList = new ArrayList<>();
        for (Object[] dailyAd : dailyAdList) {
            VideoAd videoAd = (VideoAd) dailyAd[0];
            int dailyAdCount = ((Long) dailyAd[1]).intValue();
            AdStat adStat = AdStat.of(videoAd, today, dailyAdCount);
            adStatList.add(adStat);
        }
        adStatRepository.saveAll(adStatList);
    }

}
