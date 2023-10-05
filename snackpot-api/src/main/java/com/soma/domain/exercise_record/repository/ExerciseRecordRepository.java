package com.soma.domain.exercise_record.repository;

import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {

    @Query("select er from ExerciseRecord er join fetch er.member " +
            "join JoinList  j on er.member = j.member and j.group.id = :groupId and :startDate <= er.createdAt AND er.createdAt < :endDate " +
            "order by er.createdAt desc")
    List<ExerciseRecord> findWeekExerciseTimeStatics(@Param("groupId") Long groupId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select er from ExerciseRecord er join fetch er.member " +
            "where er.member = :member and :startDate <= er.createdAt and er.createdAt < :endDate " +
            "order by er.createdAt desc")
    List<ExerciseRecord> findWeekExerciseTimeByMember(@Param("member") Member member, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
