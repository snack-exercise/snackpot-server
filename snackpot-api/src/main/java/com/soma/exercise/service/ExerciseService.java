package com.soma.exercise.service;

import com.soma.exercise.dto.request.ExerciseReadCondition;
import com.soma.exercise.dto.response.ExerciseResponse;
import com.soma.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public Slice<ExerciseResponse> readAllByCondition(ExerciseReadCondition condition, int size, String email) {
        return exerciseRepository.findAllByCondition(condition, size, email);
    }
}
