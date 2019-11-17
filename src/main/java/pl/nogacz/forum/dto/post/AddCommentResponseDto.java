package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCommentResponseDto {
    private Long topicId;
    private Long commentId;
}
