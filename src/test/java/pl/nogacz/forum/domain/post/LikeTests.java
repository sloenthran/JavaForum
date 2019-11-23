package pl.nogacz.forum.domain.post;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.domain.user.User;
import pl.nogacz.forum.domain.user.UserRole;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LikeTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void equalsContract() {
        //Given
        List<UserRole> authorities = new ArrayList<>();
        authorities.add(new UserRole(
                1L,
                Role.USER,
                new ArrayList<>()
        ));

        User user = User.builder()
                .id(1L)
                .authorities(authorities)
                .username("sloenthran")
                .password(this.passwordEncoder.encode("password"))
                .email("sloenthran@gmail.com")
                .build();

        Topic topic = new Topic(
                1L,
                "title",
                Tag.JAVA,
                new ArrayList<>(),
                0L,
                new ArrayList<>()
        );

        Like like = new Like(
                1L,
                user,
                topic
        );

        Like likeTwo = new Like(
                1L,
                user,
                topic
        );

        //Then
        Assert.assertEquals(like, likeTwo);
    }

    @Test
    public void equalsContractBad() {
        //Given
        List<UserRole> authorities = new ArrayList<>();
        authorities.add(new UserRole(
                1L,
                Role.USER,
                new ArrayList<>()
        ));

        User user = User.builder()
                .id(1L)
                .authorities(authorities)
                .username("sloenthran")
                .password(this.passwordEncoder.encode("password"))
                .email("sloenthran@gmail.com")
                .build();

        Topic topic = new Topic(
                1L,
                "title",
                Tag.JAVA,
                new ArrayList<>(),
                0L,
                new ArrayList<>()
        );

        Like like = new Like(
                1L,
                user,
                topic
        );

        Like likeTwo = new Like(
                2L,
                user,
                topic
        );

        //Then
        Assert.assertNotEquals(like, likeTwo);
    }
}