package com.soma.domain.group.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GroupJoinRequest {
    @Pattern(regexp = "^[0-9]{6}$", message = "그룹 코드는 0~9까지의 숫자 6자리여야 합니다.")
    private String groupCode;
}
