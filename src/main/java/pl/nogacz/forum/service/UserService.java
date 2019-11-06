package pl.nogacz.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.dto.authentication.RegisterRequestDto;
import pl.nogacz.forum.dto.user.UserChangePasswordDto;
import pl.nogacz.forum.exception.user.UserNotFoundException;
import pl.nogacz.forum.exception.validation.*;
import pl.nogacz.forum.exception.user.UserRoleNotFoundException;
import pl.nogacz.forum.repository.user.UserRepository;
import pl.nogacz.forum.repository.user.UserRoleRepository;
import pl.nogacz.forum.util.email.validate.EmailValidate;

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
    private EmailValidate emailValidate;

    public User loadUserById(final Long id) throws UserNotFoundException {
        return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User loadUserByUsername(final String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public UserRole loadUserRoleByRole(final Role role) throws UserRoleNotFoundException {
        return this.userRoleRepository.findByRole(role).orElseThrow(UserRoleNotFoundException::new);
    }

    public UserRole loadUserRoleByRoleWithoutException(final Role role) {
        return this.userRoleRepository.findByRole(role).orElse(null);
    }

    public List<User> loadUsers() {
        return this.userRepository.findAll();
    }

    public User registerUser(final RegisterRequestDto registerRequestDto) throws Exception {
        this.validUsername(registerRequestDto.getUsername());
        this.validEmail(registerRequestDto.getEmail());
        this.validPassword(registerRequestDto.getPassword());

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

    public Long getCountUsers() {
        return this.userRepository.count();
    }

    public User saveUser(final User user) {
        return this.userRepository.save(user);
    }

    public UserRole saveUserRole(final UserRole userRole) {
        return this.userRoleRepository.save(userRole);
    }

    public boolean changePassword(String username, final UserChangePasswordDto userChangePasswordDto) throws Exception {
        this.validPassword(userChangePasswordDto.getNewPassword());

        User user = this.loadUserByUsername(username);

        if(!passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
            throw new BadOldPasswordException();
        }

        user.setPassword(this.passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        this.saveUser(user);

        return true;
    }

    public boolean changeEmail(String username, final String email) throws Exception {
        this.validEmail(email);

        User user = this.loadUserByUsername(username);

        if(user == null) {
            throw new UserNotFoundException();
        }

        user.setEmail(email);
        this.saveUser(user);

        return true;
    }

    public void deleteUserById(final Long id) throws UserNotFoundException {
        User user = this.loadUserById(id);
        user.getAuthorities().removeAll(user.getAuthorities());

        this.userRepository.delete(user);
    }

    private void validEmail(String email) throws Exception {
        this.emailValidate.validEmail(email);

        if(this.userRepository.existsByEmail(email)) {
            throw new EmailExistException();
        }
    }

    private void validPassword(String password) throws Exception {
        if(password.length() < 6) {
            throw new PasswordTooShortException();
        }
    }

    private void validUsername(String username) throws Exception {
        if(this.userRepository.existsByUsername(username)) {
            throw new UsernameExistException();
        }
    }
}