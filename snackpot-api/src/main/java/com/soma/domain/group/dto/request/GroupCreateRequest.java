package com.soma.domain.group.dto.request;

import com.soma.domain.group.entity.Group;
import com.soma.domain.group.entity.GroupCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GroupCreateRequest {
    @NotBlank(message = "그룹 이름은 필수입니다.")
    private String groupName;


    public Group toEntity(LocalDate startDate) {
        return Group.builder()
                .name(groupName)
                .code(GroupCode.create6Number())
                .startDate(startDate)
                .build();
    }
}
