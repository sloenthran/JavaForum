package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopTopicLikesDto {
    private String topicName;
    private Long topicId;
    private Long topicLikes;
}
