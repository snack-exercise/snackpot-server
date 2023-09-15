package com.soma.group.factory.entity;

import com.soma.domain.group.entity.Group;
import com.soma.group.factory.fixtures.GroupFixtures;

import java.time.LocalDate;

public class GroupFactory {
    public static Group createGroup(){
        return Group.builder()
                .name(GroupFixtures.그룹명)
                .code(GroupFixtures.그룹코드)
                .startDate(LocalDate.now())
                .build();
    }
}
