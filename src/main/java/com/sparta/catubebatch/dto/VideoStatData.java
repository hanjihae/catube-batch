package com.sparta.catubebatch.dto;

import com.sparta.catubebatch.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VideoStatData {
    private Video video;
    private int viewCount;
    private long playTime;
}

