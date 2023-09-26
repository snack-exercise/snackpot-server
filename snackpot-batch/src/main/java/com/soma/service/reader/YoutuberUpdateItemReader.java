package com.soma.service.reader;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.soma.common.constant.Status;
import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.entity.youtuber.repository.YoutuberRepository;
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
public class YoutuberUpdateItemReader implements ItemReader<Youtuber> {

    private final YouTube youTube;
    private final YoutuberRepository youtuberRepository;
    private List<Youtuber> youtubers;
    private int nextYoutuberIndex;

    @Value("${youtube.api-key}")
    private String apiKey;

    @Override
    public Youtuber read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (youtubers == null) {
            youtubers = youtuberRepository.findAllByStatus(Status.ACTIVE);
            nextYoutuberIndex = 0;
        }

        if (nextYoutuberIndex < youtubers.size()) {
            Youtuber nextYoutuber = youtubers.get(nextYoutuberIndex);
            nextYoutuberIndex++;

            try {
                YouTube.Channels.List request = youTube.channels().list("snippet, brandingSettings");
                ChannelListResponse response = request.setId(nextYoutuber.getChannelId())
                        .setKey(apiKey).execute();

                if (response.getItems().size() > 0) {
                    Channel channel = response.getItems().get(0);
                    nextYoutuber.updateProfileImg(channel.getSnippet().getThumbnails().getDefault().getUrl());
                    nextYoutuber.updateName(channel.getSnippet().getTitle());
                    nextYoutuber.updateDescription(channel.getSnippet().getDescription());
                }
                else{
                    nextYoutuber.inActive();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return read();
            }

            return nextYoutuber;
        }else{
            return null;
        }
    }
}
