package com.sparta.catubebatch.itemreader;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.repository.VideoRepository;
import com.sparta.catubebatch.repository.VideoStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoBillDoneItemReader implements ItemReader<Map.Entry<Video, Integer>> {

    private final VideoStatRepository videoStatRepository;
    private final VideoRepository videoRepository;
    private final LocalDate today = LocalDate.now().minusDays(1);
    private Iterator<Map.Entry<Video, Integer>> iterator;

    @Override
    public Map.Entry<Video, Integer> read() throws Exception {
        if (iterator == null) {
            // 오늘 날짜의 비디오 통계 조회
            Map<Long, Integer> videoViewCounts = videoStatRepository.findTodayVideoViewCount(today)
                    .stream()
                    .collect(Collectors.toMap(
                            record -> ((Video) record[0]).getVideoId(),
                            record -> ((Number) record[1]).intValue()
                    ));

            // 모든 비디오 조회
            List<Video> videoList = videoRepository.findAll();
            List<Map.Entry<Video, Integer>> entries = new ArrayList<>();

            for (Video video : videoList) {
                Integer viewCount = videoViewCounts.get(video.getVideoId());
                if (viewCount != null) {
                    entries.add(new AbstractMap.SimpleEntry<>(video, viewCount));
                }
            }

            iterator = entries.iterator();
        }

        return iterator.hasNext() ? iterator.next() : null;
    }
}
