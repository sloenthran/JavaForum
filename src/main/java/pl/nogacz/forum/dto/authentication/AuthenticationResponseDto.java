package pl.nogacz.forum.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponseDto {
    private String token;
}