package com.sparta.catubebatch.itemwriter;

import com.sparta.catubebatch.entity.AdStat;
import com.sparta.catubebatch.repository.AdStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdStatItemWriter implements ItemWriter<AdStat> {

    private final AdStatRepository adStatRepository;

    @Override
    public void write(Chunk<? extends AdStat> adStats) throws Exception {
        adStatRepository.saveAll(adStats);
    }
}
