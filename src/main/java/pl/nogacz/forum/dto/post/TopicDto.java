package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.nogacz.forum.domain.post.Tag;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
