package com.soma.bodypart.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BodyPartType {
    UPPER_BODY("상체"),
    LOWER_BODY("하체"),
    CORE("코어"),
    FULL_BODY("전신"),
    ARMS("팔"),
    LEGS("다리"),
    BACK("등"),
    CHEST("가슴"),
    SHOULDERS("어깨");

    private final String bodyPart;

}
