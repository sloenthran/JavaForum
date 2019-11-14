package pl.nogacz.forum.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.post.Comment;
import pl.nogacz.forum.domain.post.Tag;
import pl.nogacz.forum.domain.post.Topic;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.dto.post.PostAddTopicRequestDto;
import pl.nogacz.forum.dto.post.PostAddTopicResponseDto;
import pl.nogacz.forum.dto.post.TopicDto;
import pl.nogacz.forum.exception.post.TagNotFoundException;
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

    public PostAddTopicResponseDto addPost(final String username, final PostAddTopicRequestDto postAddTopicDto) throws TagNotFoundException {
        User user = this.userService.loadUserByUsername(username);

        Topic topic = new Topic(
                null,
                this.getTagFromString(postAddTopicDto.getTag()),
                postAddTopicDto.getTitle(),
                new ArrayList<>()
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

        return new PostAddTopicResponseDto(saveTopic.getId());
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
        List<TopicDto> topicsDto = new ArrayList<>();

        for(Topic topic : topics) {
            List<Comment> comments = this.commentRepository.getCommentByTopic_IdOrderByIdAsc(topic.getId());
            Comment firstPost = comments.get(0);

            TopicDto topicDto = this.postMapper.mapTopicToTopicDto(
                    topic,
                    firstPost.getCreatedDate(),
                    firstPost.getUser().getUsername(),
                    (long) comments.size()
            );

            topicsDto.add(topicDto);
        }

        return topicsDto;
    }
}
