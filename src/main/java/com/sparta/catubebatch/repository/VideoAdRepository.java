package com.sparta.catubebatch.repository;

import com.sparta.catubebatch.entity.Video;
import com.sparta.catubebatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoAdRepository extends JpaRepository<VideoAd, Long> {
    List<VideoAd> findByVideo(Video video);
}
