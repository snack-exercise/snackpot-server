package com.soma.joinList.repository;

import com.soma.group.entity.Group;
import com.soma.joinlist.entity.JoinList;
import com.soma.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinListRepository extends JpaRepository<JoinList, Long>{
    boolean existsByGroupAndMember(Group group, Member member);
}
