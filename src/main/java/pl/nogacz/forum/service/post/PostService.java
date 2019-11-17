package pl.nogacz.forum.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.post.Comment;
import pl.nogacz.forum.domain.post.Tag;
import pl.nogacz.forum.domain.post.Topic;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.dto.post.*;
import pl.nogacz.forum.exception.post.TagNotFoundException;
import pl.nogacz.forum.exception.post.TopicNotFoundException;
import pl.nogacz.forum.mapper.PostMapper;
import pl.nogacz.forum.repository.post.CommentRepository;
import pl.nogacz.forum.repository.post.TopicRepository;
import pl.nogacz.forum.service.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PostService {
    private TopicRepository topicRepository;
    private CommentRepository commentRepository;
    private UserService userService;
    private PostMapper postMapper;

    public AddTopicResponseDto addPost(final String username, final AddTopicRequestDto postAddTopicDto) throws TagNotFoundException {
        User user = this.userService.loadUserByUsername(username);

        Topic topic = new Topic(
                null,
                this.getTagFromString(postAddTopicDto.getTag()),
                postAddTopicDto.getTitle(),
                new ArrayList<>(),
                0L,
                0L
        );

        Topic saveTopic = this.topicRepository.save(topic);

        Comment comment = new Comment(
                null,
                postAddTopicDto.getText(),
                user,
                LocalDateTime.now(),
                saveTopic
        );

        this.commentRepository.save(comment);

        return new AddTopicResponseDto(saveTopic.getId());
    }

    private Tag getTagFromString(final String tagName) throws TagNotFoundException {
        Tag tag = null;

        try {
            tag = Tag.valueOf(tagName);
        } catch (Exception e) {
            throw new TagNotFoundException();
        }

        return tag;
    }

    public List<TopicDto> getTopics() {
        List<Topic> topics = this.topicRepository.findAllByOrderByIdDesc();

        return this.postMapper.mapListTopicToListTopicDto(topics);
    }

    public TopicWithCommentDto getTopic(Long topicId) throws TopicNotFoundException {
        Topic topic = this.topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);
        topic.setViewedCount(topic.getViewedCount() + 1L);

        this.topicRepository.save(topic);

        return new TopicWithCommentDto(
                this.postMapper.mapTopicToTopicDto(topic),
                this.postMapper.mapListCommentToListCommentDto(topic.getComments())
        );
    }
}
