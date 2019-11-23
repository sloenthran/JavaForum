package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.nogacz.forum.domain.post.Tag;

@Getter
@AllArgsConstructor
public class GetTagsResponseDto {
    private Tag tag;
}
