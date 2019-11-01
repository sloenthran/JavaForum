package pl.nogacz.forum.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nogacz.forum.domain.user.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {}