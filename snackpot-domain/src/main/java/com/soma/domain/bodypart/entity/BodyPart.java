package com.soma.domain.bodypart.entity;

import com.soma.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BodyPart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BodyPartType bodyPartType;

    @Builder
    public BodyPart(BodyPartType bodyPartType) {
        this.bodyPartType = bodyPartType;
    }
}
