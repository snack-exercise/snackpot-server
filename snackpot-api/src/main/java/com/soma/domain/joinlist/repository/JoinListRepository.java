package com.soma.domain.joinlist.repository;

import com.soma.domain.group.entity.Group;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinListRepository extends JpaRepository<JoinList, Long>{
    boolean existsByGroupAndMember(Group group, Member member);
}
