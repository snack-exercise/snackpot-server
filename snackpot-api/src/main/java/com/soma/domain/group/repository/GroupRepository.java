package com.soma.domain.group.repository;

import com.soma.common.constant.Status;
import com.soma.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> , GroupRepositoryCustom{
    Optional<Group> findByCode(String groupCode);

    boolean existsByCodeAndStatus(String code, Status status);


}
