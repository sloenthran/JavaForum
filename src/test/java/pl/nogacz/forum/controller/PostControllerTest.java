package pl.nogacz.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.nogacz.forum.domain.post.Tag;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;
import pl.nogacz.forum.dto.authentication.AuthenticationRequestDto;
import pl.nogacz.forum.dto.authentication.AuthenticationResponseDto;
import pl.nogacz.forum.dto.post.AddCommentRequestDto;
import pl.nogacz.forum.dto.post.AddTopicRequestDto;
import pl.nogacz.forum.dto.post.EditCommentRequestDto;
import pl.nogacz.forum.service.post.PostService;
import pl.nogacz.forum.service.user.UserRoleService;
import pl.nogacz.forum.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

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

    private void addTopicFunction() throws Exception {
        AddTopicRequestDto addTopicRequestDto = new AddTopicRequestDto(
                "Title",
                Tag.JAVA.toString(),
                "Text"
        );

        this.postService.addTopic("sloenthran", addTopicRequestDto);
    }

    private void addMemberAuthoritiesFunction(String username, Role role) throws Exception {
        User user = this.userService.loadUserByUsername(username);
        UserRole userRole = this.userRoleService.loadUserRoleByRole(role);

        user.getAuthorities().add(userRole);

        this.userService.saveUser(user);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addTopic() throws Exception {
        //Given
        AddTopicRequestDto addTopicRequestDto = new AddTopicRequestDto(
                "Title",
                Tag.JAVA.toString(),
                "Text"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(addTopicRequestDto, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/topic", HttpMethod.POST, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addTopicWithBadTag() throws Exception {
        //Given
        AddTopicRequestDto addTopicRequestDto = new AddTopicRequestDto(
                "Title",
                "BAD_TAG",
                "Text"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(addTopicRequestDto, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/topic", HttpMethod.POST, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getTopics() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/topics", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getTags() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/tags", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getTopic() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/topic/1", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getTopicWithBadId() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/topic/6", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addComment() throws Exception {
        //Given
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto(
                1L,
                "Test"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(addCommentRequestDto, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment", HttpMethod.POST, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addCommentWithNotLoggedUser() throws Exception {
        //Given
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto(
                1L,
                "Test"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity(addCommentRequestDto, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment", HttpMethod.POST, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void addCommentWithTopicNotFound() throws Exception {
        //Given
        AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto(
                2L,
                "Test"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(addCommentRequestDto, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment", HttpMethod.POST, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getComment() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        this.addTopicFunction();
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment/1", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getCommentWithUserNotIsModerator() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment/1", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getCommentNotExisted() throws Exception {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(null, headers);

        this.addTopicFunction();
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment/6", HttpMethod.GET, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editComment() throws Exception {
        //Given
        EditCommentRequestDto editCommentRequestDto = new EditCommentRequestDto(
                1L,
                "text abcd"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(editCommentRequestDto, headers);

        this.addTopicFunction();
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editCommentWithUserIsNotModerator() throws Exception {
        //Given
        EditCommentRequestDto editCommentRequestDto = new EditCommentRequestDto(
                1L,
                "text abcd"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(editCommentRequestDto, headers);

        this.addTopicFunction();

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editCommentNotExisted() throws Exception {
        //Given
        EditCommentRequestDto editCommentRequestDto = new EditCommentRequestDto(
                6L,
                "text abcd"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginToken);

        HttpEntity httpEntity = new HttpEntity(editCommentRequestDto, headers);

        this.addTopicFunction();
        this.addMemberAuthoritiesFunction("sloenthran", Role.MODERATOR);

        //When
        ResponseEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + this.serverPort + "/post/comment", HttpMethod.PUT, httpEntity, String.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteComment() throws Exception {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void changeLike() throws Exception {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void getMostLikedTopics() throws Exception {
    }
}