package pl.nogacz.forum.util.email.validate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailValidateResponse {
    @JsonProperty("valid")
    private boolean valid = false;

    @JsonProperty("domain-error")
    private boolean domainError = false;

    @JsonProperty("syntax-error")
    private boolean syntaxError = false;

    @JsonProperty("is-disposable")
    private boolean disposable = false;
}
