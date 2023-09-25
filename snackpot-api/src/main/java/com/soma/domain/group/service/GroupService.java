package com.soma.domain.group.service;

import com.soma.common.constant.Status;
import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.request.GroupJoinRequest;
import com.soma.domain.group.dto.response.GroupListResponse;
import com.soma.domain.group.dto.response.GroupNameResponse;
import com.soma.domain.group.entity.Group;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.joinlist.repository.JoinListRepository;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.exception.group.AlreadyJoinedGroupException;
import com.soma.exception.group.GroupNotFoundException;
import com.soma.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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
    public GroupNameResponse create(GroupCreateRequest request, String email) {
        Group group = request.toEntity(LocalDate.now());
        groupRepository.save(group);
        System.out.println(group.getCode());
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);
        joinListRepository.save(JoinList.createHostJoinList(member, group));
        return GroupNameResponse.toDto(group);
    }

    @Transactional
    public GroupNameResponse join(GroupJoinRequest request, String email) {
        Group group = groupRepository.findByCode(request.getGroupCode()).orElseThrow(GroupNotFoundException::new);
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);
        if(!joinListRepository.existsByGroupAndMember(group, member)){
            joinListRepository.save(JoinList.createMemberJoinList(member, group));
        }else{
            throw new AlreadyJoinedGroupException();
        }

        return GroupNameResponse.toDto(group);
    }

    public Slice<GroupListResponse> readAll(Long groupIdCursor, Integer size, String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);
        if (groupIdCursor == null){
            return groupRepository.findFirstGroupList(member, PageRequest.of(0, size));
        }else{
            return groupRepository.findAllByCursor(member, groupIdCursor, PageRequest.of(0, size));
        }
    }
}
