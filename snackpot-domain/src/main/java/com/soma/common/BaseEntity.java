package com.soma.common;

import com.soma.common.constant.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity{
    @Enumerated(EnumType.STRING)
    private Status status; // 상태 (INACTIVE = 삭제)
    public void inActive() {
        this.status = Status.INACTIVE;
    }
    public void active(){
        this.status = Status.ACTIVE;
    }
}