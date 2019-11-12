package pl.nogacz.forum.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HerokuScheduler {
    private RestTemplate restTemplate;

    public HerokuScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void wakeUp() {
        this.restTemplate.getForObject("https://java-forum-application.herokuapp.com/heroku/wakeUp", String.class);
    }
}