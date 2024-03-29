package pl.nogacz.forum.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import pl.nogacz.forum.domain.Log;
import pl.nogacz.forum.domain.post.Comment;
import pl.nogacz.forum.domain.post.Like;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "users")
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "account_non_locked")
    @Builder.Default
    private boolean accountNonLocked = true;

    @NotNull
    @Column(name = "account_non_expired")
    @Builder.Default
    private boolean accountNonExpired = true;

    @NotNull
    @Column(name = "credentials_non_expired")
    @Builder.Default
    private boolean credentialsNonExpired = true;

    @NotNull
    @Column(name = "account_enabled")
    @Builder.Default
    private boolean enabled = true;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private List<UserRole> authorities = new ArrayList<>();


    @OneToMany(
            targetEntity = Comment.class,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            targetEntity = Log.class,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Log> logs = new ArrayList<>();

    @OneToMany(
            targetEntity = Like.class,
            cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Like> likes = new ArrayList<>();
}