package com.soma.group.repository;

import com.soma.config.QueryDSLConfig;
import com.soma.domain.group.repository.GroupRepository;
import com.soma.domain.group.entity.Group;
import com.soma.group.factory.entity.GroupFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class) // @DataJpaTest는 JPA관련 빈만 등록, @JpaQueryFactory도 빈으로 등록해야함
@DisplayName("GroupRepository JPA 동작 테스트")
class GroupRepositoryTest {
    @Autowired private GroupRepository groupRepository;

    @Test
    @DisplayName("그룹 코드로 그룹을 조회한다.")
    void findByGroupCode() throws Exception {
        //given
        Group group = GroupFactory.createGroup();
        groupRepository.save(group);

        //when
        Group resultGroup = groupRepository.findByCode(group.getCode()).get();

        //then
        Assertions.assertThat(resultGroup.getCode()).isEqualTo(group.getCode());
    }

}