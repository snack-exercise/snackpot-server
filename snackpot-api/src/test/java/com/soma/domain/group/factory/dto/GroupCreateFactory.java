package com.soma.domain.group.factory.dto;

import com.soma.domain.group.dto.request.GroupCreateRequest;
import com.soma.domain.group.dto.request.GroupJoinRequest;
import com.soma.domain.group.factory.fixtures.GroupFixtures;

public class GroupCreateFactory {
    public static GroupCreateRequest createGroupCreateRequest(){
        return new GroupCreateRequest(GroupFixtures.그룹명);
    }

    public static GroupJoinRequest createGroupJoinRequest(){
        return new GroupJoinRequest(GroupFixtures.그룹코드);
    }
}
