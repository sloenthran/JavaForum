package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCommentResponseDto {
    private Long topicId;
    private Long commentId;
}
