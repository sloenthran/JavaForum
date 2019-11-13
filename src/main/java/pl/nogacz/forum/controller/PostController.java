package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.dto.post.PostAddTopicRequestDto;
import pl.nogacz.forum.dto.post.PostAddTopicResponseDto;
import pl.nogacz.forum.exception.post.TagNotFoundException;
import pl.nogacz.forum.service.post.PostService;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(
        value = "/post/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostController {
    private PostService postService;

    @PostMapping("topic")
    private PostAddTopicResponseDto addTopic(@Autowired Authentication authentication, @RequestBody PostAddTopicRequestDto postAddTopicDto) throws TagNotFoundException {
       return this.postService.addPost(authentication.getName(), postAddTopicDto);
    }
}
