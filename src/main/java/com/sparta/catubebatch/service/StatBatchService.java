package com.sparta.catubebatch.service;

import com.sparta.catubebatch.entity.AdStat;
import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.entity.VideoStat;
import com.sparta.catubebatch.repository.*;
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
    private final AdViewsRepository adViewsRepository;
    private final AdStatRepository adStatRepository;

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

    public void saveAdViewCount() {
        LocalDate today = LocalDate.now().minusDays(1);
        List<Object[]> dailyAdList = adViewsRepository.countGroupedByVideoIdAndVideoAdId(today);
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
