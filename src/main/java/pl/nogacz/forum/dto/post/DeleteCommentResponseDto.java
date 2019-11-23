package pl.nogacz.forum.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteCommentResponseDto {
    private String message = "Comment deleted";
}
