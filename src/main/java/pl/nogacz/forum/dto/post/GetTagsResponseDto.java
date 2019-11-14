package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.nogacz.forum.domain.post.Tag;

@Data
@AllArgsConstructor
public class GetTagsResponseDto {
    private Tag tag;
}
