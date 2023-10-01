package com.soma.domain.joinlist.repository;

import com.soma.domain.group.entity.Group;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JoinListRepository extends JpaRepository<JoinList, Long>{
    boolean existsByGroupAndMember(Group group, Member member);

    @Query("select j.member from JoinList j left join ExerciseRecord er on j.member = er.member and :startDateTime <= er.createdAt AND er.createdAt < :endDateTime " +
            "where j.group.id = :groupId and er.id is null")
    List<Member> findAllAbsenteesByGroupId(@Param("groupId") Long groupId, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Query("select j.member from JoinList j where j.group.id = :groupId and j.status = 'ACTIVE'")
    List<Member> findAllMembersByGroupId(@Param("groupId") Long groupId);

    List<JoinList> findAllByGroupId(Long groupId);
}
