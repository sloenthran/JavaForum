package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.nogacz.forum.domain.post.Tag;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTagsResponseDto {
    private Tag tag;
}
