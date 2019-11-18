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
import pl.nogacz.forum.service.user.UserService;

import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(
        value = "/user/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @GetMapping(value = "me")
    public UserDto getInfoOfActualUser(@Autowired Authentication authentication) throws UserNotFoundException {
        User user = this.userService.loadUserByUsername(authentication.getName());

        if(user == null) {
            throw new UserNotFoundException();
        } else {
            return this.userMapper.mapUserToUserDto(user);
        }
    }

    @PutMapping(value = "change/password")
    public boolean changePassword(@Autowired Authentication authentication, @RequestBody UserChangePasswordDto userChangePasswordDto) throws Exception {
        return this.userService.changePassword(authentication.getName(), userChangePasswordDto);
    }

    @PutMapping(value = "change/email")
    public boolean changeEmail(@Autowired Authentication authentication, @RequestParam String email) throws Exception {
        return this.userService.changeEmail(authentication.getName(), email);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("user/{id}")
    public void removeUser(@PathVariable("id") Long id) throws UserNotFoundException {
        this.userService.deleteUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("users")
    public List<UserDto> getUsers() {
        return this.userMapper.mapListUserToListUserDto(this.userService.loadUsers());
    }

    @GetMapping("users/count")
    public Long getUsersCount() {
        return this.userService.getCountUsers();
    }
}