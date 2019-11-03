package pl.nogacz.forum.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.dto.authentication.AuthenticationRequestDto;
import pl.nogacz.forum.dto.authentication.RegisterRequestDto;
import pl.nogacz.forum.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void prepareDatabase() throws Exception {
        UserRole userRole = new UserRole(1L, Role.USER, new ArrayList<>());
        this.userService.saveUserRole(userRole);

        List<UserRole> authorities = new ArrayList<>();
        authorities.add(userRole);

        User user = new User(
                1L,
                "sloenthran",
                this.passwordEncoder.encode("password"),
                "sloenthran@gmail.com",
                true,
                true,
                true,
                true,
                authorities
        );

        this.userService.saveUser(user);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void login() throws Exception {
        //Given
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(
                "sloenthran",
                "password"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(authenticationRequestDto, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/login", HttpMethod.POST, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void register() throws Exception {
        //Given
        RegisterRequestDto registerRequestDto = new RegisterRequestDto(
                "sloenthran123",
                "password123",
                "sloenthran123@gmail.com"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(registerRequestDto, headers);

        //When
        int countUsersBeforeRegister = this.userService.loadUsers().size();

        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/register", HttpMethod.PUT, httpEntity, String.class);

        int countUsersAfterRegister = this.userService.loadUsers().size();

        //Then
        assertEquals(1, countUsersBeforeRegister);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertEquals(2, countUsersAfterRegister);
    }
}