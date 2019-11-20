package pl.nogacz.forum.mapper;

import org.springframework.stereotype.Component;
import pl.nogacz.forum.domain.post.Comment;
import pl.nogacz.forum.domain.post.Topic;
import pl.nogacz.forum.dto.post.CommentDto;
import pl.nogacz.forum.dto.post.MostLikedTopicDto;
import pl.nogacz.forum.dto.post.TopicDto;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {
    public TopicDto mapTopicToTopicDto(final Topic topic) {
        return new TopicDto(
                topic.getId(),
                topic.getTag(),
                topic.getTitle(),
                topic.getComments().get(0).getCreatedDate(),
                topic.getComments().get(0).getUser().getUsername(),
                (long) topic.getComments().size() - 1,
                topic.getViewedCount(),
                (long) topic.getLikes().size()
        );
    }

    public List<TopicDto> mapListTopicToListTopicDto(final List<Topic> topics) {
        return topics.stream()
                .map(this::mapTopicToTopicDto)
                .collect(Collectors.toList());
    }

    public CommentDto mapCommentToCommentDto(final Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getUser().getUsername(),
                comment.getCreatedDate()
        );
    }

    public List<CommentDto> mapListCommentToListCommentDto(final List<Comment> comments) {
        return comments.stream()
                .map(this::mapCommentToCommentDto)
                .collect(Collectors.toList());
    }

    public MostLikedTopicDto mapObjectToMostLikedTopicDto(final Object[] object, final String topicName) {
        BigInteger count = (BigInteger) object[0];
        BigInteger topicId = (BigInteger) object[1];

        return new MostLikedTopicDto(
                topicName,
                topicId.longValue(),
                count.longValue()
        );
    }
}
