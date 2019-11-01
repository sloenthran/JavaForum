package pl.nogacz.forum.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class RegisterRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
}
