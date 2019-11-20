package pl.nogacz.forum.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.nogacz.forum.domain.post.Like;
import pl.nogacz.forum.domain.post.Topic;
import pl.nogacz.forum.domain.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndTopic(User user, Topic topic);

    @Query(value = "SELECT COUNT(`id`), `topic_id` FROM `likes` GROUP BY `topic_id` ORDER BY COUNT(`topic_id`) DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getTopTopicLikes();
}
