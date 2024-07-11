package com.sparta.catubebatch.service;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoStat;
import com.sparta.catubebatch.repository.VideoRepository;
import com.sparta.catubebatch.repository.VideoStatRepository;
import com.sparta.catubebatch.repository.ViewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatBatchService {

    public final VideoRepository videoRepository;
    public final ViewsRepository viewsRepository;
    public final VideoStatRepository videoStatRepository;

    public LocalDate today = LocalDate.now().minusDays(1);

    // 1일치 조회수, 재생시간 저장
    public void saveDailyStat() throws Exception {
        List<Object[]> dailyStats = viewsRepository.countViewsByVideoExcludingUserGroupByVideo(today);
        List<VideoStat> vsToSave = new ArrayList<>();
        List<Video> videoToSave = new ArrayList<>();
        for (Object[] vs : dailyStats) {
            Video video = (Video)vs[0];
            int viewCount = ((Number)vs[1]).intValue();
            long playTime = (long)vs[2];
            VideoStat videoStat = VideoStat.of(video, today, viewCount, playTime);
            vsToSave.add(videoStat);
            video.saveVideoTotalPlaytime(video.getVideoTotalPlaytime() + playTime);
            videoToSave.add(video);
        }
        videoStatRepository.saveAll(vsToSave);
        videoRepository.saveAll(videoToSave);
    }
}
