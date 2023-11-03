package com.soma.domain.group.dto.request;

import com.soma.domain.group.entity.Group;
import com.soma.domain.group.entity.GroupCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GroupCreateRequest {
    @NotBlank(message = "그룹 이름은 필수입니다.")
    @Size(min = 1, max = 6, message = "그룹 이름은 1자 이상 6자 이하로 입력해주세요.")
    private String groupName;

    public Group toEntity(LocalDate startDate, String groupCode) {
        return Group.builder()
                .name(groupName)
                .code(groupCode)
                .startDate(startDate)
                .build();
    }
}
