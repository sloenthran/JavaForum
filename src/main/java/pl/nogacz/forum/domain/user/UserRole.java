package pl.nogacz.forum.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "roles")
public class UserRole implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "role")
    private Role role;

    @NotNull
    @ManyToMany(mappedBy = "authorities")
    private Collection<User> users = new ArrayList<>();

    @Override
    public String getAuthority() {
        return this.role.toString();
    }
}
