package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.domain.post.Comment;
import pl.nogacz.forum.domain.post.Tag;
import pl.nogacz.forum.dto.post.*;
import pl.nogacz.forum.exception.post.CommentNotFoundException;
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

    @PreAuthorize("hasAuthority('USER')")
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

    @GetMapping("topic/{id}")
    public TopicWithCommentDto getTopic(@PathVariable("id") Long topicId) throws TopicNotFoundException {
        return this.postService.getTopic(topicId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("comment")
    public AddCommentResponseDto addComment(@Autowired Authentication authentication, @RequestBody AddCommentRequestDto addCommentDto) throws TopicNotFoundException {
         return this.postService.addComment(authentication.getName(), addCommentDto);
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("comment/{id}")
    public CommentDto getComment(@PathVariable("id") Long id) throws CommentNotFoundException {
        return this.postService.getComment(id);
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @PutMapping("comment")
    public void editComment(@Autowired Authentication authentication, @RequestBody EditCommentRequestDto editCommentRequestDto) {
        //TODO
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("comment/{id}")
    public DeleteCommentResponseDto deleteComment(@Autowired Authentication authentication,  @PathVariable("id") Long id) throws CommentNotFoundException {
        this.postService.deleteComment(authentication.getName(), id);

        return new DeleteCommentResponseDto();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/topic/like/{id}")
    public ChangeLikeResponseDto changeLike(@Autowired Authentication authentication, @PathVariable("id") Long id) throws TopicNotFoundException {
        String message = this.postService.changeLike(authentication.getName(), id);

        return new ChangeLikeResponseDto(message, id);
    }

    @GetMapping("/topics/likes")
    public List<MostLikedTopicDto> getMostLikedTopics() throws TopicNotFoundException {
       return this.postService.getMostLikedTopics();
    }
}