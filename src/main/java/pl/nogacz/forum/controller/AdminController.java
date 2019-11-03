package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.dto.user.UserDto;
import pl.nogacz.forum.exception.user.UserNotFoundException;
import pl.nogacz.forum.mapper.UserMapper;
import pl.nogacz.forum.service.UserService;

import java.util.List;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(
        value = "/admin/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AdminController {
    private UserService userService;
    private UserMapper userMapper;

    @DeleteMapping("user/{id}")
    public void removeUser(@PathVariable("id") Long id) throws UserNotFoundException {
        this.userService.deleteUserById(id);
    }

    @GetMapping("users")
    public List<UserDto> getUsers() {
        return userMapper.mapListUserToListUserDto(userService.loadUsers());
    }
}
