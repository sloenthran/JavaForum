package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.repository.user.UserRepository;
import pl.nogacz.forum.repository.user.UserRoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;

    public User loadUserByUsername(final String username) {
        //TODO REMOVE IT
        if(username.equals("sloenthran")) {
            UserRole userRole = new UserRole(1L, Role.USER, new ArrayList<>());
            List<UserRole> roles = new ArrayList<>();
            roles.add(userRole);

            return new User(
                    1L,
                    "sloenthran",
                    "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    "sloenthran@gmail.com",
                    true,
                    true,
                    true,
                    true,
                    roles
            );
        }

        return this.userRepository.findByUsername(username).orElse(null);
    }

/*    public UserRole loadUserRoleByRole(final Role role) {
        return this.
    }*/
}