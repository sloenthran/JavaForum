package pl.nogacz.forum.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.UserRole;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(Role role);
}