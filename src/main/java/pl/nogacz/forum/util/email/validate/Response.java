package pl.nogacz.forum.util.email.validate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    @JsonProperty("valid")
    private boolean valid = false;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("domain-error")
    private boolean domainError = false;

    @JsonProperty("domain")
    private String domain;

    @JsonProperty("is-freemail")
    private boolean freemail = false;

    @JsonProperty("syntax-error")
    private boolean syntaxError = false;

    @JsonProperty("email")
    private String email;

    @JsonProperty("is-disposable")
    private boolean disposable = false;

    @JsonProperty("is-personal")
    private boolean personal = false;
}
