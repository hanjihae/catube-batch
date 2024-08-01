package com.sparta.catubebatch.itemprocessor;

import com.sparta.catubebatch.entity.AdBill;
import com.sparta.catubebatch.entity.VideoAd;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdBillItemProcessor implements ItemProcessor<Map.Entry<VideoAd, Integer>, AdBill> {

    private final LocalDate today = LocalDate.now().minusDays(1);

    @Override
    public AdBill process(Map.Entry<VideoAd, Integer> entry) throws Exception {
        VideoAd videoAd = entry.getKey();
        Integer viewCount = entry.getValue();
        double adAmount = calculateAdAmount(viewCount);

        videoAd.saveBillOrNot(true); // 청구 상태 업데이트

        // AdBill 객체 생성
        return AdBill.of(videoAd, today, adAmount);
    }

    private double calculateAdAmount(int viewCount) {
        if (viewCount >= 100000 && viewCount < 500000) {
            return viewCount * 12;
        } else if (viewCount >= 500000 && viewCount < 1000000) {
            return viewCount * 15;
        } else if (viewCount >= 1000000) {
            return viewCount * 20;
        } else {
            return viewCount * 10;
        }
    }
}

