package pl.nogacz.forum.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.exception.user.UserRoleNotFoundException;
import pl.nogacz.forum.repository.user.UserRoleRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserRoleService {
    private UserRoleRepository userRoleRepository;

    public UserRole loadUserRoleByRole(final Role role) throws UserRoleNotFoundException {
        return this.userRoleRepository.findByRole(role).orElseThrow(UserRoleNotFoundException::new);
    }

    public UserRole loadUserRoleByRoleWithoutException(final Role role) {
        return this.userRoleRepository.findByRole(role).orElse(null);
    }

    public UserRole saveUserRole(final UserRole userRole) {
        return this.userRoleRepository.save(userRole);
    }
}
