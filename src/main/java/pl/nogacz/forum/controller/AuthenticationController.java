package pl.nogacz.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.dto.authentication.AuthenticationRequestDto;
import pl.nogacz.forum.dto.authentication.AuthenticationResponseDto;
import pl.nogacz.forum.dto.authentication.RegisterRequestDto;
import pl.nogacz.forum.dto.user.UserDto;
import pl.nogacz.forum.exception.authentication.InvalidCredentialsException;
import pl.nogacz.forum.config.authentication.util.TokenUtil;
import pl.nogacz.forum.mapper.UserMapper;
import pl.nogacz.forum.service.CleanService;
import pl.nogacz.forum.service.user.UserService;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping(
        value = "/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private TokenUtil tokenUtil;
    private UserService userService;
    private UserMapper userMapper;
    private CleanService cleanService;

    @PostMapping(value = "login")
    public AuthenticationResponseDto createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws Exception {
        this.authenticate(authenticationRequestDto);

        final UserDetails user = this.userService.loadUserByUsername(authenticationRequestDto.getUsername());
        final String token = this.tokenUtil.generateToken(user);

        return new AuthenticationResponseDto(token);
    }

    @PutMapping(value = "register")
    public UserDto register(@RequestBody RegisterRequestDto registerRequestDto) throws Exception {
        registerRequestDto = this.cleanService.cleanRegisterRequestDto(registerRequestDto);
        User user = this.userService.registerUser(registerRequestDto);
        return this.userMapper.mapUserToUserDto(user);
    }

    private void authenticate(AuthenticationRequestDto authenticationRequestDto) throws Exception {
        authenticationRequestDto = this.cleanService.cleanAuthenticationRequestDto(authenticationRequestDto);
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new InvalidCredentialsException();
        }
    }
}
