package com.sparta.catubebatch.itemreader;

import com.sparta.catubebatch.dto.VideoStatData;
import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.repository.ViewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoStatItemReader implements ItemReader<VideoStatData> {

    private final ViewsRepository viewsRepository;
    private final LocalDate today = LocalDate.now().minusDays(1);
    private List<VideoStatData> videoStatDataList;
    private int currentIndex = 0;

    @Override
    public VideoStatData read() throws Exception {
        if (videoStatDataList == null) {
            List<Object[]> results = viewsRepository.countViewsByVideoExcludingUserGroupByVideo(today);

            videoStatDataList = results.stream()
                    .map(result -> new VideoStatData(
                            (Video) result[0],           // video
                            ((Number) result[1]).intValue(), // viewCount
                            (Long) result[2]            // playTime
                    ))
                    .toList();
        }
        if (currentIndex < videoStatDataList.size()) {
            return videoStatDataList.get(currentIndex++);
        } else {
            return null;
        }
    }
}
