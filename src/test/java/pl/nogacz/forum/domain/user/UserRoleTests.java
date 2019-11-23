package pl.nogacz.forum.domain.user;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRoleTests {
    @Test
    public void equalsContract() {
        //Given
        UserRole userRole = new UserRole(
                1L,
                Role.USER,
                new ArrayList<>()
        );

        UserRole userRoleTwo = new UserRole(
                1L,
                Role.USER,
                new ArrayList<>()
        );

        //Then
        Assert.assertEquals(userRole, userRoleTwo);
    }

    @Test
    public void equalsContractBad() {
        //Given
        UserRole userRole = new UserRole(
                1L,
                Role.USER,
                new ArrayList<>()
        );

        UserRole userRoleTwo = new UserRole(
                1L,
                Role.ADMIN,
                new ArrayList<>()
        );

        //Then
        Assert.assertNotEquals(userRole, userRoleTwo);
    }
}