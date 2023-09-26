package com.soma.domain.group.service;

import com.soma.common.constant.Status;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.request.GroupJoinRequest;
import com.soma.domain.group.dto.response.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final JoinListRepository joinListRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;

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

    public List<GroupAbsenteeResponse> readAllAbsentees(Long groupId) {
        if(!groupRepository.existsById(groupId)){
            throw new GroupNotFoundException();
        }
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);// 오늘 자정 구일하기
        LocalDateTime nextDay = today.plusDays(1);// 내일 자정 구하기
        return joinListRepository.findAllAbsenteesByGroupId(groupId, today, nextDay).stream().map(GroupAbsenteeResponse::toDto).toList();
    }

    public Object readExerciseTimeStatics(Long groupId) {
        if(!groupRepository.existsById(groupId)){
            throw new GroupNotFoundException();
        }
        LocalDateTime startDate = getStartLocalDateTimeOfWeek();
        LocalDateTime endDate = getEndLocalDateTimeOfWeek();
        List<ExerciseRecord> result = exerciseRecordRepository.findWeekExerciseTimeStatics(groupId, startDate, endDate);
        List<Member> members = joinListRepository.findAllMembersByGroupId(groupId);

        Map<LocalDate, Map<Member, UserTimeStaticsResponse>> map = new HashMap<>();
        for(int i=0; i<7; i++){
            LocalDate localDate = startDate.plusDays(i).toLocalDate();
            map.put(localDate,
                    members.stream().collect(Collectors.toMap(member -> member, member -> UserTimeStaticsResponse.toDto(member, 0))));
        }

        for (ExerciseRecord exerciseRecord : result) {
            map.get(exerciseRecord.getCreatedAt().toLocalDate()).get(exerciseRecord.getMember()).add(exerciseRecord.getTime());
        }

        List<GroupTimeStaticsResponse> response = new ArrayList<>();
        for(int i=0; i<7; i++) {
            LocalDate localDate = startDate.plusDays(i).toLocalDate();
            response.add(GroupTimeStaticsResponse.toDto(localDate, map.get(localDate).values().stream().toList()));
        }

        return response;
    }

    private LocalDateTime getStartLocalDateTimeOfWeek(){
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);// 오늘 자정
        int dayOfWeek = today.getDayOfWeek().getValue(); // 오늘 요일(숫자), 월(1), 일(7)
        return today.minusDays(dayOfWeek-1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime getEndLocalDateTimeOfWeek(){
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);// 오늘 자정
        int dayOfWeek = today.getDayOfWeek().getValue(); // 오늘 요일(숫자), 월(1), 일(7)
        return today.plusDays(8-dayOfWeek).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
}
