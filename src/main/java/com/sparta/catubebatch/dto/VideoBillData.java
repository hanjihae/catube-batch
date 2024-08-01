package com.sparta.catubebatch.dto;

import com.sparta.catubebatch.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoBillData {
    private Video video;
    private int viewCount;
}
