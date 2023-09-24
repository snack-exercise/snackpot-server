package com.soma.domain.group.dto.response;


import com.soma.domain.group.entity.Group;
import com.soma.domain.joinlist.entity.JoinList;
import com.soma.domain.joinlist.entity.JoinType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GroupListResponse {
    private Long groupId;
    private String groupName;
    private LocalDate startDate;
    private String hostName;
    private int groupNumber;
    private List<String> memberProfileImageList;

    public static GroupListResponse toDto(Group group, ArrayList<JoinList> joinLists) {
        String hostName = joinLists.stream().filter(joinList -> joinList.getJoinType().equals(JoinType.HOST)).findFirst().get().getMember().getName();
        List<String> memberProfileImageList = joinLists.stream().map(joinList -> joinList.getMember().getProfileImg()).collect(Collectors.toList());

        return GroupListResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .startDate(group.getStartDate())
                .hostName(hostName)
                .groupNumber(joinLists.size())
                .memberProfileImageList(memberProfileImageList)
                .build();
    }

    @Builder
    public GroupListResponse(Long groupId, String groupName, LocalDate startDate, String hostName, int groupNumber, List<String> memberProfileImageList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.startDate = startDate;
        this.hostName = hostName;
        this.groupNumber = groupNumber;
        this.memberProfileImageList = memberProfileImageList;
    }
}
