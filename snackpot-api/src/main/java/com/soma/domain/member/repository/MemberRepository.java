package com.soma.domain.member.repository;

import com.soma.common.constant.Status;
import com.soma.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndStatus(String email, Status status);

    Boolean existsByName(String name);

    Optional<Member> findByNameAndStatus(String name, Status status);

    Optional<Member> findByIdAndStatus(Long id, Status status);
}
