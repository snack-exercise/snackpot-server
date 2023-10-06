package com.soma.domain.body_part.factory.entity;

import com.soma.domain.bodypart.entity.BodyPart;
import com.soma.domain.bodypart.entity.BodyPartType;

public class BodyPartFactory {
    public static BodyPart createBodyPart(BodyPartType bodyPartType) {
        return BodyPart.builder()
                .bodyPartType(bodyPartType).build();
    }
}
