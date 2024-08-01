package com.sparta.catubebatch.itemreader;

import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.AdStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdBillItemReader implements ItemReader<Map.Entry<VideoAd, Integer>> {

    private final AdStatRepository adStatRepository;
    private final LocalDate today = LocalDate.now().minusDays(1);
    private Iterator<Map.Entry<VideoAd, Integer>> iterator;

    @Override
    public Map.Entry<VideoAd, Integer> read() throws Exception {
        if (iterator == null) {
            // 오늘 날짜의 광고 통계 조회
            List<Object[]> dailyAdStats = adStatRepository.findDailyViewCount(today);
            Map<VideoAd, Integer> adViewCountMap = new HashMap<>();

            // 만약 videoAd가 동일하다면 viewCount끼리 더해주는 코드 추가
            for (Object[] vs : dailyAdStats) {
                VideoAd videoAd = (VideoAd) vs[0];
                int viewCount = videoAd.getAdWatchedCount() + ((Number) vs[1]).intValue();
                if (viewCount != 0) {
                    adViewCountMap.put(videoAd, viewCount);
                }
            }
            iterator = adViewCountMap.entrySet().iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}

