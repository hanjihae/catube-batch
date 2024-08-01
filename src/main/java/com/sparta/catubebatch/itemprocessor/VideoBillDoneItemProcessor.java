package com.sparta.catubebatch.itemprocessor;

import com.sparta.catubebatch.entity.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VideoBillDoneItemProcessor implements ItemProcessor<Map.Entry<Video, Integer>, Video> {

    @Override
    public Video process(Map.Entry<Video, Integer> entry) throws Exception {
        Video video = entry.getKey();
        int viewCount = video.getVideoTotalViews() + entry.getValue();

        if (video.getBillOrNot()) { // 이미 청구된 경우에만 업데이트
            video.saveBillOrNot(false);
            video.saveVideoTotalViews(viewCount);
            return video;
        }

        return null; // 청구되지 않은 경우 처리하지 않음
    }
}

