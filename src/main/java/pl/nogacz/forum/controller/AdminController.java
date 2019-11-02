package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.exception.user.UserNotFoundException;
import pl.nogacz.forum.service.UserService;

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

    @DeleteMapping("user/{id}")
    public void removeUser(@PathVariable("id") Long id) throws UserNotFoundException {
        this.userService.deleteUserById(id);
    }
}
