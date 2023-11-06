package com.soma.domain.member.service;

import com.soma.common.constant.Status;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.member.dto.request.UpdateDailyGoalTimeRequest;
import com.soma.domain.member.dto.response.DailyGoalTime;
import com.soma.domain.member.dto.response.MemberMyInfoResponse;
import com.soma.domain.member.dto.response.MemberTotalNumResponse;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.exception.member.ExceedDailyExerciseGoalException;
import com.soma.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;

    public MemberTotalNumResponse readTotalNum() {
        return MemberTotalNumResponse.toDto(memberRepository.countByStatus(Status.ACTIVE));
    }

    public MemberMyInfoResponse readMyInfo(String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);

        LocalDateTime startDate = getStartLocalDateTimeOfWeek();
        LocalDateTime endDate = getEndLocalDateTimeOfWeek();

        List<ExerciseRecord> records = exerciseRecordRepository.findWeekExerciseTimeByMember(member, startDate, endDate);

        Map<LocalDate, Integer> map = records.stream().collect(Collectors.groupingBy(record -> record.getCreatedAt().toLocalDate(), Collectors.summingInt(ExerciseRecord::getTime)));

        ArrayList<DailyGoalTime> weeklyGoalTime = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate localDate = startDate.plusDays(i).toLocalDate();
            String day = localDate.getDayOfWeek().name().toLowerCase().substring(0, 3);
            Integer time = map.get(localDate);
            weeklyGoalTime.add(DailyGoalTime.toDto(day, time));
        }

        return MemberMyInfoResponse.toDto(member.getName(), member.getProfileImg(), member.getDailyGoalTime(), weeklyGoalTime);
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

    @Transactional
    public void updateDailyGoalTime(UpdateDailyGoalTimeRequest request, String email) {

        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);

        // 하루 운동 목표 시간이 24시간을 초과한다면
        if (request.getDailyGoalTime() > 1440) {
            throw new ExceedDailyExerciseGoalException();
        }

        member.updateDailyGoalTime(request.getDailyGoalTime());
    }

    @Transactional
    public void delete(String email) {
        Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE).orElseThrow(MemberNotFoundException::new);
        member.inActive();
    }
}
