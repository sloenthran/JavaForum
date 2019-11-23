package pl.nogacz.forum.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class HtmlCleanConfig {
    private String url = "https://neutrinoapi-html-clean.p.rapidapi.com/html-clean";
    private String host = "neutrinoapi-html-clean.p.rapidapi.com";
    private String key = "da0f8145d1mshea7ca66e6eb845cp12c36ejsn6040076673f7";
}
