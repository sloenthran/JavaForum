package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdDate;
}
