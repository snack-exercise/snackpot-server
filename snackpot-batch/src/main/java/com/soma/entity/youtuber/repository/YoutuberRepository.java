package com.soma.entity.youtuber.repository;

import com.soma.common.constant.Status;
import com.soma.domain.youtuber.entity.Youtuber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YoutuberRepository extends JpaRepository<Youtuber, Long> {
    List<Youtuber> findAllByStatus(Status status);
}
