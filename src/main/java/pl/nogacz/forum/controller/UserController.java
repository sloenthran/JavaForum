package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.dto.user.UserChangePasswordDto;
import pl.nogacz.forum.dto.user.UserDto;
import pl.nogacz.forum.exception.user.UserNotFoundException;
import pl.nogacz.forum.mapper.UserMapper;
import pl.nogacz.forum.service.CleanService;
import pl.nogacz.forum.service.user.UserService;

import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {
    private UserService userService;
    private UserMapper userMapper;
    private CleanService cleanService;

    @GetMapping(value = "/user/me")
    public UserDto getInfoOfActualUser(@Autowired Authentication authentication) {
        User user = this.userService.loadUserByUsername(authentication.getName());

        return this.userMapper.mapUserToUserDto(user);
    }

    @PutMapping(value = "/user/change/password")
    public boolean changePassword(@Autowired Authentication authentication, @RequestBody UserChangePasswordDto userChangePasswordDto) throws Exception {
        userChangePasswordDto = this.cleanService.cleanUserChangePasswordDto(userChangePasswordDto);
        return this.userService.changePassword(authentication.getName(), userChangePasswordDto);
    }

    @PutMapping(value = "/user/change/email")
    public boolean changeEmail(@Autowired Authentication authentication, @RequestParam String email) throws Exception {
        email = this.cleanService.cleanStringPlainText(email);
        return this.userService.changeEmail(authentication.getName(), email);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/user/{id}")
    public void removeUser(@PathVariable("id") Long id) throws UserNotFoundException {
        this.userService.deleteUserById(id);
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return this.userMapper.mapListUserToListUserDto(this.userService.loadUsers());
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @GetMapping("users/count")
    public Long getUsersCount() {
        return this.userService.getCountUsers();
    }
}