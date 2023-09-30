package com.soma.domain.review.repository;

import com.soma.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r join fetch r.member " +
            "where r.exercise.id = :exerciseId and r.createdAt < (select r2.createdAt from Review r2 where r2.id = :cursorId) " +
            "or (r.id < :cursorId and r.createdAt = (select r2.createdAt from Review r2 where r2.id = :cursorId)) " +
            "order by r.createdAt desc, r.id desc ")
    Slice<Review> getReviewSliceByCursorId(@Param("exerciseId") Long exerciseId, @Param("cursorId") Long cursorId, Pageable pageable);

    @Query("select r from Review r join fetch r.member " +
            "where r.exercise.id = :exerciseId " +
            "order by r.createdAt desc, r.id desc ")
    Slice<Review> getFirstReviewSlice(@Param("exerciseId") Long exerciseId, Pageable pageable);
}
