package com.sparta.catubebatch.itemwriter;
import com.sparta.catubebatch.entity.AdBill;
import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.AdBillRepository;
import com.sparta.catubebatch.repository.VideoAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class AdBillItemWriter implements ItemWriter<AdBill> {

    private final AdBillRepository adBillRepository;
    private final VideoAdRepository videoAdRepository;

    @Override
    public void write(Chunk<? extends AdBill> chunk) throws Exception {
        // AdBill 저장
        adBillRepository.saveAll(chunk.getItems());

        // 광고 상태 업데이트
        List<VideoAd> videoAds = chunk.getItems().stream()
                .map(AdBill::getVideoAd)
                .distinct()
                .collect(Collectors.toList());

        videoAdRepository.saveAll(videoAds);
    }

}



