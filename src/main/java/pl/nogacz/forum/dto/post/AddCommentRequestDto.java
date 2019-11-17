package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCommentRequestDto {
    private Long topicId;
    private String text;
}