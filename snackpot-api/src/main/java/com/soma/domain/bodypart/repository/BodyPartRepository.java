package com.soma.domain.bodypart.repository;

import com.soma.domain.bodypart.entity.BodyPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {

}

