package com.sparta.catubebatch.itemreader;

import com.sparta.catubebatch.dto.VideoBillData;
import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.repository.VideoRepository;
import com.sparta.catubebatch.repository.VideoStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoBillItemReader implements ItemReader<VideoBillData> {

    private final VideoStatRepository videoStatRepository;
    private final VideoRepository videoRepository;
    private final LocalDate today = LocalDate.now().minusDays(1);
    private boolean isRead = false;
    private List<VideoBillData> videoBillDataList;
    private int nextIndex;

    @Override
    public VideoBillData read() throws Exception {
        if (isRead) {
            return null;
        }

        // 사용자의 비디오 리스트 및 비디오 ID 목록 조회
        List<Video> myVideoList = videoRepository.findAll();
        Set<Long> myVideoIds = myVideoList.stream()
                .map(Video::getVideoId)
                .collect(Collectors.toSet());

        // 오늘 날짜의 비디오 통계 조회
        List<Object[]> dailyStats = videoStatRepository.findTodayVideoViewCount(today);
        List<VideoBillData> videoBillDataList = dailyStats.stream()
                .filter(vs -> myVideoIds.contains(((Video) vs[0]).getVideoId()))
                .map(vs -> {
                    Video video = (Video) vs[0];
                    int viewCount = ((Number) vs[1]).intValue();
                    return new VideoBillData(video, viewCount);
                })
                .collect(Collectors.toList());

        this.videoBillDataList = videoBillDataList;
        this.nextIndex = 0;
        isRead = true;
        return nextIndex < videoBillDataList.size() ? videoBillDataList.get(nextIndex++) : null;
    }
}
