package pl.nogacz.forum.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin("*")
public class HerokuController {
    private Logger LOGGER = LoggerFactory.getLogger(HerokuController.class);

    @GetMapping("/")
    public void homepage(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://sloenthran.github.io/JavaForum/");
    }

    @GetMapping("/heroku/wakeUp")
    public void wakeUp() {
        LOGGER.info("App wakeUp");
    }
}
