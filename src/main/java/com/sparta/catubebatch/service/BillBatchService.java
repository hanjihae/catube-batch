package com.sparta.catubebatch.service;

import com.sparta.catubebatch.entity.AdBill;
import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.entity.VideoBill;
import com.sparta.catubebatch.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillBatchService {

    private final VideoStatRepository videoStatRepository;
    private final VideoRepository videoRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdStatRepository adStatRepository;
    private final VideoBillRepository videoBillRepository;
    private final AdBillRepository adBillRepository;

    public LocalDate today = LocalDate.now().minusDays(1);

    public void calculateVideoAmount() throws Exception {
        List<VideoBill> vbList = new ArrayList<>();
        List<Video> todayBillDone = new ArrayList<>();

        // 사용자의 비디오 리스트, 그 비디오들의 ID 목록 조회
        List<Video> myVideoList = videoRepository.findAll();
        Set<Long> myVideoIds = myVideoList.stream()
                .map(Video::getVideoId)
                .collect(Collectors.toSet());

        // 오늘 날짜의 비디오 통계 조회
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoViewCount(today);
        for (Object[] vs : dailyStats) {
            double videoAmount = 0.0;
            Video video = (Video) vs[0];
            int viewCount = video.getVideoTotalViews() + ((Number) vs[1]).intValue();
            if (myVideoIds.contains(video.getVideoId())) {
                videoAmount = multiflyVideoPrice(viewCount);
            }
            video.saveBillOrNot(true);
            todayBillDone.add(video);
            VideoBill videoBill = VideoBill.of(video, today, videoAmount);
            vbList.add(videoBill);
        }
        videoRepository.saveAll(todayBillDone);
        videoBillRepository.saveAll(vbList);
    }

    public double multiflyVideoPrice(int viewCount) {
        if (viewCount >= 100000 && viewCount < 500000) { // 조회수 10만 이상, 50만 미만
            return viewCount * 1.1;
        } else if (viewCount >= 500000 && viewCount < 1000000) { // 조회수 50만 이상, 100만 미만
            return viewCount * 1.3;
        } else if (viewCount >= 1000000) { // 조회수 100만 이상
            return viewCount * 1.5;
        } else { // 조회수 10만 이하
            return viewCount;
        }
    }

    public void calculateAdAmount() throws Exception {
        List<AdBill> abList = new ArrayList<>();
        List<VideoAd> todayAdBillDone = new ArrayList<>();
        // 사용자의 동영상 리스트 가져오기
        List<Video> myVideoList = videoRepository.findAll();
        // 오늘 쌓인 videoAdId별 광고 조회수 가져오기
        List<Object[]> dailyAdStats = adStatRepository.findDailyViewCount(today);
        Map<VideoAd, Integer> adViewCountMap = new HashMap<>();
        // Map<VideoAd, 오늘치 광고 조회수>
        for (Object[] vs : dailyAdStats) {
            VideoAd videoAd = (VideoAd) vs[0];
            int viewCount = videoAd.getAdWatchedCount() + ((Number) vs[1]).intValue();
            if (viewCount != 0) {
                adViewCountMap.put(videoAd, viewCount);
            }
        }
        // 내 비디오 하나당
        for (Video video : myVideoList) {
            // 꽂힌 광고 리스트
            List<VideoAd> myAdList = videoAdRepository.findByVideo(video);
            for (VideoAd videoAd : myAdList) {
                double adAmount = 0.0;
                Integer viewCount = adViewCountMap.get(videoAd);
                if (viewCount != null) {
                    if (viewCount >= 100000 && viewCount < 500000) {
                        adAmount += viewCount * 12;
                    } else if (viewCount >= 500000 && viewCount < 1000000) {
                        adAmount += viewCount * 15;
                    } else if (viewCount >= 1000000) {
                        adAmount += viewCount * 20;
                    } else {
                        adAmount += viewCount * 10;
                    }
                }
                videoAd.saveBillOrNot(true);
                todayAdBillDone.add(videoAd);
                AdBill ab = AdBill.of(videoAd, today, adAmount);
                abList.add(ab);
            }
        }
        videoAdRepository.saveAll(todayAdBillDone);
        adBillRepository.saveAll(abList);
    }

    @Transactional
    public void saveTodayVideoBillDone() {
        // 오늘 날짜의 비디오 통계 조회 (비디오 ID별로 총 조회수 합산)
        Map<Long, Integer> videoViewCounts = videoStatRepository.findTodayVideoViewCount(today)
                .stream()
                .collect(Collectors.toMap(
                        record -> ((Video) record[0]).getVideoId(),
                        record -> ((Number) record[1]).intValue()
                ));
        // 모든 비디오 조회
        List<Video> videoList = videoRepository.findAll();
        List<Video> videoToSave = new ArrayList<>();
        // 비디오 정보 업데이트
        for (Video video : videoList) {
            if (videoViewCounts.containsKey(video.getVideoId())) {
                int viewCount = video.getVideoTotalViews() + videoViewCounts.get(video.getVideoId());
                if (video.getBillOrNot()) { // 이미 청구된 경우에만 업데이트
                    video.saveBillOrNot(false);
                    video.saveVideoTotalViews(viewCount);
                    videoToSave.add(video);
                }
            }
        }
        videoRepository.saveAll(videoToSave);
    }

    @Transactional
    public void saveTodayAdBillDone() {
        Map<Long, Integer> adViewCounts = adStatRepository.findDailyViewCount(today)
                .stream()
                .collect(Collectors.toMap(
                        record -> ((VideoAd) record[0]).getVideoAdId(),
                        record -> ((Number) record[1]).intValue()
                ));
        List<VideoAd> videoAdList = videoAdRepository.findAll();
        List<VideoAd> videoAdToSave = new ArrayList<>();
        for (VideoAd videoAd : videoAdList) {
            if (adViewCounts.containsKey(videoAd.getVideoAdId())) {
                int viewCount = videoAd.getAdWatchedCount() + adViewCounts.get(videoAd.getVideoAdId());
                if (videoAd.isBillOrNot()) {
                    videoAd.saveBillOrNot(false);
                    videoAd.saveAdWatchedCount(viewCount);
                    videoAdToSave.add(videoAd);
                }
            }
        }
        videoAdRepository.saveAll(videoAdToSave);
    }
}
