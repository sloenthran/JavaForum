package pl.nogacz.forum.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.post.Comment;
import pl.nogacz.forum.domain.post.Like;
import pl.nogacz.forum.domain.post.Tag;
import pl.nogacz.forum.domain.post.Topic;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.dto.post.*;
import pl.nogacz.forum.exception.post.CommentNotFoundException;
import pl.nogacz.forum.exception.post.TagNotFoundException;
import pl.nogacz.forum.exception.post.TopicNotFoundException;
import pl.nogacz.forum.mapper.PostMapper;
import pl.nogacz.forum.repository.post.CommentRepository;
import pl.nogacz.forum.repository.post.LikeRepository;
import pl.nogacz.forum.repository.post.TopicRepository;
import pl.nogacz.forum.service.LogService;
import pl.nogacz.forum.service.user.UserService;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PostService {
    private TopicRepository topicRepository;
    private CommentRepository commentRepository;
    private LikeRepository likeRepository;
    private UserService userService;
    private PostMapper postMapper;
    private LogService logService;

    public AddTopicResponseDto addTopic(final String username, final AddTopicRequestDto postAddTopicDto) throws TagNotFoundException {
        User user = this.userService.loadUserByUsername(username);

        Topic topic = new Topic(
                null,
                postAddTopicDto.getTitle(),
                this.getTagFromString(postAddTopicDto.getTag()),
                new ArrayList<>(),
                0L,
                new ArrayList<>()
        );

        Topic saveTopic = this.topicRepository.save(topic);

        Comment comment = Comment.builder()
                .text(postAddTopicDto.getText())
                .topic(topic)
                .user(user)
                .build();

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

    public TopicWithCommentDto getTopic(final Long topicId) throws TopicNotFoundException {
        Topic topic = this.topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);
        topic.setViewedCount(topic.getViewedCount() + 1L);

        this.topicRepository.save(topic);

        return new TopicWithCommentDto(
                this.postMapper.mapTopicToTopicDto(topic),
                this.postMapper.mapListCommentToListCommentDto(topic.getComments())
        );
    }

    public AddCommentResponseDto addComment(final String username, final AddCommentRequestDto addCommentDto) throws TopicNotFoundException {
        User user = this.userService.loadUserByUsername(username);
        Topic topic = this.topicRepository.findById(addCommentDto.getTopicId()).orElseThrow(TopicNotFoundException::new);

        Comment comment = Comment.builder()
                .text(addCommentDto.getText())
                .topic(topic)
                .user(user)
                .build();

        Comment saveComment = this.commentRepository.save(comment);

        return new AddCommentResponseDto(
                topic.getId(),
                saveComment.getId()
        );
    }

    public void deleteComment(final String username, final Long commentId) throws CommentNotFoundException {
        User user = this.userService.loadUserByUsername(username);

        Comment comment = this.commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        comment.getTopic().getComments().remove(comment);

        this.commentRepository.delete(comment);
        this.logService.addLog(user, "Deleted comment");
    }

    public String changeLike(final String username, final Long topicId) throws TopicNotFoundException {
        User user = this.userService.loadUserByUsername(username);

        Topic topic = this.topicRepository.findById(topicId).orElseThrow(TopicNotFoundException::new);

        Like like = this.likeRepository.findByUserAndTopic(user, topic).orElse(null);

        String message = "";

        if(like == null) {
            like = new Like(null, user, topic);

            this.likeRepository.save(like);

            message = "Like added";
        } else {
            this.likeRepository.delete(like);

            message = "Like deleted";
        }

        return message;
    }

    public List<MostLikedTopicDto> getMostLikedTopics() throws TopicNotFoundException {
        List<Object[]> topTopic = this.likeRepository.getMostLikedTopics();
        List<MostLikedTopicDto> topTopicDto = new ArrayList<>();

        for(Object[] object : topTopic) {
            BigInteger topicId = (BigInteger) object[1];

            Topic topic = this.topicRepository.findById(topicId.longValue()).orElseThrow(TopicNotFoundException::new);
            topTopicDto.add(this.postMapper.mapObjectToMostLikedTopicDto(object, topic.getTitle()));
        }

        return topTopicDto;
    }

    public CommentDto getComment(final Long id) throws CommentNotFoundException {
        Comment comment = this.commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        return this.postMapper.mapCommentToCommentDto(comment);
    }
}
