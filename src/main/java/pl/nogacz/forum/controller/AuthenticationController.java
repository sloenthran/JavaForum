package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nogacz.forum.dto.authentication.AuthenticationRequestDto;
import pl.nogacz.forum.dto.authentication.AuthenticationResponseDto;
import pl.nogacz.forum.exception.authentication.InvalidCredentialsException;
import pl.nogacz.forum.config.authentication.util.TokenUtil;
import pl.nogacz.forum.service.UserService;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private TokenUtil tokenUtil;
    private UserService userService;

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticationResponseDto createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws Exception {
        this.authenticate(authenticationRequestDto);

        final UserDetails user = this.userService.loadUserByUsername(authenticationRequestDto.getUsername());
        final String token = this.tokenUtil.generateToken(user);

        return new AuthenticationResponseDto(token);
    }

    private void authenticate(AuthenticationRequestDto authenticationRequestDto) throws Exception {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }
}
