package pl.nogacz.forum.domain;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LogTest {
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

        Log log = new Log(
                1L,
                user,
                LocalDateTime.MIN,
                "abcd"
        );

        Log logTwo = new Log(
                1L,
                user,
                LocalDateTime.MIN,
                "abcd"
        );

        //Then
        Assert.assertEquals(log, logTwo);
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

        Log log = new Log(
                1L,
                user,
                LocalDateTime.MIN,
                "abcd"
        );

        Log logTwo = new Log(
                1L,
                user,
                LocalDateTime.MIN,
                "efgh"
        );

        //Then
        Assert.assertNotEquals(log, logTwo);
    }
}