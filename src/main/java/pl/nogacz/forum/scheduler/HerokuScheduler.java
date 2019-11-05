package pl.nogacz.forum.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class HerokuScheduler {
    private RestTemplate restTemplate;
    private Logger LOGGER = LoggerFactory.getLogger(HerokuScheduler.class);

    public HerokuScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "* */15 * * * *")
    public void wakeUp() {
        this.restTemplate.getForObject("https://java-forum-application.herokuapp.com/heroku/wakeUp", String.class);
        LOGGER.info("App wakeUp");
    }
}