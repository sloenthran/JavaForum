package pl.nogacz.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.dto.authentication.AuthenticationRequestDto;
import pl.nogacz.forum.dto.authentication.AuthenticationResponseDto;
import pl.nogacz.forum.dto.user.UserChangePasswordDto;
import pl.nogacz.forum.dto.user.UserDto;
import pl.nogacz.forum.dto.user.UserRoleDto;
import pl.nogacz.forum.mapper.UserMapper;
import pl.nogacz.forum.service.user.UserRoleService;
import pl.nogacz.forum.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    private String loginToken;

    @Before
    public void prepareDatabase() throws Exception {
        List<UserRole> authorities = new ArrayList<>();
        authorities.add(this.userRoleService.loadUserRoleByRole(Role.USER));

        User user = User.builder()
                .id(1L)
                .authorities(authorities)
                .username("sloenthran")
                .password(this.passwordEncoder.encode("password"))
                .email("sloenthran@gmail.com")
                .build();

        this.userService.saveUser(user);

        this.loginFunction();
    }

    private void loginFunction() throws Exception {
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
                "sloenthran",
                "password"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(authenticationRequestDto, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/login", HttpMethod.POST, httpEntity, String.class);

        this.loginToken = new ObjectMapper().readValue(responseEntity.getBody(), AuthenticationResponseDto.class).getToken();
    }

    @Test
    public void getInfoOfActualUser() throws Exception {
        //Given
        List<UserRole> authorities = new ArrayList<>();
        authorities.add(this.userRoleService.loadUserRoleByRole(Role.USER));

        List<UserRoleDto> authoritiesDto = this.userMapper.mapListUserRoleToListUserRoleDto(authorities);

        UserDto userDto = new UserDto(
                1L,
                "sloenthran",
                "sloenthran@gmail.com",
                true,
                true,
                true,
                true,
                authoritiesDto
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/me", HttpMethod.GET, httpEntity, String.class);
        UserDto userDtoRead = new ObjectMapper().readValue(responseEntity.getBody(), UserDto.class);

        //Then
        Assert.assertEquals(userDto, userDtoRead);
    }

    @Test
    public void changePassword() throws Exception {
        //Given
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(
                "password",
                "new_password"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(userChangePasswordDto, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/password", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    public void changePasswordWithTooShortPassword() throws Exception {
        //Given
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(
                "password",
                "np"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(userChangePasswordDto, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/password", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    public void changePasswordWithBadOldPassword() throws Exception {
        //Given
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto(
                "password_bad",
                "new_password"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(userChangePasswordDto, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/password", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    public void changeEmail() throws Exception {
    }

    @Test
    public void removeUser() throws Exception {
    }

    @Test
    public void getUsers() throws Exception {
    }

    @Test
    public void getUsersCount() throws Exception {
    }
}