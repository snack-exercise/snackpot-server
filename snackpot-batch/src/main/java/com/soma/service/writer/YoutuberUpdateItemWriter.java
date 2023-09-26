package com.soma.service.writer;

import com.soma.domain.youtuber.entity.Youtuber;
import com.soma.entity.youtuber.repository.YoutuberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class YoutuberUpdateItemWriter implements ItemWriter<Object> {
    private final YoutuberRepository youtuberRepository;

    @Override
    public void write(Chunk<?> chunk) throws Exception {
        for (Object youtuber : chunk.getItems()) {
            youtuberRepository.save((Youtuber) youtuber);
        }
    }
}
