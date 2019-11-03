package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nogacz.forum.mapper.UserMapper;
import pl.nogacz.forum.service.UserService;
import pl.nogacz.forum.util.email.validate.EmailValidate;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(
        value = "/statistic/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class StatisticController {
    private UserService userService;
    private UserMapper userMapper;
    private EmailValidate emailValidate;

    @GetMapping("user/count")
    public Long getUsersCount() {
        return this.userService.getCountUsers();
    }
}