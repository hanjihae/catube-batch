package com.sparta.catubebatch.itemwriter;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoStat;
import com.sparta.catubebatch.repository.VideoRepository;
import com.sparta.catubebatch.repository.VideoStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoStatItemWriter implements ItemWriter<VideoStat> {

    private final VideoStatRepository videoStatRepository;
    private final VideoRepository videoRepository;

    @Override
    public void write(Chunk<? extends VideoStat> videoStats) throws Exception {
        videoStatRepository.saveAll(videoStats);

        // Video 업데이트
        for (VideoStat videoStat : videoStats) {
            Video video = videoStat.getVideo();
            videoRepository.save(video);
        }
    }
}

