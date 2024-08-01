package com.sparta.catubebatch.itemwriter;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoBillDoneItemWriter implements ItemWriter<Video> {

    private final VideoRepository videoRepository;

    @Override
    public void write(Chunk<? extends Video> videos) throws Exception {
        // 청크 단위로 비디오 정보 저장
        videoRepository.saveAll(videos);
    }
}

