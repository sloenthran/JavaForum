package pl.nogacz.forum.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nogacz.forum.domain.post.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {}
