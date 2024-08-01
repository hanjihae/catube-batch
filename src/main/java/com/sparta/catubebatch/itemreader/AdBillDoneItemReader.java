package com.sparta.catubebatch.itemreader;

import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.AdStatRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AdBillDoneItemReader implements ItemReader<Map.Entry<Long, Integer>> {

    private final AdStatRepository adStatRepository;
    private final LocalDate today;
    private List<Map.Entry<Long, Integer>> adViewCounts;
    private int nextIndex;

    public AdBillDoneItemReader(AdStatRepository adStatRepository) {
        this.adStatRepository = adStatRepository;
        this.today = LocalDate.now().minusDays(1); // 어제 날짜를 설정
        this.adViewCounts = adStatRepository.findDailyViewCount(today)
                .stream()
                .collect(Collectors.toMap(
                        record -> ((VideoAd) record[0]).getVideoAdId(),
                        record -> ((Number) record[1]).intValue(),
                        Integer::sum
                )).entrySet()
                .stream()
                .collect(Collectors.toList());
        this.nextIndex = 0;
    }

    @Override
    public Map.Entry<Long, Integer> read() {
        if (nextIndex < adViewCounts.size()) {
            return adViewCounts.get(nextIndex++);
        } else {
            return null; // End of data
        }
    }
}

