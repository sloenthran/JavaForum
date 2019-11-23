package pl.nogacz.forum.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EmailValidateConfig {
    private String url = "https://community-neutrino-email-validate.p.rapidapi.com/email-validate";
    private String host = "community-neutrino-email-validate.p.rapidapi.com";
    private String key = "da0f8145d1mshea7ca66e6eb845cp12c36ejsn6040076673f7";
}
