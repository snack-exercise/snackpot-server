package com.soma.group.service;

import com.soma.common.constant.Status;
import com.soma.exception.MemberNotFoundException;
import com.soma.group.dto.request.GroupCreateRequest;
import com.soma.group.dto.response.GroupCreateResponse;
import com.soma.group.entity.Group;
import com.soma.group.repository.GroupRepository;
import com.soma.joinlist.entity.JoinList;
import com.soma.joinlist.repository.JoinListRepository;
import com.soma.member.entity.Member;
import com.soma.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final JoinListRepository joinListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GroupCreateResponse create(GroupCreateRequest request, String email) {
        Group group = request.toEntity(LocalDate.now());
        groupRepository.save(group);
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);
        joinListRepository.save(JoinList.createHostJoinList(member, group));
        return GroupCreateResponse.toDto(group);
    }
}
