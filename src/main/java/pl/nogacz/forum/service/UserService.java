package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.dto.authentication.RegisterRequestDto;
import pl.nogacz.forum.exception.user.UserRoleNotFoundException;
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
    private PasswordEncoder passwordEncoder;

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

    public UserRole loadUserRoleByRole(final Role role) throws UserRoleNotFoundException {
        return this.userRoleRepository.findByRole(role).orElseThrow(UserRoleNotFoundException::new);
    }

    public User registerUser(final RegisterRequestDto registerRequestDto) throws UserRoleNotFoundException {
        UserRole userRole = this.loadUserRoleByRole(Role.USER);
        List<UserRole> authorities = new ArrayList<>();
        authorities.add(userRole);

        User user = new User(
                null,
                registerRequestDto.getUsername(),
                passwordEncoder.encode(registerRequestDto.getPassword()),
                registerRequestDto.getEmail(),
                true,
                true,
                true,
                true,
                authorities
        );

        return this.saveUser(user);
    }

    public User saveUser(final User user) {
        return this.userRepository.save(user);
    }

    public UserRole saveUserRole(final UserRole userRole) {
        return this.userRoleRepository.save(userRole);
    }
}