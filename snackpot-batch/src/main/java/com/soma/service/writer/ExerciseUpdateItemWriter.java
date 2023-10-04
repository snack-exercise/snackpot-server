package com.soma.service.writer;

import com.soma.domain.exercise.entity.Exercise;
import com.soma.entity.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExerciseUpdateItemWriter implements ItemWriter<Object> {

    private final ExerciseRepository exerciseRepository;

    @Override
    public void write(Chunk<?> chunk) throws Exception {
        for (Object exercise : chunk.getItems()) {
            exerciseRepository.save((Exercise) exercise);
        }
    }
}
