package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MostLikedTopicDto {
    private String topicName;
    private Long topicId;
    private Long topicLikes;
}
