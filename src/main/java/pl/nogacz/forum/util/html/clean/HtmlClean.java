package pl.nogacz.forum.util.html.clean;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.nogacz.forum.config.HtmlCleanConfig;

@Component
@AllArgsConstructor
public class HtmlClean {
    private HtmlCleanConfig config;
    private RestTemplate restTemplate;

    public String cleanText(final String text, final HtmlCleanType cleanType) {
        try {
            HttpResponse<String> response = Unirest.post(config.getUrl())
                    .header("x-rapidapi-host", config.getHost())
                    .header("x-rapidapi-key", config.getKey())
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body("content="+ text +"&output-type="+ cleanType.getValue() +"").asString();

            if(response.getStatus() == 200) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return text.replaceAll("\\<.*?\\>", "");
    }
}
