package com.soma.bodypart.repository;

import com.soma.bodypart.entity.BodyPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {
}
