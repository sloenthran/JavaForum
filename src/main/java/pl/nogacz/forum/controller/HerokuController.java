package pl.nogacz.forum.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin("*")
public class HerokuController {
    @RequestMapping("/")
    public void homepage(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://sloenthran.github.io/JavaForum/");
    }

    @RequestMapping("/heroku/wakeUp")
    public boolean wakeUp() {
        return true;
    }
}