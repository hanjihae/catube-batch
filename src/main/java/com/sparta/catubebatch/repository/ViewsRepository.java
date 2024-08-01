package com.sparta.catubebatch.repository;

import com.sparta.catubebatch.dto.VideoStatData;
import com.sparta.catubebatch.entity.Views;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ViewsRepository extends JpaRepository<Views, Long> {

//    @Query("SELECT v.video, COUNT(v), SUM(v.viewsPlayTime) FROM Views v WHERE v.user <> :user AND date_format(v.createdAt, '%Y%m%d') = :today GROUP BY v.video")
//    List<Object[]> countViewsByVideoExcludingUserGroupByVideo(@Param("user") User user, @Param("today") LocalDate today);
    @Query("SELECT v.video, COUNT(v), SUM(v.viewsPlayTime) FROM Views v " +
            "WHERE FUNCTION('DATE_FORMAT', v.createdAt, '%Y%m%d') = FUNCTION('DATE_FORMAT', :today, '%Y%m%d') GROUP BY v.video")
    List<Object[]> countViewsByVideoExcludingUserGroupByVideo(@Param("today") LocalDate today);

}
