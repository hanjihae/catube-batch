package com.sparta.catubebatch.repository;

import com.sparta.catubebatch.entity.VideoStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VideoStatRepository extends JpaRepository<VideoStat, Long> {

    @Query("SELECT v.video, SUM(v.dailyViewCount) cnt FROM VideoStat v " +
            "WHERE v.createdAt = :today " +
            "GROUP BY v.video")
    List<Object[]> findTodayVideoViewCount(@Param("today")LocalDate today);
}
