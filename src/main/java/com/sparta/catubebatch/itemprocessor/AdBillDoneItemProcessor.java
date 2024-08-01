package com.sparta.catubebatch.itemprocessor;

import com.sparta.catubebatch.entity.VideoAd;
import com.sparta.catubebatch.repository.VideoAdRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class AdBillDoneItemProcessor implements ItemProcessor<Map.Entry<Long, Integer>, VideoAd> {

    private final VideoAdRepository videoAdRepository;

    public AdBillDoneItemProcessor(VideoAdRepository videoAdRepository) {
        this.videoAdRepository = videoAdRepository;
    }

    @Override
    public VideoAd process(Map.Entry<Long, Integer> adViewCountEntry) {
        Long videoAdId = adViewCountEntry.getKey();
        Integer viewCount = adViewCountEntry.getValue();

        Optional<VideoAd> optionalVideoAd = videoAdRepository.findById(videoAdId);
        if (optionalVideoAd.isPresent()) {
            VideoAd videoAd = optionalVideoAd.get();
            int newViewCount = videoAd.getAdWatchedCount() + viewCount;
            if (videoAd.isBillOrNot()) {
                videoAd.saveBillOrNot(false);
                videoAd.saveAdWatchedCount(newViewCount);
                return videoAd;
            }
        }
        return null;
    }
}


