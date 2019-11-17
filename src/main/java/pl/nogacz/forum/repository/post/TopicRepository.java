package pl.nogacz.forum.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nogacz.forum.domain.post.Topic;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByOrderByIdDesc();
    Optional<Topic> findById(Long id);
}
