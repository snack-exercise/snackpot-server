package com.soma.group.factory.dto;

import com.soma.group.dto.request.GroupCreateRequest;

public class GroupCreateFactory {
    public static GroupCreateRequest createGroupCreateRequest(){
        return new GroupCreateRequest("테스트 그룹");
    }
}
