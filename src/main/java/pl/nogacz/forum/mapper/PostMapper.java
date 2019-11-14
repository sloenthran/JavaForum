package pl.nogacz.forum.mapper;

import org.springframework.stereotype.Component;
import pl.nogacz.forum.domain.post.Topic;
import pl.nogacz.forum.dto.post.TopicDto;

import java.time.LocalDateTime;

@Component
public class PostMapper {
    public TopicDto mapTopicToTopicDto(final Topic topic, LocalDateTime createdDate, String createdBy, Long commentsCount) {
        return new TopicDto(
                topic.getId(),
                topic.getTag(),
                topic.getTitle(),
                createdDate,
                createdBy,
                commentsCount
        );
    }
}
