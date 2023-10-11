package com.soma.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.soma.domain.exercise.entity.Exercise;
import com.soma.entity.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class SchedulerService {
    private final YouTube youTube;
    private final ExerciseRepository exerciseRepository;

    @Value("${youtube.api-key}")
    private String apiKey;


    @Scheduled(cron = "0 0 2 * * ?")
    public void updateExerciseInfo() {


        // Update Exercise info from external API or any other source
        for(Exercise exercise : exerciseRepository.findAll()) {
            try {
                YouTube.Videos.List request = youTube.videos().list("snippet, contentDetails");
                VideoListResponse response = request.setId(exercise.getVideoId())
                        .setKey(apiKey).execute();

                if (response.getItems().size() > 0) {
                    Video video = response.getItems().get(0);
                    exercise.updateTitle(video.getSnippet().getTitle());
                    exercise.updateThumbnail(video.getSnippet().getThumbnails().getDefault().getUrl());
                    exerciseRepository.save(exercise);
                } else {
                    exercise.inActive();
                    exerciseRepository.save(exercise);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
