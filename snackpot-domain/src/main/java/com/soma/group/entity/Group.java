package com.soma.group.entity;

import com.soma.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Exgroup")
@Entity
public class Group extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startDate; // 시작 기간

    private String code; // 그룹 입장 코드

    @Builder
    public Group(String name, LocalDate startDate, String code) {
        this.name = name;
        this.startDate = startDate;
        this.code = code;
        active();
    }


}
