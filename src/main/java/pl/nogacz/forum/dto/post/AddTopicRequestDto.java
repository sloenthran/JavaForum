package pl.nogacz.forum.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddTopicRequestDto {
    private String title;
    private String tag;
    private String text;
}
