package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.nogacz.forum.domain.post.Tag;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TopicDto {
    private Long id;
    private Tag tag;
    private String title;
    private LocalDateTime createdDate;
    private String createdBy;
    private Long commentsCount;
    private Long viewedCount;
    private Long likesCount;
}
