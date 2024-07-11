package com.sparta.catubebatch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Table(name = "ad_stat")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "video_ad_id")
    private VideoAd videoAd;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    private int dailyViewCount;

    public static AdStat of(VideoAd videoAd, LocalDate today, int dailyViewCount) {
        return AdStat.builder()
                .videoAd(videoAd)
                .createdAt(today)
                .dailyViewCount(dailyViewCount)
                .build();
    }

}
