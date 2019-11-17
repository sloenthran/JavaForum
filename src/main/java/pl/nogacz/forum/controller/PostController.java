package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.domain.post.Tag;
import pl.nogacz.forum.dto.post.*;
import pl.nogacz.forum.exception.post.TagNotFoundException;
import pl.nogacz.forum.exception.post.TopicNotFoundException;
import pl.nogacz.forum.service.post.PostService;

import java.util.ArrayList;
import java.util.List;

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

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("topic")
    public AddTopicResponseDto addTopic(@Autowired Authentication authentication, @RequestBody AddTopicRequestDto postAddTopicDto) throws TagNotFoundException {
       return this.postService.addTopic(authentication.getName(), postAddTopicDto);
    }

    @GetMapping("topics")
    public List<TopicDto> getTopics() {
        return this.postService.getTopics();
    }

    @GetMapping("tags")
    public List<GetTagsResponseDto> getTags() {
        List<GetTagsResponseDto> tags = new ArrayList<>();

        for(Tag tag : Tag.values()) {
            tags.add(new GetTagsResponseDto(tag));
        }

        return tags;
    }

    @GetMapping("topic")
    public TopicWithCommentDto getTopic(@RequestParam Long topicId) throws TopicNotFoundException {
        return this.postService.getTopic(topicId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("comment")
    public void addComment(@Autowired Authentication authentication, @RequestBody AddCommentRequestDto addCommentDto) throws TopicNotFoundException {
        this.postService.addComment(authentication.getName(), addCommentDto);
    }
}
