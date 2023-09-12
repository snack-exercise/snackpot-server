package com.soma.youtuber.repository;

import com.soma.youtuber.entity.Youtuber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutuberRepository extends JpaRepository<Youtuber, Long> {
}
