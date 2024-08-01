package com.sparta.catubebatch.itemreader;

import com.sparta.catubebatch.dto.AdStatData;
import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.AdViewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdStatItemReader implements ItemReader<AdStatData> {

    private final AdViewsRepository adViewsRepository;
    private final LocalDate today = LocalDate.now().minusDays(1);
    private List<AdStatData> adStatDataList;
    private int currentIndex = 0;

    @Override
    public AdStatData read() throws Exception {
        if (adStatDataList == null) {
            List<Object[]> results = adViewsRepository.countGroupedByVideoIdAndVideoAdId(today);
            adStatDataList = results.stream()
                    .map(result -> new AdStatData(
                            (VideoAd) result[0],
                            ((Number)result[1]).intValue()
                    )).toList();
        }

        if (currentIndex < adStatDataList.size()) {
            return adStatDataList.get(currentIndex++);
        } else {
            return null;
        }
    }
}

