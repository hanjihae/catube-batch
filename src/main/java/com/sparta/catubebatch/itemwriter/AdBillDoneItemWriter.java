package com.sparta.catubebatch.itemwriter;

import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.VideoAdRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class AdBillDoneItemWriter implements ItemWriter<VideoAd> {

    private final VideoAdRepository videoAdRepository;

    public AdBillDoneItemWriter(VideoAdRepository videoAdRepository) {
        this.videoAdRepository = videoAdRepository;
    }

    @Override
    public void write(Chunk<? extends VideoAd> videoAds) {
        videoAdRepository.saveAll(videoAds);
    }
}
