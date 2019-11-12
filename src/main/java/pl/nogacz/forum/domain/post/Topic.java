package pl.nogacz.forum.domain.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tag")
    private Tag tag;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @OneToMany(
            targetEntity = Comment.class,
            cascade = CascadeType.PERSIST,
            mappedBy = "topic",
            fetch = FetchType.EAGER
    )
    private List<Comment> comments;
}
