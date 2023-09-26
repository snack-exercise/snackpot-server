package com.soma.domain.joinlist.repository;

import com.soma.config.QueryDSLConfig;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.domain.exercise.factory.entity.ExerciseFactory;
import com.soma.domain.exercise.repository.ExerciseRepository;
import com.soma.domain.exercise_record.entity.ExerciseRecord;
import com.soma.domain.exercise_record.factory.entity.ExerciseRecordFactory;
import com.soma.domain.exercise_record.repository.ExerciseRecordRepository;
import com.soma.domain.group.entity.Group;
import com.soma.domain.group.factory.entity.GroupFactory;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.joinlist.entity.JoinType;
import com.soma.domain.member.entity.Member;
import com.soma.domain.member.factory.entity.MemberFactory;
import com.soma.domain.member.repository.MemberRepository;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.domain.youtuber.factory.entity.YoutuberFactory;
import com.soma.domain.youtuber.repository.YoutuberRepository;
import com.soma.joinlist.factory.JoinListFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class) // @DataJpaTest는 JPA관련 빈만 등록, @JpaQueryFactory도 빈으로 등록해야함
@DisplayName("JoinListRepository JPA 동작 테스트")
class JoinListRepositoryTest {
    @Autowired private JoinListRepository joinListRepository;
    @Autowired private GroupRepository groupRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ExerciseRepository exerciseRepository;
    @Autowired private ExerciseRecordRepository exerciseRecordRepository;
    @Autowired private YoutuberRepository youtuberRepository;


    @Test
    @DisplayName("회원이 그룹을 가입하지 않으면 false를 반환한다.")
    void existsByGroupAndMemberFalse() throws Exception {
        //given
        Group group = GroupFactory.createGroup();
        groupRepository.save(group);
        Member member = MemberFactory.createUserRoleMember();
        memberRepository.save(member);

        //when
        boolean result = joinListRepository.existsByGroupAndMember(group, member);

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("회원이 그룹을 가입했다면 true를 반환한다.")
    void existsByGroupAndMemberTrue() throws Exception {
        //given
        Group group = GroupFactory.createGroup();
        groupRepository.save(group);
        Member member = MemberFactory.createUserRoleMember();
        memberRepository.save(member);
        joinListRepository.save(
                JoinList.builder()
                .joinType(JoinType.HOST)
                .group(group)
                .member(member)
                .build()
        );

        //when
        boolean result = joinListRepository.existsByGroupAndMember(group, member);

        //then
        Assertions.assertThat(result).isTrue();
    }


    @Test
    @DisplayName("그룹에서 오늘 운동을 수행하지 않은 회원 목록을 조회한다.")
    void findAllAbsenteesByGroupIdTest() throws Exception {
        //given
        Group 그룹 = GroupFactory.createGroup();
        groupRepository.save(그룹);

        Member 회원A = MemberFactory.createUserRoleMemberWithNameAndEmail("회원A", "A@gmail.com");
        Member 회원B = MemberFactory.createUserRoleMemberWithNameAndEmail("회원B", "B@gmail.com");
        Member 회원C = MemberFactory.createUserRoleMemberWithNameAndEmail("회원C", "C@gmail.com");
        Member 회원D = MemberFactory.createUserRoleMemberWithNameAndEmail("회원D", "D@gmail.com");
        memberRepository.saveAll(List.of(회원A, 회원B, 회원C, 회원D));

        joinListRepository.save(JoinListFactory.createHostJoinList(회원A, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원B, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원C, 그룹));
        joinListRepository.save(JoinListFactory.createMemberJoinList(회원D, 그룹));

        Youtuber 유튜버 = YoutuberFactory.createYoutuber();
        youtuberRepository.save(유튜버);

        Exercise 운동 = ExerciseFactory.createExerciseWithYoutuber(유튜버);
        exerciseRepository.save(운동);

        ExerciseRecord 기록1 = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMember(운동, 회원A);
        ExerciseRecord 기록2 = ExerciseRecordFactory.createExerciseRecordWithExerciseAndMember(운동, 회원D);
        exerciseRecordRepository.saveAll(List.of(기록1, 기록2));
        LocalDateTime 하루전날 = LocalDateTime.now().minusDays(1);
        기록2.updateCreatedAt(하루전날);

        //when
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);// 오늘 자정 구일하기
        LocalDateTime nextDay = today.plusDays(1);// 내일 자정 구하기
        List<Member> result = joinListRepository.findAllAbsenteesByGroupId(그룹.getId(), today, nextDay);

        //then
        Assertions.assertThat(result).hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "회원B", "회원C", "회원D"
                );

        Assertions.assertThat(기록2.getCreatedAt()).isEqualTo(하루전날);
    }
}