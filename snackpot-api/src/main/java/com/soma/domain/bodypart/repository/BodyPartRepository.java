package com.soma.domain.bodypart.repository;

import com.soma.common.constant.Status;
import com.soma.domain.bodypart.entity.BodyPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {
    List<BodyPart> findAllByExerciseIdAndStatus(Long exerciseId, Status status);
}
