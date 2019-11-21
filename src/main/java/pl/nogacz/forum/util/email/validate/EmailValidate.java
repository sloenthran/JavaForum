package pl.nogacz.forum.util.email.validate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.nogacz.forum.config.EmailValidateConfig;
import pl.nogacz.forum.exception.validation.BadEmailException;
import pl.nogacz.forum.exception.validation.EmailDisposableException;
import pl.nogacz.forum.exception.validation.EmailDomainNotFound;

@Component
@Data
@AllArgsConstructor
public class EmailValidate {
    private EmailValidateConfig config;
    private RestTemplate restTemplate;

    public boolean validEmail(String email) throws Exception  {
        EmailValidateResponse responseBody = null;

        try {
            HttpResponse<String> response = Unirest.post(config.getUrl())
                    .header("x-rapidapi-host", config.getHost())
                    .header("x-rapidapi-key", config.getKey())
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body("email="+ email).asString();

            if(response.getStatus() == 200) {
                responseBody = new ObjectMapper().readValue(response.getBody(), EmailValidateResponse.class);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        if(responseBody != null) {
            if(responseBody.isDisposable()) {
                throw new EmailDisposableException();
            }

            if(responseBody.isDomainError()) {
                throw new EmailDomainNotFound();
            }

            if(!responseBody.isValid()) {
                throw new BadEmailException();
            }
        } else {
            if(!EmailValidator.getInstance().isValid(email)) {
                throw new BadEmailException();
            }
        }

        return true;
    }
}
