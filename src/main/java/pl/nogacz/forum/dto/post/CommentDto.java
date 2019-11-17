package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdDate;
}
