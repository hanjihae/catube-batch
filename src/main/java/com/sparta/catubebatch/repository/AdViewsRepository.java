package com.sparta.catubebatch.repository;

import com.sparta.catubebatch.entity.AdViews;
import com.sparta.catubebatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AdViewsRepository extends JpaRepository<AdViews, Long> {

}
