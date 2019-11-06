package pl.nogacz.forum.util.email.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.*;
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
        HttpHeaders headers = new HttpHeaders();

        headers.add("x-rapidapi-host", this.config.getHost());
        headers.add("x-rapidapi-key", this.config.getKey());

        EmailValidateRequest request = new EmailValidateRequest(email);

        HttpEntity httpEntity = new HttpEntity(request, headers);

        ResponseEntity<EmailValidateResponse> responseEntity = this.restTemplate.exchange(this.config.getUrl(), HttpMethod.POST, httpEntity, EmailValidateResponse.class);

        if(responseEntity.hasBody() && responseEntity.getStatusCode().is2xxSuccessful()) {
            EmailValidateResponse response = responseEntity.getBody();

            if(response.isDisposable()) {
                throw new EmailDisposableException();
            }

            if(response.isDomainError()) {
                throw new EmailDomainNotFound();
            }

            if(!response.isValid()) {
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
