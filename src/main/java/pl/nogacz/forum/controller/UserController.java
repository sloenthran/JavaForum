package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.dto.user.UserDto;
import pl.nogacz.forum.exception.user.UserNotFoundException;
import pl.nogacz.forum.mapper.UserMapper;
import pl.nogacz.forum.service.UserService;

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
        User user = userService.loadUserByUsername(authentication.getName());

        if(user == null) {
            throw new UserNotFoundException();
        } else {
            return userMapper.mapUserToUserDto(user);
        }
    }
}