package com.soma.service.reader;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.soma.common.constant.Status;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.entity.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ExerciseUpdateItemReader implements ItemReader<Exercise> {

    private final YouTube youTube;
    private final ExerciseRepository exerciseRepository;
    private List<Exercise> exercises;
    private int nextExerciseIndex;

    @Value("${youtube.api-key}")
    private String apiKey;


    @Override
    public Exercise read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (exercises == null) {
            exercises = exerciseRepository.findAllByStatus(Status.ACTIVE);
            nextExerciseIndex = 0;
        }

        if (nextExerciseIndex < exercises.size()) {
            Exercise nextExercise = exercises.get(nextExerciseIndex);
            nextExerciseIndex++;

            try {
                YouTube.Videos.List request = youTube.videos().list("snippet, contentDetails");
                VideoListResponse response = request.setId(nextExercise.getVideoId())
                        .setKey(apiKey).execute();

                if(response.getItems().size() > 0){
                    Video video = response.getItems().get(0);
                    nextExercise.updateTitle(video.getSnippet().getTitle());
                    nextExercise.updateThumbnail(video.getSnippet().getThumbnails().getDefault().getUrl());
                    System.out.println(nextExercise.getTitle());
                    System.out.println(nextExercise.getThumbnail());
                }
                else{ // 만약 videoId로 영상을 못찾을 경우, 엔티티 inActive()처리
                    nextExercise.inActive();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return read();
            }

            return nextExercise;
        } else{
            return null;
        }
    }
}
