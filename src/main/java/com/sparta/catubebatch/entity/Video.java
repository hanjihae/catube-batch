package com.sparta.catubebatch.entity;

import com.sparta.catubebatch.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "video")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Video extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(nullable = false)
    private String videoTitle;

    private String videoDescription;

    @Column(nullable = false)
    private String videoUrl;

    private int videoTotalViews;
    private long videoTotalPlaytime;
    private long videoLength;

    @Column(nullable = false)
    private Boolean videoDeleteCheck;

    @Column(nullable = false)
    private Boolean videoPublicCheck;

    private Boolean billOrNot;

    @OneToMany(mappedBy = "video")
    private List<VideoBill> videoBills;

    @OneToMany(mappedBy = "video")
    private List<VideoStat> videoStats;

    public void saveVideoTotalViews(int videoTotalViews) {
        this.videoTotalViews = videoTotalViews;
    }

    public void saveVideoTotalPlaytime(long videoTotalPlaytime) {
        this.videoTotalPlaytime = videoTotalPlaytime;
    }

    public void saveBillOrNot(boolean billOrNot) {
        this.billOrNot = billOrNot;
    }

}
