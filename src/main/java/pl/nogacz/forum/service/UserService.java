package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.dto.authentication.RegisterRequestDto;
import pl.nogacz.forum.exception.user.UserNotFoundException;
import pl.nogacz.forum.exception.validation.BadEmailException;
import pl.nogacz.forum.exception.validation.PasswordTooShortException;
import pl.nogacz.forum.exception.validation.EmailExistException;
import pl.nogacz.forum.exception.user.UserRoleNotFoundException;
import pl.nogacz.forum.exception.validation.UsernameExistException;
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

    public User loadUserById(final Long id) throws UserNotFoundException {
        return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User loadUserByUsername(final String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public UserRole loadUserRoleByRole(final Role role) throws UserRoleNotFoundException {
        return this.userRoleRepository.findByRole(role).orElseThrow(UserRoleNotFoundException::new);
    }

    public User registerUser(final RegisterRequestDto registerRequestDto) throws Exception {
        if(this.userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new UsernameExistException();
        }

        if(!EmailValidator.getInstance().isValid(registerRequestDto.getEmail())) {
            throw new BadEmailException();
        }

        if(this.userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new EmailExistException();
        }

        if(registerRequestDto.getPassword().length() < 6) {
            throw new PasswordTooShortException();
        }

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

    public void deleteUserById(final Long id) throws UserNotFoundException {
        User user = this.loadUserById(id);
        user.getAuthorities().removeAll(user.getAuthorities());

        this.userRepository.delete(user);
    }
}