package com.sparta.catubebatch.itemprocessor;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoStat;
import com.sparta.catubebatch.dto.VideoStatData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VideoStatItemProcessor implements ItemProcessor<VideoStatData, VideoStat> {

    private final LocalDate today = LocalDate.now().minusDays(1);

    @Override
    public VideoStat process(VideoStatData data) throws Exception {
        Video video = data.getVideo();
        int viewCount = data.getViewCount();
        long playTime = data.getPlayTime();

        // VideoStat 객체 생성
        VideoStat videoStat = VideoStat.of(video, today, viewCount, playTime);

        // Video의 총 재생 시간 업데이트
        video.saveVideoTotalPlaytime(video.getVideoTotalPlaytime() + playTime);

        return videoStat;
    }
}

