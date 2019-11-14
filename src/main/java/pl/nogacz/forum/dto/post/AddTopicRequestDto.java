package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddTopicRequestDto {
    private String title;
    private String tag;
    private String text;
}
