package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TopicWithCommentDto {
    private TopicDto topic;
    private List<CommentDto> comments;
}
