package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TopicWithCommentDto {
    private TopicDto topic;
    private List<CommentDto> comments;
}
