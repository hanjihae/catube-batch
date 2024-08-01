package com.sparta.catubebatch.itemprocessor;

import com.sparta.catubebatch.dto.VideoBillData;
import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoBill;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VideoBillItemProcessor implements ItemProcessor<VideoBillData, VideoBill> {

    private final LocalDate today = LocalDate.now().minusDays(1);

    @Override
    public VideoBill process(VideoBillData data) throws Exception {
        Video video = data.getVideo();
        int viewCount = video.getVideoTotalViews() + data.getViewCount();

        // 비디오 청구 금액 계산
        double videoAmount = calculateVideoAmount(viewCount);

        // 비디오 청구 상태 업데이트
        video.saveBillOrNot(true);

        return VideoBill.of(video, today, videoAmount);
    }

    private double calculateVideoAmount(int viewCount) {
        if (viewCount >= 100000 && viewCount < 500000) {
            return viewCount * 1.1;
        } else if (viewCount >= 500000 && viewCount < 1000000) {
            return viewCount * 1.3;
        } else if (viewCount >= 1000000) {
            return viewCount * 1.5;
        } else {
            return viewCount;
        }
    }
}

