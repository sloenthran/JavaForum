package pl.nogacz.forum.service.user;

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
import pl.nogacz.forum.repository.user.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserValidService userValidService;
    private UserRoleService userRoleService;

    public User loadUserById(final Long id) throws UserNotFoundException {
        return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User loadUserByUsername(final String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public List<User> loadUsers() {
        return this.userRepository.findAll();
    }

    public User registerUser(final RegisterRequestDto registerRequestDto) throws Exception {
        this.userValidService.validUsername(registerRequestDto.getUsername());
        this.userValidService.validEmail(registerRequestDto.getEmail());
        this.userValidService.validPassword(registerRequestDto.getPassword());

        UserRole userRole = this.userRoleService.loadUserRoleByRole(Role.USER);
        List<UserRole> authorities = new ArrayList<>();
        authorities.add(userRole);

        User user = User.builder()
                .authorities(authorities)
                .username(registerRequestDto.getUsername())
                .password(this.passwordEncoder.encode(registerRequestDto.getPassword()))
                .email(registerRequestDto.getEmail())
                .build();

        return this.saveUser(user);
    }

    public Long getCountUsers() {
        return this.userRepository.count();
    }

    public User saveUser(final User user) {
        return this.userRepository.save(user);
    }

    public boolean changePassword(String username, final UserChangePasswordDto userChangePasswordDto) throws Exception {
        this.userValidService.validPassword(userChangePasswordDto.getNewPassword());

        User user = this.loadUserByUsername(username);

        if(!passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
            throw new BadOldPasswordException();
        }

        user.setPassword(this.passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
        this.saveUser(user);

        return true;
    }

    public boolean changeEmail(String username, final String email) throws Exception {
        this.userValidService.validEmail(email);

        User user = this.loadUserByUsername(username);

        user.setEmail(email);
        this.saveUser(user);

        return true;
    }

    public void deleteUserById(final Long id) throws UserNotFoundException {
        User user = this.loadUserById(id);
        user.getAuthorities().removeAll(user.getAuthorities());
        user.getComments().removeAll(user.getComments());
        user.getLikes().removeAll(user.getLikes());
        user.getLogs().removeAll(user.getLogs());

        this.userRepository.delete(user);
    }
}