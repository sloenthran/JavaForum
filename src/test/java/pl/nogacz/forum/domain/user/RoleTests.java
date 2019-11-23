package pl.nogacz.forum.domain.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RoleTests {
    @Test
    public void isUser() {
        //Given
        Role role = Role.USER;

        //When
        Assert.assertTrue(role.isUser());
        Assert.assertFalse(role.isAdmin());
        Assert.assertFalse(role.isModerator());
    }

    @Test
    public void isModerator() {
        //Given
        Role role = Role.MODERATOR;

        //When
        Assert.assertFalse(role.isUser());
        Assert.assertFalse(role.isAdmin());
        Assert.assertTrue(role.isModerator());
    }

    @Test
    public void isAdmin() {
        //Given
        Role role = Role.ADMIN;

        //When
        Assert.assertFalse(role.isUser());
        Assert.assertTrue(role.isAdmin());
        Assert.assertFalse(role.isModerator());
    }
}