package com.sparta.catubebatch.itemwriter;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoBill;
import com.sparta.catubebatch.repository.VideoBillRepository;
import com.sparta.catubebatch.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoBillItemWriter implements ItemWriter<VideoBill> {

    private final VideoBillRepository videoBillRepository;
    private final VideoRepository videoRepository;

    @Override
    public void write(Chunk<? extends VideoBill> chunk) throws Exception {
        // Chunk에서 List<? extends VideoBill>로 가져오기
        List<? extends VideoBill> videoBills = chunk.getItems();

        // List<? extends VideoBill>을 List<VideoBill>로 변환하기
        List<VideoBill> videoBillList = videoBills.stream()
                .map(vb -> (VideoBill) vb)
                .collect(Collectors.toList());

        // VideoBill을 저장
        videoBillRepository.saveAll(videoBillList);

        // 비디오 업데이트
        List<Video> todayBillDone = videoBillList.stream()
                .map(VideoBill::getVideo)
                .collect(Collectors.toList());

        videoRepository.saveAll(todayBillDone);
    }
}
