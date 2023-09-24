package com.soma.domain.group.repository;

import com.soma.domain.group.dto.response.GroupListResponse;
import com.soma.domain.member.entity.Member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


import java.time.LocalDate;

public interface GroupRepositoryCustom {
    Slice<GroupListResponse> findAllByCursor(Member member, Long groupIdCursor, LocalDate startDateCursor, Pageable pageable);
    Slice<GroupListResponse> findFirstGroupList(Member member, Pageable pageable);
}
