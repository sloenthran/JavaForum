package pl.nogacz.forum.domain.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserTests {
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
                .password("password")
                .email("sloenthran@gmail.com")
                .build();

        User userTwo = User.builder()
                .id(1L)
                .authorities(authorities)
                .username("sloenthran")
                .password("password")
                .email("sloenthran@gmail.com")
                .build();

        //Then
        Assert.assertEquals(user, userTwo);
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
                .password("password")
                .email("sloenthran@gmail.com")
                .build();

        User userTwo = User.builder()
                .id(1L)
                .authorities(authorities)
                .username("sloenthran123")
                .password("password")
                .email("sloenthran@gmail.com")
                .build();

        //Then
        Assert.assertNotEquals(user, userTwo);
    }
}