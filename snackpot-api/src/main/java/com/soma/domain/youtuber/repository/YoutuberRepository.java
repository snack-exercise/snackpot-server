package com.soma.domain.youtuber.repository;

import com.soma.domain.youtuber.entity.Youtuber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutuberRepository extends JpaRepository<Youtuber, Long> {
}
