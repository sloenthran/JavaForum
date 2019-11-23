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
import org.springframework.test.annotation.DirtiesContext;
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
public class UserControllerTests {
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

    private void addMemberAuthoritiesFunction(String username, Role role) throws Exception {
        User user = this.userService.loadUserByUsername(username);
        UserRole userRole = this.userRoleService.loadUserRoleByRole(role);

        user.getAuthorities().add(userRole);

        this.userService.saveUser(user);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeEmail() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/email?email=sloenthran123@gmail.com", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeEmailWithNotLoggedUser() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/email?email=sloenthran123@gmail.com", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeEmailWithExistingEmail() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/email?email=sloenthran@gmail.com", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeEmailWithBadEmail() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/email?email=sloenthran123@gmail", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeEmailWithNotExistingDomain() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/email?email=sloenthran123@gmailasdasdasdasdas123asd.com", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeEmailWithDisposableEmail() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/change/email?email=sloenthran123@eveav.com", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void removeUser() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);
        this.addMemberAuthoritiesFunction("sloenthran", Role.ADMIN);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/1", HttpMethod.DELETE, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void removeUserWithBadId() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);
        this.addMemberAuthoritiesFunction("sloenthran", Role.ADMIN);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/6", HttpMethod.DELETE, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void removeUserWithUserIsNotAdmin() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/user/1", HttpMethod.DELETE, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getUsers() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/users", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getUsersWithUserIsNotModerator() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/users", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getUsersCount() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/users/count", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getUsersCountWithUserIsNotModerator() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/users/count", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }
}